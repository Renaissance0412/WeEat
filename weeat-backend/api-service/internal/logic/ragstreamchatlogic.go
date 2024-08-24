package logic

import (
	"context"
	"io"
	"net/http"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/ragserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type RAGStreamChatLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRAGStreamChatLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RAGStreamChatLogic {
	return &RAGStreamChatLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RAGStreamChatLogic) RAGStreamChat(req *types.RAGChatMessageRequest, w http.ResponseWriter) (err error) {
	if req.UserId == "" || req.Message == "" {
		err = xerr.NewErrMsg("user_id or context_id is empty")
	}

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.RAGServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	client := ragserviceclient.NewRAGService(conn)
	streamChatResult, err := client.StreamChat(l.ctx, &ragserviceclient.ChatMessageRequest{
		UserId:    req.UserId,
		Message:   req.Message,
		ContextId: req.ContextId,
		Addition:  req.Addition,
	})
	if err != nil {
		l.Logger.Errorf("client.SyncChat error: %v", err)
		return
	}

	for {
		chatResult, err := streamChatResult.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			l.Logger.Errorf("streamChatResult.Recv error: %v", err)
			return err
		}

		_, err = w.Write([]byte(chatResult.Message))
		if err != nil {
			l.Logger.Errorf("w.Write error: %v", err)
			return err
		}

		if f, ok := w.(http.Flusher); ok {
			f.Flush()
		} else {
			l.Logger.Errorf("expected http.ResponseWriter to be an http.Flusher")
			return xerr.NewErrMsg("expected http.ResponseWriter to be an http.Flusher")
		}
	}

	return
}

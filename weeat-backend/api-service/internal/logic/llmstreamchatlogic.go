package logic

import (
	"context"
	"io"
	"net/http"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/llmserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type LLMStreamChatLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewLLMStreamChatLogic(ctx context.Context, svcCtx *svc.ServiceContext) *LLMStreamChatLogic {
	return &LLMStreamChatLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *LLMStreamChatLogic) LLMStreamChat(req *types.ChatMessageRequest, w http.ResponseWriter) (err error) {
	if req.UserId == "" || req.Message == "" {
		err = xerr.NewErrMsg("user_id or context_id is empty")
	}

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.LLMServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	// Convert chat message history to chat message history type
	var history []*llmserviceclient.ChatMessage
	for _, h := range req.History {
		history = append(history, &llmserviceclient.ChatMessage{
			UserId:  h.UserId,
			Message: h.Message,
		})
	}

	client := llmserviceclient.NewLLMService(conn)
	streamChatResult, err := client.StreamChat(l.ctx, &llmserviceclient.ChatMessageRequest{
		UserId:  req.UserId,
		Message: req.Message,
		History: history,
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

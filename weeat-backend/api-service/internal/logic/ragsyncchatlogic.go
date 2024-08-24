package logic

import (
	"context"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/ragserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type RAGSyncChatLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRAGSyncChatLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RAGSyncChatLogic {
	return &RAGSyncChatLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RAGSyncChatLogic) RAGSyncChat(req *types.RAGChatMessageRequest) (resp *types.RAGChatMessageResponse, err error) {
	if req.UserId == "" || req.ContextId == "" {
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
	chatResult, err := client.SyncChat(l.ctx, &ragserviceclient.ChatMessageRequest{
		UserId:    req.UserId,
		ContextId: req.ContextId,
		Message:   req.Message,
		Addition:  req.Addition,
	})
	if err != nil {
		l.Logger.Errorf("client.SyncChat error: %v", err)
		return
	}

	resp = &types.RAGChatMessageResponse{
		Message: chatResult.Message,
	}

	return
}

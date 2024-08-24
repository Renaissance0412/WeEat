package logic

import (
	"context"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/llmserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type LLMSyncChatLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewLLMSyncChatLogic(ctx context.Context, svcCtx *svc.ServiceContext) *LLMSyncChatLogic {
	return &LLMSyncChatLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *LLMSyncChatLogic) LLMSyncChat(req *types.ChatMessageRequest) (resp *types.ChatMessageResponse, err error) {
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
	chatResult, err := client.SyncChat(l.ctx, &llmserviceclient.ChatMessageRequest{
		UserId:  req.UserId,
		Message: req.Message,
		History: history,
	})
	if err != nil {
		l.Logger.Errorf("client.SyncChat error: %v", err)
		return
	}

	resp = &types.ChatMessageResponse{
		Message: chatResult.Message,
	}

	return
}

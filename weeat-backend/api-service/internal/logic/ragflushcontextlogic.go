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

type RAGFlushContextLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRAGFlushContextLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RAGFlushContextLogic {
	return &RAGFlushContextLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RAGFlushContextLogic) RAGFlushContext(req *types.RAGFlushContextRequest) (resp *types.RAGFlushContextResponse, err error) {
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
	flushResult, err := client.FlushChatHistory(l.ctx, &ragserviceclient.FlushContextRequest{
		ContextId: req.ContextId,
		UserId:    req.UserId,
	})
	if err != nil {
		l.Logger.Errorf("client.FlushChatHistory error: %v", err)
		return
	}

	resp = &types.RAGFlushContextResponse{
		Status: flushResult.Status,
	}

	return
}

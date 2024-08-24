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

type RAGCreateContextLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRAGCreateContextLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RAGCreateContextLogic {
	return &RAGCreateContextLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RAGCreateContextLogic) RAGCreateContext(req *types.RAGCreateContextRequest) (resp *types.RAGCreateContextResponse, err error) {
	if req.UserId == "" {
		err = xerr.NewErrMsg("user_id is empty")
	}

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.RAGServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	client := ragserviceclient.NewRAGService(conn)
	createChatResult, err := client.CreateChatContext(l.ctx, &ragserviceclient.CreateContextRequest{
		UserId: req.UserId,
	})
	if err != nil {
		l.Logger.Errorf("client.CreateChatContext error: %v", err)
		return
	}

	l.Logger.Infof("createChatResult: %v", createChatResult)

	resp = &types.RAGCreateContextResponse{
		ContextId: createChatResult.ContextId,
		UserId:    createChatResult.UserId,
	}

	return
}

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

type RAGCreateUserLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRAGCreateUserLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RAGCreateUserLogic {
	return &RAGCreateUserLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RAGCreateUserLogic) RAGCreateUser(req *types.RAGCreateUserRequest) (resp *types.RAGCreateUserResponse, err error) {
	if req.Username == "" || req.Token == "" {
		err = xerr.NewErrMsg("username or password is empty")
	}

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.RAGServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	client := ragserviceclient.NewRAGService(conn)
	createResult, err := client.CreateUser(l.ctx, &ragserviceclient.CreateUserRequest{
		Username:  req.Username,
		Token:     req.Token,
		Interests: req.Interests,
	})
	if err != nil {
		l.Logger.Errorf("client.CreateUser error: %v", err)
		return
	}

	resp = &types.RAGCreateUserResponse{
		UserId: createResult.UserId,
	}

	return
}

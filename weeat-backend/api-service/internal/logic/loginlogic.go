package logic

import (
	"context"
	"time"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/ragserviceclient"

	"github.com/dgrijalva/jwt-go"
	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type LoginLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewLoginLogic(ctx context.Context, svcCtx *svc.ServiceContext) *LoginLogic {
	return &LoginLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *LoginLogic) Login(req *types.LoginRequest) (resp *types.LoginResponse, err error) {
	accessExpire := l.svcCtx.Config.JwtAuth.AccessExpire

	// Check current user
	if req.UserId == "" || req.Token == "" {
		return nil, nil
	}

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.RAGServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return nil, xerr.NewErrMsg("user not valid")
	}

	client := ragserviceclient.NewRAGService(conn)
	checkResult, err := client.CheckUserValid(l.ctx, &ragserviceclient.CheckUserRequest{
		UserId: req.UserId,
		Token:  req.Token,
	})
	if err != nil {
		l.Logger.Errorf("client.CheckUserValid error: %v", err)
		return nil, xerr.NewErrMsg("user not valid")
	}

	if !checkResult.Valid {
		return nil, xerr.NewErrMsg("user not valid")
	}

	now := time.Now().Unix()
	accessToken, err := l.GenToken(now, l.svcCtx.Config.JwtAuth.AccessSecret, nil, accessExpire)
	if err != nil {
		return nil, err
	}

	resp = &types.LoginResponse{
		// For dev, we don't need to check this access token in header
		AccessToken:  accessToken,
		AccessExpire: now + accessExpire,
		RefreshAfter: now + accessExpire/2,
	}

	return
}

func (l *LoginLogic) GenToken(iat int64, secretKey string, payloads map[string]interface{}, seconds int64) (string, error) {
	claims := make(jwt.MapClaims)
	claims["exp"] = iat + seconds
	claims["iat"] = iat
	for k, v := range payloads {
		claims[k] = v
	}

	token := jwt.New(jwt.SigningMethodHS256)
	token.Claims = claims

	return token.SignedString([]byte(secretKey))
}

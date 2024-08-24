package logic

import (
	"context"
	"io"
	"net/http"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/asrserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

const maxAudioSize = 50 << 20 // 50 MB

type ASRSyncLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewASRSyncLogic(ctx context.Context, svcCtx *svc.ServiceContext) *ASRSyncLogic {
	return &ASRSyncLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *ASRSyncLogic) ASRSync(req *http.Request) (resp *types.ASRResponse, err error) {
	err = req.ParseMultipartForm(maxAudioSize)
	if err != nil {
		l.Logger.Errorf("req.ParseMultipartForm error: %v", err)
		return
	}

	file, handler, err := req.FormFile("audio")
	if err != nil {
		l.Logger.Errorf("req.ParseMultipartForm error: %v", err)
		return
	}
	defer file.Close()

	audioData, err := io.ReadAll(file)
	if err != nil {
		l.Logger.Errorf("io.ReadAll error: %v", err)
		return nil, xerr.NewErrMsg("read image error")
	}

	l.Logger.Infof("Uploaded File: %+v\n", handler.Filename)
	l.Logger.Infof("File Size: %+v\n", handler.Size)

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.ASRServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	client := asrserviceclient.NewASRService(conn)
	asrResult, err := client.SyncRecognize(l.ctx, &asrserviceclient.ASRRequest{
		Audio: audioData,
	})
	if err != nil {
		l.Logger.Errorf("client.SyncRecognize error: %v", err)
		return
	}

	resp = &types.ASRResponse{
		Transcript: asrResult.Transcript,
	}

	return
}

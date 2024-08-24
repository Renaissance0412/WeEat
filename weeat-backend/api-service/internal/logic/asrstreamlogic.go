package logic

import (
	"context"
	"io"
	"net/http"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/protos/asrserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

type ASRStreamLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewASRStreamLogic(ctx context.Context, svcCtx *svc.ServiceContext) *ASRStreamLogic {
	return &ASRStreamLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *ASRStreamLogic) ASRStream(req *http.Request, w http.ResponseWriter) (err error) {
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
		return xerr.NewErrMsg("read image error")
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
	streamASRResult, err := client.StreamRecognize(l.ctx, &asrserviceclient.ASRRequest{
		Audio: audioData,
	})
	if err != nil {
		l.Logger.Errorf("client.SyncRecognize error: %v", err)
		return
	}

	for {
		asrResult, err := streamASRResult.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			l.Logger.Errorf("streamChatResult.Recv error: %v", err)
			return err
		}

		_, err = w.Write([]byte(asrResult.Transcript))
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

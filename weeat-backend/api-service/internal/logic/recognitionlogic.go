package logic

import (
	"context"
	"io"
	"net/http"

	"api-service/common/xerr"
	"api-service/internal/models"
	"api-service/internal/svc"
	"api-service/internal/types"
	"api-service/protos/recognitionserviceclient"

	"github.com/zeromicro/go-zero/core/logx"
	"github.com/zeromicro/go-zero/zrpc"
)

const maxFileSize = 50 << 20 // 50 MB

type RecognitionLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewRecognitionLogic(ctx context.Context, svcCtx *svc.ServiceContext) *RecognitionLogic {
	return &RecognitionLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *RecognitionLogic) Recognition(req *http.Request) (resp *types.RecognitionResponse, err error) {
	err = req.ParseMultipartForm(maxFileSize)
	if err != nil {
		l.Logger.Errorf("req.ParseMultipartForm error: %v", err)
		return
	}

	file, handler, err := req.FormFile("image")
	if err != nil {
		l.Logger.Errorf("req.ParseMultipartForm error: %v", err)
		return
	}
	defer file.Close()

	imageData, err := io.ReadAll(file)
	if err != nil {
		l.Logger.Errorf("io.ReadAll error: %v", err)
		return nil, xerr.NewErrMsg("read image error")
	}

	l.Logger.Infof("Uploaded File: %+v\n", handler.Filename)
	l.Logger.Infof("File Size: %+v\n", handler.Size)
	// l.Logger.Infof("MIME Header: %+v", handler.Header)

	discov := l.svcCtx.Config.AIServiceDiscovery
	discov.Key = models.RecognitionServiceDiscoveryKey
	conn, err := zrpc.NewClient(zrpc.RpcClientConf{Etcd: discov})
	if err != nil {
		l.Logger.Errorf("zrpc.NewClient error: %v", err)
		return
	}

	client := recognitionserviceclient.NewRecognitionService(conn)
	// 	Image []byte `protobuf:"bytes,1,opt,name=image,proto3" json:"image,omitempty"`
	chatResult, err := client.Recognize(l.ctx, &recognitionserviceclient.RecognitionRequest{
		Image: imageData,
	})
	if err != nil {
		l.Logger.Errorf("client.Recognize error: %v", err)
		return
	}

	// Convert chatResult items to RecognitionResponse items
	items := make([]types.RecognitionResponseItem, 0, len(chatResult.Items))
	for _, item := range chatResult.Items {
		items = append(items, types.RecognitionResponseItem{
			Name:       item.Name,
			Confidence: item.Confidence,
		})
	}

	resp = &types.RecognitionResponse{
		Items: items,
		Count: chatResult.Count,
	}

	return
}

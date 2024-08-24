package handler

import (
	"net/http"

	"api-service/internal/logic"
	"api-service/internal/svc"

	"github.com/zeromicro/go-zero/rest/httpx"
)

func RecognitionHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		l := logic.NewRecognitionLogic(r.Context(), svcCtx)
		resp, err := l.Recognition(r)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		} else {
			httpx.OkJsonCtx(r.Context(), w, resp)
		}
	}
}

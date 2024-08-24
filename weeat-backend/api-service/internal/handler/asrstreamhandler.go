package handler

import (
	"net/http"

	"api-service/internal/logic"
	"api-service/internal/svc"

	"github.com/zeromicro/go-zero/rest/httpx"
)

func ASRStreamHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// For stream request
		w.Header().Set("Content-Type", "text/event-stream")
		w.Header().Set("Cache-Control", "no-cache")
		w.Header().Set("Connection", "keep-alive")

		l := logic.NewASRStreamLogic(r.Context(), svcCtx)
		err := l.ASRStream(r, w)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		}
	}
}

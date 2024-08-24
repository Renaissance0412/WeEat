package handler

import (
	"net/http"

	"api-service/internal/logic"
	"api-service/internal/svc"

	"github.com/zeromicro/go-zero/rest/httpx"
)

func ASRSyncHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		l := logic.NewASRSyncLogic(r.Context(), svcCtx)
		resp, err := l.ASRSync(r)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		} else {
			httpx.OkJsonCtx(r.Context(), w, resp)
		}
	}
}

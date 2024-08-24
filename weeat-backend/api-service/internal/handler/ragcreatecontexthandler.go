package handler

import (
	"net/http"

	"api-service/internal/logic"
	"api-service/internal/svc"
	"api-service/internal/types"
	"github.com/zeromicro/go-zero/rest/httpx"
)

func RAGCreateContextHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var req types.RAGCreateContextRequest
		if err := httpx.Parse(r, &req); err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
			return
		}

		l := logic.NewRAGCreateContextLogic(r.Context(), svcCtx)
		resp, err := l.RAGCreateContext(&req)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		} else {
			httpx.OkJsonCtx(r.Context(), w, resp)
		}
	}
}

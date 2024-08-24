package handler

import (
	"net/http"

	"api-service/internal/logic"
	"api-service/internal/svc"
	"api-service/internal/types"

	"github.com/zeromicro/go-zero/rest/httpx"
)

func LLMStreamChatHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// For stream request
		w.Header().Set("Content-Type", "text/event-stream")
		w.Header().Set("Cache-Control", "no-cache")
		w.Header().Set("Connection", "keep-alive")

		var req types.ChatMessageRequest
		if err := httpx.Parse(r, &req); err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
			return
		}

		l := logic.NewLLMStreamChatLogic(r.Context(), svcCtx)
		err := l.LLMStreamChat(&req, w)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		}
	}
}

package xerr

var message map[uint32]string

func init() {
	message = make(map[uint32]string)
	message[OK] = "success"
	message[SERVER_COMMON_ERROR] = "server error"
	message[REUQEST_PARAM_ERROR] = "request param error"
	message[DB_ERROR] = "db error"
	message[DB_UPDATE_AFFECTED_ZERO_ERROR] = "db update affected zero error"
}

func MapErrMsg(errcode uint32) string {
	if msg, ok := message[errcode]; ok {
		return msg
	} else {
		return "server error"
	}
}

func IsCodeErr(errcode uint32) bool {
	if _, ok := message[errcode]; ok {
		return true
	} else {
		return false
	}
}

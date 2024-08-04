package com.xzf.backend.utils;

import com.xzf.backend.cconst.EHttpCode;
import com.xzf.backend.response.MyResponse;

public class SetResponseUtils {
	public static void setResponseSuccess(MyResponse response, Object data) {
		setResponse(response, EHttpCode.SUCCESS, data);
	}

	public static void setResponseFail(MyResponse response, Object data) {
		setResponse(response, EHttpCode.FAIL, data);
	}

	public static void setResponseFailParam(MyResponse response, Object data) {
		setResponse(response, EHttpCode.CODE_600, data);
	}


	private static void setResponse(MyResponse response, EHttpCode httpCode, Object data) {
		response.setStatus(httpCode.getStatus());
		response.setCode(httpCode.getCode());
		response.setInfo(httpCode.getInfo());
		response.setData(data);
	}
}
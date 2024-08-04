package com.xzf.backend.exception;

import com.xzf.backend.response.MyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseEntity<MyResponse <Object>> handleBusinessException(BusinessException e) {
		MyResponse<Object> response = e.getResponse();
		return ResponseEntity.status(response.getCode()).body(response);

	}
}

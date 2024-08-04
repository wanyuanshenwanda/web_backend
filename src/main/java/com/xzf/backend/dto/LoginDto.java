package com.xzf.backend.dto;

import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.cconst.enums.VerifyRegexEnum;
import lombok.Data;

@Data
public class LoginDto {
	@VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150)
	private String email;

	@VerifyParam(required = true)
	private String password;

	@VerifyParam(required = true)
	private String checkCode;
}

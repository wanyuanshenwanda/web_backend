package com.xzf.backend.controller;

import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.enums.VerifyRegexEnum;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.vo.ResponseVO;
import com.xzf.backend.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController extends BaseController {

	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("/register")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO register(HttpSession session,
	                           @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
	                           @VerifyParam(required = true, max = 20) String nickName,
	                           @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password
	                           ) {
		try {
			userInfoService.register(email, nickName, password);
			return getSuccessResponseVO(null);
		} finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}

	@RequestMapping("/login")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO login(HttpSession session,
	                        @VerifyParam(required = true) String email,
	                        @VerifyParam(required = true) String password
	                        ) {
		try {
			SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password);
			session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
			return getSuccessResponseVO(sessionWebUserDto);
		} finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}

	@RequestMapping("/getUserInfo")
	@GlobalInterceptor
	public ResponseVO getUserInfo(HttpSession session) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		return getSuccessResponseVO(sessionWebUserDto);
	}

	@RequestMapping("/logout")
	public ResponseVO logout(HttpSession session) {
		session.invalidate();
		return getSuccessResponseVO(null);
	}

}

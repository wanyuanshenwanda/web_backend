package com.xzf.backend.controller;

import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.EHttpCode;
import com.xzf.backend.dto.LoginDto;
import com.xzf.backend.dto.RegisterRequestDTO;
import com.xzf.backend.dto.SessionWebUserDto;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.response.MyResponse;
import com.xzf.backend.service.UserService;
import com.xzf.backend.utils.SetResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/register")
	@GlobalInterceptor(checkParams = true)
	public MyResponse<Object> register(HttpSession session, @RequestBody RegisterRequestDTO registerRequestDTO) {
		try {
			MyResponse<Object> response = new MyResponse<>();
			String email = registerRequestDTO.getEmail();
			String nickName = registerRequestDTO.getNickName();
			String password = registerRequestDTO.getPassword();
			userService.register(email, nickName, password);
			SetResponseUtils.setResponseSuccess(response, null);
			return response;
		} finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}

	@RequestMapping(value = "/login")
	@GlobalInterceptor(checkParams = true)
	public MyResponse<Object> login(HttpSession session, HttpServletRequest request, @RequestBody LoginDto loginDto) {
		try {
			MyResponse<Object> response = new MyResponse<>();

			String email = loginDto.getEmail();
			String password = loginDto.getPassword();
			SessionWebUserDto sessionWebUserDto = userService.login(email,password);
			session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
			SetResponseUtils.setResponseSuccess(response, sessionWebUserDto);
			return response;
		} finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}

	}

}

package com.xzf.backend.service;

import com.xzf.backend.dto.SessionWebUserDto;
import com.xzf.backend.entity.pojo.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
	void register(String email,  String nickName, String password);

	SessionWebUserDto login(String email, String password);
}

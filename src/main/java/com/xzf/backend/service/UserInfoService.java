package com.xzf.backend.service;

import com.xzf.backend.cconst.enums.UserIntegralOperTypeEnum;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.po.UserInfo;
import com.xzf.backend.entity.query.UserInfoQuery;
import com.xzf.backend.entity.vo.PaginationResultVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserInfoService {
	void register(String email,  String nickName, String password);

	SessionWebUserDto login(String email, String password);

	@Transactional(rollbackFor = Exception.class)
	void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral);

	UserInfo getUserInfoByUserId(String userId);

	void updateUserInfo(UserInfo userInfo, MultipartFile avatar);

	Integer findCountByParam(UserInfoQuery param);

	List <UserInfo> findListByParam(UserInfoQuery param);

	PaginationResultVO <UserInfo> findListByPage(UserInfoQuery param);

	void updateUserStatus(String userId);

	void sendMessage(String userId, String message, Integer integral);
}

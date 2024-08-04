package com.xzf.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.EUserStatus;
import com.xzf.backend.dto.SessionWebUserDto;
import com.xzf.backend.entity.pojo.UserInfo;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mapper.UserInfoMapper;
import com.xzf.backend.service.UserService;
import com.xzf.backend.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl <UserInfoMapper, UserInfo> implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserInfoMapper userInfoMapper;

	@Override
	public void register(String email, String nickName, String password) {
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("email");
		queryWrapper.eq("email", email);
		UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
		System.out.println(userInfo);
		if (null != userInfo) {
			throw new BusinessException("邮箱账号已经存在");
		}

		QueryWrapper<UserInfo> queryWrapper1 = new QueryWrapper<>();
		queryWrapper1.select("nick_name");
		queryWrapper1.eq("nick_name", nickName);
		userInfo = userInfoMapper.selectOne(queryWrapper1);
		if (null != userInfo) {
			throw new BusinessException("昵称已存在");
		}

		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);

		UserInfo insertInfo = new UserInfo();
		insertInfo.setUserId(Long.valueOf(userId));
		insertInfo.setNickName(nickName);
		insertInfo.setEmail(email);
		insertInfo.setPassword(StringTools.encodeMd5(password));
		insertInfo.setJoinTime(new Date());
		insertInfo.setStatus(EUserStatus.NORMAL.getCode());
		userInfoMapper.insert(insertInfo);
	}

	@Override
	public SessionWebUserDto login(String email, String password) {
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("email", email);
		UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

		if (null == userInfo || !userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或者密码错误");
		}

		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());

		UpdateWrapper <UserInfo> userInfoUpdateWrapper = new UpdateWrapper<>();
		userInfoUpdateWrapper.eq("user_id", userInfo.getUserId());
		userInfoMapper.update(updateInfo, userInfoUpdateWrapper);
		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setNickName(userInfo.getNickName());
		sessionWebUserDto.setUserId(String.valueOf(userInfo.getUserId()));
		return sessionWebUserDto;
	}
}

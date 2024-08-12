package com.xzf.backend.service.impl;

import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.enums.*;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.po.UserInfo;
import com.xzf.backend.entity.po.UserIntegralRecord;
import com.xzf.backend.entity.po.UserMessage;
import com.xzf.backend.entity.query.SimplePage;
import com.xzf.backend.entity.query.UserInfoQuery;
import com.xzf.backend.entity.query.UserIntegralRecordQuery;
import com.xzf.backend.entity.vo.PaginationResultVO;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumArticleMapper;
import com.xzf.backend.mappers.ForumCommentMapper;
import com.xzf.backend.mappers.UserInfoMapper;
import com.xzf.backend.mappers.UserIntegralRecordMapper;
import com.xzf.backend.service.UserInfoService;
import com.xzf.backend.service.UserMessageService;
import com.xzf.backend.utils.FileUtils;
import com.xzf.backend.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserIntegralRecordMapper <UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordMapper;

	@Resource
	private UserInfoMapper <UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private UserMessageService userMessageService;

	@Resource
	private FileUtils fileUtils;

	@Resource
	private ForumArticleMapper forumArticleMapper;

	@Resource
	private ForumCommentMapper forumCommentMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickName, String password) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null != userInfo) {
			throw new BusinessException("邮箱账号已经存在");
		}
		UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
		if (null != nickNameUser) {
			throw new BusinessException("昵称已经存在");
		}
		
		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setNickName(nickName);
		userInfo.setEmail(email);
		userInfo.setPassword(StringTools.encodeByMD5(password));
		userInfo.setJoinTime(new Date());
		userInfo.setCurrentIntegral(0);
		this.userInfoMapper.insert(userInfo);

		updateUserIntegral(userId, UserIntegralOperTypeEnum.REGISTER, UserIntegralChangeTypeEnum.ADD.getChangeType(), 5);

		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(userId);
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(new Date());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent("欢迎加入这个论坛,请友善交流,谢谢");
		userMessageService.add(userMessage);

	}

	@Override
	public SessionWebUserDto login(String email, String password) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null == userInfo || !userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或者密码错误");
		}
		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		this.userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());
		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setNickName(userInfo.getNickName());
		sessionWebUserDto.setUserId(userInfo.getUserId());
		return sessionWebUserDto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral) {
		integral = changeType * integral;

		if (integral == 0) {
			return;
		}

		UserInfo userInfo = userInfoMapper.selectByUserId(userId);
		if (UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changeType) && userInfo.getCurrentIntegral() + integral < 0) {
			integral = changeType * userInfo.getCurrentIntegral();
		}

		UserIntegralRecord record = new UserIntegralRecord();
		record.setUserId(userId);
		record.setOperType(operTypeEnum.getOperType());
		record.setCreateTime(new Date());
		record.setIntegral(integral);
		this.userIntegralRecordMapper.insert(record);

		Integer count = this.userInfoMapper.updateIntegral(userId, integral);
		if (count == 0) {
			throw new BusinessException("更新用户积分失败");
		}
	}

	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo, MultipartFile avatar) {
		userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());
		if (avatar != null) {
			fileUtils.uploadFile2Local(avatar, FileUploadTypeEnum.AVATAR, userInfo.getUserId());
		}
	}

	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	@Override
	public PaginationResultVO <UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List <UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserStatus( String userId) {
		UserInfo userInfo = new UserInfo();
		userInfoMapper.updateByUserId(userInfo, userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendMessage(String userId, String message, Integer integral) {
		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(userId);
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(new Date());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent(message);
		userMessageService.add(userMessage);

		UserIntegralChangeTypeEnum changeTypeEnum = UserIntegralChangeTypeEnum.ADD;
		if (integral != null && integral != 0) {
			if (integral < 0) {
				integral = integral * -1;
				changeTypeEnum = UserIntegralChangeTypeEnum.REDUCE;
			}
			updateUserIntegral(userId, UserIntegralOperTypeEnum.Other, changeTypeEnum.getChangeType(), integral);
		}
	}
}

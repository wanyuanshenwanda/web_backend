package com.xzf.backend.controller;


import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.cconst.enums.MessageTypeEnum;
import com.xzf.backend.cconst.enums.ResponseCodeEnum;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.dto.UserMessageCountDto;
import com.xzf.backend.entity.po.BoardIntegralConnection;
import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.po.UserInfo;
import com.xzf.backend.entity.query.ForumArticleQuery;
import com.xzf.backend.entity.query.LikeRecordQuery;
import com.xzf.backend.entity.query.UserIntegralRecordQuery;
import com.xzf.backend.entity.query.UserMessageQuery;
import com.xzf.backend.entity.vo.*;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumBoardMapper;
import com.xzf.backend.mappers.UserBoardConnectionMapper;
import com.xzf.backend.service.*;
import com.xzf.backend.utils.CopyTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController("userCenterController")
@RequestMapping("/ucenter")
public class UserCenterController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private UserIntegralRecordService userIntegralRecordService;

    @Resource
    private UserBoardConnectionMapper userBoardConnectionMapper;

    @Resource
    private ForumBoardMapper forumBoardMapper;

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getUserInfo(@VerifyParam(required = true) String userId) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null == userInfo ) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setUserId(userId);
        Integer postCount = forumArticleService.findCountByParam(articleQuery);
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setPostCount(postCount);

        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVO.setLikeCount(likeCount);
        userInfoVO.setCurrentIntegral(userInfo.getCurrentIntegral());
        return getSuccessResponseVO(userInfoVO);
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO updateUserInfo(HttpSession session, Integer sex,
                                     @VerifyParam(max = 100) String personDescription,
                                     MultipartFile avatar) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userDto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPersonDescription(personDescription);
        userInfoService.updateUserInfo(userInfo, avatar);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadUserIntegralRecord")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO loadUserIntegralRecord(HttpSession session, Integer pageNo, String createTimeStart, String createTimeEnd) {
        UserIntegralRecordQuery recordQuery = new UserIntegralRecordQuery();
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        recordQuery.setUserId(userDto.getUserId());
        recordQuery.setPageNo(pageNo);
        recordQuery.setCreateTimeStart(createTimeStart);
        recordQuery.setCreateTimeEnd(createTimeEnd);
        recordQuery.setOrderBy("record_id desc");
        PaginationResultVO resultVO = userIntegralRecordService.findListByPage(recordQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadUserArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserArticle(HttpSession session,
                                      @VerifyParam(required = true) String userId,
                                      @VerifyParam(required = true) Integer type,
                                      Integer pageNo) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null == userInfo ) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setOrderBy("post_time desc");
        if (type == 0) {
            articleQuery.setUserId(userId);
        } else if (type == 1) {
            articleQuery.setCommentUserId(userId);
        } else if (type == 2) {
            articleQuery.setLikeUserId(userId);
        }
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if (userDto != null) {
            articleQuery.setCurrentUserId(userDto.getUserId());
        }
        articleQuery.setPageNo(pageNo);
        PaginationResultVO<ForumArticle> result = forumArticleService.findListByPage(articleQuery);
        return getSuccessResponseVO(convert2PaginationVO(result, ForumArticleVO.class));
    }

    @RequestMapping("/getMessageCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getMessageCount(HttpSession session) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if (null == userDto) {
            return getSuccessResponseVO(new UserMessageCountDto());
        }
        return getSuccessResponseVO(userMessageService.getUserMessageCount(userDto.getUserId()));
    }


    @RequestMapping("/loadMessageList")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO loadMessageList(HttpSession session, @VerifyParam(required = true) String code, Integer pageNo) {
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByCode(code);
        if (null == messageTypeEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setPageNo(pageNo);
        userMessageQuery.setReceivedUserId(userDto.getUserId());
        userMessageQuery.setMessageType(messageTypeEnum.getType());
        userMessageQuery.setOrderBy("message_id desc");
        PaginationResultVO result = userMessageService.findListByPage(userMessageQuery);
        if (pageNo == null || pageNo == 1) {
            userMessageService.readMessageByType(userDto.getUserId(), messageTypeEnum.getType());
        }
        PaginationResultVO resultVO = convert2PaginationVO(result, UserMessageVO.class);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getUserActive")
    public ResponseVO getUserActive(HttpSession session,Integer pageNo) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (null == userDto) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        String userId = userDto.getUserId();
        List<BoardIntegralConnection> list = userBoardConnectionMapper.getAllbyUserId(userId);
        for(BoardIntegralConnection connection : list){
            connection.setBoardName(forumBoardMapper.getBoardName(connection.getBoardId()));
        }
        PaginationResultVO resultVO = new PaginationResultVO(list,pageNo);
        return getSuccessResponseVO(resultVO);
    }
}

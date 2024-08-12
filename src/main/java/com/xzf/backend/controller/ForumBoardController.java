package com.xzf.backend.controller;
import com.xzf.backend.cconst.enums.ResponseCodeEnum;
import com.xzf.backend.cconst.enums.UserStatusEnum;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.po.ForumBoard;
import com.xzf.backend.entity.po.UserBoardConnection;
import com.xzf.backend.entity.vo.ResponseVO;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumArticleMapper;
import com.xzf.backend.mappers.ForumBoardMapper;
import com.xzf.backend.mappers.UserBoardConnectionMapper;
import com.xzf.backend.service.ForumBoardService;
import com.xzf.backend.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
public class ForumBoardController extends BaseController {

	@Resource
	private ForumBoardService forumBoardService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private UserBoardConnectionMapper userBoardConnectionMapper;

	@Resource
	private ForumBoardMapper forumBoardMapper;
	@Autowired
	private ForumArticleMapper forumArticleMapper;

	@RequestMapping("/loadBoard")
	public ResponseVO loadBoard() {
		return getSuccessResponseVO(forumBoardService.getBoardTree());
	}
	@RequestMapping("/createBoard")
	public ResponseVO createBoard(HttpSession session ,String boardName, String description, MultipartFile cover) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		if(sessionWebUserDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		String createUserID = sessionWebUserDto.getUserId();
		Integer boardId = forumBoardService.createBoard(createUserID,boardName,description,cover);
		return getSuccessResponseVO(boardId);
	}

	@RequestMapping("/joinBoard")
	public ResponseVO joinBoard(HttpSession session ,Integer boardId) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		if(sessionWebUserDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		String userId = sessionWebUserDto.getUserId();
		UserBoardConnection userBoardConnection = userBoardConnectionMapper.selectByUserIdAndBoard(userId,boardId);
		if(userBoardConnection==null){
			userBoardConnectionMapper.addRecord(userId,boardId,3);
			forumBoardMapper.addJoinCount(boardId);
			ForumBoard forumBoard = (ForumBoard) forumBoardMapper.selectByBoardId(boardId);
			userInfoService.sendMessage(userId,"欢迎加入"+forumBoard.getBoardName(),0);
			return getSuccessResponseVO("欢迎加入"+forumBoard.getBoardName());
		}
		return getSuccessResponseVO("欢迎回来");
	}

	@RequestMapping("/getJoinedCount")
	public ResponseVO getJoinedCount(Integer boardId) {
		return getSuccessResponseVO(forumBoardMapper.getJoinCount(boardId));
	}

	@RequestMapping("/getUserStatus")
	public ResponseVO getUserStatus(HttpSession session ,Integer boardId) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		if(sessionWebUserDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		String userId = sessionWebUserDto.getUserId();
		if(userId.equals(forumBoardMapper.getCreateUserId(boardId))){
			return getSuccessResponseVO("吧主");
		}
		if(userBoardConnectionMapper.getIntegralByUserIdAndBoardId(userId,boardId)==null){
			return getSuccessResponseVO("未知生物");
		}
		Integer integral = userBoardConnectionMapper.getIntegralByUserIdAndBoardId(userId,boardId);
		if(integral>=0 && integral <20){
			return getSuccessResponseVO("新鲜血液");
		}
		if(integral>=20 && integral <50){
			return getSuccessResponseVO("中登");
		}
		if(integral>=50){
			return getSuccessResponseVO("老家伙");
		}
		return getSuccessResponseVO("未知生物");
	}
	@RequestMapping("/getUserStatus1")
	public ResponseVO getUserStatus(String userId ,Integer boardId) {
		String createUserId = forumBoardMapper.getCreateUserId(boardId);
		if(userId.equals(createUserId)){
			return getSuccessResponseVO("吧主");
		}
		Integer integral = userBoardConnectionMapper.getIntegralByUserIdAndBoardId(userId,boardId);
		if(integral>=0 && integral <20){
			return getSuccessResponseVO("新鲜血液");
		}
		if(integral>=20 && integral <50){
			return getSuccessResponseVO("中登");
		}
		if(integral>=50){
			return getSuccessResponseVO("老家伙");
		}
		return getSuccessResponseVO("未知生物");
	}

	@RequestMapping("/getUserStatus2")
	public ResponseVO getUserStatus(String userId ,String articleId) {
		Integer boardId = forumArticleMapper.getBoardIdByArticleId(articleId);
		String createUserId = forumBoardMapper.getCreateUserId(boardId);
		if(userId.equals(createUserId)){
			return getSuccessResponseVO("吧主");
		}
		Integer integral = userBoardConnectionMapper.getIntegralByUserIdAndBoardId(userId,boardId);
		if(integral>=0 && integral <20){
			return getSuccessResponseVO("新鲜血液");
		}
		if(integral>=20 && integral <50){
			return getSuccessResponseVO("中登");
		}
		if(integral>=50){
			return getSuccessResponseVO("老家伙");
		}
		return getSuccessResponseVO("未知生物");
	}
}

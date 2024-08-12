package com.xzf.backend.controller;

import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.cconst.enums.*;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.po.ForumBoard;
import com.xzf.backend.entity.po.LikeRecord;
import com.xzf.backend.entity.query.ForumArticleQuery;
import com.xzf.backend.entity.vo.*;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumBoardMapper;
import com.xzf.backend.mappers.UserBoardConnectionMapper;
import com.xzf.backend.service.ForumArticleService;
import com.xzf.backend.service.ForumBoardService;
import com.xzf.backend.service.LikeRecordService;
import com.xzf.backend.utils.CopyTools;
import com.xzf.backend.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends BaseController {

	@Resource
	private ForumBoardService forumBoardService;

	@Resource
	private ForumArticleService forumArticleService;


	@Resource
	private LikeRecordService likeRecordService;

	@Resource
	private UserBoardConnectionMapper userBoardConnectionMapper;
	@Autowired
	private ForumBoardMapper forumBoardMapper;


	@RequestMapping("/loadArticle")
	public ResponseVO loadArticle(HttpSession session, Integer boardId, Integer orderType, Integer pageNo) {
		ForumArticleQuery articleQuery = new ForumArticleQuery();
		articleQuery.setBoardId(boardId == null || boardId == 0 ? null : boardId);
		articleQuery.setPageNo(pageNo);
		SessionWebUserDto userDto = getUserInfoFromSession(session);
		if (userDto != null) {
			articleQuery.setCurrentUserId(userDto.getUserId());
		}
		ArticleOrderTypeEnum orderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
		orderTypeEnum = orderTypeEnum == null ? ArticleOrderTypeEnum.HOT : orderTypeEnum;
		articleQuery.setOrderBy(orderTypeEnum.getOrderSql());
		PaginationResultVO resultVO = forumArticleService.findListByPage(articleQuery);
		PaginationResultVO resultVO1 = convert2PaginationVO(resultVO, ForumArticleVO.class);
		return getSuccessResponseVO(resultVO1);
	}

	@RequestMapping("/getArticleDetail")
	public ResponseVO getArticleDetail(HttpSession session, String articleId) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);

		ForumArticle forumArticle = forumArticleService.readArticle(articleId);

		/*if (forumArticle == null ||   (sessionWebUserDto == null || !sessionWebUserDto.getUserId().equals(forumArticle.getUserId())))
		{
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}*/
		FormArticleDetailVO detailVO = new FormArticleDetailVO();
		detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

		if (sessionWebUserDto != null) {
			LikeRecord like = likeRecordService.getUserOperRecordByObjectIdAndUserIdAndOpType(articleId, sessionWebUserDto.getUserId(),
					OperRecordOpTypeEnum.ARTICLE_LIKE.getType());
			if (like != null) {
				detailVO.setHaveLike(true);
			}
		}
		return getSuccessResponseVO(detailVO);
	}

	@RequestMapping("/doLike")
	@GlobalInterceptor(checkLogin = true, checkParams = true)
	public ResponseVO doLike(HttpSession session, @VerifyParam(required = true) String articleId) {
		SessionWebUserDto userDto = getUserInfoFromSession(session);
		if(userDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		likeRecordService.doLike(articleId, userDto.getUserId(), userDto.getNickName(), OperRecordOpTypeEnum.ARTICLE_LIKE);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/loadBoard4Post")
	@GlobalInterceptor(checkLogin = true)
	public ResponseVO loadBoard4Post() {
		return getSuccessResponseVO(forumBoardService.getBoardTree());
	}

	@RequestMapping("/postArticle")
	@GlobalInterceptor(checkLogin = true, checkParams = true)
	public ResponseVO postArticle(HttpSession session,
	                              MultipartFile cover,
	                              Integer boardId,
	                              @VerifyParam(required = true, max = 150) String title,
	                              @VerifyParam String content,
	                              String markdownContent,
	                              @VerifyParam(required = true) Integer editorType,
	                              @VerifyParam(max = 200) String summary) {
		title = StringTools.escapeTitle(title);
		SessionWebUserDto userDto = getUserInfoFromSession(session);
		if(userDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		String userId = userDto.getUserId();
		if(userBoardConnectionMapper.selectByUserIdAndBoard(userId,boardId)==null){
			userBoardConnectionMapper.addRecord(userId,boardId, 3);
		}
		userBoardConnectionMapper.adddIntegral(3, userId, boardId);
		ForumArticle forumArticle = new ForumArticle();
		forumArticle.setBoardId(boardId);
		String boardName = forumBoardMapper.getBoardName(boardId);
		forumArticle.setBoardName(boardName);
		forumArticle.setTitle(title);
		forumArticle.setContent(content);
		if (EditorTypeEnum.MARKDOWN.getType().equals(editorType) && StringTools.isEmpty(markdownContent)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		forumArticle.setMarkdownContent(markdownContent);
		forumArticle.setEditorType(editorType);
		forumArticle.setSummary(summary);
		forumArticle.setUserId(userDto.getUserId());
		forumArticle.setNickName(userDto.getNickName());
		forumArticleService.postArticle(forumArticle, cover);
		return getSuccessResponseVO(forumArticle.getArticleId());
	}

	@RequestMapping("/articleDetail4Update")
	@GlobalInterceptor(checkLogin = true, checkParams = true)
	public ResponseVO articleDetail4Update(HttpSession session, @VerifyParam(required = true) String articleId) {
		SessionWebUserDto userDto = getUserInfoFromSession(session);
		if(userDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(articleId);
		if (forumArticle == null || !forumArticle.getUserId().equals(userDto.getUserId())) {
			throw new BusinessException("文章不存在或你无权编辑该文章");
		}
		FormArticleUpdateDetailVO detailVO = new FormArticleUpdateDetailVO();
		detailVO.setForumArticle(forumArticle);
		return getSuccessResponseVO(detailVO);
	}

	@RequestMapping("/updateArticle")
	@GlobalInterceptor(checkLogin = true, checkParams = true)
	public ResponseVO updateArticle(HttpSession session,
	                                MultipartFile cover,
	                                @VerifyParam(required = true) String articleId,
	                                Integer boardId,
	                                @VerifyParam(required = true, max = 150) String title,
	                                @VerifyParam(required = true) String content,
	                                String markdownContent,
	                                @VerifyParam(required = true) Integer editorType,
	                                @VerifyParam(max = 200) String summary) {
		title = StringTools.escapeTitle(title);
		SessionWebUserDto userDto = getUserInfoFromSession(session);
		if(userDto== null){
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		ForumArticle forumArticle = new ForumArticle();
		forumArticle.setArticleId(articleId);
		forumArticle.setBoardId(boardId);
		forumArticle.setTitle(title);
		forumArticle.setContent(content);
		forumArticle.setMarkdownContent(markdownContent);
		forumArticle.setEditorType(editorType);
		forumArticle.setSummary(summary);
		forumArticle.setUserId(userDto.getUserId());

		forumArticleService.updateArticle( forumArticle, cover);
		return getSuccessResponseVO(forumArticle.getArticleId());
	}

	@RequestMapping("/search")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO updateArticle(@VerifyParam(required = true) String keyword) {
		ForumArticleQuery query = new ForumArticleQuery();
		query.setTitleFuzzy(keyword);
		PaginationResultVO result = forumArticleService.findListByPage(query);
		return getSuccessResponseVO(result);
	}
}

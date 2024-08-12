package com.xzf.backend.controller;


import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.cconst.enums.*;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.dto.SessionWebUserDto;
import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.po.ForumComment;
import com.xzf.backend.entity.po.LikeRecord;
import com.xzf.backend.entity.query.ForumCommentQuery;
import com.xzf.backend.entity.vo.ResponseVO;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumBoardMapper;
import com.xzf.backend.mappers.UserBoardConnectionMapper;
import com.xzf.backend.service.ForumCommentService;
import com.xzf.backend.service.LikeRecordService;
import com.xzf.backend.service.impl.ForumArticleServiceImpl;
import com.xzf.backend.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/comment")
public class ForumCommentController extends BaseController {

    @Resource
    private ForumCommentService forumCommentService;

    @Resource
    private LikeRecordService likeRecordService;
	@Resource
	private ForumArticleServiceImpl forumArticleService;

    @Resource
    private UserBoardConnectionMapper userBoardConnectionMapper;

    @Resource
    private ForumBoardMapper forumBoardMapper;

    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadComment(HttpSession session, @VerifyParam(required = true) String articleId, Integer pageNo, Integer orderType) {
        ForumCommentQuery commentQuery = new ForumCommentQuery();
        commentQuery.setArticleId(articleId);
        commentQuery.setLoadChildren(true);
        String orderBy = orderType == null || orderType == 0 ? "good_count desc,comment_id asc" : "comment_id desc";
        commentQuery.setOrderBy("top_type desc," + orderBy);
        commentQuery.setPageNo(pageNo);

        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null) {
            commentQuery.setQueryLikeType(true);
            commentQuery.setCurrentUserId(userDto.getUserId());
        }
        commentQuery.setPageSize(PageSize.SIZE50.getSize());
        commentQuery.setpCommentId(0);
        return getSuccessResponseVO(forumCommentService.findListByPage(commentQuery));
    }

    @RequestMapping("/postComment")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO postComment(HttpSession session,
                                  @VerifyParam(required = true) String articleId,
                                  @VerifyParam(required = true) Integer pCommentId,
                                  @VerifyParam(min = 5, max = 800) String content,
                                  String replyUserId, MultipartFile image) {

        if (image == null && StringTools.isEmpty(content)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(articleId);
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        String userId = userDto.getUserId();
        Integer boardId = forumArticle.getBoardId();
        if(userBoardConnectionMapper.selectByUserIdAndBoard(userId,boardId)==null){
            userBoardConnectionMapper.addRecord(userId,boardId, 2);
            forumBoardMapper.addJoinCount(boardId);
        }
        userBoardConnectionMapper.adddIntegral(2, userId, boardId);
        ForumComment comment = new ForumComment();
        content = StringTools.escapeHtml(content);
        comment.setUserId(userDto.getUserId());
        comment.setNickName(userDto.getNickName());
        comment.setpCommentId(pCommentId);
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setReplyUserId(replyUserId);
        comment.setTopType(CommentTopTypeEnum.NO_TOP.getType());
        forumCommentService.postComment(comment, image);
        if (pCommentId != 0) {
            ForumCommentQuery commentQuery = new ForumCommentQuery();
            commentQuery.setArticleId(articleId);
            commentQuery.setpCommentId(pCommentId);
            commentQuery.setOrderBy("top_type desc,comment_id asc");
            commentQuery.setCurrentUserId(userDto.getUserId());
            List<ForumComment> children = forumCommentService.findListByParam(commentQuery);
            return getSuccessResponseVO(children);
        }
        return getSuccessResponseVO(comment);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO doLike(HttpSession session,
                             @VerifyParam(required = true) Integer commentId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        String objectId = String.valueOf(commentId);
        likeRecordService.doLike(objectId, userDto.getUserId(), userDto.getNickName(), OperRecordOpTypeEnum.COMMENT_LIKE);
        LikeRecord userOperRecord = likeRecordService.getUserOperRecordByObjectIdAndUserIdAndOpType(objectId, userDto.getUserId(),
                OperRecordOpTypeEnum.COMMENT_LIKE.getType());
        ForumComment comment = forumCommentService.getForumCommentByCommentId(commentId);
        comment.setLikeType(userOperRecord == null ? null : 1);
        return getSuccessResponseVO(comment);
    }

    @RequestMapping("/changeTopType")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO changeTopType(HttpSession session,
                                    @VerifyParam(required = true) Integer commentId, @VerifyParam(required = true) Integer topType) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto== null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        forumCommentService.changeTopType(userDto.getUserId(), commentId, topType);
        return getSuccessResponseVO(null);
    }
}

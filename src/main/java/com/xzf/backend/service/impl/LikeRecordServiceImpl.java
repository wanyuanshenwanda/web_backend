package com.xzf.backend.service.impl;

import com.xzf.backend.cconst.enums.*;
import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.po.ForumComment;
import com.xzf.backend.entity.po.LikeRecord;
import com.xzf.backend.entity.po.UserMessage;
import com.xzf.backend.entity.query.*;
import com.xzf.backend.entity.vo.PaginationResultVO;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.*;
import com.xzf.backend.service.LikeRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 用户操作记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

    @Resource
    private LikeRecordMapper <LikeRecord, LikeRecordQuery> likeRecordMapper;

    @Resource
    private ForumArticleMapper <ForumArticle, ForumArticleQuery> forumArticleMapper;

    @Resource
    private ForumCommentMapper <ForumComment, ForumCommentQuery> forumCommentMapper;

    @Resource
    private UserMessageMapper <UserMessage, UserMessageQuery> userMessageMapper;

    @Resource
    private UserBoardConnectionMapper userBoardConnectionMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<LikeRecord> findListByParam(LikeRecordQuery param) {
        return this.likeRecordMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(LikeRecordQuery param) {
        return this.likeRecordMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO <LikeRecord> findListByPage(LikeRecordQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<LikeRecord> list = this.findListByParam(param);
        return new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer add(LikeRecord bean) {
        return this.likeRecordMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<LikeRecord> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.likeRecordMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.likeRecordMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据OpId获取对象
     */
    @Override
    public LikeRecord getUserOperRecordByOpId(Integer opId) {
        return this.likeRecordMapper.selectByOpId(opId);
    }

    /**
     * 根据OpId修改
     */
    @Override
    public Integer updateUserOperRecordByOpId(LikeRecord bean, Integer opId) {
        return this.likeRecordMapper.updateByOpId(bean, opId);
    }

    /**
     * 根据OpId删除
     */
    @Override
    public Integer deleteUserOperRecordByOpId(Integer opId) {
        return this.likeRecordMapper.deleteByOpId(opId);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType获取对象
     */
    @Override
    public LikeRecord getUserOperRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType修改
     */
    @Override
    public Integer updateUserOperRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType删除
     */
    @Override
    public Integer deleteUserOperRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doLike(String objectId, String userId, String nickName, OperRecordOpTypeEnum opTypeEnum) {
        UserMessage userMessage = new UserMessage();
        userMessage.setCreateTime(new Date());
        LikeRecord likeRecord = null;
        switch (opTypeEnum) {
            case ARTICLE_LIKE:
                likeRecord = articleLike(objectId, userId, opTypeEnum);
                ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
                userMessage.setArticleId(objectId);
                userMessage.setArticleTitle(forumArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
                userMessage.setCommentId(0);
                userMessage.setReceivedUserId(forumArticle.getUserId());
                break;
            case COMMENT_LIKE:
                likeRecord = commentLike(objectId, userId, opTypeEnum);
                ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
                ForumArticle commentArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
                userMessage.setArticleId(commentArticle.getArticleId());
                userMessage.setArticleTitle(commentArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
                userMessage.setCommentId(Integer.parseInt(objectId));
                userMessage.setReceivedUserId(forumComment.getUserId());
                userMessage.setMessageContent(forumComment.getContent());
                break;
        }
        userMessage.setSendUserId(userId);
        userMessage.setSendNickName(nickName);
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        if (likeRecord == null && !userId.equals(userMessage.getReceivedUserId())) {
            userMessageMapper.insert(userMessage);
        }
    }

    public LikeRecord articleLike(String objectId, String userId, OperRecordOpTypeEnum opTypeEnum) {
        LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
        if (record != null) {
            this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), -1, objectId);
        } else {
            ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
            if (null == forumArticle) {
                throw new BusinessException("文章不存在");
            }
            Integer boardId = forumArticle.getBoardId();
            if(userBoardConnectionMapper.selectByUserIdAndBoard(userId, boardId) == null){
                userBoardConnectionMapper.addRecord(userId, boardId, 1);
            }
            userBoardConnectionMapper.adddIntegral(1, userId, boardId);
            LikeRecord operRecord = new LikeRecord();
            operRecord.setObjectId(objectId);
            operRecord.setUserId(userId);
            operRecord.setOpType(opTypeEnum.getType());
            operRecord.setCreateTime(new Date());
            operRecord.setAuthorUserId(forumArticle.getUserId());
            this.likeRecordMapper.insert(operRecord);
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), 1, objectId);
        }
        return record;
    }

    public LikeRecord commentLike(String objectId, String userId, OperRecordOpTypeEnum opTypeEnum) {
        LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
        if (record != null) {
            this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
            forumCommentMapper.updateCommenCount(-1, Integer.parseInt(objectId));
        } else {
            ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
            if (null == forumComment) {
                throw new BusinessException("评论不存在");
            }
            ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
            Integer boardId =  forumArticle.getBoardId();
            if(userBoardConnectionMapper.selectByUserIdAndBoard(userId,boardId)==null){
                userBoardConnectionMapper.addRecord(userId,boardId, 1);
            }
            userBoardConnectionMapper.adddIntegral(1, userId, boardId);
            LikeRecord likeRecord = new LikeRecord();
            likeRecord.setObjectId(objectId);
            likeRecord.setUserId(userId);
            likeRecord.setOpType(opTypeEnum.getType());
            likeRecord.setCreateTime(new Date());
            likeRecord.setAuthorUserId(forumComment.getUserId());
            this.likeRecordMapper.insert(likeRecord);
            forumCommentMapper.updateCommenCount(1, Integer.parseInt(objectId));
        }
        return record;
    }


}
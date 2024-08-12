package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class ForumCommentQuery extends BaseParam {
    private Integer commentId;

    private Integer pCommentId;

    private String articleId;

    private String articleIdFuzzy;

    private String content;

    private String contentFuzzy;

    private String imgPath;

    private String imgPathFuzzy;

    private String userId;

    private String userIdFuzzy;

    private String nickName;

    private String nickNameFuzzy;

    private String replyUserId;

    private String replyUserIdFuzzy;

    private String replyNickName;

    private String replyNickNameFuzzy;

    private Integer topType;

    private String postTime;

    private String postTimeStart;

    private String postTimeEnd;

    private Integer goodCount;

    private Boolean loadChildren;

    private Boolean queryLikeType;

    private String currentUserId;

    private Boolean onlyQueryChildren;


    public void setpCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }
}

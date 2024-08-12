package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class UserMessageQuery extends BaseParam {

    private Integer messageId;

    private String receivedUserId;

    private String receivedUserIdFuzzy;

    private String articleId;

    private String articleIdFuzzy;

    private String articleTitle;

    private String articleTitleFuzzy;

    private Integer commentId;

    private String sendUserId;

    private String sendUserIdFuzzy;

    private String sendNickName;

    private String sendNickNameFuzzy;

    private Integer messageType;

    private String messageContent;

    private String messageContentFuzzy;

    private Integer status;

    private String createTime;

    private String createTimeStart;

    private String createTimeEnd;

}

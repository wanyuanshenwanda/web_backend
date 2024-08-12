package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class ForumArticleQuery extends BaseParam {

    private String articleId;

    private String articleIdFuzzy;

    private Integer boardId;

    private String boardName;

    private String boardNameFuzzy;

    private String userId;

    private String userIdFuzzy;

    private String nickName;

    private String nickNameFuzzy;

    private String title;

    private String titleFuzzy;

    private String cover;

    private String coverFuzzy;

    private String content;

    private String contentFuzzy;

    private String markdownContent;

    private String markdownContentFuzzy;

    private Integer editorType;

    private String summary;

    private String summaryFuzzy;

    private String postTime;

    private String postTimeStart;

    private String postTimeEnd;

    private String lastUpdateTime;

    private String lastUpdateTimeStart;

    private String lastUpdateTimeEnd;

    private Integer readCount;

    private Integer goodCount;

    private Integer commentCount;

    private Integer topType;

    private String commentUserId;

    private String likeUserId;

    private String keyword;

    private String currentUserId;
}

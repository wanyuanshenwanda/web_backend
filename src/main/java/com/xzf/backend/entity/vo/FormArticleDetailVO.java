package com.xzf.backend.entity.vo;


import lombok.Data;

@Data
public class FormArticleDetailVO {
    private ForumArticleVO forumArticle;
    private Boolean haveLike = false;
}

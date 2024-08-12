package com.xzf.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
public class ForumArticleVO implements Serializable {
    private String articleId;

    private Integer boardId;

    private String boardName;

    private String userId;

    private String nickName;

    private String title;

    private String cover;

    private String content;

    private String summary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    private Integer readCount;

    private Integer goodCount;

    private Integer commentCount;

    private Integer topType;
}

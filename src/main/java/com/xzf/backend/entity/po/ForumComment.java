package com.xzf.backend.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class ForumComment implements Serializable {

    private Integer commentId;

    private Integer pCommentId;

    private String articleId;

    private String content;

    private String imgPath;

    private String userId;

    private String nickName;

    private String replyUserId;

    private String replyNickName;

    private Integer topType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    private Integer goodCount;

    private Integer likeType;

    private List<ForumComment> children;

    public Integer getpCommentId() {
        return pCommentId;
    }

    public void setpCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }
}

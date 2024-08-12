package com.xzf.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserMessageVO implements Serializable {

    private Integer messageId;

    private String articleId;

    private String articleTitle;

    private Integer commentId;

    private String sendUserId;

    private String sendNickName;

    private Integer messageType;

    private String messageContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

package com.xzf.backend.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzf.backend.cconst.enums.DateTimePatternEnum;
import com.xzf.backend.utils.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserMessage implements Serializable {

    private Integer messageId;

    private String receivedUserId;

    private String articleId;

    private String articleTitle;

    private Integer commentId;

    private String sendUserId;

    private String sendNickName;

    private Integer messageType;

    private String messageContent;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

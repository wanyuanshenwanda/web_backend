package com.xzf.backend.entity.dto;

import lombok.Data;

@Data
public class UserMessageCountDto {
    private Long total = 0L;
    public Long sys = 0L;
    public Long reply = 0L;
    private Long likePost = 0L;
    private Long likeComment = 0L;
    private Long downloadAttachment = 0L;
}

package com.xzf.backend.entity.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Comment {
	private UserInfo author;
	private String content;
	private Date comment_time;
	private int like_count;
	private List<Comment> replies;
}

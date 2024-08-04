package com.xzf.backend.entity.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Post {
	private UserInfo author;
	private String content;
	private String[] post_image_urls;
	private Date post_time;
	private List<Comment> comments;
	private int like_count;
	private int collect_count;
	private int watch_count;
}

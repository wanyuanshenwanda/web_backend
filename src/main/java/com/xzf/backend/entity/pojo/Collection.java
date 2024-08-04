package com.xzf.backend.entity.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Collection {
	private String title;
	private UserInfo collector;
	private List<Post> collected_posts;
}

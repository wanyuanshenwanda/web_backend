package com.xzf.backend.entity.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PostBar {
	private List<Post> posts;
	private String post_bar_name;
	private String post_bar_description;
	private String post_bar_image_url;
	private List<UserInfo> followers;
}

package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class UserQueryVo extends BasePageQuery {
	private String nickNameFuzzy;

	private Integer sex;

	private Integer status;

	public UserQueryVo() {}
}
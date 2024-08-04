package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class UserInfoQuery {
	private String userId;
	private String userIdFuzzy;
	private String nickName;
	private String nickNameFuzzy;
	private String email;
	private String emailFuzzy;
	private String password;
	private String passwordFuzzy;
	private Integer sex;
	private String personalDescription;
	private String personalDescriptionFuzzy;
	private String registerTime;
	private String registerTimeStart;
	private String registerTimeEnd;
	private String lastLoginTime;
	private String lastLoginTimeStart;
	private String lastLoginTimeEnd;
	private Integer status;
}

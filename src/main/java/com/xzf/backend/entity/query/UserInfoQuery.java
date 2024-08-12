package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class UserInfoQuery extends BaseParam {

	private String userId;

	private String userIdFuzzy;

	private String nickName;

	private String nickNameFuzzy;

	private String email;

	private String emailFuzzy;

	private String password;

	private String passwordFuzzy;

	private Integer sex;

	private String personDescription;

	private String personDescriptionFuzzy;

	private String joinTime;

	private String joinTimeStart;

	private String joinTimeEnd;

	private String lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	private Integer totalIntegral;

	private Integer currentIntegral;


}

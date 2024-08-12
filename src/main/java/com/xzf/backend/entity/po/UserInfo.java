package com.xzf.backend.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;


@TableName(value = "user_info")
@Data
public class UserInfo {
	@TableId
	private String userId;

	private String nickName;

	private String email;

	private String password;

	private Integer sex;

	private String personDescription;

	private Date joinTime;

	private Date lastLoginTime;

	private Integer totalIntegral;

	private Integer currentIntegral;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}

package com.xzf.backend.cconst.enums;

public enum UserStatusEnum {
	CREATER("创建者"),
	NORMAL("初级"),
	SENIOR("中级"),
	SUPER("高级");


	private String desc;

	UserStatusEnum(String desc) {
		this.desc = desc;
	}
}

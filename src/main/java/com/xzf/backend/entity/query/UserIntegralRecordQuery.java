package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class UserIntegralRecordQuery extends BaseParam {

	private Integer recordId;

	private String userId;

	private String userIdFuzzy;

	private Integer operType;

	private Integer integral;

	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

}

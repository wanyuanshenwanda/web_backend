package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class LikeRecordQuery extends BaseParam {
	private Integer opId;

	private Integer opType;

	private String objectId;

	private String objectIdFuzzy;

	private String userId;

	private String userIdFuzzy;

	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	private String authorUserId;

	private String authorUserIdFuzzy;
}

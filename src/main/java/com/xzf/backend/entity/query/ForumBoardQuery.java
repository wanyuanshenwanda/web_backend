package com.xzf.backend.entity.query;

import lombok.Data;

@Data
public class ForumBoardQuery extends BaseParam {

	private Integer boardId;

	private String boardName;

	private String boardNameFuzzy;

	private String cover;

	private String coverFuzzy;

	private String boardDesc;

	private String boardDescFuzzy;

	private Integer sort;

	private Integer joinedCount;

	private String createUserId;

}

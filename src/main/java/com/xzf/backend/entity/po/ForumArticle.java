package com.xzf.backend.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

@Data
public class ForumArticle implements Serializable {

	private String articleId;

	private Integer boardId;

	private String boardName;

	private String userId;

	private String nickName;

	private String title;

	private String cover;

	private String content;

	private String markdownContent;

	private Integer editorType;

	private String summary;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date postTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	private Integer readCount;

	private Integer goodCount;

	private Integer commentCount;

	/**
	 * 0未置顶  1:已置顶
	 */
	private Integer topType;

}

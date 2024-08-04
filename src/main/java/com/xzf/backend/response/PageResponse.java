package com.xzf.backend.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
	private Long totalCount;
	private Integer pageNo;
	private Long pageSize;
	private Long pageTotal;
	private List <T> list;
}

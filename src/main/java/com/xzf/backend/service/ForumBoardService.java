package com.xzf.backend.service;

import com.xzf.backend.entity.po.ForumBoard;
import com.xzf.backend.entity.query.ForumBoardQuery;
import com.xzf.backend.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ForumBoardService {
	/**
	 * 根据条件查询列表
	 */
	List <ForumBoard> findListByParam(ForumBoardQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ForumBoardQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO <ForumBoard> findListByPage(ForumBoardQuery param);

	/**
	 * 新增
	 */
	Integer add(ForumBoard bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ForumBoard> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ForumBoard> listBean);

	/**
	 * 根据BoardId查询对象
	 */
	ForumBoard getForumBoardByBoardId(Integer boardId);


	/**
	 * 根据BoardId修改
	 */
	Integer updateForumBoardByBoardId(ForumBoard bean, Integer boardId);


	/**
	 * 根据BoardId删除
	 */
	Integer deleteForumBoardByBoardId(Integer boardId);

	List<ForumBoard> getBoardTree();

	void saveForumBoard(ForumBoard forumBoard);

	void changeSort(String boardIds);

	Integer createBoard(String createUserId ,String boardName, String description, MultipartFile cover);
}

package com.xzf.backend.service;

import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.query.ForumArticleQuery;
import com.xzf.backend.entity.vo.PaginationResultVO;
import com.xzf.backend.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ForumArticleService {

	/**
	 * 根据条件查询列表
	 */
	List <ForumArticle> findListByParam(ForumArticleQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ForumArticleQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO <ForumArticle> findListByPage(ForumArticleQuery param);

	/**
	 * 新增
	 */
	Integer add(ForumArticle bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ForumArticle> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ForumArticle> listBean);

	/**
	 * 根据ArticleId查询对象
	 */
	ForumArticle getForumArticleByArticleId(String articleId);


	/**
	 * 根据ArticleId修改
	 */
	Integer updateForumArticleByArticleId(ForumArticle bean, String articleId);


	/**
	 * 根据ArticleId删除
	 */
	Integer deleteForumArticleByArticleId(String articleId);

	/**
	 * 发布文章
	 *
	 * @param article
	 * @param cover
	 * @throws BusinessException
	 */
	void postArticle( ForumArticle article, MultipartFile cover) throws BusinessException;

	/**
	 * 更新文章
	 *
	 * @param article
	 * @param cover
	 * @throws BusinessException
	 */
	void updateArticle(ForumArticle article, MultipartFile cover
	                   ) throws BusinessException;

	ForumArticle readArticle(String artcileId);

	/**
	 * 更新板块
	 *
	 * @param articleId
	 * @param pBoardId
	 * @param boardId
	 */
	void updateBoard(String articleId, Integer pBoardId, Integer boardId);

	/**
	 * 删除文章
	 *
	 * @param articleIds
	 */
	void delArticle(String articleIds);


}

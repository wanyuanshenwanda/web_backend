package com.xzf.backend.service.impl;


import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.enums.*;
import com.xzf.backend.entity.dto.FileUploadDto;
import com.xzf.backend.entity.po.ForumArticle;
import com.xzf.backend.entity.po.ForumBoard;
import com.xzf.backend.entity.query.ForumArticleQuery;
import com.xzf.backend.entity.query.SimplePage;
import com.xzf.backend.entity.vo.PaginationResultVO;
import com.xzf.backend.exception.BusinessException;
import com.xzf.backend.mappers.ForumArticleMapper;
import com.xzf.backend.service.ForumArticleService;
import com.xzf.backend.service.ForumBoardService;
import com.xzf.backend.service.UserInfoService;
import com.xzf.backend.utils.FileUtils;
import com.xzf.backend.utils.ImageUtils;
import com.xzf.backend.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;


@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

	@Resource
	private ForumArticleMapper <ForumArticle, ForumArticleQuery> forumArticleMapper;


	@Resource
	private ForumBoardService forumBoardService;

	@Resource
	private UserInfoService userInfoService;


	@Resource
	private ImageUtils imageUtils;

	@Resource
	private FileUtils fileUtils;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ForumArticle> findListByParam(ForumArticleQuery param) {
		return this.forumArticleMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ForumArticleQuery param) {
		return this.forumArticleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumArticle> list = this.findListByParam(param);
		PaginationResultVO<ForumArticle> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumArticle bean) {
		return this.forumArticleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据ArticleId获取对象
	 */
	@Override
	public ForumArticle getForumArticleByArticleId(String articleId) {
		return this.forumArticleMapper.selectByArticleId(articleId);
	}

	/**
	 * 根据ArticleId修改
	 */
	@Override
	public Integer updateForumArticleByArticleId(ForumArticle bean, String articleId) {
		return this.forumArticleMapper.updateByArticleId(bean, articleId);
	}

	/**
	 * 根据ArticleId删除
	 */
	@Override
	public Integer deleteForumArticleByArticleId(String articleId) {
		return this.forumArticleMapper.deleteByArticleId(articleId);
	}

	@Override
	public void postArticle( ForumArticle article, MultipartFile cover) throws BusinessException {

		resetBoardInfo(article);

		String articleId = StringTools.getRandomString(Constants.LENGTH_15);
		article.setArticleId(articleId);
		article.setPostTime(new Date());
		article.setLastUpdateTime(new Date());

		if (cover != null) {
			FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, FileUploadTypeEnum.ARTICLE_COVER, Constants.FILE_FOLDER_IMAGE);
			article.setCover(fileUploadDto.getLocalPath());
		}

		//替换图片
		String content = article.getContent();
		if (!StringTools.isEmpty(content)) {
			String month = imageUtils.resetImageHtml(content);
			//避免替换博客中template关键，所以前后带上/
			String replaceMonth = "/" + month + "/";
			content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
			article.setContent(content);
			String markdownContent = article.getMarkdownContent();
			if (!StringTools.isEmpty(markdownContent)) {
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}

		this.forumArticleMapper.insert(article);

		Integer postIntegral = 5;

		this.userInfoService.updateUserIntegral(article.getUserId(),
				UserIntegralOperTypeEnum.POST_COMMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), postIntegral);

	}

	@Override
	public void updateArticle( ForumArticle article, MultipartFile cover) throws BusinessException {
		ForumArticle dbInfo = forumArticleMapper.selectByArticleId(article.getArticleId());
		if ( !dbInfo.getUserId().equals(article.getUserId())) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		resetBoardInfo( article);
		article.setLastUpdateTime(new Date());

		if (cover != null) {
			FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, FileUploadTypeEnum.ARTICLE_COVER, Constants.FILE_FOLDER_IMAGE);
			article.setCover(fileUploadDto.getLocalPath());
		}

		//替换图片
		String content = article.getContent();
		if (!StringTools.isEmpty(content)) {
			String month = imageUtils.resetImageHtml(content);
			//避免替换博客中template关键，所以前后带上/
			String replaceMonth = "/" + month + "/";
			content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
			article.setContent(content);
			String markdownContent = article.getMarkdownContent();
			if (!StringTools.isEmpty(markdownContent)) {
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}

		this.forumArticleMapper.updateByArticleId(article, article.getArticleId());
	}

	@Override
	public ForumArticle readArticle(String artcileId) {
		ForumArticle forumArticle = this.forumArticleMapper.selectByArticleId(artcileId);
		if (forumArticle == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), 1, artcileId);
		return forumArticle;
	}

	@Override
	public void updateBoard(String articleId, Integer pBoardId, Integer boardId) {

	}

	@Override
	public void delArticle(String articleIds) {

	}

	private void resetBoardInfo(ForumArticle article) {
		if (article.getBoardId() != null && article.getBoardId() != 0) {
			ForumBoard board = forumBoardService.getForumBoardByBoardId(article.getBoardId());
			if (null == board ) {
				throw new BusinessException("板块不存在");
			}
			article.setBoardName(board.getBoardName());
		} else {
			article.setBoardId(0);
			article.setBoardName("");
		}
	}

}
package com.xzf.backend.service.impl;

import com.xzf.backend.cconst.Constants;
import com.xzf.backend.cconst.enums.FileUploadTypeEnum;
import com.xzf.backend.cconst.enums.PageSize;
import com.xzf.backend.entity.dto.FileUploadDto;
import com.xzf.backend.entity.po.ForumBoard;
import com.xzf.backend.entity.query.ForumBoardQuery;
import com.xzf.backend.entity.query.SimplePage;
import com.xzf.backend.entity.vo.PaginationResultVO;
import com.xzf.backend.mappers.ForumBoardMapper;
import com.xzf.backend.service.ForumBoardService;
import com.xzf.backend.utils.FileUtils;
import com.xzf.backend.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service("forumBoardService")
public class ForumBoardServiceImpl implements ForumBoardService {

	@Resource
	private FileUtils fileUtils;

	@Resource
	private ForumBoardMapper <ForumBoard, ForumBoardQuery> forumBoardMapper;

	@Override
	public List<ForumBoard> findListByParam(ForumBoardQuery param) {
		return this.forumBoardMapper.selectList(param);
	}

	@Override
	public Integer findCountByParam(ForumBoardQuery param) {
		return this.forumBoardMapper.selectCount(param);
	}

	@Override
	public PaginationResultVO<ForumBoard> findListByPage(ForumBoardQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumBoard> list = this.findListByParam(param);
		return new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumBoard bean) {
		return this.forumBoardMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumBoardMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumBoardMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据BoardId获取对象
	 */
	@Override
	public ForumBoard getForumBoardByBoardId(Integer boardId) {
		return this.forumBoardMapper.selectByBoardId(boardId);
	}

	/**
	 * 根据BoardId修改
	 */
	@Override
	public Integer updateForumBoardByBoardId(ForumBoard bean, Integer boardId) {
		return this.forumBoardMapper.updateByBoardId(bean, boardId);
	}

	/**
	 * 根据BoardId删除
	 */
	@Override
	public Integer deleteForumBoardByBoardId(Integer boardId) {
		return this.forumBoardMapper.deleteByBoardId(boardId);
	}

	@Override
	public List<ForumBoard> getBoardTree() {
		ForumBoardQuery forumBoardQuery = new ForumBoardQuery();
		forumBoardQuery.setOrderBy("sort asc");
		return this.forumBoardMapper.selectList(forumBoardQuery);
	}

	@Override
	public void saveForumBoard(ForumBoard forumBoard) {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeSort(String boardIds) {
		String[] boardIdArray = boardIds.split(",");
		Integer index = 1;
		for (String boardIdStr : boardIdArray) {
			Integer boardId = Integer.parseInt(boardIdStr);
			ForumBoard board = new ForumBoard();
			board.setSort(index);
			forumBoardMapper.updateByBoardId(board, boardId);
			index++;
		}
	}

	@Override
	public Integer createBoard(String createUserId, String boardName, String description, MultipartFile cover){
		ForumBoard forumBoard = new ForumBoard();
		forumBoard.setSort(0);
		Integer boardId = Integer.parseInt(StringTools.getRandomNumber(5));
		forumBoard.setBoardId(boardId);
		forumBoard.setCreateUserId(createUserId);
		forumBoard.setBoardName(boardName);
		forumBoard.setBoardDesc(description);
		forumBoard.setJoinedCount(1);
		if (cover != null) {
			FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, FileUploadTypeEnum.BOARD_COVER, Constants.FILE_FOLDER_IMAGE);
			forumBoard.setCover(fileUploadDto.getLocalPath());
		}
		forumBoardMapper.insert(forumBoard);
		return boardId;
	}
}

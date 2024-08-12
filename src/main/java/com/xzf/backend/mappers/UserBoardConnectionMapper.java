package com.xzf.backend.mappers;

import com.xzf.backend.entity.po.BoardIntegralConnection;
import com.xzf.backend.entity.po.UserBoardConnection;

import java.util.List;

public interface UserBoardConnectionMapper {

	UserBoardConnection selectByUserIdAndBoard(String userId,Integer boardId);

	void adddIntegral(Integer addIntegral, String userId, Integer boardId);

	void addRecord(String userId, Integer boardId, Integer integral);

	Integer getIntegralByUserIdAndBoardId(String userId, Integer boardId);

	List <BoardIntegralConnection> getAllbyUserId(String userId);//获取用户所有板块积分
}

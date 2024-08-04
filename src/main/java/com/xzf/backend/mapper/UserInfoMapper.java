package com.xzf.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzf.backend.entity.pojo.UserInfo;
import com.xzf.backend.entity.query.UserQueryVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserInfoMapper extends BaseMapper <UserInfo> {
	int deleteByPrimaryKey(Long id);

	int insert(UserInfo record);

	int insertSelective(UserInfo record);

	UserInfo selectByPrimaryKey(Long id);

	Integer updateIntegral(@Param("userId") Long userId, Integer integral);

	List <UserInfo> loadUserList(UserQueryVo query);

}

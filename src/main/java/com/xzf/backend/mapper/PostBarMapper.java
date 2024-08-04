package com.xzf.backend.mapper;

import org.apache.ibatis.annotations.Param;

public interface PostBarMapper<T> {

	Integer updateByBarId(@Param("bean") T t, @Param("barId") Integer barId);

	Integer deleteByBarId(@Param("barId") Integer barId);

	T selectByBarId(@Param("barId") Integer barId);
}

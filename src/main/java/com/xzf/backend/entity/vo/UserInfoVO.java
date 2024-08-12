package com.xzf.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserInfoVO implements Serializable {

    private String userId;

    private String nickName;

    private Integer sex;

    private String personDescription;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastLoginTime;

    private Integer currentIntegral;

    public Integer postCount;

    public Integer likeCount;
}

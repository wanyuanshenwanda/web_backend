package com.xzf.backend.entity.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzf.backend.cconst.enums.UserIntegralOperTypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserIntegralRecord implements Serializable {

    private Integer recordId;

    private String userId;

    private Integer operType;

    private Integer integral;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String operTypeName;

    public String getOperTypeName() {
        UserIntegralOperTypeEnum opTypeEnum = UserIntegralOperTypeEnum.getByType(operType);
        return opTypeEnum == null ? "" : opTypeEnum.getDesc();
    }
}

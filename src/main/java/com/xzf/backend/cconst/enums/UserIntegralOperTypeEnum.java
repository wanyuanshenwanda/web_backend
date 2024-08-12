package com.xzf.backend.cconst.enums;


public enum UserIntegralOperTypeEnum {
    REGISTER(1, "账号注册"),
    POST_COMMENT(4, "发布评论"),
    POST_ARTICLE(5, "发布文章"),
    Other(6, "未知操作"),
    DEL_ARTICLE(7, "文章被删除"),
    DEL_COMMENT(8, "评论被删除");

    private Integer operType;

    private String desc;

    UserIntegralOperTypeEnum(Integer operType, String desc) {
        this.operType = operType;
        this.desc = desc;
    }

    public Integer getOperType() {
        return operType;
    }

    public String getDesc() {
        return desc;
    }

    public static UserIntegralOperTypeEnum getByType(Integer operType) {
        for (UserIntegralOperTypeEnum typeEnum : UserIntegralOperTypeEnum.values()) {
            if (typeEnum.getOperType().equals(operType)) {
                return typeEnum;
            }
        }
        return null;
    }
}

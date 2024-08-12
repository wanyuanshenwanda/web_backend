package com.xzf.backend.cconst.enums;

public enum ResponseCodeEnum {
    CODE_200(200, "请求成功,太酷啦"),
    CODE_404(404, "服务器又又又抽风了,联系下亲爱的deverloper吧"),
    CODE_600(600, "难道你是梁上君子?不要攻击我的项目求求了"),
    CODE_601(601, "信息已经存在"),
    CODE_500(500, "服务器抽风了,不妨刷新一下吧"),
    CODE_900(900, "http请求超时"),
    CODE_901(901, "尚未登录或登录超时，请重新登录试试吧");

    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

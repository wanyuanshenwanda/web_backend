package com.xzf.backend.controller;

import com.xzf.backend.annotation.GlobalInterceptor;
import com.xzf.backend.annotation.VerifyParam;
import com.xzf.backend.controller.base.BaseController;
import com.xzf.backend.entity.query.UserInfoQuery;
import com.xzf.backend.entity.vo.ResponseVO;
import com.xzf.backend.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadUserList")
    @GlobalInterceptor
    public ResponseVO loadUserList(UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("join_time desc");
        return getSuccessResponseVO(userInfoService.findListByPage(userInfoQuery));
    }


    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateUserStatus(@VerifyParam(required = true) String userId) {
        userInfoService.updateUserStatus(userId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/sendMessage")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO sendMessage(@VerifyParam(required = true) String userId,
                                  @VerifyParam(required = true) String message,
                                  Integer integral) {
        userInfoService.sendMessage(userId, message, integral);
        return getSuccessResponseVO(null);
    }
}

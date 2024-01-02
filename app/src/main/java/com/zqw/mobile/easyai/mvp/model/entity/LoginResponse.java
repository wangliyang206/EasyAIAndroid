package com.zqw.mobile.easyai.mvp.model.entity;

/**
 * 包名： PACKAGE_NAME
 * 对象名： LoginResponse
 * 描述：登录    响应结构
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/5/31 11:28
 */

public class LoginResponse {
    /* Token */
    private String token = "";

    /* 用户id */
    private String userId = "";

    /* 姓名 */
    private String userName = "";

    /* 手机号 */
    private String userPhone = "";

    /* 头像 */
    private String accountImage = "";

    public LoginResponse() {
    }

    public LoginResponse(String token, String userId, String userName, String userPhone, String accountImage) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.accountImage = accountImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(String accountImage) {
        this.accountImage = accountImage;
    }
}

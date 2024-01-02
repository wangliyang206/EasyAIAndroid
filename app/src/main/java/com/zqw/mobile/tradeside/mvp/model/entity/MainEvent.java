package com.zqw.mobile.tradeside.mvp.model.entity;

/**
 * 包名： com.zqw.mobile.tradeside.mvp.model.entity
 * 对象名： MainEvent
 * 描述：首页监听
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/6/2 17:52
 */

public class MainEvent {
    public MainEvent() {
    }

    public MainEvent(int code) {
        this.code = code;
    }

    /**
     * 忘记密码 - 返回
     */
    public MainEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 0 = 上传极光推送id；
     */
    private int code = 0;
    private String msg = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

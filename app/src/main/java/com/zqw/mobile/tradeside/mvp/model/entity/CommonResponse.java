package com.zqw.mobile.tradeside.mvp.model.entity;

import java.util.List;

/**
 * 包名： com.zqw.mobile.recycling.model
 * 对象名： CommonResponse
 * 描述：通用响应
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/28 17:52
 */

public class CommonResponse {
    public CommonResponse() {
    }

    /*1表示成功，0表示失败*/
    private int succ = 0;
    // ishaveUser为0时此字段为空字符串，为空字符串是显示默认的提示信息
    private String msg = "";

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

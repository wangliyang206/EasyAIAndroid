package com.zqw.mobile.easyai.mvp.model.entity;

/**
 * @ProjectName: EasyAIAndroid
 * @Package: com.zqw.mobile.easyai.mvp.model.entity
 * @ClassName: HomeInfo
 * @Description: 首页 对话 列表
 * @Author: WLY
 * @CreateDate: 2024/1/3 16:45
 */
public class HomeChatInfo {

    public HomeChatInfo(String avatar, String name, String intr) {
        this.avatar = avatar;
        this.name = name;
        this.intr = intr;
    }

    public HomeChatInfo() {
    }

    // 头像
    private String avatar;
    // 姓名
    private String name;
    // 介绍
    private String intr;

    public String getIntr() {
        return intr;
    }

    public void setIntr(String intr) {
        this.intr = intr;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

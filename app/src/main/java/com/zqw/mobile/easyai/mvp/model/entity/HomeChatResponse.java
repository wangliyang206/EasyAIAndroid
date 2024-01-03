package com.zqw.mobile.easyai.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: EasyAIAndroid
 * @Package: com.zqw.mobile.easyai.mvp.model.entity
 * @ClassName: HomeInfo
 * @Description: 首页 对话 列表
 * @Author: WLY
 * @CreateDate: 2024/1/3 16:45
 */
public class HomeChatResponse {

    // 列表
    private List<HomeChatInfo> list;
    // 总页数
    private int totalPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<HomeChatInfo> getList() {
        return list;
    }

    public void setList(List<HomeChatInfo> list) {
        this.list = list;
    }
}

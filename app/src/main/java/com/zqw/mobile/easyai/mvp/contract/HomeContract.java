package com.zqw.mobile.easyai.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 11:36
 * ================================================
 */
public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 控制列表是否到底
        void hasLoadedAllItems(boolean val);

        // 开始加载更多
        void startLoadMore();

        // 结束加载更多
        void endLoadMore();

        // 暂无信息
        void noInfo();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 获取对话列表
        Observable<HomeChatResponse> getChatList(int pageNo, int pageSize);
    }
}
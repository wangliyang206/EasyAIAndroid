package com.zqw.mobile.easyai.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
public interface SplashContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 跳转致登录界面
        void jumbToLogin();
        // 跳转致首页
        void jumbToMain();

        // 同意
        void approved();
        // 不同意
        void disagree();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /**
         * 验证token
         */
        Observable<LoginResponse> validToken();
    }
}
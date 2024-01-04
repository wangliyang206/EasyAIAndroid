package com.zqw.mobile.easyai.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.model.entity.CommonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/04 09:10
 * ================================================
 */
public interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 设置 账号
        void setUsernName(String usernName);

        // 跳转致首页
        void jumbToMain();

        // 倒计时结果
        void setTipsValue(Long str);

        // 倒计时时控制按钮是否可用
        void setBtnEnabled(boolean val);

        // 升级询问
        void askDialog(AppUpdate info);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 获取短信验证码
        Observable<CommonResponse> loginSMS(String mobile);

        // 登录
        Observable<LoginResponse> quickLogin(String mobile, String code);

        // 获取APP版本信息
        Observable<AppUpdate> getVersion(String type);
    }
}
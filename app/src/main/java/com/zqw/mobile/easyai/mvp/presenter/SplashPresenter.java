package com.zqw.mobile.easyai.mvp.presenter;

import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.easyai.BuildConfig;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.utils.RxUtils;
import com.zqw.mobile.easyai.mvp.contract.SplashContract;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:欢迎界面
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView) {
        super(model, rootView);
    }

    public void initData() {
        // 是否使用隐私政策待确定，确定使用时在放开
//        mRootView.approved();
        if (mAccountManager.getPrivacyPolicy()) {
            // 已同意 - 获取权限
            mRootView.approved();
        } else {
            // 未同意
            mRootView.disagree();
        }
    }

    /**
     * 控制业务逻辑
     */
    public void initPresenter() {
        // 验证Token
        validToken();
    }

    /**
     * 验证Token
     */
    private void validToken() {
        //1、验证Token和Userid是否存在，存在表示此次并不是第一次登录；
        String userid = mAccountManager.getUserid();
        String token = mAccountManager.getToken();

        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(userid)) {
            RxUtils.startDelayed(1, mRootView, () -> mRootView.jumbToLogin());
            return;
        }

        if (BuildConfig.IS_DEBUG_DATA) {
            mRootView.jumbToLogin();
        } else
            //2、验证Token的有效性
            mModel.validToken()
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<LoginResponse>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            mRootView.jumbToLogin();
                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null) {
                                mAccountManager.updateAccountInfo(loginResponse);
                            }
                            mRootView.jumbToMain();
                        }
                    });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
    }
}
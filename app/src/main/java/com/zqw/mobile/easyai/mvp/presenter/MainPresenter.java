package com.zqw.mobile.easyai.mvp.presenter;

import static com.zqw.mobile.easyai.BuildConfig.IS_DEBUG_DATA;

import android.os.Bundle;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.utils.RxUtils;
import com.zqw.mobile.easyai.mvp.contract.MainContract;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.model.entity.LoginFastGptResponse;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/02 16:33
 * ================================================
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
    }

    /**
     * 初始化
     */
    public void initData(Bundle bundle) {
        // APP升级更新
        if (mAccountManager.getUpgrade())
            checkUpdateManager();

        // 获取FastGPT登录Token
        logiFastGpt();
    }

    /**
     * FastGPT登录
     */
    private void logiFastGpt() {
        mModel.logiFastGpt("root", "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4")
                .compose(RxUtils.applySchedulers(mRootView, true, true))     // 切换线程
                .subscribe(new ErrorHandleSubscriber<LoginFastGptResponse>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        // 不做任何处理
                    }

                    @Override
                    public void onNext(LoginFastGptResponse response) {
                        if (response.getCode() == 200) {
                            mAccountManager.saveFastGptToken(response.getData().getToken());
                        }
                    }
                });
    }

    /**
     * APP升级更新
     */
    private void checkUpdateManager() {
        // 提醒过了
        mAccountManager.setUpgrade(false);
        if (IS_DEBUG_DATA) {
            AppUpdate appUpdate = new AppUpdate(100, "1.1.2", "小的来了", "小的来了.apk", "http://www.buypb.cn/softDownLoad/xdllrecycle_jlt-1.8.0.apk", 1, 18, "1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            if (haveNew(appUpdate)) {
                // 先提醒升级
                mRootView.askDialog(appUpdate);
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView, true, true))                                            // 切换线程
                    .subscribe(new ErrorHandleSubscriber<AppUpdate>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 不做任何处理
                        }

                        @Override
                        public void onNext(AppUpdate au) {
                            if (haveNew(au)) {
                                // 先提醒升级
                                mRootView.askDialog(au);
                            }
                        }
                    });
        }
    }

    /**
     * 版本号比较
     *
     * @return 需要升级返回true，否则返回false
     */
    private boolean haveNew(AppUpdate appUpdate) {
        boolean haveNew = false;
        if (appUpdate == null) {
            return false;
        }

        int curVersionCode = DeviceUtils.getVersionCode(mRootView.getActivity().getApplicationContext());
        if (curVersionCode < appUpdate.getVerCode()) {
            haveNew = true;
        }
        return haveNew;
    }
}
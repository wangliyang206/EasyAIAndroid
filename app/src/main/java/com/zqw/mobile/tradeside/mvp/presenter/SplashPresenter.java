package com.zqw.mobile.tradeside.mvp.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.tradeside.BuildConfig;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.global.Constant;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.SplashContract;

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
        // 创建文件夹
        initFile();

        // 定时清理日志
        initLog();

        // 测试Bugly上报
//        CrashReport.testJavaCrash();

        // 初始APP升级提醒数据设置
        mAccountManager.setUpgrade(true);

        // 验证Token
        validToken();
    }

    /**
     * 创建文件夹
     */
    private void initFile() {
        FileUtils.createOrExistsDir(Constant.IMAGE_PATH);                                           // 创建图片目录
        FileUtils.createOrExistsDir(Constant.LOG_PATH);                                             // 创建日志目录
        FileUtils.createOrExistsDir(Constant.APP_UPDATE_PATH);                                      // 创建升级目录
        FileUtils.createOrExistsDir(Constant.AUDIO_PATH);                                           // 设置拍摄视频缓存路径
    }

    /**
     * 定时清理日志
     * ps:正式环境下每启动50次清理一次Log日志。
     */
    private void initLog() {
        if (!BuildConfig.DEBUG) {
            int num = mAccountManager.getStartTime();
            if (num >= 50) {
                //清理日志
                ThreadUtils.getFixedPool(3).execute(() -> {
                    try {
                        FileUtils.delete(Constant.LOG_PATH + "log.txt");
                        mAccountManager.setStartTime(0);
                    } catch (Exception ignored) {
                    }
                });
            } else {
                //不到清理日期，暂时先增加APP启动次数
                mAccountManager.setStartTime(++num);
            }
        }
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
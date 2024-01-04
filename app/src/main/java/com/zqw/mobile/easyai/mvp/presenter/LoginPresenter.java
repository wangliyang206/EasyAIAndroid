package com.zqw.mobile.easyai.mvp.presenter;

import static com.zqw.mobile.easyai.BuildConfig.IS_DEBUG_DATA;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.RegexUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.utils.RxUtils;
import com.zqw.mobile.easyai.mvp.contract.LoginContract;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.model.entity.CommonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/04 09:10
 * ================================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
    }

    /**
     * 初始化默认值
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

        if (IS_DEBUG_DATA) {
            mRootView.setUsernName("15032134297");
        } else {
            mRootView.setUsernName(mAccountManager.getAccount());
        }

        //APP升级更新
        if (mAccountManager.getUpgrade())
            checkUpdateManager();
    }

    /**
     * 短信验证码登录
     */
    public void loginSMS(String username, boolean isAgreement) {
        // 用户输入有效性验证
        if ("".equals(username)) {
            mRootView.showMessage("请输入手机号");
            return;
        }
        if (!RegexUtils.isMobileSimple(username)) {
            mRootView.showMessage("请输入有效的手机号！");
            return;
        }
        if (!isAgreement) {
            mRootView.showMessage("请阅读并同意相关协议");
            return;
        }

        if (IS_DEBUG_DATA) {
            startCountdown();
        } else {
            // 执行登录操作
            mModel.loginSMS(username)
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<CommonResponse>(mErrorHandler) {
                        @Override
                        public void onNext(CommonResponse loginResponse) {
                            startCountdown();
                        }
                    });
        }
    }

    /**
     * 开启60秒倒计时
     */
    public void startCountdown() {
        final int count = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .observeOn(AndroidSchedulers.mainThread())                                          // 切换线程
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))                               // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .map(aLong -> count - aLong)
                .doOnSubscribe(disposable -> mRootView.setBtnEnabled(false))
                .doFinally(() -> mRootView.setBtnEnabled(true))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Timber.i("###str=%s", aLong);
                        mRootView.setTipsValue(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 点击登录按钮
     *
     * @param mobile      账号(手机号)
     * @param code        验证码
     * @param isAgreement 协议是否同意
     */
    public void btnLogin(String mobile, String code, boolean isAgreement) {
//        测试Bugly上报
//        CrashReport.testJavaCrash();
        // 用户输入有效性验证
        if (!checkInput(mobile, code, isAgreement)) {
            return;
        }

        // 短信验证码登录
        if (IS_DEBUG_DATA) {
            mAccountManager.saveAccountInfo(mobile, "", new LoginResponse("50154b09-14bd-49bf-87a7-d53ce5d29f80", "R000014", "吴存柏", "18124027304", "http://q.qlogo.cn/qqapp/1105462061/00EF3352A990BEAA5C066F45655A4A06/100"));
            mRootView.jumbToMain();
        } else {
            // 执行登录操作
            mModel.quickLogin(mobile, code)
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<LoginResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null) {
                                mAccountManager.saveAccountInfo(mobile, "", loginResponse);
                            }

                            mRootView.jumbToMain();
                        }
                    });
        }
    }


    /**
     * 用户输入有效性验证
     *
     * @return 校验是否通过
     */
    private boolean checkInput(String username, String code, boolean isAgreement) {
        // 用户名和密码不能为空，为空时返回false同时给出提示。
        if ("".equals(username)) {
            mRootView.showMessage("请输入手机号");
            return false;
        }
        if (!RegexUtils.isMobileSimple(username)) {
            mRootView.showMessage("请输入有效的手机号！");
            return false;
        }

        if ("".equals(code)) {
            mRootView.showMessage("请输入验证码");
            return false;
        }

        if (!isAgreement) {
            mRootView.showMessage("请阅读并同意相关协议");
            return false;
        }
        return true;
    }

    /**
     * APP升级更新
     */
    private void checkUpdateManager() {
        // 提醒过了
        mAccountManager.setUpgrade(false);
        if (IS_DEBUG_DATA) {
            AppUpdate appUpdate = new AppUpdate(112, "1.1.2", "小的来了", "小的来了.apk", "http://www.buypb.cn/softDownLoad/xdllrecycle_jlt-1.8.0.apk", 0, 18, "1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            if (haveNew(appUpdate)) {
                // 先提醒升级
                mRootView.askDialog(appUpdate);
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView, true, true)) // 切换线程
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
     * @return 是否升级
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
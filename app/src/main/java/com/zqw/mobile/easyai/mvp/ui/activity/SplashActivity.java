package com.zqw.mobile.easyai.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.dialog.NotPrivacyPolicyDialog;
import com.zqw.mobile.easyai.app.dialog.PrivacyPolicyDialog;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.di.component.DaggerSplashComponent;
import com.zqw.mobile.easyai.mvp.contract.SplashContract;
import com.zqw.mobile.easyai.mvp.presenter.SplashPresenter;

import javax.inject.Inject;

/**
 * Description: 欢迎界面
 * <p>
 * Created on 2023/05/31 17:04
 *
 * @author 赤槿
 * module name is SplashActivity
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    @Inject
    AccountManager mAccountManager;

    /**
     * 是否使用StatusBarCompat
     *
     * @return 不使用
     */
    @Override
    public boolean useStatusBar() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void setForm() {
        super.setForm();

        // 设置全屏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        } else {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(lp);
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        // 隐藏导航栏 | 隐藏状态栏
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.initData();
        }
    }

    @Override
    public void approved() {
        // 已同意 - 获取权限
        if (mPresenter != null) {
            mPresenter.initPresenter();
        }
    }

    @Override
    public void disagree() {
        // 未同意
        PrivacyPolicyDialog mDialog = new PrivacyPolicyDialog(this,
                isVal -> {
                    if (isVal) {
                        // 设置隐私政策
                        mAccountManager.setPrivacyPolicy(isVal);
                        approved();
                    } else {
                        // 不同意就再问一次
                        notPrivacyPolicyDialog();
                    }
                });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * 不同意就再问一次
     */
    private void notPrivacyPolicyDialog() {
        NotPrivacyPolicyDialog mDialog = new NotPrivacyPolicyDialog(this,
                isVal -> {
                    if (isVal) {
                        // 设置隐私政策
                        mAccountManager.setPrivacyPolicy(isVal);
                        approved();
                    } else {
                        // 关闭APP
                        AppUtils.exitApp();
                    }
                });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }


    /**
     * 屏蔽返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    /**
     * 屏蔽返回按钮
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * 跳转到主界面
     */
    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }

    /**
     * 跳转致登录页
     */
    @Override
    public void jumbToLogin() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
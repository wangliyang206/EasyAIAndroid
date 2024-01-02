package com.zqw.mobile.tradeside.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
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
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.dialog.NotPrivacyPolicyDialog;
import com.zqw.mobile.tradeside.app.dialog.PrivacyPolicyDialog;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.di.component.DaggerSplashComponent;
import com.zqw.mobile.tradeside.mvp.contract.SplashContract;
import com.zqw.mobile.tradeside.mvp.presenter.SplashPresenter;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description: 欢迎界面
 * <p>
 * Created on 2023/05/31 17:04
 *
 * @author 赤槿
 * module name is SplashActivity
 */
@RuntimePermissions
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
        SplashActivityPermissionsDispatcher.runAppWithPermissionCheck(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    })
    public void showNeverAskAgain() {
        runApp();
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    })
    public void onPermissionDenied() {
        runApp();
    }

    /**
     * 申请权限成功后的逻辑
     */
    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    })
    public void runApp() {
        if (mPresenter != null) {
            mPresenter.initPresenter();
        }
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
//        ActivityUtils.startActivity(MainActivity.class);
//        killMyself();
    }

    /**
     * 跳转致登录页
     */
    @Override
    public void jumbToLogin() {
//        ActivityUtils.startActivity(LoginActivity.class);
//        killMyself();
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
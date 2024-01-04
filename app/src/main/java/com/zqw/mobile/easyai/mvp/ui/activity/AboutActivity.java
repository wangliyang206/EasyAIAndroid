package com.zqw.mobile.easyai.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.easyai.BuildConfig;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.di.component.DaggerAboutComponent;
import com.zqw.mobile.easyai.mvp.contract.AboutContract;
import com.zqw.mobile.easyai.mvp.presenter.AboutPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:关于
 * <p>
 * Created on 2024/01/04 15:45
 *
 * @author 赤槿
 * module name is AboutActivity
 */
public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.txvi_aboutactivity_name)
    TextView txviName;                                                                              // 名称

    @BindView(R.id.txvi_aboutactivity_version)
    TextView txviVersion;                                                                           // 版本号
    /*--------------------------------业务信息--------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAboutComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("关于");

        // 设置名称
        txviName.setText(BuildConfig.DEBUG ? R.string.app_name_debug : R.string.app_name);

        // 设置版本号
        txviVersion.setText("Version." + BuildConfig.VERSION_NAME);
    }

    @OnClick({
            R.id.txvi_aboutactivity_serviceAgreement,                                               // 服务协议
            R.id.txvi_aboutactivity_privacyPolicy,                                                  // 隐私政策
    })
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowTop", true);
        switch (v.getId()) {
            case R.id.txvi_aboutactivity_serviceAgreement:                                          // 服务协议
                bundle.putString("URL", Constant.serviceAgreementUrl);
                bundle.putString("TITLE", "服务协议");
                ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
                break;
            case R.id.txvi_aboutactivity_privacyPolicy:                                             // 隐私政策
                bundle.putString("URL", Constant.privacyPolicyUrl);
                bundle.putString("TITLE", "隐私政策");
                ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
                break;
        }
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
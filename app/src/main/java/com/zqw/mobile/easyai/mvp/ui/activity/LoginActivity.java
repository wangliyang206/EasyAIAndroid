package com.zqw.mobile.easyai.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.dialog.PopupTipsDialog;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.app.utils.MyClickableSpan;
import com.zqw.mobile.easyai.di.component.DaggerLoginComponent;
import com.zqw.mobile.easyai.mvp.contract.LoginContract;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:登录
 * <p>
 * Created on 2024/01/04 09:10
 *
 * @author 赤槿
 * module name is LoginActivity
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_login)
    LinearLayout contentLayout;                                                                     // 界面总布局

    @BindView(R.id.edttxt_loginactivity_username)
    EditText edtTxtUsername;                                                                        // 用户名输入框
    @BindView(R.id.edttxt_loginactivity_code)
    EditText edtTxtCode;                                                                            // 短信验证码输入框
    @BindView(R.id.txvi_loginactivity_sendmessage)                                                  // 发送验证码按钮
    TextView txviSendMsg;

    @BindView(R.id.btn_loginactivity_submit)
    Button btnLogin;                                                                                // 登录

    @BindView(R.id.txvi_loginactivity_tips)
    TextView txviAgreement;                                                                         // 协议提示
    @BindView(R.id.checkbox_loginactivity_cb)
    CheckBox checkBox;                                                                              // 勾选协议
    /*------------------------------------------------业务信息------------------------------------------------*/
    @Inject
    AccountManager mAccountManager;
    // 登录对话框
    private MaterialDialog mDialog;

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        if (mDialog != null) {
            this.mDialog.dismiss();
        }

        super.onDestroy();
        this.mDialog = null;

        this.mAccountManager = null;
    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 不需要默认事件
        getIntent().putExtra("isInitToolbar", true);

        SpannableString agreement = new SpannableString(getString(R.string.login_agreement_tips));
        agreement.setSpan(new MyClickableSpan("《服务协议》"), 7, 13, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        agreement.setSpan(new MyClickableSpan("《隐私政策》"), 14, 20, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        if (txviAgreement != null) {
            txviAgreement.setText(agreement);
            txviAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnLogin.setEnabled(isChecked);
        });

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.loginactivity_login_processtips).progress(true, 0).build();

    }

    /**
     * 点击事件
     */
    @OnClick({
            R.id.imvi_login_close,                                                                  // 返回
            R.id.btn_loginactivity_submit,                                                          // 登录
            R.id.txvi_loginactivity_sendmessage,                                                    // 发送短信验证码
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.imvi_login_close:                                                             // 返回
                onBackPressed();
                break;
            case R.id.btn_loginactivity_submit:                                                     // 登录
                String username = edtTxtUsername.getText().toString().trim();
                String code = edtTxtCode.getText().toString().trim();
                if (mPresenter != null) {
                    mPresenter.btnLogin(username, code, checkBox.isChecked());
                }
                break;
            case R.id.txvi_loginactivity_sendmessage:                                               // 获取验证码
                if (mPresenter != null) {
                    mPresenter.loginSMS(edtTxtUsername.getText().toString().trim(), checkBox.isChecked());
                }
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(edtTxtUsername);
        KeyboardUtils.hideSoftInput(edtTxtCode);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        PopupTipsDialog.PopupClick popupClick = click -> {
            if (click) {
                AppUtils.exitApp();
            }
        };
        PopupTipsDialog phoneDialog = new PopupTipsDialog(this, "你真的要退出吗？", popupClick);
        phoneDialog.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void setUsernName(String usernName) {
        edtTxtUsername.setText(usernName);
        if (!TextUtils.isEmpty(usernName)) {
            // 设置光标
            edtTxtUsername.setSelection(usernName.length());
        }

    }

    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);

        // 关闭当前窗口
        killMyself();
    }

    @Override
    public void setTipsValue(Long str) {
        txviSendMsg.setText(str + "s");
    }

    @Override
    public void setBtnEnabled(boolean val) {
        if (val) {
            // 倒计时完毕
            txviSendMsg.setText("发送验证码");
            txviSendMsg.setOnClickListener(this);
        } else {
            // 正在倒计时
            txviSendMsg.setOnClickListener(null);
        }
    }

    @Override
    public void askDialog(AppUpdate info) {
        Q.show(this, new CheckUpdateOption.Builder()
                .setAppName(info.getName())
                .setFileName("/" + info.getFileName())
                .setFilePath(Constant.APP_UPDATE_PATH)
//                .setImageUrl("http://imgsrc.baidu.com/imgad/pic/item/6c224f4a20a446233d216c4f9322720e0cf3d730.jpg")
                .setImageResId(R.mipmap.icon_upgrade_logo)
                .setIsForceUpdate(info.getForce() == 1)
                .setNewAppSize(info.getNewAppSize())
                .setNewAppUpdateDesc(info.getNewAppUpdateDesc())
                .setNewAppUrl(info.getFilePath())
                .setNewAppVersionName(info.getVerName())
                .setNotificationSuccessContent("下载成功，点击安装")
                .setNotificationFailureContent("下载失败，点击重新下载")
                .setNotificationIconResId(R.mipmap.ic_launcher)
                .setNotificationTitle(getString(R.string.app_name))
                .build(), (view, imageUrl) -> {
            // 下载图片
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            mImageLoader.loadImage(getActivity(),
//                    ImageConfigImpl
//                            .builder()
//                            .url(imageUrl)
//                            .imageView(view)
//                            .build());
        });
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog != null)
            mDialog.cancel();
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
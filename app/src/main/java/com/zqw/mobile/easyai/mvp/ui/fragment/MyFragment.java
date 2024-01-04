package com.zqw.mobile.easyai.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.di.component.DaggerMyComponent;
import com.zqw.mobile.easyai.mvp.contract.MyContract;
import com.zqw.mobile.easyai.mvp.presenter.MyPresenter;
import com.zqw.mobile.easyai.mvp.ui.activity.AboutActivity;
import com.zqw.mobile.easyai.mvp.ui.activity.LoginActivity;
import com.zqw.mobile.easyai.mvp.ui.activity.NewWindowX5Activity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Description:我的
 * <p>
 * Created on 2024/01/03 11:38
 *
 * @author 赤槿
 * module name is MyActivity
 */
public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.civi_fragmentmytab_pic)
    CircleImageView imviUserPhoto;                                                                  // 头像
    @BindView(R.id.txvi_fragmentmytab_name)
    TextView txviUsername;                                                                          // 姓名

    /*--------------------------------业务信息--------------------------------*/
    @Inject
    AccountManager mAccountManager;
    @Inject
    ImageLoader mImageLoader;

    // 退出登录
    private MaterialDialog mOutLoginTips = null;

    @Override
    public void onDestroy() {
        if (mOutLoginTips != null) {
            this.mOutLoginTips.dismiss();
        }
        super.onDestroy();
        this.mOutLoginTips = null;

        this.mImageLoader = null;
        this.mAccountManager = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 加载数据
        txviUsername.setText(mAccountManager.getUserName());

        // 加载头像
        mImageLoader.loadImage(getContext(),
                ImageConfigImpl.builder().url(mAccountManager.getPhotoUrl())
                        .imageView(imviUserPhoto).build());
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMyComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    /**
     * 在 onActivityCreate()时调用
     */
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 退出登录
        mOutLoginTips = new MaterialDialog.Builder(getContext()).title("温馨提示").content("确认退出当前账号？").positiveText("确定").negativeText("取消")
                .onPositive((dialog, which) -> {
                    // 清除缓存
                    mAccountManager.clearAccountInfo();
                    ActivityUtils.startActivity(LoginActivity.class);
                })
                .onNegative((dialog, which) -> {
                }).cancelable(false).build();
    }

    @OnClick({
            R.id.lila_fragmentmytab_serviceagreement,                                               // 服务协议
            R.id.lila_fragmentmytab_privacypolicy,                                                  // 隐私政策
            R.id.lila_fragmentmytab_about,                                                          // 关于
            R.id.lila_fragmentmytab_out,                                                            // 退出登录
    })
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.lila_fragmentmytab_serviceagreement:                                          // 服务协议
                bundle.putBoolean("isShowTop", true);
                bundle.putString("URL", Constant.serviceAgreementUrl);
                bundle.putString("TITLE", "服务协议");
                ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
                break;
            case R.id.lila_fragmentmytab_privacypolicy:                                             // 隐私政策
                bundle.putBoolean("isShowTop", true);
                bundle.putString("URL", Constant.privacyPolicyUrl);
                bundle.putString("TITLE", "隐私政策");
                ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
                break;
            case R.id.lila_fragmentmytab_about:                                                     // 关于
                ActivityUtils.startActivity(AboutActivity.class);
                break;
            case R.id.lila_fragmentmytab_out:                                                       // 退出登录
                if (mOutLoginTips != null) {
                    mOutLoginTips.show();
                }
                break;
        }
    }


    @Override
    public void setData(@Nullable Object data) {

    }

    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
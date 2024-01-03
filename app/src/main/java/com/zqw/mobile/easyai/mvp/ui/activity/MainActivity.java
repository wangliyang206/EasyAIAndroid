package com.zqw.mobile.easyai.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.app.utils.CommonUtils;
import com.zqw.mobile.easyai.di.component.DaggerMainComponent;
import com.zqw.mobile.easyai.mvp.contract.MainContract;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.presenter.MainPresenter;
import com.zqw.mobile.easyai.mvp.ui.fragment.ChatGptFragment;
import com.zqw.mobile.easyai.mvp.ui.fragment.MyFragment;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description: 首页
 * <p>
 * Created on 2024/01/02 16:33
 *
 * @author 赤槿
 * module name is MainActivity
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.activity_main)
    ConstraintLayout contentLayout;                                                                 // 总布局

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigation;                                                               // 底部导航
    /*--------------------------------业务信息--------------------------------*/
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private int position;                                                                           // 保存当前tab下标
    private long firstClickTime = 0;                                                                // 双击刷新功能 - 记录点击时间

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MY = 2;

    // 首页
//    private HomeFragment tabHome;
    private ChatGptFragment tabHome;
    // 我的
    private MyFragment tabMe;

    @Inject
    AccountManager mAccountManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mNavigation = null;
        this.tabHome = null;
        this.tabMe = null;
        this.mAccountManager = null;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 将状态栏改为浅色模式(状态栏 icon 和字体会变成深色)
     */
    public boolean useLightStatusBar() {
        return true;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 初始化
        mNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    showFragment(FRAGMENT_HOME);
                    doubleClick(FRAGMENT_HOME);
                    break;
                case R.id.action_me:
                    showFragment(FRAGMENT_MY);
                    break;
            }
            return true;
        });

        if (savedInstanceState != null) {
//            tabHome = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
            tabHome = (ChatGptFragment) getSupportFragmentManager().findFragmentByTag(ChatGptFragment.class.getName());
            tabMe = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.class.getName());
            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            mNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_HOME);
        }

        if (mPresenter != null) {
            mPresenter.initData(getIntent().getExtras());
        }

    }

    /**
     * 显示Fragment
     */
    private void showFragment(int index) {
        this.position = index;

        // 第一步，隐藏所有Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        // 第二步，显示指定的Fragment
        switch (index) {
            case FRAGMENT_HOME:
//                if (tabHome == null) {
//                    tabHome = new HomeFragment();
//                    ft.add(R.id.container, tabHome, HomeFragment.class.getName());
//                } else {
//                    ft.show(tabHome);
//                }

                if (tabHome == null) {
                    tabHome = new ChatGptFragment();
                    ft.add(R.id.container, tabHome, ChatGptFragment.class.getName());
                } else {
                    ft.show(tabHome);
                }
                break;
            case FRAGMENT_MY:
                if (tabMe == null) {
                    tabMe = new MyFragment();
                    ft.add(R.id.container, tabMe, MyFragment.class.getName());
                } else {
                    ft.show(tabMe);
                }
        }

        ft.commit();
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (tabHome != null) {
            ft.hide(tabHome);
        }

        if (tabMe != null) {
            ft.hide(tabMe);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // recreate 时记录当前位置 (在 Manifest 已禁止 Activity 旋转,所以旋转屏幕并不会执行以下代码)
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, mNavigation.getSelectedItemId());
    }

    /**
     * 双击刷新界面
     */
    public void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if ((secondClickTime - firstClickTime < 500)) {
            if (index == FRAGMENT_HOME) {
//                tabHome.onDoubleClick();
            }
        } else {
            firstClickTime = secondClickTime;
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

    @Override
    public void onBackPressed() {
        CommonUtils.exitSys(getActivity());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 照片
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                if (tabHome != null)
                    tabHome.onResultImage(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));
            }
        }
    }
}
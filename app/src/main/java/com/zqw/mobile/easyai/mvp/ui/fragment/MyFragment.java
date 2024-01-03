package com.zqw.mobile.easyai.mvp.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.easyai.di.component.DaggerMyComponent;
import com.zqw.mobile.easyai.di.module.MyModule;
import com.zqw.mobile.easyai.mvp.contract.MyContract;
import com.zqw.mobile.easyai.mvp.presenter.MyPresenter;
import com.zqw.mobile.easyai.R;

/**
 * Description:我的
 * <p>
 * Created on 2024/01/03 11:38
 *
 * @author 赤槿
 * module name is MyActivity
 */
public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMyComponent //如找不到该类,请编译一下项目
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
        //setToolBarNoBack(toolbar, "My");

        initListener();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void initListener() {

    }

    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
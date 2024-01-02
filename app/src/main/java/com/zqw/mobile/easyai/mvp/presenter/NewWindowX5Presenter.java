package com.zqw.mobile.easyai.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.easyai.mvp.contract.NewWindowX5Contract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description: 新窗口 X5
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class NewWindowX5Presenter extends BasePresenter<NewWindowX5Contract.Model, NewWindowX5Contract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public NewWindowX5Presenter(NewWindowX5Contract.Model model, NewWindowX5Contract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
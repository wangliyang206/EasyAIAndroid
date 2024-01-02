package com.zqw.mobile.easyai.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.global.RequestMapper;
import com.zqw.mobile.easyai.mvp.contract.GeneralAssistantContract;
import com.zqw.mobile.easyai.mvp.model.GeneralAssistantModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/02 16:33
 * ================================================
 */
@Module
//构建GeneralAssistantModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class GeneralAssistantModule {

    @Binds
    abstract GeneralAssistantContract.Model bindGeneralAssistantModel(GeneralAssistantModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(GeneralAssistantContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(GeneralAssistantContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}
package com.zqw.mobile.easyai.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.global.RequestMapper;
import com.zqw.mobile.easyai.mvp.contract.ChatGptContract;
import com.zqw.mobile.easyai.mvp.model.ChatGptModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 17:45
 * ================================================
 */
@Module
//构建ChatGptModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ChatGptModule {

    @Binds
    abstract ChatGptContract.Model bindChatGptModel(ChatGptModel model);


    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(ChatGptContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(ChatGptContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}
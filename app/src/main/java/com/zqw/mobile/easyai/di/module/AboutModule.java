package com.zqw.mobile.easyai.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.easyai.mvp.contract.AboutContract;
import com.zqw.mobile.easyai.mvp.model.AboutModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/04 15:45
 * ================================================
 */
@Module
//构建AboutModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class AboutModule {

    @Binds
    abstract AboutContract.Model bindAboutModel(AboutModel model);
}
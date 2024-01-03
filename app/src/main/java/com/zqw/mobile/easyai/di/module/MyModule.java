package com.zqw.mobile.easyai.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.easyai.mvp.contract.MyContract;
import com.zqw.mobile.easyai.mvp.model.MyModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 11:36
 * ================================================
 */
@Module
//构建MyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class MyModule {

    @Binds
    abstract MyContract.Model bindMyModel(MyModel model);
}
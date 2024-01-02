package com.zqw.mobile.tradeside.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.tradeside.mvp.contract.NewWindowX5Contract;
import com.zqw.mobile.tradeside.mvp.model.NewWindowX5Model;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@Module
//构建NewWindowX5Module时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class NewWindowX5Module {

    @Binds
    abstract NewWindowX5Contract.Model bindNewWindowX5Model(NewWindowX5Model model);
}
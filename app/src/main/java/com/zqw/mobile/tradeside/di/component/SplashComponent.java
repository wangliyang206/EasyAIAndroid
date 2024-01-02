package com.zqw.mobile.tradeside.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.tradeside.di.module.SplashModule;
import com.zqw.mobile.tradeside.mvp.contract.SplashContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.mvp.ui.activity.SplashActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
public interface SplashComponent {

    void inject(SplashActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SplashComponent.Builder view(SplashContract.View view);

        SplashComponent.Builder appComponent(AppComponent appComponent);

        SplashComponent build();
    }
}
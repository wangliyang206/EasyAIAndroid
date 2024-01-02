package com.zqw.mobile.easyai.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.easyai.di.module.MainModule;
import com.zqw.mobile.easyai.mvp.contract.MainContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.easyai.mvp.ui.activity.MainActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/02 16:33
 * ================================================
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MainComponent.Builder view(MainContract.View view);

        MainComponent.Builder appComponent(AppComponent appComponent);

        MainComponent build();
    }
}
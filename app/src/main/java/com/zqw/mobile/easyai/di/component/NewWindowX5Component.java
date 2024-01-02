package com.zqw.mobile.easyai.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.easyai.di.module.NewWindowX5Module;
import com.zqw.mobile.easyai.mvp.contract.NewWindowX5Contract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.easyai.mvp.ui.activity.NewWindowX5Activity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
@Component(modules = NewWindowX5Module.class, dependencies = AppComponent.class)
public interface NewWindowX5Component {

    void inject(NewWindowX5Activity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NewWindowX5Component.Builder view(NewWindowX5Contract.View view);

        NewWindowX5Component.Builder appComponent(AppComponent appComponent);

        NewWindowX5Component build();
    }
}
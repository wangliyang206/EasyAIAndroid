package com.zqw.mobile.easyai.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.easyai.di.module.GeneralAssistantModule;
import com.zqw.mobile.easyai.mvp.contract.GeneralAssistantContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.easyai.mvp.ui.activity.GeneralAssistantActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/02 16:33
 * ================================================
 */
@ActivityScope
@Component(modules = GeneralAssistantModule.class, dependencies = AppComponent.class)
public interface GeneralAssistantComponent {

    void inject(GeneralAssistantActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GeneralAssistantComponent.Builder view(GeneralAssistantContract.View view);

        GeneralAssistantComponent.Builder appComponent(AppComponent appComponent);

        GeneralAssistantComponent build();
    }
}
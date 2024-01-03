package com.zqw.mobile.easyai.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.easyai.di.module.ChatGptModule;
import com.zqw.mobile.easyai.mvp.contract.ChatGptContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.easyai.mvp.ui.fragment.ChatGptFragment;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 17:45
 * ================================================
 */
@ActivityScope
@Component(modules = ChatGptModule.class, dependencies = AppComponent.class)
public interface ChatGptComponent {

    void inject(ChatGptFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatGptComponent.Builder view(ChatGptContract.View view);

        ChatGptComponent.Builder appComponent(AppComponent appComponent);

        ChatGptComponent build();
    }
}
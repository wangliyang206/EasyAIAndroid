/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqw.mobile.tradeside.app.config;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zqw.mobile.tradeside.R;

import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link Application.ActivityLifecycleCallbacks} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:14
 * ================================================
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.i("%s - onActivityCreated", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.i("%s - onActivityStarted", activity.getClass().getSimpleName());
        if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
            //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            activity.getIntent().putExtra("isInitToolbar", true);
            //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
            Toolbar mToolbar = activity.findViewById(R.id.t_comm_toolbar);
            if (mToolbar != null) {
                if (activity instanceof AppCompatActivity) {
                    // 绑定Toolbar
                    ((AppCompatActivity) activity).setSupportActionBar(mToolbar);
                    // 默认会显示应用名，将其隐藏
                    ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    mToolbar.setOnClickListener(v -> activity.onBackPressed());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.t_comm_toolbar));
                        activity.getActionBar().setDisplayShowTitleEnabled(false);
                        ((android.widget.Toolbar) activity.findViewById(R.id.t_comm_toolbar)).setOnClickListener(v -> {
                            activity.onBackPressed();
                        });
                    }
                }
            }

            if (activity.findViewById(R.id.txvi_commtop_title) != null) {
                ((TextView) activity.findViewById(R.id.txvi_commtop_title)).setText(activity.getTitle());
            }
            if (activity.findViewById(R.id.imvi_commtop_back) != null) {
                activity.findViewById(R.id.imvi_commtop_back).setOnClickListener(v -> activity.onBackPressed());
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.i("%s - onActivityResumed", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.i("%s - onActivityPaused", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.i("%s - onActivityStopped", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.i("%s - onActivitySaveInstanceState", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.i("%s - onActivityDestroyed", activity.getClass().getSimpleName());
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.getIntent().removeExtra("isInitToolbar");
    }
}

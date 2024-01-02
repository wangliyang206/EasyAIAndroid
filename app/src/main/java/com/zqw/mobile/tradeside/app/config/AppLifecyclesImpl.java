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

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zqw.mobile.tradeside.BuildConfig;
import com.zqw.mobile.tradeside.app.utils.FileLoggingTree;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        // 这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
        MultiDex.install(base);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        // 初始化工具类
        Utils.init(application);

        initTimber();

        initBugly(application);

        // java
        // 是否同意隐私政策，默认为false
        SDKInitializer.setAgreePrivacy(application, true);
        // 初始化百度地图
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(application);

        // 初始化定位
        initLocation(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    /**
     * 初始化log
     */
    private void initTimber() {
        if (BuildConfig.DEBUG) {
            // 测试环境
            // Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            // 并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            // 比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(@NotNull StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);

        } else {
            // 正式环境
            Timber.plant(new FileLoggingTree());
        }

    }

    /**
     * 初始化Bugly
     */
    private void initBugly(Application application) {
        // 初始化腾讯Bugly SDK
        CrashReport.initCrashReport(application, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
    }

    /**
     * 初始始化定位
     */
    private void initLocation(Application application) {
        // 验证一下经纬度，如果没有经纬度则需要初始一个默认经纬度
//        AccountManager accountManager = new AccountManager(application);
//        if (TextUtils.isEmpty(accountManager.getProvince())) {
//            accountManager.updateLocation(38.031693, 114.540032, 72.81403f, "河北省", "石家庄市", "裕华区", "中国河北省石家庄市裕华区体育南大街227号");
//        }

        LocationService locationService;
        try {
            // setAgreePrivacy接口需要在LocationClient实例化之前调用
            // 如果setAgreePrivacy接口参数设置为了false，则定位功能不会实现
            // true，表示用户同意隐私合规政策
            // false，表示用户不同意隐私合规政策
            LocationClient.setAgreePrivacy(true);
            locationService = new LocationService(application);
            // 配置定位信息
//        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            // 定位回调监听
            locationService.registerListener(new MyLocationListener(application));
            // 开启定位
//        locationService.start();

            // 存储到内存中
            ArmsUtils.obtainAppComponentFromContext(application).extras().put(IntelligentCache.getKeyOfKeep(LocationService.class.getName()), locationService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

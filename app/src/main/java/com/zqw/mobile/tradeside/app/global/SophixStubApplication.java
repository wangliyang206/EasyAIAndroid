package com.zqw.mobile.tradeside.app.global;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

import com.jess.arms.base.BaseApplication;
import com.jess.arms.utils.ArmsUtils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * 包名： com.zqw.mobile.recycling
 * 对象名： SophixStubApplication
 * 描述："阿里云Sophix"稳健接入方式(目前只有正式环境支持热更新功能)
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/5/31 16:32
 */

public class SophixStubApplication extends SophixApplication {
    /*
     * Sophix入口类，专门用于初始化Sophix，不应包含任何业务逻辑。
     * 此类必须继承自SophixApplication，onCreate方法不需要实现。
     * 此类不应与项目中的其他类有任何互相调用的逻辑，必须完全做到隔离。
     * AndroidManifest中设置application为此类，而SophixEntry中设为原先Application类。
     * 注意原先Application里不需要再重复初始化Sophix，并且需要避免混淆原先Application类。
     * 如有其它自定义改造，请咨询官方后妥善处理。
     */

    private final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    // 只有这里改成自己的Application类，下面static不要改
    @Keep
    @SophixEntry(BaseApplication.class)
    static class RealApplicationStub {
    }

    // 这里不能调用非系统API的类
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 初始化分包
        MultiDex.install(this);

        // 初始化阿里云热更新
        initSophix();
    }


    /**
     * 初始化阿里云热更新
     */
    private void initSophix() {
        // 获取版本号(版本名称)
        String appVersion = "0.0.0";
        try {
//            appVersion = BuildConfig.VERSION_NAME;
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(null, null, null)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.i(TAG, "sophix加载补丁成功! mode = " + mode + "；code = " + code + "；info = " + info + "；handlePatchVersion = " + handlePatchVersion);
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见接入文档-快速接入章节1.3.2.3
                            Log.i(TAG, "sophix预加载补丁成功。重新启动应用程序以使效果生效。mode = " + mode + "；code = " + code + "；info = " + info + "；handlePatchVersion = " + handlePatchVersion);
                            // 注意：不可以直接Process.killProcess(Process.myPid())来杀进程，这样会扰乱Sophix的内部状态。因此如果需要杀死进程，建议使用killProcessSafely方法，它在内部做一些适当处理后才杀死本进程。
                            try {
                                // 解决在子线程中调用Toast的异常情况处理
                                Looper.prepare();
                                Handler mHandler = new Handler();
                                ArmsUtils.makeText(getApplicationContext(), "热更新成功重启后生效！");
                                mHandler.postDelayed(() -> instance.killProcessSafely(), 3000);
                                // 这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
                                Looper.loop();
                            } catch (Exception ex) {
                                Log.i(TAG, "弹出提示报错了=" + ex.getMessage());
                            }
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.i(TAG, "其它错误信息, 查看PatchStatus类说明；mode = " + mode + "；code = " + code + "；info = " + info + "；handlePatchVersion = " + handlePatchVersion);
                        }
                    }
                }).initialize();
    }
}

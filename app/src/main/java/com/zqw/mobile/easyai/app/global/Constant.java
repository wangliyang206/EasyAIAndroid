package com.zqw.mobile.easyai.app.global;

import com.blankj.utilcode.util.PathUtils;

/**
 * 包名： com.zqw.mobile.operation.app.global
 * 对象名： Constant
 * 描述：公共设置
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/5/31 14:47
 */

public interface Constant {
    /*----------------------------------------------APP SdCard目录地址-------------------------------------------------*/

    /**
     * 项目目录
     */
    String APP_CATALOGUE = "EasyAi";
    /* 创建目录与文件需要做详细配置，10以下正常使用File，10+需要走分区存储 */
    /**
     * APP升级路径
     */
    String APP_UPDATE_PATH = PathUtils.getExternalDownloadsPath() + "/" + APP_CATALOGUE + "/AppUpdate/";

    /**
     * log路径
     */
    String LOG_PATH = PathUtils.getExternalDocumentsPath() + "/" + APP_CATALOGUE + "/Log/";

    /**
     * 音频缓存路径
     */
    String AUDIO_PATH = PathUtils.getExternalDownloadsPath() + "/" + APP_CATALOGUE + "/";

    /**
     * 保存图片路径
     */
    String IMAGE_PATH = PathUtils.getExternalPicturesPath() + "/" + APP_CATALOGUE + "/Image/";

    /**
     * 模板路径
     */
    String TEMPLATE_PATH = PathUtils.getExternalPicturesPath() + "/" + APP_CATALOGUE + "/Template/";

    /*----------------------------------------------------业务变量-------------------------------------------------------*/
    /**
     * 服务协议
     */
    String serviceAgreementUrl = "http://www.buypb.cn/useragreement/zqwservicegreement_jlt.html";

    /**
     * 隐私政策
     */
    String privacyPolicyUrl = "http://www.buypb.cn/useragreement/ruserprivacy_jlt.html";

    /**
     * API版本号
     */
    int version = 1;

    /*----------------------------------------------------跳转设定-------------------------------------------------------*/

    /**
     * 图片参数key
     */
    String IMAGE_URL = "IMAGE_URL";
}

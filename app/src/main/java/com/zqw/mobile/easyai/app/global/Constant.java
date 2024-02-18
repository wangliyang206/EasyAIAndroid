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
     * ChatGPT “官方”服务地址
     */
//    String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";
    /**
     * ChatGPT “OpenKEY”服务“闲聊”地址
     */
    String CHATGPT_CHAT_URL = "https://openkey.cloud/v1/chat/completions";
    /**
     * ChatGPT “OpenKEY”服务“图片”地址
     */
    String CHATGPT_IMAGE_URL = "https://openkey.cloud/v1/images/generations";
    /**
     * ChatGPT 查询令牌余额
     */
    String CHATGPT_TOKEN = "https://billing.openkey.cloud/api/token";
    /**
     * 语音转文字
     */
    String CHATGPT_TRANSCRIPTIONS_URL = "https://openkey.cloud/v1/audio/transcriptions";
    /**
     * 文字转语音
     */
    String CHATGPT_SPEECH_URL = "https://openkey.cloud/v1/audio/speech";
    /**
     * ChatGPT key，有额度(1元 500000 tokens)
     */
    String CHATGPT_KEY = "sk-GlgmPE0qiewPnNg6760703686fD4468683C655Ed1eA75e37";


//    https://api.fastgpt.in/api
//    http://yunguichu.e2.luyouxia.net:22172/api

    /**
     * FastGPT API 地址
     */
    String FASTGPT_CHAT_URL = "http://yunguichu.e2.luyouxia.net:22172/api/v1/chat/completions";
    /**
     * FastGPT 登录
     */
    String FASTGPT_TOKEN = "http://yunguichu.e2.luyouxia.net:22172/api/support/user/account/loginByPassword";
    /**
     * 获取历史记录
     */
    String FASTGPT_HISTORY_URL = "http://yunguichu.e2.luyouxia.net:22172/api/core/chat/init";
    /**
     * 语音转文字
     */
    String FASTGPT_TRANSCRIPTIONS_URL = "http://yunguichu.e2.luyouxia.net:22172/api/v1/audio/transcriptions";
    /**
     * 文字转语音
     */
    String FASTGPT_SPEECH_URL = "http://yunguichu.e2.luyouxia.net:22172/api/v1/audio/speech";
    // 企业智能客服：带有知识库，可以回答企业任何问题
//    String FASTGPT_KEY = "fastgpt-jeq4kr7IUN9Qvvi1Bv4C7ddJIeW3GJtHE";
//    String FASTGPT_APPID = "656fce2d993ca09b160e9ea7";
    String FASTGPT_KEY = "fastgpt-yV1VTGscfI3Gejet5x3kFI9da1";
    String FASTGPT_APPID = "65a778deffbc01a98b7f63b0";

    // 豆芽AI助手
    String FASTGPT_DOUYA_KEY = "fastgpt-lEmLoX75QqwHeUmvwbVFkIXwJSsREJ";
    String FASTGPT_DOUYA_APPID = "6571425b3edacb78a123cf0c";

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

    /**
     * 默认展示20条
     */
    int PAGESIZE = 20;
    /*----------------------------------------------------跳转设定-------------------------------------------------------*/

    /**
     * 图片参数key
     */
    String IMAGE_URL = "IMAGE_URL";

    /**
     * 选择图片
     */
    int REQUEST_SELECT_IMAGES_CODE = 0x01;
}

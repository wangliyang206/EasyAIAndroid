package com.zqw.mobile.easyai.mvp.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.easyai.app.config.CommonRetryWithDelay;
import com.zqw.mobile.easyai.app.global.AccountManager;
import com.zqw.mobile.easyai.app.utils.CommonUtils;
import com.zqw.mobile.easyai.app.utils.MediaStoreUtils;
import com.zqw.mobile.easyai.mvp.contract.ChatGptContract;
import com.zqw.mobile.easyai.mvp.model.entity.ChatCompletionChunk;
import com.zqw.mobile.easyai.mvp.model.entity.ChatHistoryResponse;
import com.zqw.mobile.easyai.mvp.model.entity.ChatInputs;
import com.zqw.mobile.easyai.mvp.model.entity.ChatUserGuideModule;
import com.zqw.mobile.easyai.mvp.model.entity.ImageUploadResponse;
import com.zqw.mobile.easyai.mvp.model.entity.WhisperResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 17:45
 * ================================================
 */
@ActivityScope
public class ChatGptPresenter extends BasePresenter<ChatGptContract.Model, ChatGptContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    // 用于接收消息，流式输出
    private StringBuffer buffer;
    private ChatCompletionChunk chatCompletionChunk;
    private Gson gson = new Gson();
    // 默认提示语
    private final String mDefaultTips = "您好，我是易收网智能小助手小铅，请问有什么可以帮助您的吗？";

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.gson = null;
        this.buffer = null;

        this.chatCompletionChunk = null;
    }

    @Inject
    public ChatGptPresenter(ChatGptContract.Model model, ChatGptContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取对话日志
     */
    public void getChatHistory() {
        mModel.getChatHistory()
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ChatHistoryResponse>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        Timber.i("##### t=%s", t.getMessage());
                        mRootView.onLoadOpeningRemarks(mDefaultTips);
                    }

                    @Override
                    public void onNext(ChatHistoryResponse info) {
                        Timber.i("##### getChatHistory------------【Start】-------------");

                        Timber.i("##### onNext Code=%s", info.getCode());
                        try {
                            Timber.i("##### onNext History=%s", info.getData().getHistory().size());
                            if (info.getCode() == 200) {
                                // 拿到开场白
                                ChatUserGuideModule mChatUserGuideModule = info.getData().getApp().getUserGuideModule();

                                // 判断是否有历史记录
                                if (CommonUtils.isNotEmpty(info.getData().getHistory())) {
                                    // 有历史记录
                                    mRootView.onLoadHistory(getOpeningRemarks(mChatUserGuideModule), info.getData().getHistory());
                                } else {
                                    // 没有历史记录，单独加载 提示语
                                    mRootView.onLoadOpeningRemarks(getOpeningRemarks(mChatUserGuideModule));
                                }

                            } else {
                                mRootView.onLoadOpeningRemarks(mDefaultTips);
                            }
                        } catch (Exception ex) {
                            Timber.i("##### getChatHistory error=%s", ex.getMessage());
                            mRootView.onLoadOpeningRemarks(mDefaultTips);
                        }

                        Timber.i("##### getChatHistory------------【End】-------------");
                    }
                });
    }

    /**
     * 获取开场白
     */
    private String getOpeningRemarks(ChatUserGuideModule mChatUserGuideModule) {
        String tips = "";
        if (mChatUserGuideModule != null) {
            Timber.i("##### name=%s", mChatUserGuideModule.getName());
            List<ChatInputs> mInputs = mChatUserGuideModule.getInputs();
            if (CommonUtils.isNotEmpty(mInputs)) {
                Timber.i("##### onNext Inputs=%s", mInputs.size());
                tips = mInputs.get(0).getValue().toString();
            }
        }

        return TextUtils.isEmpty(tips) ? mDefaultTips : tips;
    }

    /**
     * 创建会话，类型：聊天、图片
     *
     * @param message 需要内容
     */
    public void chatCreate(String message) {
        // 创建 聊天 会话
        mModel.chatCreate(message)
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        Timber.i("##### t=%s", t.getMessage());
                        showError(1, t.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody info) {
                        onAnalysis(info);
                    }
                });
    }

    /**
     * 多模型会话
     *
     * @param message        聊天文字对话
     * @param isNetworkImage 是否为网络图片：true代表前缀是http；false代表为本地图片
     * @param imageUrl       图片路径
     */
    public void chatMultipleModels(String message, boolean isNetworkImage, String imageUrl) {
        if (isNetworkImage) {
            mModel.chatMultipleModels(imageUrl, message)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            Timber.i("##### t=%s", t.getMessage());
                            showError(1, t.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody info) {
                            onAnalysis(info);
                        }
                    });
        } else {
            ArrayList<String> imagePath = new ArrayList<>();
            imagePath.add(imageUrl);
            // 本地图片，需要上传一下得到URL
            mModel.uploadChatFiles(imagePath)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new CommonRetryWithDelay(0, 2))                 // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .flatMap((Function<ImageUploadResponse, ObservableSource<ResponseBody>>) infoResponse -> {
                        String url = null;
                        if (infoResponse != null) {
                            url = infoResponse.getImgList().get(0).getUrl();
                        }

                        return mModel.chatMultipleModels(url, message)
                                .subscribeOn(Schedulers.io())
                                .retryWhen(new CommonRetryWithDelay(0, 2))// 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                .doOnSubscribe(disposable -> {

                                })
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally(() -> {

                                })
                                .compose(RxLifecycleUtils.bindToLifecycle(mRootView));
                    })
                    .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                        @Override
                        public void onNext(ResponseBody info) {
                            onAnalysis(info);
                        }
                    });
        }
    }

    /**
     * 解析结果
     */
    private void onAnalysis(ResponseBody info) {
        // 流式输出
        buffer = new StringBuffer();
        // 获取response输入流
        InputStream inputStream = info.byteStream();
        // 读取响应数据
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 处理每一行数据：
                // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"中国"},"finish_reason":null}]}
                // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"文"},"finish_reason":null}]}
                // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"化"},"finish_reason":null}]}
                // 判断是否返回了数据，去除response前data关键字，不然解析不了
                Timber.d("##### onResponse:%s", line);
                if (line.contains("event: error")) {
                    // 出现错误
                    buffer.append("请求有误，请检查key的余额。解决办法：调整API最大额度或充值。");
                    mRootView.onLoadError(buffer);
                    break;
                } else {
                    // 正常返回
                    if (line.length() > 6) {
                        Timber.d("##### onResponse: %s", line.substring(6));
                        try {
                            chatCompletionChunk = gson.fromJson(line.substring(5), ChatCompletionChunk.class);
                            Timber.d("onAnalysis: %s", chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                            if (chatCompletionChunk.getChoices().get(0).getDelta().getContent() != null) {
                                addNewlineAfterPeriod(chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                                buffer.append(chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                            }
                            if (chatCompletionChunk.getChoices().get(0).getFinishReason() != null) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            buffer.append("数据解析有误，请联系管理员进行排查。");
                            mRootView.onLoadError(buffer);
                            break;
                        }
                    }
                }
            }

            // 流式展示
            mRootView.onLoadMessage(buffer);
            // 语音播报
            mRootView.onVoiceAnnouncements(buffer.toString());

            Timber.d("onResult: %s", buffer.toString());
        } catch (Exception ignored) {

        }
    }

    /**
     * 语音转文字
     * 目前文件上传限制为 25 MB，并支持以下输入文件类型：mp3、mp4、mpeg、mpga、m4a、wav 和 webm。
     *
     * <p>暂未开放</p>
     */
    public void voiceToText(String path) {
        mModel.voiceToText(new File(path))
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<WhisperResponse>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        Timber.i("##### t=%s", t.getMessage());
                    }

                    @Override
                    public void onNext(WhisperResponse info) {
                        Timber.i("##### Whisper=%s", info.getText());
                        if (!TextUtils.isEmpty(info.getText()))
                            mRootView.onLoadVoiceToText(info.getText());
                    }
                });
    }

    /**
     * 文字转语音
     *
     * <p>暂未开放</p>
     */
    public void textToSpeech(String message) {
        mModel.textToSpeech(message)
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(0, 2))                 // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                // 显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                // 隐藏进度条
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        Timber.i("##### t=%s", t.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody info) {
                        // 获取response输入流
                        InputStream inputStream = info.byteStream();
                        String fileName = "audio_" + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".mp3";
                        try {
                            // 保存流媒体
                            OutputStream mOutputStream = MediaStoreUtils.createFile(mRootView.getActivity(), fileName);
                            MediaStoreUtils.saveFile(mOutputStream, inputStream);
                        } catch (Exception ex) {
                        }

                        // 播放
                        Timber.i("##### tts Succ path=%s", fileName);
                    }
                });
    }

    /**
     * 对字符串进行处理
     */
    public void addNewlineAfterPeriod(String str) {
        StringBuilder sb = new StringBuilder();
        boolean periodFound = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.' || c == '。') {
                periodFound = true;
                sb.append(c);
                sb.append('\n');
            } else if (c == '\n') {
                continue;
            } else {
                sb.append(c);
            }
        }
        if (!periodFound) {
            return;
        }
        sb.toString();
    }


    /**
     * 显示错误信息
     *
     * @param type 类型：0代表自定义错误；1代表系统返回的错误；
     * @param str  错误内容
     */
    private void showError(int type, String str) {
        buffer = new StringBuffer();

        if (type == 0) {
            // 自定义错误
            buffer.append(str);
        } else {
            // 系统返回的错误
            if (str.contains("307")) {
                buffer.append("账户额度不足，请及时联系管理员充值！");
            } else if (str.contains("401")) {
                buffer.append("API密钥无效或未提供。你需要检查你的API密钥是否正确使用，拥有权限并且未过期或者提供。");
            } else if (str.contains("403")) {
                buffer.append("你是未授权访客。");
            } else if (str.contains("413")) {
                buffer.append("请求体太大。你可能需要压缩/减小你的请求数据量。");
            } else if (str.contains("429")) {
                buffer.append("由于发送的同类请求太多的话，你已经超过了你的连接限额。");
            } else if (str.contains("500")) {
                buffer.append("服务器内部错误。这可能是由于服务器发生的问题，不是你的问题。");
            } else if (str.contains("503")) {
                buffer.append("服务器暂时不可用。这可能是由于临时服务器维护/停机等服务器原因导致。");
            } else {
                buffer.append("请求超时，请检查网络并重试");
            }
        }
        mRootView.onLoadError(buffer);
        mRootView.onSucc();
    }
}
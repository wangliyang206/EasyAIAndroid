package com.zqw.mobile.easyai.mvp.model.api;

import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.ChatHistoryResponse;
import com.zqw.mobile.easyai.mvp.model.entity.CommonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatResponse;
import com.zqw.mobile.easyai.mvp.model.entity.ImageUploadResponse;
import com.zqw.mobile.easyai.mvp.model.entity.LoginFastGptResponse;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;
import com.zqw.mobile.easyai.mvp.model.entity.WhisperResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 包名： PACKAGE_NAME
 * 对象名： AccountService
 * 描述：账户相关接口
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 10:03
 */

public interface AccountService {

    /*-----------------------------------------------------------------------用户基本-----------------------------------------------------------------------*/
    //验证Token有效性
    @POST("member/validToken")
    Observable<GsonResponse<LoginResponse>> validToken(@Body GsonRequest<Map<String, Object>> request);

    //获取短信验证码
    @POST("member/loginSMS")
    Observable<GsonResponse<CommonResponse>> loginSMS(@Body GsonRequest<Map<String, String>> request);

    //快捷登录
    @POST("member/quickLogin")
    Observable<GsonResponse<LoginResponse>> quickLogin(@Body GsonRequest<Map<String, String>> request);
    /*-----------------------------------------------------------------------通用-----------------------------------------------------------------------*/

    // 下载
    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String Url);

    // 获取对话列表
    @POST("member/getChatList")
    Observable<GsonResponse<HomeChatResponse>> getChatList(@Body GsonRequest<Map<String, Object>> request);

    // 文件上传（图片）
    @Multipart
    @POST("fileUploadController/uploadFiles")
    Observable<GsonResponse<ImageUploadResponse>> uploadChatFiles(@Part List<MultipartBody.Part> file);
    /*-----------------------------------------------------------------------GPT-----------------------------------------------------------------------*/
    // fastGPT 登录
    @POST()
    Observable<LoginFastGptResponse> loginFastGpt(@Url String Url, @Body RequestBody params);

    // chatGPT 获取历史记录
    @GET()
    Observable<ChatHistoryResponse> getChatHistory(@Url String Url);

    // chatGPT 对话
    @POST()
    Observable<ResponseBody> chatCreate(@Url String Url, @Body RequestBody params);

    // 语音转文字
    @Multipart
    @POST()
    Observable<WhisperResponse> voiceToText(@Url String Url, @Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> requestBodyMap);

    // 文本转语音
    @POST()
    @Streaming
    Observable<ResponseBody> textToSpeech(@Url String Url, @Body RequestBody params);
}

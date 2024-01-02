package com.zqw.mobile.tradeside.mvp.model.api;

import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    //登录-获取短信验证码
    @POST("member/loginSMS")
    Observable<GsonResponse<CommonResponse>> loginSMS(@Body GsonRequest<Map<String, String>> request);
    /*-----------------------------------------------------------------------通用-----------------------------------------------------------------------*/

    // 下载
    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String Url);

}

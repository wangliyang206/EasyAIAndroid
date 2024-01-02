package com.zqw.mobile.easyai.mvp.model.api;

import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 包名： com.zqw.mobile.recycling.api
 * 对象名： SystemApi
 * 描述：系统相关接口
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/30 11:05
 */

public interface SystemService {

    //获取APP版本信息
    @POST("system/getVersion")
    Observable<GsonResponse<AppUpdate>> getVersion(@Body GsonRequest<Map<String, Object>> request);

}

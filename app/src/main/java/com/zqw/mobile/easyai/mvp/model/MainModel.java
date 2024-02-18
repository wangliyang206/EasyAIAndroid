package com.zqw.mobile.easyai.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.mvp.contract.MainContract;
import com.zqw.mobile.easyai.mvp.model.api.AccountService;
import com.zqw.mobile.easyai.mvp.model.api.SystemService;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.model.entity.LoginFastGptResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/02 16:33
 * ================================================
 */
@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<LoginFastGptResponse> logiFastGpt(String username, String password) {
        String text = "{" +
                "\"username\": \"" + username + "\"," +
                "\"password\": \"" + password + "\"" +
                "}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), text);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).loginFastGpt(Constant.FASTGPT_TOKEN,requestBodyJson);
    }

    @Override
    public Observable<AppUpdate> getVersion(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(SystemService.class).getVersion(request));
    }
}
package com.zqw.mobile.easyai.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.easyai.mvp.contract.LoginContract;
import com.zqw.mobile.easyai.mvp.model.api.AccountService;
import com.zqw.mobile.easyai.mvp.model.api.SystemService;
import com.zqw.mobile.easyai.mvp.model.entity.AppUpdate;
import com.zqw.mobile.easyai.mvp.model.entity.CommonResponse;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/04 09:10
 * ================================================
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<CommonResponse> loginSMS(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).loginSMS(request));
    }

    @Override
    public Observable<LoginResponse> quickLogin(String mobile, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).quickLogin(request));
    }

    @Override
    public Observable<AppUpdate> getVersion(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(SystemService.class).getVersion(request));
    }
}
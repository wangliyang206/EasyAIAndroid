package com.zqw.mobile.easyai.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.easyai.mvp.contract.SplashContract;
import com.zqw.mobile.easyai.mvp.model.api.AccountService;
import com.zqw.mobile.easyai.mvp.model.entity.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class SplashModel extends BaseModel implements SplashContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public SplashModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<LoginResponse> validToken() {
        Map<String, Object> params = new HashMap<>();

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).validToken(request));
    }
}
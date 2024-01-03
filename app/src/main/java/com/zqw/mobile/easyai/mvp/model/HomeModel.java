package com.zqw.mobile.easyai.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.easyai.mvp.contract.HomeContract;
import com.zqw.mobile.easyai.mvp.model.api.AccountService;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 11:36
 * ================================================
 */
@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;

    }

    @Override
    public Observable<HomeChatResponse> getChatList(int pageNo, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).getChatList(request));
    }
}
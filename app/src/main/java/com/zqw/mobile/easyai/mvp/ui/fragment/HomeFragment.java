package com.zqw.mobile.easyai.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.di.component.DaggerHomeComponent;
import com.zqw.mobile.easyai.mvp.contract.HomeContract;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatInfo;
import com.zqw.mobile.easyai.mvp.presenter.HomePresenter;
import com.zqw.mobile.easyai.mvp.ui.activity.GeneralAssistantActivity;
import com.zqw.mobile.easyai.mvp.ui.adapter.HomeAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description:首页
 * <p>
 * Created on 2024/01/03 11:36
 *
 * @author 赤槿
 * module name is HomeActivity
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, DefaultAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    /*-------------------------------------------控件信息-------------------------------------------*/
    @BindView(R.id.srla_fragmenthometab_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.revi_fragmenthometab_content)
    RecyclerView mRecyclerView;

    /*-------------------------------------------业务信息-------------------------------------------*/
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    HomeAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();

        this.mLayoutManager = null;
        this.mAdapter = null;
        this.mPaginate = null;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * 在 onActivityCreate()时调用
     */
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        initPaginate();
        onRefresh();
    }

    /**
     * 刷新
     */
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.getChatList(true);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    if (mPresenter != null) {
                        mPresenter.getChatList(false);
                    }
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    /**
     * 点击事件
     */
    @Override
    public void onItemClick(@NonNull View view, int viewType, @NonNull Object data, int position) {
        HomeChatInfo info = (HomeChatInfo) data;

        if (position == 0) {
            ActivityUtils.startActivity(GeneralAssistantActivity.class);
        } else {
            showMessage("暂未开放！");
        }
    }


    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void noInfo() {

    }

    /**
     * 控制是否到底
     */
    @Override
    public void hasLoadedAllItems(boolean val) {
        hasLoadedAllItems = val;
    }

    @Override
    public void showLoading() {
//        if (lilaNotData != null)
//            lilaNotData.setVisibility(View.GONE);
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }
}
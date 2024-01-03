package com.zqw.mobile.easyai.mvp.presenter;

import static com.zqw.mobile.easyai.BuildConfig.IS_DEBUG_DATA;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.easyai.app.config.CommonRetryWithDelay;
import com.zqw.mobile.easyai.app.global.Constant;
import com.zqw.mobile.easyai.app.utils.CommonUtils;
import com.zqw.mobile.easyai.mvp.contract.HomeContract;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatInfo;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatResponse;
import com.zqw.mobile.easyai.mvp.ui.adapter.HomeAdapter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/01/03 11:36
 * ================================================
 */
@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    // 页码
    private int pageNo = 1;
    private int preEndIndex;
    // 数据源
    @Inject
    List<HomeChatInfo> mList;
    @Inject
    HomeAdapter mAdapter;                                                                           // 内容适配器


    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mList != null) {
            this.mList.clear();
            this.mList = null;
        }

        this.mErrorHandler = null;
        this.mAdapter = null;
    }

    /**
     * 获取对话列表
     *
     * @param pullToRefresh 是否下拉刷新
     */
    public void getChatList(final boolean pullToRefresh) {
        //下拉刷新默认只请求第一页
        if (pullToRefresh) pageNo = 1;

        if (IS_DEBUG_DATA) {

            if (pullToRefresh)
                //如果是下拉刷新则清空列表
                mList.clear();

            mList.add(new HomeChatInfo("https://preview.qiantucdn.com/original_origin_pic/19/04/16/3c6a0a1489bb8a7863b001661556e665.png%21qt_gao320", "易收网AI助手","我是易收网智能小助手，我叫小铅，可以回答各种关于易收网的问题。"));
            mList.add(new HomeChatInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202202%2F26%2F20220226175136_ff5d7.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1706864740&t=c4806d79fbb76d3108a5854a665fd4e4", "AI 图片生成","我是一个AI艺术创作者，能够为用户生成各种精美的图片。"));

            if (1 > pageNo) {
                pageNo++;
                mRootView.hasLoadedAllItems(false);
            } else {
                //禁用加载更多
                mRootView.hasLoadedAllItems(true);
            }

            if (pullToRefresh)
                //隐藏下拉刷新的进度条
                mRootView.hideLoading();
            else
                //隐藏加载更多的进度条
                mRootView.endLoadMore();

            mAdapter.notifyDataSetChanged();
        } else {
            mModel.getChatList(pageNo, Constant.PAGESIZE)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new CommonRetryWithDelay(2, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {                                                  // 显示加载效果
                        if (pullToRefresh)
                            //显示下拉刷新的进度条
                            mRootView.showLoading();
                        else
                            //显示上拉加载更多的进度条
                            mRootView.startLoadMore();
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {                                                              // 隐藏加载效果
                        if (pullToRefresh)
                            //隐藏下拉刷新的进度条
                            mRootView.hideLoading();
                        else
                            //隐藏上拉加载更多的进度条
                            mRootView.endLoadMore();
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<HomeChatResponse>(mErrorHandler) {

                        @Override
                        public void onNext(HomeChatResponse infoResponse) {
                            if (infoResponse != null) {
                                if (infoResponse.getTotalPage() > pageNo) {
                                    pageNo++;
                                    mRootView.hasLoadedAllItems(false);
                                } else {
                                    //禁用加载更多
                                    mRootView.hasLoadedAllItems(true);
                                }

                                if (pullToRefresh)
                                    //如果是下拉刷新则清空列表
                                    mList.clear();

                                //更新之前列表总长度,用于确定加载更多的起始位置
                                preEndIndex = mList.size();

                                if (CommonUtils.isNotEmpty(infoResponse.getList())) {
                                    mList.addAll(infoResponse.getList());
                                }

                                if (mList.size() == 0) {
                                    // 显示“暂无信息”
                                    mRootView.noInfo();
                                }

                                if (pullToRefresh)
                                    mAdapter.notifyDataSetChanged();
                                else
                                    mAdapter.notifyItemRangeInserted(preEndIndex, infoResponse.getList().size());
                            }
                        }
                    });
        }
    }
}
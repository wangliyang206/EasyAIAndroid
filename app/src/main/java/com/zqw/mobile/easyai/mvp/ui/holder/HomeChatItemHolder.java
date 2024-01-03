/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqw.mobile.easyai.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.app.utils.CommonUtils;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatInfo;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class HomeChatItemHolder extends BaseHolder<HomeChatInfo> {
    @BindView(R.id.home_item_layout)
    ConstraintLayout mLayout;

    @BindView(R.id.civi_homeitemlayout_pic)
    CircleImageView viewPic;
    @BindView(R.id.txvi_homeitemlayout_name)
    TextView txviName;
    @BindView(R.id.txvi_homeitemlayout_intr)
    TextView txviIntr;

    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public HomeChatItemHolder(View itemView) {
        super(itemView);

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(0, ConvertUtils.dp2px(1), 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    public void setData(@NonNull HomeChatInfo info, int position) {
        if (position == 0) {
            setLayoutMargin(false);
            mLayout.setBackgroundResource(R.color.layout_bg_color);
        } else {
            setLayoutMargin(true);
            mLayout.setBackgroundResource(android.R.color.white);
        }

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.mis_default_error)
                        .errorPic(R.mipmap.mis_default_error)
                        .url(info.getAvatar())
                        .imageView(viewPic)
                        .build());
        txviName.setText(CommonUtils.isEmptyReturnStr(info.getName()));
        txviIntr.setText(CommonUtils.isEmptyReturnStr(info.getIntr()));
    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.viewPic = null;
        this.txviName = null;
        this.txviIntr = null;
    }
}

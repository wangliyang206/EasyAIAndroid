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
package com.zqw.mobile.easyai.mvp.ui.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.easyai.R;
import com.zqw.mobile.easyai.mvp.model.entity.HomeChatInfo;
import com.zqw.mobile.easyai.mvp.ui.holder.HomeChatItemHolder;

import java.util.List;


/**
 * ================================================
 * 首页“对话”列表
 * Created by JessYan on 09/04/2016 12:57
 * ================================================
 */
public class HomeAdapter extends DefaultAdapter<HomeChatInfo> {

    public HomeAdapter(List<HomeChatInfo> infos) {
        super(infos);
    }

    @NonNull
    @Override
    public BaseHolder<HomeChatInfo> getHolder(@NonNull View v, int viewType) {
        return new HomeChatItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.home_item_layout;
    }
}

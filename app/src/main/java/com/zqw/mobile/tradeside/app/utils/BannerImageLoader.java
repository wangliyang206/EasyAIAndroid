package com.zqw.mobile.tradeside.app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Banner加载图片逻辑
 */
public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */

        Banner mBanner = (Banner) path;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (TextUtils.isEmpty(mBanner.getAdvertisingImage())) {
            //Glide 加载图片简单用法
            Glide.with(context).load(mBanner.getLocal()).into(imageView);
        } else {
            //Glide 加载图片简单用法
            Glide.with(context).load(mBanner.getAdvertisingImage()).into(imageView);
        }

    }
}

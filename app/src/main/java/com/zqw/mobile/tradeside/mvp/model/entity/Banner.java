package com.zqw.mobile.tradeside.mvp.model.entity;

/** 轮播图 */
public class Banner {

    public Banner(String advertisingImage, String advertisingLink) {
        this.advertisingImage = advertisingImage;
        this.advertisingLink = advertisingLink;
    }

    public Banner() {
    }

    public Banner(int local) {
        this.local = local;
    }
    // 轮播图地址
    private String advertisingImage = "";
    // 超链接
    private String advertisingLink = "";

    // 本地默认图片
    private int local = 0;

    public String getAdvertisingLink() {
        return advertisingLink;
    }

    public void setAdvertisingLink(String advertisingLink) {
        this.advertisingLink = advertisingLink;
    }

    public String getAdvertisingImage() {
        return advertisingImage;
    }

    public void setAdvertisingImage(String advertisingImage) {
        this.advertisingImage = advertisingImage;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }
}

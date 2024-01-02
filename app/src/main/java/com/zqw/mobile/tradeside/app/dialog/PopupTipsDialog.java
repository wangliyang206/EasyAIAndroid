package com.zqw.mobile.tradeside.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zqw.mobile.tradeside.R;

/**
 * 提示框
 * <p>
 * PopupTipsDialog phoneDialog = new PopupTipsDialog(this, "", this);
 * phoneDialog.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
 *
 * @author wly
 */
public class PopupTipsDialog extends PopupWindow implements
        OnClickListener {

    public interface PopupClick {
        void popupClick(boolean click);
    }

    private View popup;
    private String content;
    private String title;
    private String ok;
    private String cancel;
    private PopupClick popupClick;
    /**
     * 默认不隐藏取消按钮
     */
    private boolean isHideCancel = false;
    // 点击空白区域是否关闭
    private boolean isBlankClose = false;

    /**
     * 构造函数，内容、确定、取消
     */
    public PopupTipsDialog(Context context, String content, PopupClick popupClick) {

        super(context);

        this.content = content;
        this.popupClick = popupClick;

        popup = LayoutInflater.from(context).inflate(R.layout.popup_tips_dialog, null);

        setContentView(popup);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);

        initView();
    }

    /**
     * 构造函数，标题、内容、确定、没有取消
     */
    public PopupTipsDialog(Context context, String title, String content, boolean isHideCancel, PopupClick popupClick) {

        super(context);

        this.title = title;
        this.content = content;
        this.popupClick = popupClick;
        this.isHideCancel = isHideCancel;
        popup = LayoutInflater.from(context).inflate(R.layout.popup_tips_dialog, null);

        setContentView(popup);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);

        initView();
    }

    /**
     * 构造函数，标题、内容、确定(可修改)、取消(可修改)
     */
    public PopupTipsDialog(Context context, String title, String content, String ok, String cancel, PopupClick popupClick) {

        super(context);

        this.title = title;
        this.content = content;
        this.popupClick = popupClick;
        this.cancel = cancel;
        this.ok = ok;
        popup = LayoutInflater.from(context).inflate(R.layout.popup_tips_dialog, null);

        setContentView(popup);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);

        initView();
    }

    /**
     * 构造函数，标题、内容、确定(可修改)、取消(可修改)、点击空白区域是否关闭
     */
    public PopupTipsDialog(Context context, String title, String content, String ok, String cancel, boolean isBlankClose, PopupClick popupClick) {

        super(context);

        this.title = title;
        this.content = content;
        this.popupClick = popupClick;
        this.cancel = cancel;
        this.ok = ok;
        this.isBlankClose = isBlankClose;
        popup = LayoutInflater.from(context).inflate(R.layout.popup_tips_dialog, null);

        setContentView(popup);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);

        initView();
    }

    private void initView() {

        popup.findViewById(R.id.view_pperson_content_layout).setOnClickListener(this);
        popup.findViewById(R.id.imvi_phoneregistconfirm_cancel).setOnClickListener(this);
        if (isBlankClose)
            popup.findViewById(R.id.view_pperson_pop_layout).setOnClickListener(this);

        TextView yes = popup.findViewById(R.id.txvi_phoneregistconfirm_yes);
        TextView no = popup.findViewById(R.id.txvi_phoneregistconfirm_no);
        View line = popup.findViewById(R.id.view_phoneregistconfirm_line);

        TextView txviTitle = popup.findViewById(R.id.txvi_phoneregistconfirm_title);
        if (!TextUtils.isEmpty(title)) {
            txviTitle.setText(title);
        }

        if (!TextUtils.isEmpty(cancel)) {
            no.setText(cancel);
        }

        if (!TextUtils.isEmpty(ok)) {
            yes.setText(ok);
        }

        no.setOnClickListener(this);
        yes.setOnClickListener(this);

        if (isHideCancel) {
            no.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        TextView txviContent = popup.findViewById(R.id.txvi_phoneregistconfirm_tips);
        txviContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_phoneregistconfirm_yes:                                                  // 确定
                if (popupClick != null) {
                    popupClick.popupClick(true);
                }
                dismiss();
                break;
            case R.id.txvi_phoneregistconfirm_no:                                                   // 取消
            case R.id.imvi_phoneregistconfirm_cancel:
            case R.id.view_pperson_pop_layout:
                if (popupClick != null) {
                    popupClick.popupClick(false);
                }
                dismiss();
                break;
            case R.id.view_pperson_content_layout:
                break;
        }

    }

}

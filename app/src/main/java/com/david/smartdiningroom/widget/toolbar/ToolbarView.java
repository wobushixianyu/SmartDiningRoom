package com.david.smartdiningroom.widget.toolbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ToolbarView extends ViewGroup {

    /**
     * 上下文
     */
    private Context context;

    /**
     * toolbar左侧ImageButton，展示返回或者app的logo(默认)
     */
    private ImageButton back_logo_imgBtn;

    /**
     * toolbar居中显示页面名称的TextView
     */
    private TextView tv_title;

    /**
     * toolbar最右侧展示的的关闭按钮(默认)
     */
    private ImageButton close_imgBtn;

    /**
     * toolbar右侧靠左展示的分享按钮(默认)
     */
    private ImageButton share_imgBtn;

    /**
     * 初始化画笔
     */
    private Paint mPaint;

    public ToolbarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        back_logo_imgBtn = new ImageButton(context);
        tv_title = new TextView(context);
        share_imgBtn = new ImageButton(context);
        close_imgBtn = new ImageButton(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    //设置toolbar的title名称
    public void setTitle(String title) {
        tv_title.setText(title);
        tv_title.setTextColor(Color.parseColor("#333333"));
        tv_title.setTextSize(17);
        tv_title.setMaxLines(1);
        tv_title.setEllipsize(TextUtils.TruncateAt.END);
    }

    public void setBackImgButton(@DrawableRes int id, OnClickListener listener){
        back_logo_imgBtn.setImageDrawable(context.getDrawable(id));
        back_logo_imgBtn.setBackgroundResource(android.R.color.transparent);
        back_logo_imgBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (null != listener){
            back_logo_imgBtn.setOnClickListener(listener);
        }
    }

    public void setShareImgBtn(@DrawableRes int id, OnClickListener listener){
        share_imgBtn.setImageDrawable(context.getDrawable(id));
        share_imgBtn.setBackgroundResource(android.R.color.transparent);
        share_imgBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (null != listener){
            share_imgBtn.setOnClickListener(listener);
        }
    }

    public void setCloseImgBtn(@DrawableRes int id, OnClickListener listener){
        close_imgBtn.setImageDrawable(context.getDrawable(id));
        close_imgBtn.setBackgroundResource(android.R.color.transparent);
        close_imgBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (null != listener){
            close_imgBtn.setOnClickListener(listener);
        }
    }
}

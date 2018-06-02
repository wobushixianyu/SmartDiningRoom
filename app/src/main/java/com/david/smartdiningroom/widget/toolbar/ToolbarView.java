package com.david.smartdiningroom.widget.toolbar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.david.smartdiningroom.R;


public class ToolbarView extends RelativeLayout {

    private Context context;

    /**
     * toolbar左侧ImageButton，展示返回或者app的logo(默认)
     */
    private ImageButton backImgBtn;

    /**
     * toolbar居中显示页面名称的TextView
     */
    private TextView tvTitle;

    /**
     * toolbar最右侧展示的的关闭按钮(默认)
     */
    private ImageButton closeImgBtn;

    /**
     * toolbar右侧靠左展示的分享按钮(默认)
     */
    private ImageButton shareImgBtn;

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.my_toolbar_view,this,true);
        backImgBtn = findViewById(R.id.toolbar_back);
        tvTitle = findViewById(R.id.toolbar_title);
        shareImgBtn = findViewById(R.id.toolbar_share);
        closeImgBtn = findViewById(R.id.toolbar_close);
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
        if (tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setBackImgButton(@DrawableRes int id, OnClickListener listener){
        if (backImgBtn != null){
            backImgBtn.setImageDrawable(context.getDrawable(id));
            if (null != listener){
                backImgBtn.setEnabled(true);
                backImgBtn.setOnClickListener(listener);
            }else {
                backImgBtn.setEnabled(false);
            }
        }
    }

    public void setShareImgBtn(@DrawableRes int id, OnClickListener listener){
        if (shareImgBtn != null){
            shareImgBtn.setImageDrawable(context.getDrawable(id));
            if (null != listener){
                shareImgBtn.setEnabled(true);
                shareImgBtn.setOnClickListener(listener);
            }else {
                shareImgBtn.setEnabled(false);
            }
        }
    }

    public void setCloseImgBtn(@DrawableRes int id, OnClickListener listener){
        if (closeImgBtn != null){
            closeImgBtn.setImageDrawable(context.getDrawable(id));
            if (null != listener){
                closeImgBtn.setEnabled(true);
                closeImgBtn.setOnClickListener(listener);
            }else {
                closeImgBtn.setEnabled(false);
            }
        }
    }
}

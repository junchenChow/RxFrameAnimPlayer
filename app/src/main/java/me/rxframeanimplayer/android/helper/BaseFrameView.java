package me.rxframeanimplayer.android.helper;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;


/**
 * Created by zhoujunchen
 * on 16/12/22.
 *
 * @version 2.0, 5/25/2017
 */
public abstract class BaseFrameView extends FrameLayout {
    public BaseFrameView(Context context) {
        this(context, null);
    }

    public BaseFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(getLayoutResId(), this);
        ButterKnife.bind(this, this);
        int bgRes = getBackgroundRes();
        if (bgRes != 0) setBackgroundResource(bgRes);
        initView(context);
    }

    protected abstract int getLayoutResId();

    @DrawableRes
    protected int getBackgroundRes() {
        return 0;
    }

    protected void initView(Context context) {
    }

    protected void bindAttrs(Context context, AttributeSet attributeSet){}
}

package me.rxframeanimplayer.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


import java.lang.ref.WeakReference;

import me.rxframeanimplayer.android.overscroll.IOverScrollDecor;
import me.rxframeanimplayer.android.overscroll.IOverScrollStateListener;
import me.rxframeanimplayer.android.overscroll.IOverScrollUpdateListener;
import me.rxframeanimplayer.android.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.rxframeanimplayer.android.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;


/**
 * Created by zhoujunchen
 * on 17/9/20.
 */

public class ReboundRecyclerView extends RecyclerView implements IOverScrollUpdateListener, IOverScrollStateListener {

    private static final int MSG_TOUCHING_CHECK = 1002;

    private int currOffset;
    private IOverScrollDecor mOverScrollEffect;
    private OnReboundListener onReboundListener;
    private TouchingHandler touchingHandler = new TouchingHandler(this);

    public ReboundRecyclerView(Context context) {
        super(context);
    }

    public ReboundRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReboundRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutManager(new LinearLayoutManager(getContext()));
        mOverScrollEffect = new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(this));
        mOverScrollEffect.setOverScrollUpdateListener(this);
        mOverScrollEffect.setOverScrollStateListener(this);
    }

    @Override
    public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
        if (touchingHandler != null) {
            currOffset = (int) offset;
            Message message = touchingHandler.obtainMessage();
            message.obj = currOffset;
            message.what = MSG_TOUCHING_CHECK;
            touchingHandler.sendMessageDelayed(message, 70L);
        }
    }

    public ReboundRecyclerView setOnReboundListener(OnReboundListener onReboundListener) {
        this.onReboundListener = onReboundListener;
        return this;
    }

    public IOverScrollDecor getOverScrollEffect() {
        return mOverScrollEffect;
    }

    @Override
    public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
        if (onReboundListener != null)
            onReboundListener.onReboundTouchingOverStat(newState);
    }

    public interface OnReboundListener {
        void onReboundTouchingOverStat(int stat);

        void onReboundTouchingOffset(boolean touchingIdle, int offset);
    }

    private static final class TouchingHandler extends Handler {
        WeakReference<ReboundRecyclerView> reboundWeakReference;

        TouchingHandler(ReboundRecyclerView reboundRecyclerView) {
            super(Looper.getMainLooper());
            reboundWeakReference = new WeakReference<>(reboundRecyclerView);
        }

        @Override
        public void handleMessage(Message msg) {
            ReboundRecyclerView reboundRecyclerView = reboundWeakReference.get();
            if (reboundRecyclerView != null) {
                switch (msg.what) {
                    case MSG_TOUCHING_CHECK:
                        int currOffset = (int) msg.obj;
                        reboundRecyclerView.onReboundListener.onReboundTouchingOffset(currOffset == reboundRecyclerView.currOffset && currOffset != 0, reboundRecyclerView.currOffset);
                        break;
                }
            }
        }
    }
}

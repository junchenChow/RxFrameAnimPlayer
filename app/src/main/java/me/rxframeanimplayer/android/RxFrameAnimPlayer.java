package me.rxframeanimplayer.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by zhoujunchen
 * on 17/9/25.
 */
public class RxFrameAnimPlayer {
    public static final String TAG = "RxFrameAnimator";


    private List<AnimationFrame> mAnimationFrames;

    private int mPosition;
    private boolean mPause;
    private boolean mOneShot;

    private Bitmap mRecycleBitmap;

    private ImageView imageView;
    private Disposable mDisposable;

    private FrameAnimListener frameAnimListener;

    public interface FrameAnimListener {
        void onAnimRunning(boolean running, int position);
    }

    private RxFrameAnimPlayer(ImageView view) {
        imageView = view;
    }

    public static RxFrameAnimPlayer with(ImageView imageView) {
        return new RxFrameAnimPlayer(imageView);
    }

    public RxFrameAnimPlayer listener(FrameAnimListener frameAnimListener) {
        this.frameAnimListener = frameAnimListener;
        return this;
    }

    public RxFrameAnimPlayer oneShot() {
        mOneShot = true;
        return this;
    }

    public RxFrameAnimPlayer frames(int[] resIds) {
        if (mAnimationFrames != null && mAnimationFrames.size() > 0) {
            mAnimationFrames.clear();
        } else {
            mAnimationFrames = new ArrayList<>();
        }
        for (int resId : resIds) {
            mAnimationFrames.add(new AnimationFrame(resId));
        }
        return this;
    }

    public RxFrameAnimPlayer frames(List<AnimationFrame> animationFrames) {
        if (mAnimationFrames != null && mAnimationFrames.size() > 0) {
            mAnimationFrames.clear();
        } else {
            mAnimationFrames = new ArrayList<>();
        }
        mAnimationFrames.addAll(animationFrames);
        return this;
    }

    public RxFrameAnimPlayer performInterval(long period, TimeUnit unit) {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        if (mAnimationFrames == null || mAnimationFrames.size() == 0) {
            Log.e(TAG, "not found mPngData！！");
            return this;
        }

        //noinspection Convert2MethodRef
        mDisposable = Flowable.interval(period, unit)
                .filter(l -> checkRunning())
                .map(aLong -> {
                    AnimationFrame frameData = getNext();
                    decodeBitmap(frameData.getResourceId());
                    mPosition++;
                    return new BitmapDrawable(imageView.getResources(), mRecycleBitmap);
                })
                .onBackpressureDrop()
                .filter(bitmapDrawable -> null != bitmapDrawable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(bitmapDrawable -> imageView.setImageDrawable(bitmapDrawable))
                .subscribe();
        return this;
    }

    private boolean checkRunning() {
        boolean run = !mPause && imageView.isShown();
        if(frameAnimListener!=null)
            frameAnimListener.onAnimRunning(run, mPosition);
        return run;
    }

    private void decodeBitmap(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        if (mRecycleBitmap != null)
            options.inBitmap = mRecycleBitmap;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        mRecycleBitmap = BitmapFactory.decodeResource(imageView.getResources(), resId, options);
    }

    public void pause() {
        this.mPause = true;
    }

    public RxFrameAnimPlayer start() {
        this.mPause = false;
        return this;
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (isRunning()) {
            mDisposable.dispose();
        }
    }

    public boolean isRunning() {
        return mDisposable != null && !mDisposable.isDisposed() && !mPause;
    }

    public void destroy() {
        stop();
        mDisposable = null;
        imageView.setImageBitmap(null);
        imageView = null;
    }

    private AnimationFrame getNext() {
        if (mPosition >= mAnimationFrames.size()) {
            mPosition = 0;
            if (mOneShot) {
                decodeBitmap(mAnimationFrames.get(mPosition).getResourceId());
                imageView.setImageDrawable(new BitmapDrawable(imageView.getResources(), mRecycleBitmap));
                stop();
            }
        }
        return mAnimationFrames.get(mPosition);
    }

    public int getPosition() {
        return mPosition;
    }

    private class AnimationFrame {
        private int mResourceId;

        AnimationFrame(int resourceId) {
            mResourceId = resourceId;
        }

        public int getResourceId() {
            return mResourceId;
        }
    }
}

package me.rxframeanimplayer.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.rxframeanimplayer.android.helper.BaseFrameView;
import me.rxframeanimplayer.android.helper.ViewAnimHelper;
import me.rxframeanimplayer.android.overscroll.IOverScrollState;

/**
 * Created by zhoujunchen
 * on 17/9/21.
 */

public class KoalaClimbTreeView extends BaseFrameView {

    private static final int[] KOALA_ANIM_ARRAY = {
            R.mipmap.koala_anim_0001,
            R.mipmap.koala_anim_0002,
            R.mipmap.koala_anim_0003,
            R.mipmap.koala_anim_0004,
            R.mipmap.koala_anim_0005,
            R.mipmap.koala_anim_0006,
            R.mipmap.koala_anim_0007,
            R.mipmap.koala_anim_0008};

    private static final int[] KOALA_EYES_ANIM_ARRAY = {
            R.mipmap.koala_eyes_anim_0001,
            R.mipmap.koala_eyes_anim_0002,
            R.mipmap.koala_eyes_anim_0003,
            R.mipmap.koala_eyes_anim_0004,
            R.mipmap.koala_eyes_anim_0005,
            R.mipmap.koala_eyes_anim_0006,
            R.mipmap.koala_eyes_anim_0007,
            R.mipmap.koala_eyes_anim_0008};

    private static final int[] KOALA_TREE_ANIM_ARRAY = {
            R.mipmap.koala_tree_01,
            R.mipmap.koala_tree_02,
            R.mipmap.koala_tree_03,
            R.mipmap.koala_tree_04,
            R.mipmap.koala_tree_05,
            R.mipmap.koala_tree_06,
            R.mipmap.koala_tree_07,
            R.mipmap.koala_tree_08,
            R.mipmap.koala_tree_09,
            R.mipmap.koala_tree_10,
            R.mipmap.koala_tree_11,
            R.mipmap.koala_tree_12,
            R.mipmap.koala_tree_13,
            R.mipmap.koala_tree_14,
            R.mipmap.koala_tree_15,
            R.mipmap.koala_tree_16,
            R.mipmap.koala_tree_17,
            R.mipmap.koala_tree_18};


    @BindView(R.id.ivTreeBranch)
    ImageView ivTreeBranch;
    @BindView(R.id.iv_koala)
    ImageView ivKoala;
    @BindView(R.id.tv_koala_text)
    TextView tvKoalaText;
    @BindView(R.id.view_guide)
    View guideView;
    @BindView(R.id.iv_tree_cloud)
    ImageView ivCloud;

    public static final int POSITION_Y = 1;
    public static final int CLIMB_STAT_ARRIVE = 1001;

    private int treeBranchPositionY;
    private int[] koalaPosition = new int[2];
    private int[] treeCloudPosition = new int[2];
    private int[] treeBranchPosition = new int[2];


    private boolean arrivingTreeBranch = true;
    private RxFrameAnimPlayer koalaAnimPlayer, koalaEyesAnimPlayer, koalaTreeAnimPlayer;

    private OnKoalaClimbTreeListener onKoalaClimbTreeListener;

    public interface OnKoalaClimbTreeListener {
        void onKoalaClimbStat(int status);
    }

    public KoalaClimbTreeView setOnKoalaClimbTreeListener(OnKoalaClimbTreeListener onKoalaClimbTreeListener) {
        this.onKoalaClimbTreeListener = onKoalaClimbTreeListener;
        return this;
    }

    public KoalaClimbTreeView(Context context) {
        super(context);
    }

    public KoalaClimbTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KoalaClimbTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_koala_climb_tree;
    }

    @Override
    protected void initView(Context context) {
        koalaAnimPlayer = RxFrameAnimPlayer.with(ivKoala)
                .frames(KOALA_ANIM_ARRAY)
                .performInterval(130, TimeUnit.MILLISECONDS);
        koalaEyesAnimPlayer = RxFrameAnimPlayer.with(ivKoala)
                .frames(KOALA_EYES_ANIM_ARRAY)
                .performInterval(130, TimeUnit.MILLISECONDS);
        koalaTreeAnimPlayer = RxFrameAnimPlayer.with(ivTreeBranch)
                .frames(KOALA_TREE_ANIM_ARRAY)
                .performInterval(130, TimeUnit.MILLISECONDS);
        tvKoalaText.setVisibility(INVISIBLE);
    }

    public void reset() {
        if (koalaAnimPlayer != null)
            koalaAnimPlayer.reset();
        tvKoalaText.setVisibility(INVISIBLE);
        tvKoalaText.setText("继续上滑发现更多精彩");
    }

    public void startKoalaClimbTreeAnim(boolean touchingIdle, int offset, int overStat) {
        guideView.getLocationInWindow(getKoalaPosition());
        ivCloud.getLocationInWindow(getCloudPositions());
        int koalaPositionY = (getKoalaPosition()[POSITION_Y]);
        if (treeBranchPositionY == 0) {
            ivTreeBranch.getLocationInWindow(getTreeBranchPosition());
            treeBranchPositionY = (getTreeBranchPosition()[POSITION_Y] + ivTreeBranch.getHeight() / 2);
        }
        ViewAnimHelper.setTranslationY(guideView, offset);
        ViewAnimHelper.setTranslationY(ivCloud, (-offset) / 3);

        if (koalaPositionY <= (treeBranchPositionY - ivKoala.getHeight() / 3)) {
            if (overStat == IOverScrollState.STATE_BOUNCE_BACK) {
                tvKoalaText.postDelayed(() -> {
                            if (onKoalaClimbTreeListener != null) {
                                onKoalaClimbTreeListener.onKoalaClimbStat(CLIMB_STAT_ARRIVE);
                            }
                        }, 300L
                );
            }
            arrivingTreeBranch = true;
            tvKoalaText.setText("释放跳转传送门");
        }
        if (koalaPositionY <= treeBranchPositionY) {
            if (arrivingTreeBranch) {
                arrivingTreeBranch = false;
                tvKoalaText.setVisibility(VISIBLE);

                if (koalaTreeAnimPlayer != null)
                    koalaTreeAnimPlayer.start();

                if (koalaAnimPlayer != null) {
                    koalaAnimPlayer.pause();
                }
                if (koalaEyesAnimPlayer != null)
                    koalaEyesAnimPlayer.start();
            }
            return;
        } else {
            arrivingTreeBranch = true;
            if (koalaPositionY > treeBranchPositionY) {
                ViewAnimHelper.setTranslationY(ivKoala, offset);
            }
        }
        if (offset != 0) {
            if (tvKoalaText.isShown()) {
                tvKoalaText.postDelayed(() -> tvKoalaText.setVisibility(INVISIBLE), 50L);
            }
            if (touchingIdle) {
                if (koalaAnimPlayer != null)
                    koalaAnimPlayer.pause();
                if (koalaEyesAnimPlayer != null)
                    koalaEyesAnimPlayer.start();
            } else {
                if (koalaEyesAnimPlayer != null)
                    koalaEyesAnimPlayer.pause();
                if (koalaAnimPlayer != null)
                    koalaAnimPlayer.start();
            }
        } else {
            koalaAnimPlayer.reset();
            koalaTreeAnimPlayer.reset();
        }
    }

    public void stop() {
        reset();
        if (koalaEyesAnimPlayer != null)
            koalaEyesAnimPlayer.stop();
        if (koalaTreeAnimPlayer != null)
            koalaTreeAnimPlayer.stop();
    }

    private int[] getKoalaPosition() {
        if (koalaPosition == null)
            koalaPosition = new int[2];
        return koalaPosition;
    }

    private int[] getTreeBranchPosition() {
        if (treeBranchPosition == null)
            treeBranchPosition = new int[2];
        return treeBranchPosition;
    }

    private int[] getCloudPositions() {
        if (treeCloudPosition == null)
            treeCloudPosition = new int[2];
        return treeCloudPosition;
    }
}

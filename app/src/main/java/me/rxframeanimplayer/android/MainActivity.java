package me.rxframeanimplayer.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static me.rxframeanimplayer.android.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.rxframeanimplayer.android.overscroll.IOverScrollState.STATE_IDLE;

public class MainActivity extends AppCompatActivity implements ReboundRecyclerView.OnReboundListener, KoalaClimbTreeView.OnKoalaClimbTreeListener {

    @BindView(R.id.reboundScrollLayout)
    ReboundRecyclerView reboundRecyclerView;
    @BindView(R.id.koala_climb_tree)
    KoalaClimbTreeView koalaClimbTreeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        reboundRecyclerView.setOnReboundListener(this);
        koalaClimbTreeView.setOnKoalaClimbTreeListener(this);
        ReboundAdapter reboundAdapter = new ReboundAdapter();
        reboundRecyclerView.setAdapter(reboundAdapter);
    }

    @Override
    public void onReboundTouchingOverStat(int stat) {
        if (stat == STATE_BOUNCE_BACK || stat == STATE_IDLE)
            koalaClimbTreeView.reset();
    }

    @Override
    public void onReboundTouchingOffset(boolean touchingIdle, int offset) {
        koalaClimbTreeView.startKoalaClimbTreeAnim(touchingIdle, offset, reboundRecyclerView.getOverScrollEffect().getCurrentState());
    }

    private boolean hasClimbTrigger;
    @Override
    public void onKoalaClimbStat(int status) {
        if (status == KoalaClimbTreeView.CLIMB_STAT_ARRIVE && !hasClimbTrigger) {
            hasClimbTrigger = true;
            Toast.makeText(this, "release all frame anim~", Toast.LENGTH_SHORT).show();
        }
    }
}

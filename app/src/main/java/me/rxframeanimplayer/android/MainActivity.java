package me.rxframeanimplayer.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int[] IMAGE_RESOURCES = {
            R.mipmap.koala_anim_0001,
            R.mipmap.koala_anim_0002,
            R.mipmap.koala_anim_0003,
            R.mipmap.koala_anim_0004,
            R.mipmap.koala_anim_0005,
            R.mipmap.koala_anim_0006,
            R.mipmap.koala_anim_0007,
            R.mipmap.koala_anim_0008};

    RxFrameAnimPlayer rxFrameAnimPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);


        rxFrameAnimPlayer = RxFrameAnimPlayer.with(imageView)
                .frames(IMAGE_RESOURCES)
                .listener(((run, position) -> {}))
                .performInterval(130, TimeUnit.MILLISECONDS)
                .start();

        imageView.setOnClickListener(v -> {
            if(rxFrameAnimPlayer.isRunning()){
                rxFrameAnimPlayer.pause();
            }else {
                rxFrameAnimPlayer.start();
            }
        });
    }

}

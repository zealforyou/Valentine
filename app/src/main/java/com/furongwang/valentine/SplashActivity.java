package com.furongwang.valentine;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.baidu.navi.shelldemo.R;


public class SplashActivity extends FragmentActivity {

    private VideoView videoView;
    private TextView btn_im_enter;
    private MediaController mediaco;
    private String uri;
    Handler handler = new Handler();
    private View fl_video;
    private ImageView iv_window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void playVideo() {
        //让VideiView获取焦点
        iv_window.setVisibility(View.GONE);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                uri = "android.resource://" + getPackageName() + "/" + R.raw.sugar;
                videoView.setVideoURI(Uri.parse(uri));
                videoView.start();

            }
        });
    }

    private void initView() {
        fl_video = findViewById(R.id.fl_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        btn_im_enter = (TextView) findViewById(R.id.btn_im_enter);
        iv_window = (ImageView) findViewById(R.id.iv_window);
        mediaco = new MediaController(this);
        mediaco.setVisibility(View.INVISIBLE);
        uri = "android.resource://" + getPackageName() + "/" + R.raw.sugar;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(mediaco);
        mediaco.setMediaPlayer(videoView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fl_video.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_down);
                animation.setInterpolator(new OvershootInterpolator());
                fl_video.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        playVideo();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        }, 500);
        btn_im_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_window.setVisibility(View.VISIBLE);
                if (videoView.canPause()) {
                    videoView.pause();
                }
                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_out);
                animation.setInterpolator(new AnticipateInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fl_video.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fl_video.startAnimation(animation);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_im_enter.setVisibility(View.VISIBLE);
                btn_im_enter.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim
                        .aph));
            }
        }, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

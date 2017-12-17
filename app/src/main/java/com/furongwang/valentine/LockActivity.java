package com.furongwang.valentine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navi.shelldemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LockActivity extends FragmentActivity {

    private View ll_limit_timer;
    private Subscription subscribe;
    private long limitS;
    TextView tv_hour;
    TextView tv_minute;
    TextView tv_second;
    private ImageView iv_left;
    private ImageView iv_right;
    Handler handler = new Handler();
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ll_limit_timer = findViewById(R.id.ll_limit_timer);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_minute = (TextView) findViewById(R.id.tv_minute);
        tv_second = (TextView) findViewById(R.id.tv_second);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        ll_limit_timer.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }

        }, 1000);
    }

    private void startAnim() {
        iv_left.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.VISIBLE);
        Animation left = AnimationUtils.loadAnimation(this, R.anim.lock_anim_left);
        Animation right = AnimationUtils.loadAnimation(this, R.anim.lock_anim_right);
        left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_limit_timer.setVisibility(View.VISIBLE);
                initTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_left.startAnimation(left);
        iv_right.startAnimation(right);

    }


    /**
     * 初始化倒计时
     */
    private void initTimer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = null;
        try {
            endTime = sdf.parse("2017-08-28 18:2:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        limitS = endTime.getTime() - System.currentTimeMillis();
        if (limitS < 0) {
            startActivity(new Intent(LockActivity.this, SplashActivity.class));
            return;
        }
        limitS = limitS / 1000;
        subscribe = Observable.interval(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (limitS == 0 && isFirst) {
                            isFirst = false;
                            startActivity(new Intent(LockActivity.this, SplashActivity.class));
                            finish();
                            return;
                        }
                        long hour = limitS / 60 / 60;
                        long m = limitS / 60 % 60;
                        long ss = limitS % 60;
                        tv_hour.setText(hour < 10 ? "0" + hour : "" + hour);
                        tv_minute.setText(m < 10 ? "0" + m : "" + m);
                        tv_second.setText(ss < 10 ? "0" + ss : "" + ss);
                        limitS--;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.unsubscribe();
    }
}

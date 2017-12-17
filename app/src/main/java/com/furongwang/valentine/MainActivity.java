package com.furongwang.valentine;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navi.shelldemo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;


public class MainActivity extends FragmentActivity {

    private TextView tv_lyric, textView, tv_author, tv_enter, tv_msg_girl, tv_msg, btn_send;
    private char[] lyricList;
    private Timer timer;
    private Handler handler;
    private int lyric_index;
    private StringBuffer strb = new StringBuffer();
    private ImageView iv_kiss, iv_music;
    public static MainActivity activity;
    private EditText et_msg;
    private BmobIMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        musicService = new MusicService();
        bindServiceConnection();
        initView();
        initData();
        initCtrl();
        initMob();

    }

    private void initMob() {
        BmobIM.connect("5201314", new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        //如果需要更新用户资料，开发者只需要传新的info进去就可以了
        BmobIMUserInfo info = new BmobIMUserInfo("15080772093", "zz", "");
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_send.setClickable(true);
                            sendMsg("对方打开APP", false);
                        }
                    }, 1000);

                } else {
                    Toast.makeText(MainActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initView() {
        tv_lyric = ((TextView) findViewById(R.id.tv_lyric));
        textView = ((TextView) findViewById(R.id.textView));
        tv_enter = ((TextView) findViewById(R.id.tv_enter));
        tv_author = ((TextView) findViewById(R.id.tv_author));
        tv_msg = ((TextView) findViewById(R.id.tv_msg));
        tv_msg_girl = ((TextView) findViewById(R.id.tv_msg_girl));
        btn_send = ((TextView) findViewById(R.id.btn_send));
        iv_kiss = ((ImageView) findViewById(R.id.iv_kiss));
        iv_music = ((ImageView) findViewById(R.id.iv_music));
        et_msg = ((EditText) findViewById(R.id.et_msg));
        timer = new Timer();
        handler = new Handler();
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "迷你简丫丫.ttf");
        tv_lyric.setTypeface(typeFace);
        textView.setTypeface(typeFace);
        tv_enter.setTypeface(typeFace);
        et_msg.setTypeface(typeFace);
        btn_send.setTypeface(typeFace);
        tv_msg.setTypeface(typeFace);
        tv_msg_girl.setTypeface(typeFace);
        tv_author.setTypeface(typeFace);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setDuration(2500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        iv_music.setAnimation(animation);
    }

    private void initData() {
        lyricList = getResources().getString(R.string.song_lyric).toCharArray();
    }

    private void initCtrl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (lyric_index == lyricList.length - 1) {
                            tv_enter.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            findViewById(R.id.ll_input).setVisibility(View.VISIBLE);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.move1);
                            textView.startAnimation(animation);
                            timer.cancel();
                        } else if (lyric_index < lyricList.length) {
                            strb.append(lyricList[lyric_index]);
                            tv_lyric.setText(strb.toString());
                            lyric_index++;
                        }
                    }
                });
            }
        }, 0, 300);
        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_kiss.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.kiss);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv_kiss.setVisibility(View.GONE);
                        startActivity(new Intent(MainActivity.this, MapActivity.class));

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv_kiss.startAnimation(animation);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_msg.getText().toString();
                sendMsg(s, true);
            }
        });
    }

    private void sendMsg(final String text, final boolean show) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {
                    et_msg.setText("");
                    if (show) {
                        tv_msg_girl.setText(text);
                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.move2);
                        tv_msg_girl.startAnimation(animation);
                    }
                }
            }
        });
    }

    public void setMsg(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.move2);
                tv_msg.setText(msg);
                tv_msg.startAnimation(animation);
            }
        });
    }

    private MusicService musicService;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
//        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
//        startService(new Intent(HouseActivity.this, MusicService.class).putExtra("cmd",
//                MusicService.CMD_START));
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        activity = null;
        unbindService(sc);
        BmobIM.getInstance().disConnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        finish();
    }
}

package com.furongwang.valentine;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    public static final int CMD_START = 2;
    public final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public static MediaPlayer mp = new MediaPlayer();

    public MusicService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int cmd = intent.getIntExtra("cmd", 0);
        switch (cmd) {
            case CMD_START:
                play();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        try {
            AssetManager assetMg = this.getApplicationContext().getAssets();
            AssetFileDescriptor fileDescriptor = assetMg.openFd("wwdxf.mp3");
            mp.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Log.d("hint", "can't get to the song");
            e.printStackTrace();
        }
        return binder;
    }

    public void play() {
        if (!mp.isPlaying()) {
            mp.start();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        return super.onUnbind(intent);
    }
}

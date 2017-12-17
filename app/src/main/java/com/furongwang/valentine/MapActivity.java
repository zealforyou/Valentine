package com.furongwang.valentine;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navi.shelldemo.R;


public class MapActivity extends FragmentActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Handler handler = new Handler();
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        imageView = (ImageView) findViewById(R.id.image);
        mBaiduMap = mMapView.getMap();
        //定义Maker坐标点113.081204,28.201534
        final LatLng point = new LatLng(28.201534, 113.081204);
        //
        MapStatusUpdate zo = MapStatusUpdateFactory.newLatLngZoom(point, 10);
        mBaiduMap.animateMapStatus(zo);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBaiduMap != null) {
                    MapStatusUpdate zo = MapStatusUpdateFactory.newLatLngZoom(point, 19);
                    mBaiduMap.animateMapStatus(zo);
                }
            }
        }, 2500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAnim();
            }
        }, 3000);
    }

    void initAnim() {
        AnimationDrawable anim = new AnimationDrawable();
        for (int i = 1; i <= 10; i++) {
            int id = getResources().getIdentifier("axt" + i, "mipmap",
                    getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            anim.addFrame(drawable, 100);
        }
        anim.setOneShot(false);
        imageView.setImageDrawable(anim);
        anim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

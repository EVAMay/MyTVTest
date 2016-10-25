package com.myapp.mytvtest.mytvtest.fragment;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.mytvtest.mytvtest.R;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainLayout;
import com.open.androidtvwidget.view.MainUpView;

import ijkplayer.Settings;
import ijkplayer.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by lenovo on 2016/10/20.
 */

public class HotFragment extends Fragment {
    private IjkVideoView videoView;
    private Settings mSetting;
    private MainUpView mainUpView;//顶层边框
    private OpenEffectBridge mOpenEffectBridge;//边框移动的动画
    private View mOldFocus;//焦点,4.3以下版本需要自己保存.
    private MainLayout mian_lay;//防止被覆盖的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView= (IjkVideoView) view.findViewById(R.id.ijkVideoView);
        mainUpView= (MainUpView) view.findViewById(R.id.mainUpView);//顶层边框
        mOpenEffectBridge= (OpenEffectBridge) mainUpView.getEffectBridge();
        mian_lay= (MainLayout) view.findViewById(R.id.main_lay);//防止放大的view被压在下面. (建议使用MainLayout)

        // 4.2 绘制有问题，所以不使用绘制边框.
        // 也不支持倒影效果，绘制有问题.
        // 请大家不要按照我这样写.
        // 如果你不想放大小人超出边框(demo，张靓颖的小人)，可以不使用OpenEffectBridge.
        // 我只是测试----DEMO.(建议大家使用 NoDrawBridge)
        if (Utils.getSDKVersion()==17){
            switchNoDrawBridgeVersion();
        }else {
            //其他版本SDK直接设置边框及阴影资源
            mainUpView.setUpRectResource(R.drawable.white_light_10);//这里是白色边框
            mainUpView.setShadowResource(R.drawable.item_shadow);//设置音乐图片资源
        }
        videoPlay();//播放视频的方法


    }

    /**
     * 根据版本设置移动边框的图片.设置移动边框的阴影.
     */
    private void switchNoDrawBridgeVersion() {
        EffectNoDrawBridge effectNoDrawBridge=new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);//设置边框动画时间
        mainUpView.setEffectBridge(effectNoDrawBridge);
        mainUpView.setUpRectResource(R.drawable.white_light_10);//设置边框图片，这里是白色边框
        mainUpView.setDrawUpRectPadding(new Rect(5,5,5,5));//设置边框间距
    }

    private void videoPlay() {
        mSetting=new Settings(getContext());
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");//调用解码库
//        videoView.setVideoURI(Uri.parse("http://106.36.45.36/live.aishang.ctlcdn.com/00000110240001_1/encoder/1/playlist.m3u8"));
        videoView.setVideoURI(Uri.parse("http://220.194.215.22:8080/nn_live/nn_x64/aWQ9S0xDRE1QUDM2MCZ1cmxfYzE9MjAwMCZubl9haz0wMTExNjRmZWQ3MDEwYjE3ZDY4NDg2MTYyYzE5NTQxY2JlJm50dGw9MyZuY21zaWQ9MTIwMTIwMDYmbmdzPTU4MDg3NmVhMDAwMzcxYjRhMGExNTA0MDhhNGUwYTU1Jm5uZD16Z2x0LnNpY2h1YW4ueWFhbiZuZnQ9dHMmZGZsb3c9MSZubl91c2VyX2lkPTAmbmRpPTEwJm5kdj0yMDE1MDYwNC4xJm5kdD1wYWQmbmFsPTAxZWE3NjA4NTgwNjA3ZjQwZjcxOWM0NzQ2OGZiOWM4ZWYzYTBlNGJlNzNkMjI,/KLCDMPP360.m3u8"));
//       videoView.setVideoURI(Uri.parse("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com   /D046015255134077DDB3ACA0D7E68D45.flv"));
//      videoView.setVideoURI(Uri.parse("http://220.194.215.41:8080/nn_live/nn_x64/aWQ9U0hEWU1QUDM2MCZ1cmxfYzE9MjAwMCZubl9haz0wMTliYjg2ZjE3ODYwNDUzZDQ0ODVhODA5Mzg0YTBlNzRiJm50dGw9MyZuY21zaWQ9MTIwMTIwMDgmbmdzPTU4MDk4MmY3MDAwYjU3ZWMxY2NmM2ViYzJkYjMzYWEzJm5uZD16Z2x0LnNpY2h1YW4ueWFhbiZuZnQ9dHMmZGZsb3c9MSZubl91c2VyX2lkPTAmbmRpPTEwJm5kdj0yMDE1MDYwNC4xJm5kdD1wYyZuYWw9MDFmNzgyMDk1ODA2MDdkNGNlZTQ0NWNlODY0Mzg1NTBiMGYwMjZkMzMxN2RjMw,,/SHDYMPP360.flv"));
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.getVideoHeight();
                videoView.start();
            }
        });
   }

    //失去焦点时就停止播放
    @Override
    public void onPause() {
        videoView.release(true);

        super.onPause();
    }


    @Override
    public void onDetach() {
        videoView.release(true);
        super.onDetach();
    }
}

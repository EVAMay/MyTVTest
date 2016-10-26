package com.myapp.mytvtest.mytvtest.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.myapp.mytvtest.mytvtest.R;

import ijkplayer.media.IjkVideoView;
import pl.droidsonroids.gif.GifImageView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by lenovo on 2016/10/25.
 */

public class VideoPlayActivity extends Activity {
    private IjkVideoView ijkVideoView;
    private String videoPath="";
    private GifImageView gif_play;//加载动画
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        initView();
        videoPath="http://58.135.196.138:8090/live/6E1087643B3D445fBCECAF3B54544767/6E1087643B3D445fBCECAF3B54544767.m3u8";

    }

    @Override
    protected void onResume() {
        super.onResume();
        time=5;
        handlder.sendEmptyMessage(1);
    }

    private void playVideo() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");//调用解码库
        ijkVideoView.setVideoURI(Uri.parse(videoPath));

        //视频准备好的监听，当时视频加载完成后再隐藏加载动画
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                ijkVideoView.start();
                gif_play.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 显示5秒动画后再加载视频
     */
    int time=5;
    private Handler handlder=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            if (time>0){
                handlder.sendEmptyMessageDelayed(1,1000);
            }else if(time<=0){

                playVideo();
            }
        }
    };
    private void initView() {
    ijkVideoView= (IjkVideoView) findViewById(R.id.ijkVideoView_play);
        gif_play= (GifImageView) findViewById(R.id.gif_play);
    }

    @Override
    public void onBackPressed() {
        if (ijkVideoView.isPlaying()){
            ijkVideoView.release(true);
        }
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        handlder.removeMessages(1);
        super.onDestroy();
    }
}

package com.myapp.mytvtest.mytvtest.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.myapp.mytvtest.mytvtest.R;
import com.myapp.mytvtest.mytvtest.util.TimeUtil;
import com.open.androidtvwidget.view.ReflectItemView;

import ijkplayer.media.IjkVideoView;
import pl.droidsonroids.gif.GifImageView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by lenovo on 2016/10/25.
 */

public class VideoPlayActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private IjkVideoView ijkVideoView;
    private String videoPath="";
    private GifImageView gif_play;//加载动画
    private TextView currentTime_play,durationTime_play,textPercent;//时间显示
    private SeekBar seekBar_play;//进度条
    private TimeUtil timeUtil;//设置时间格式的工具类
    private ImageButton rewind_play,start_play,forward_play;
    private ReflectItemView start_play_item;

    private int currentProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        initView();
        timeUtil=new TimeUtil();//初始化时间工具类
//        videoPath="http://58.135.196.138:8090/live/6E1087643B3D445fBCECAF3B54544767/6E1087643B3D445fBCECAF3B54544767.m3u8";
        videoPath="http://119.6.239.57:9999/61.182.133.114/youku/6772B42E504448316826A82A69/0300800B00577B84E98EA8188AEEA33576CDB3-F137-58B2-6A1E-6F44C8B9D856.flv";

    }

    //进入页面获取到焦点时，动画播放完毕后开始加载视频
    @Override
    protected void onResume() {
        super.onResume();
        time=3;
        handlder.sendEmptyMessage(1);
        seekBar_play.setOnSeekBarChangeListener(this);
        rewind_play.setOnClickListener(this);
        start_play.setOnClickListener(this);
        forward_play.setOnClickListener(this);
//        start_play_item.setOnClickListener(this);
    }

    /**
     * ijkplayer播放相关监听
     */
    private void playVideo() {

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");//调用解码库
        ijkVideoView.setVideoURI(Uri.parse(videoPath));

        //视频准备好的监听，当视频加载完成后再隐藏加载动画
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
//                if (!(ijkVideoView.isPlaying())){
//                    ijkVideoView.seekTo(seekBar_play.getProgress());
//                }
                startVideo();

            }
        });
        //视频播放完的监听
        ijkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                currentTime_play.setText("00:00");
                seekBar_play.setProgress(0);
                start_play.setImageResource(R.drawable.play_button);

            }
        });

    }

    private void startVideo() {
        start_play.setImageResource(R.drawable.pause_button);
        ijkVideoView.start();
        gif_play.setVisibility(View.GONE);
        durationTime_play.setText(timeUtil.time(ijkVideoView.getDuration()));
        showSeekBar();
        showTime();
        Log.d("debug","当前进度是："+(ijkVideoView.getCurrentPosition()+""));
        Log.d("debug","总的进度是："+(ijkVideoView.getDuration()+""));
    }

    //seekBar更新的方法
    private void showSeekBar() {
        timeHanlder.sendEmptyMessage(2);
        seekBar_play.setMax(ijkVideoView.getDuration());

    }

    /**
     * 视频开始播放，显示时间的方法
     */
    private void showTime() {
        timeHanlder.sendEmptyMessage(2);

    }

    /**
     * 用来设置时间的及seekBar 的hanlder
     */
    private Handler timeHanlder=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ijkVideoView.isPlaying()){
                currentTime_play.setText(timeUtil.time(ijkVideoView.getCurrentPosition()));
                currentProgress=ijkVideoView.getCurrentPosition();
                seekBar_play.setProgress(currentProgress);
                timeHanlder.sendEmptyMessageDelayed(2,300);//每隔300毫秒通知设置当前时间
            }


        }
    };

    /**
     * 显示3秒动画后再加载视频
     */
    int time=3;
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
        currentTime_play= (TextView) findViewById(R.id.currentTime_play);
        durationTime_play= (TextView) findViewById(R.id.durationTime_play);
        seekBar_play= (SeekBar) findViewById(R.id.seekBar_play);
        textPercent= (TextView) findViewById(R.id.textPercent);
        rewind_play= (ImageButton) findViewById(R.id.rewind_play);
        start_play= (ImageButton) findViewById(R.id.start_play);
        forward_play= (ImageButton) findViewById(R.id.forward_play);
//        start_play_item= (ReflectItemView) findViewById(R.id.start_item_play);
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
        handlder.removeMessages(2);
        super.onDestroy();
    }

    /**
     * seekBar进度条拖动的监听
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentTime_play.setText(timeUtil.time(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        ijkVideoView.pause();
        start_play.setImageResource(R.drawable.play_button);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        currentProgress=seekBar.getProgress();
        ijkVideoView.seekTo(currentProgress+2000);//保存一次播放进度因为捕获关键帧的原因，所以多增加2秒
        Log.d("ssss","seekBar拖动的进度是"+seekBar.getProgress()+"");
        ijkVideoView.getCurrentPosition();//获取当前进度
        startVideo();

    }

    /**
     * imgaeButton的点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rewind_play:
                    ijkVideoView.seekTo(ijkVideoView.getCurrentPosition()-4000);
                    startVideo();
                break;
            case R.id.start_play:
                if (ijkVideoView.isPlaying()){
                    ijkVideoView.pause();
                    start_play.setImageResource(R.drawable.play_button);
                }else {

                    start_play.setImageResource(R.drawable.pause_button);
                    startVideo();
                }

                break;
            case R.id.forward_play:
                ijkVideoView.seekTo(ijkVideoView.getCurrentPosition()+6000);
                startVideo();
                break;
        }
    }
}

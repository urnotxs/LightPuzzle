package com.xs.lightpuzzle.demo.a_mediaplayer_demo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.IOException;

/**
 * @author xs
 * @description 原生播放器控制 demo
 * @since 2019/1/11
 */

public class TestMediaPlayerLayout extends FrameLayout implements View.OnClickListener {

    private static String TAG = "TestMediaPlayerLayout";
    private String musicPath = "/storage/emulated/0/391e05bb0b0dbfa1d177d65e3165f5e2.mp3";
    private Button playMusicBtn;
    private Button pauseMusicBtn;
    private Button replayMusicBtn;
    private Button stopMusicBtn;
    private MediaPlayer mPlayer;

    public TestMediaPlayerLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(Utils.getRealPixel(20), Utils.getRealPixel(200),
                Utils.getRealPixel(20), Utils.getRealPixel(200));
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(linearLayout, fParams);
        {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            playMusicBtn = addButton(context, "播放", R.id.demo_media_player_play);
            linearLayout.addView(playMusicBtn, lParams);
            pauseMusicBtn = addButton(context, "暂停", R.id.demo_media_player_pause);
            linearLayout.addView(pauseMusicBtn, lParams);
            replayMusicBtn = addButton(context, "重播", R.id.demo_media_player_replay);
            linearLayout.addView(replayMusicBtn, lParams);
            stopMusicBtn = addButton(context, "停止", R.id.demo_media_player_stop);
            linearLayout.addView(stopMusicBtn, lParams);
        }
    }

    private Button addButton(Context context, String text, int viewId) {
        Button button = new Button(context);
        button.setBackgroundColor(0xFFBFF323);
        button.setTextColor(0xFFFFFFFF);
        button.setText(text);
        button.setId(viewId);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demo_media_player_play:
                play();
                break;
            case R.id.demo_media_player_pause:
                pause();
                break;
            case R.id.demo_media_player_replay:
                replay();
                break;
            case R.id.demo_media_player_stop:
                stop();
                break;
            default:
                break;
        }
    }

    private void play() {
        if (FileUtils.isFileExists(musicPath)) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(musicPath);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mPlayer.start();
                        playMusicBtn.setEnabled(false);
                    }
                });
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playMusicBtn.setEnabled(true);
                    }
                });
                mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {

                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "播放失败");
            }
        } else {
            Log.e(TAG, "文件不存在");
        }
    }

    private void pause() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                pauseMusicBtn.setText("继续");
            } else {
                mPlayer.start();
                pauseMusicBtn.setText("暂停");
            }
        }
    }

    private void replay() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(0);
            pauseMusicBtn.setText("暂停");
            return;
        }
        play();
    }

    private void stop(){
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            playMusicBtn.setEnabled(true);
        }
    }
    public void destroy() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

}

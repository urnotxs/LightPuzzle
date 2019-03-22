package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view;

import android.content.Context;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.widget.ContentView;
import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.widget.ShaderView;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.IOException;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class AdjustmentVideoLayout
        extends MvpFrameLayout<AdjustmentView, AdjustmentPresenter>
        implements AdjustmentView{
    private String videoPath = "/storage/emulated/0/DCIM/InterPhoto/InterPhoto_1530616618094.mp4";
    private Context mContext;
    private RelativeLayout mMainContainer;

    private int mWidth;
    private int mHeight;
    private float mViewRatio;
    private float mViewWidth;
    private float mViewHeight;
    public AdjustmentVideoLayout(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(context);
        initData();
    }

    private void initView(Context context) {
        setBackgroundColor(0xFF6F575E);
        mMainContainer = new RelativeLayout(context);
        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mMainContainer, rParams);
        {
            initAdjustmentView(context);
        }
    }
    private RelativeLayout mAdjustmentView;
    private void initAdjustmentView(Context context) {

        mAdjustmentView = new RelativeLayout(context);
        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(926));
        rParams.topMargin = Utils.getRealPixel3(166);
        mMainContainer.addView(mAdjustmentView, rParams);
    }

    @Override
    public AdjustmentPresenter createPresenter() {
        return new AdjustmentPresenter();
    }

    private void initData() {
        mWidth = Utils.getScreenW();
        mHeight = Utils.getRealPixel3(926);
        mViewRatio = (mWidth * 1.0f) / mHeight;
        initShowView();
//        getPresenter().initData();
    }

    private ContentView mContentView;
    private AdjustmentVideoView mVideoView;
    private ShaderView mShaderView;
    private ImageView mStartIv;
    @Override
    public void initShowView() {
        final MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = mediaPlayer.getVideoWidth();
        int height = mediaPlayer.getVideoHeight();
        mediaPlayer.release();
//        int width = 1920;
//        int height = 1072;
        // 获取视频比例
        float ratio = (width * 1.0f) / height;
//        if (userVideoInfo.getRotation() % 180 != 0) {
//            ratio = 1 / ratio;
//        }

        if (ratio > mViewRatio) {
            mViewWidth = mWidth;
            mViewHeight = mViewWidth / ratio;
        } else {
            mViewHeight = mHeight;
            mViewWidth = mViewHeight * ratio;
        }
        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(
                        (int) mViewWidth, (int) mViewHeight);
        rParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mVideoView = new AdjustmentVideoView(mContext);
        mVideoView.setId(R.id.adjustment_video_page_video_view);
        mVideoView.setParams(mViewWidth, mViewHeight);
//        mVideoView.setVideoInfo(userVideoInfo.getClipStartPos(), userVideoInfo.getClipEndPos(), videoPath);
        mAdjustmentView.addView(mVideoView, rParams);

        mContentView = new ContentView(mContext);
        mContentView.setParams(mViewWidth, mViewHeight, calculateClipRect());
        mContentView.setVertexPotsChangedListener(mListener);
        mAdjustmentView.addView(mContentView, rParams);

        mShaderView = new ShaderView(mContext);
        mShaderView.setParams(mViewWidth, mViewHeight, calculateClipRect());
        mAdjustmentView.addView(mShaderView, rParams);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(126), Utils.getRealPixel3(126));
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.adjustment_video_page_video_view);
        params.addRule(RelativeLayout.ALIGN_LEFT, R.id.adjustment_video_page_video_view);
        mStartIv = new ImageView(getContext());
        mAdjustmentView.addView(mStartIv, params);
        mStartIv.setImageResource(R.drawable.cutphotopage_play_selector);
        mStartIv.setVisibility(INVISIBLE);
    }

    private VertexPotsChangedListener mListener = new VertexPotsChangedListener() {

        @Override
        public void onChanged(float[] vertexPots) {
            if (mVideoView != null) {
                mVideoView.setVertexPots(vertexPots);
            }
        }

        @Override
        public void onStop() {
            if (mVideoView != null) {
                if (mVideoView.isPlaying()) {
                    mVideoView.onPaused();
                    mStartIv.setVisibility(VISIBLE);
                } else {
                    mVideoView.onResumed();
                    mStartIv.setVisibility(INVISIBLE);
                }
            }
        }

        @Override
        public void onPlay() {
            if (mVideoView != null) {
                if (!mVideoView.isPlaying()) {
                    mVideoView.onResumed();
                    mStartIv.setVisibility(INVISIBLE);
                }
            }
        }

        @Override
        public void onSave(final float[] texturePots) {
            mVideoView.setPrepared(false);
        }
    };


    public RectF calculateClipRect() {
        float clipWidth;
        float clipHeight;
        float ratio = 1;
        float videoRatio = mViewWidth / mViewHeight;

        if (ratio > videoRatio) {
            // 齐宽
            clipWidth = mViewWidth;
            clipHeight = clipWidth / ratio;
        } else {
            // 齐高
            clipHeight = mViewHeight;
            clipWidth = clipHeight * ratio;
        }
        float top = (mViewHeight - clipHeight) / 2;
        float left = (mViewWidth - clipWidth) / 2;

        return new RectF(left, top, left + clipWidth, top + clipHeight);
    }

    @Override
    public void seekToFinishPlay(int seekTime, int end) {

    }

    @Override
    public void seekToFinishNoPlay(int seekTime) {

    }

    public interface VertexPotsChangedListener {
        void onChanged(float[] vertexPots);

        void onStop();

        void onPlay();

        void onSave(float[] texturePots);
    }
}

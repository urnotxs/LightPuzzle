package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view;

import android.content.Context;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.opengl.gllayer.surfaceview.AdjustmentTextureView;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class AdjustmentVideoView extends RelativeLayout {

    private RectF mRect;
    private Context mContext;
    private AdjustmentTextureView mEditView;

    public AdjustmentVideoView(Context context) {
        super(context);
        mContext = context;
        initEditFilterSurface();
    }

    private void initEditFilterSurface() {
        mEditView = new AdjustmentTextureView(mContext);

        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(mEditView, rParams);
    }

    public void setParams(float width, float height) {
        mRect = new RectF(0, 0, width, height);
    }

//    public void setVideoInfo(int clipStartPos, int clipEndPos, String videoPath) {
//        mEditView.setVideoInfo(clipStartPos , clipEndPos , videoPath);
//    }

    public void setVertexPots(float[] vertexPots) {
//        mEditView.setVerTexPots(vertexPots);
    }

    public void onResumed() {
//        mEditView.onResumed();
    }

    public void onPaused() {
//        mEditView.onPaused();
    }

    public void onClose() {
//        mEditView.onClose();
    }

    public void setDrag(boolean drag) {
//        mEditView.setDrag(drag);
    }

    public void seekTo(int seekTime) {
//        mEditView.seekTo(seekTime);
    }

    public void seekTo(int start, int end) {
//        mEditView.seekTo(start , end);
    }

    public void setPrepared(boolean prepared) {
//        mEditView.setPrepared(prepared);
    }

    public boolean isPlaying() {
        return mEditView.isPlaying();
    }
}

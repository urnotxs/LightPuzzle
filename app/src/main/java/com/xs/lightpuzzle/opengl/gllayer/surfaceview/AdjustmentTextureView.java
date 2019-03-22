package com.xs.lightpuzzle.opengl.gllayer.surfaceview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;

import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.RendererCallBack;
import com.xs.lightpuzzle.opengl.gllayer.renderer.AdjustmentRenderer;
import com.xs.lightpuzzle.opengl.gllayer.surfaceview.base.BaseSurfaceView;

import java.io.IOException;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class AdjustmentTextureView extends BaseSurfaceView {
    private String videoPath = "/storage/emulated/0/DCIM/InterPhoto/InterPhoto_1530616618094.mp4";

    private MediaPlayer mPlayer;
    private AdjustmentRenderer mRenderer;
    private boolean isFirst = true;
    private boolean isLoop = true;
    private boolean isPrepared = true;

    public AdjustmentTextureView(Context context) {
        super(context);
        mPlayer = new MediaPlayer();
        setRenderer();
    }

    @Override
    protected void setRenderer() {
        mRenderer = new AdjustmentRenderer(mContext);
        mRenderer.setRendererCallBack(new RendererCallBack() {
            @Override
            public void onSurfaceCreated(SurfaceTexture surfaceTexture) {
                surfaceTexture.setOnFrameAvailableListener(
                        new SurfaceTexture.OnFrameAvailableListener() {
                            @Override
                            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                                // 新的数据帧有效时的回调接口
                                AdjustmentTextureView.this.requestRender();
                            }
                        }
                );
                if (isFirst){
                    isFirst = false;
                    try {
                        mPlayer.setDataSource(videoPath);
                        mPlayer.setLooping(isLoop);
                        mPlayer.setSurface(new Surface(surfaceTexture));
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                isPrepared = true;
                                mPlayer.start();
                            }
                        });
                        mPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    mPlayer.setSurface(new Surface(surfaceTexture));
                }
            }
        });
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void setEGLContextClientVersion() {
        setEGLContextClientVersion(2);
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

}

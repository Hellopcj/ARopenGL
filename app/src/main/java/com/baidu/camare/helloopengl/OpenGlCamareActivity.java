package com.baidu.camare.helloopengl;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
    Camera 相机预览
 */
public class OpenGlCamareActivity extends AppCompatActivity implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, Camera.FaceDetectionListener, Camera.PreviewCallback {

    private GLSurfaceView mGlSurfaceView;
    private SurfaceTexture mCamareView;
    private int SurfaceTextureId = -1;

    private Camera mCamare;
    private int mPreviewWidth = 1280;
    private int mPreviewHeigth = 720;
    // camareid 0 1  前置摄像头和后置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl_camare);
        mGlSurfaceView = findViewById(R.id.gl_camare_view);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(this);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //  创建纹理 id
        SurfaceTextureId = createTextureID();
        Log.i("pcj","-- onSurfaceCreated");

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.i("pcj","-- onSurfaceChanged");
        //  调用相机预览
        if (mCamareView == null) {
            mCamareView = new SurfaceTexture(SurfaceTextureId);
            //  开启相机
            opencamare(mCamareView, true);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.i("pcj","-- onDrawFrame");
    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }


    //  surfaceteture
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i("pcj","-- onFrameAvailable");

    }

    public void releaseCamera() {
        if (null != mCamare) {
            mCamare.setPreviewCallback(null);
            mCamare.stopPreview();
            mCamare.release();
            mCamare = null;
        }
    }
    private void opencamare(SurfaceTexture surfaceTexture, boolean isfront) {
        try {
            mCamare = getInstanceCamare(isfront);
            if (mCamare == null) {
                opencamare(mCamareView, isfront);
                return;
            }
            Camera.Parameters params = mCamare.getParameters();
            params.setPreviewSize(mPreviewWidth, mPreviewHeigth);
            if (!isfront) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            mCamare.setParameters(params);
            mCamare.setPreviewTexture(surfaceTexture);
            mCamare.setDisplayOrientation(90);

            mCamare.setPreviewCallback(this);
            mCamare.startPreview();
            mCamare.cancelAutoFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Camera getInstanceCamare(boolean isfront) {
        Camera c = null;
        try {
            if (isfront) {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                c = Camera.open();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        //   谷歌 人脸检测
       Log.i("pcj",faces.toString());
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        //   camare 回调
        Log.i("pcj","-- onPreviewFrame");
        if (mGlSurfaceView!= null){
            mGlSurfaceView.requestRender();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
      //  mGlSurfaceView.surfaceDestroyed();
    }



}

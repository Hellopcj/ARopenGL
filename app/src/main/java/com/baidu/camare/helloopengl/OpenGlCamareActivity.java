package com.baidu.camare.helloopengl;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
camare 预览
 */
public class OpenGlCamareActivity extends AppCompatActivity implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, Camera.FaceDetectionListener, Camera.PreviewCallback {

    private GLSurfaceView mGlSurfaceView;

    private SurfaceTexture mCamareView;
    private int SurfaceTextureId;

    private Camera mCamare;
    private int mPreviewWidth = 1280;
    private int mPreviewHeigth = 720;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl_camare);
        mGlSurfaceView = findViewById(R.id.gl_camare_view);
        mGlSurfaceView.setRenderer(this);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //  创建纹理 id
        SurfaceTextureId = createTextureID();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        //  调用相机预览
        if (mCamareView == null){
            mCamareView = new SurfaceTexture(SurfaceTextureId);
            //  开启相机
            opencamare(mCamareView,true);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

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

    }

    private void opencamare(SurfaceTexture surfaceTexture, boolean isfront) {
        try {
            mCamare = getInstanceCamare(true);
            if (mCamare == null) {
                opencamare(mCamareView, true);
                return;
            }
            Camera.Parameters parameters = mCamare.getParameters();
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeigth);
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeigth);
            if (!isfront) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCamare.setParameters(parameters);
            mCamare.setPreviewTexture(surfaceTexture);
            // 设置相机旋转角度

            mCamare.setPreviewCallback(this);
            // 谷歌自带人脸识别
            mCamare.setFaceDetectionListener(this);

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

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }
}

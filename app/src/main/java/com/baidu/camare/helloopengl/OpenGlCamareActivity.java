package com.baidu.camare.helloopengl;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.i("pcj","onSurfaceChanged");
        //  调用相机预览
        if (mCamareView == null) {
            mCamareView = new SurfaceTexture(SurfaceTextureId);
            //  开启相机
            opencamare(mCamareView, true);
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
        if (mGlSurfaceView!= null){
            mGlSurfaceView.requestRender();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAllPermissions(REQUEST_CODE_ASK_ALL_PERMISSIONS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
      //  mGlSurfaceView.surfaceDestroyed();
    }
    // 权限请求相关相关
    private static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_ASK_ALL_PERMISSIONS = 154;
    private boolean mIsDenyAllPermission = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_ALL_PERMISSIONS) {
            mIsDenyAllPermission = false;
            for (int i = 0; i < permissions.length; i++) {
                if (i >= grantResults.length || grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    mIsDenyAllPermission = true;
                    break;
                }
            }
            if (mIsDenyAllPermission) {
                finish();
            }
        }

    }

    /**
     * 请求权限
     *
     * @param requestCode
     */
    private void requestAllPermissions(int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<String> permissionsList = getRequestPermissions(this);
                if (permissionsList.size() == 0) {
                    return;
                }
                if (!mIsDenyAllPermission) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            requestCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getRequestPermissions(Activity activity) {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList;
    }

}

package com.baidu.camare.helloopengl;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.camare.helloopengl.base.CameraManager;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 相机
 */
public class CameraActivity extends Activity {
    /**
     * 需要手动申请的权限  其实现在都不需要权限的啦。 camera权限已经申请好的了
     */
    private static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_CODE_ASK_ALL_PERMISSIONS = 188;
    private GLSurfaceView glSurfaceView;
    private SurfaceTexture surfaceTexture;
    private CameraManager cameraManager;
    private int CameraId = 1;
    // 相机管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        // 6.0以下版本直接同意使用权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            cameraManager = new CameraManager();
        } else {
            if (hasNecessaryPermission()) {
                cameraManager = new CameraManager();
            } else {
                requestPermissions(ALL_PERMISSIONS, REQUEST_CODE_ASK_ALL_PERMISSIONS);
            }
        }
        initRenderData();
    }

    private void initRenderData() {
        // 设置
        glSurfaceView = findViewById(R.id.gl_camera_view);
        // 为什么是空指针啊  我的天啊 啊啊啊啊啊啊 不知道为什么是空指针
        glSurfaceView.setPreserveEGLContextOnPause(true);
        glSurfaceView.setEGLContextClientVersion(2);
        // 减少资源消耗， 设置渲染模式
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // 初始化数据
        glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                // 默认打开前置摄像头
                if (surfaceTexture == null) {
                    int textureID = createTextureID();
                    surfaceTexture = new SurfaceTexture(textureID);
                    cameraManager.setPreViewTexture(surfaceTexture);
                }
                cameraManager.openCamera(CameraId);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                if (cameraManager != null) {

                }
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                // 渲染

            }
        });

    }

        private int createTextureID(){
            int[] texture = new int[1];
            GLES20.glGenTextures(1, texture, 0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                    GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                    GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                    GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                    GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
            return texture[0];
        }


    /**
     * 请求权限
     *
     * @return
     */
    private boolean hasNecessaryPermission() {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList.size() == 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_ALL_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                // 允许权限
                cameraManager = new CameraManager();
            } else {
                // Permission Denied
                // 退出
                this.finish();
            }
        }
    }

}

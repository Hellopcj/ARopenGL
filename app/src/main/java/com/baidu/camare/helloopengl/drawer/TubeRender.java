package com.baidu.camare.helloopengl.drawer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by puchunjie .
 * 正方体的渲染render
 */

public class TubeRender implements GLSurfaceView.Renderer {
    private TubeDrawer mDrawer;

    public TubeRender() {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Log.i("pcj","onSurfaceCreated");
        if (mDrawer == null) {
            mDrawer = new TubeDrawer();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        if (mDrawer != null) {
            Log.i("pcj","onSurfaceChanged");
            mDrawer.drawchanced(width, height);
        }

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (mDrawer != null) {
            Log.i("pcj","onDrawFrame");
            mDrawer.drawFrame();
        }
    }

}

package com.baidu.camare.helloopengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.camare.helloopengl.drawer.TubeRender;

public class TubeActivity extends AppCompatActivity {
    private GLSurfaceView mView;
    private TubeRender mRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tube);
        mView = findViewById(R.id.gl_tube_view);
        mView.setEGLContextClientVersion(2);
        mRender = new TubeRender();
        mView.setRenderer(mRender);
        mView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  mView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

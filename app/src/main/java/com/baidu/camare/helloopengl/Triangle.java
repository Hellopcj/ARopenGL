package com.baidu.camare.helloopengl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Triangle extends Activity implements GLSurfaceView.Renderer {
    private GLSurfaceView mTriangleView;
    private int programe;
    private FloatBuffer vertexBuffer;
    // 顶点偏移量
    private final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {0.1f, 0.3f, 0.2f, 0f};

    //各个顶点坐标
    private float triangleCoords[] = {  // 正三角形的三个顶点坐标
            // 顺序分别为  顶点  left  right
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // left
            0.5f, -0.5f, 0.0f};// right
    // 顶点个数
    private final int vertextCount = triangleCoords.length / COORDS_PER_VERTEX;
    // 顶点着色器
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    // 片元着色器
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);
        mTriangleView = findViewById(R.id.gl_trangle_view);
        initData();
    }

    private void initData() {
        mTriangleView.setEGLContextClientVersion(2);
        mTriangleView.setRenderer(this);
        mTriangleView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /*********************************
     * 渲染
     ***********************************/
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
  /*------ Log  start------*/
        Log.i("pcj", "sufaceview oncreated");
  /*------ Log  end------*/
        //  清除屏幕颜色
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertrixshader = loadshader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentshader = loadshader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //创建一个空的程序
        programe = GLES20.glCreateProgram();

        //顶点着色器和片元着色器分别加入到程序
        GLES20.glAttachShader(programe,vertrixshader);
        GLES20.glAttachShader(programe,fragmentshader);
        // 链接程序
        GLES20.glLinkProgram(programe);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }



    private int mPositionHandle;
    private int mColorHandle;

    @Override
    public void onDrawFrame(GL10 gl10) {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(programe);

        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(programe, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(programe, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertextCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public int loadshader(int type, String shadercode) {
        //  根据type 创建不同的shader
        int shader = GLES20.glCreateShader(type);
        //  将资源加入到着色器，并编译
        GLES20.glShaderSource(shader, shadercode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

package com.baidu.camare.helloopengl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 彩色 等腰三角形
 */
public class ColorTriangleActivity extends Activity {

    private GLSurfaceView mSurfaceView;

    private FloatBuffer vertrixBuffer, colorBuffer;
    private int mProgram;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "varying  vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "  vColor=aColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    //各个顶点坐标
    private float triangleCoords[] = {  // 正三角形的三个顶点坐标
            // 顺序分别为  顶点  left  right
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // left
            0.5f, -0.5f, 0.0f};// right

    //  渐变的颜色数据
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    private final int COORDS_PER_VERTEX = 3;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节

    private int mPositionHandler;
    private int mColorHandler;
    private int mMatrixHandler;


    private float[] MVPMatrix = new float[16];
    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_triangle);
        mSurfaceView = findViewById(R.id.gl_colortriangle_view);
        initData();
    }

    private void initData() {
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            //   注意 allocate  崩溃
                GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                ByteBuffer aa = ByteBuffer.allocateDirect(triangleCoords.length * 4);
                aa.order(ByteOrder.nativeOrder());

                vertrixBuffer = aa.asFloatBuffer();
                vertrixBuffer.put(triangleCoords);
                vertrixBuffer.position(0);

                ByteBuffer bb = ByteBuffer.allocateDirect(color.length * 4);
                bb.order(ByteOrder.nativeOrder());
                colorBuffer = bb.asFloatBuffer();
                colorBuffer.put(color);
                colorBuffer.position(0);

                int vertrixshader = loadshader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
                int fragmentshader = loadshader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

                mProgram = GLES20.glCreateProgram();
                // 将shader加入到program
                GLES20.glAttachShader(mProgram, vertrixshader);
                GLES20.glAttachShader(mProgram, fragmentshader);
                GLES20.glLinkProgram(mProgram);

            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                //计算宽高比
                float ratio = (float) width / height;
                //设置透视投影
                Matrix.frustumM(projectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
                //设置相机位置
                Matrix.setLookAtM(viewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
                //计算变换矩阵
                Matrix.multiplyMM(MVPMatrix, 0, projectMatrix, 0, viewMatrix, 0);
                GLES20.glViewport(0, 0, width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                //将程序加入到OpenGLES2.0环境
                GLES20.glUseProgram(mProgram);
                //获取变换矩阵vMatrix成员句柄
                mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
                //指定vMatrix的值
                GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, MVPMatrix, 0);
                //获取顶点着色器的vPosition成员句柄
                mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
                //启用三角形顶点的句柄
                GLES20.glEnableVertexAttribArray(mPositionHandler);
                //准备三角形的坐标数据
                GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX,
                        GLES20.GL_FLOAT, false,
                        vertexStride, vertrixBuffer);
                //获取片元着色器的vColor成员的句柄
                mColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
                //设置绘制三角形的颜色
                GLES20.glEnableVertexAttribArray(mColorHandler);
                GLES20.glVertexAttribPointer(mColorHandler, 4,
                        GLES20.GL_FLOAT, false,
                        0, colorBuffer);
                //绘制三角形
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
                //禁止顶点数组的句柄
                GLES20.glDisableVertexAttribArray(mPositionHandler);
            }
        });
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private int loadshader(int type, String shadercode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shadercode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

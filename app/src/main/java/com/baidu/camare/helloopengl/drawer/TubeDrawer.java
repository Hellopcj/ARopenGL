package com.baidu.camare.helloopengl.drawer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by puchunjie .
 * 具体的渲染
 */

public class TubeDrawer {


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
    //  渐变的颜色数据
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };
    // 顶点坐标
    final float cubePositions[] = {
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,    //反面左上4
            -1.0f, -1.0f, -1.0f,   //反面左下5
            1.0f, -1.0f, -1.0f,    //反面右下6
            1.0f, 1.0f, -1.0f,     //反面右上7
    };
    final short index[] = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };


    private static final int COORDS_PER_VERTEX = 3;
    // 顶点偏移量
    private static final int vertexStride = COORDS_PER_VERTEX * 4;// 4 bytes per vertex
    // 顶点个数
    private final int vertexCount = cubePositions.length / COORDS_PER_VERTEX;

    private FloatBuffer vertexBuffer, colorBuffer;
    private ShortBuffer drawListBuffer;

    private int mProgram;
    private int colorhandle;
    private int mPositionHandle;
    private int mMatrixHandle;


    //  矩阵
    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];


    public TubeDrawer() {
        //  on serface created
        ByteBuffer aa = ByteBuffer.allocateDirect(cubePositions.length * 4);
        aa.order(ByteOrder.nativeOrder());
        vertexBuffer = aa.asFloatBuffer();
        vertexBuffer.put(cubePositions);
        vertexBuffer.position(0);

        ByteBuffer bb = ByteBuffer.allocateDirect(index.length * 2);
        bb.order(ByteOrder.nativeOrder());
        drawListBuffer = bb.asShortBuffer();
        drawListBuffer.put(index);
        drawListBuffer.position(0);

        ByteBuffer cc = ByteBuffer.allocateDirect(color.length * 4);
        cc.order(ByteOrder.nativeOrder());
        colorBuffer = cc.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        int vertrixshader = loadshader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentshader = loadshader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertrixshader);
        GLES20.glAttachShader(mProgram, fragmentshader);
        GLES20.glLinkProgram(mProgram);

    }

    // on draw frame
    public void drawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        //  获取句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        colorhandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        //  指定矩阵的值
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        // 启动顶点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, 2, color, 0);
        GLES20.glEnableVertexAttribArray(colorhandle);
        GLES20.glVertexAttribPointer(colorhandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);
        //索引法绘制正方体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void drawchanced(int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    private int loadshader(int type, String shadercode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shadercode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

package com.baidu.camare.helloopengl.base;

import android.graphics.Point;
import android.graphics.SurfaceTexture;

/**
 * 相机接口
 */
public interface ICamera {
    // 开启相机
    boolean openCamera(int cameraId);

    // 关闭相机
    boolean closeCamera();

    // 切换摄像头
    boolean changeCamera(int cameraId);

    // 拍照
    void takePhoto(TakePhotoCallBack takePhotoCallBack);

    // 设置预览
    void setPreViewTexture(SurfaceTexture texture);

    // 预览宽高
    Point getPreviewSize();

    // pic宽高
    Point getPicViewSize();

    // camera配置设置
    void setCameraConfig(Config config);

    // 相机预览数据
    void setPreViewCallBack(PreviewFrameCallback callBack);

    // 开始预览相机数据
    boolean preView();

    class Config {
        // 宽高比
        float rate;
        int mPreViewHeight;
        int mPreViewWidth;
    }


    // 预览回调
    interface PreviewFrameCallback {
        void onPreviewFrame(byte[] bytes, int width, int height);
    }
}


package com.baidu.camare.helloopengl.base;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraManager implements ICamera {
    // 系统camera
    private Camera camera;
    // camera 预览宽高 的变量
    private Camera.Size mPreSize;
    // pic宽高 的变量
    private Camera.Size mPicSize;

    // 相机数据
    private Point mPreData;
    private Point mPicData;

    // 配置
    private Config config;
    // 自定义的比较器
    private CameraSizeComparator comparator;

    public CameraManager() {
        // 设置camera宽高等信息
        // 其实这个config可以作为一个静态变量？ 可以不放在这里
        config = new Config();
        comparator = new CameraSizeComparator();
        config.mPreViewHeight = 1080;
        config.mPreViewWidth = 720;
        config.rate = 1.778f;
    }


    @Override
    public boolean openCamera(int id) {
        try {
            camera = Camera.open(id);
            // 设置camera参数
            // 其实camera的设置还是比较简单的 就是设置下参数 和预览宽高就好了。
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                if (parameters.getSupportedFocusModes().equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    // 设置自动自动对焦
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
                // 计算pic
                mPicSize = calculatePicSize(parameters.getSupportedPictureSizes(), config.rate, config.mPreViewWidth);
                // pre
                mPreSize = calculatePreSize(parameters.getSupportedPictureSizes(), config.rate, config.mPreViewWidth);

                // 设置camera parameters
                parameters.setPictureSize(mPicSize.width, mPicSize.height);
                parameters.setPreviewSize(mPreSize.width, mPreSize.height);

                camera.setParameters(parameters);
                // 设置picSize
                Camera.Size pictureSize = parameters.getPictureSize();
                mPicData = new Point(pictureSize.width, pictureSize.height);
                Camera.Size previewSize = parameters.getPreviewSize();
                mPreData = new Point(previewSize.width, previewSize.height);
                // 设置preSize
                Log.d("puchunjie", "camera preWidth: " + mPreSize.width + "  preHeight :" + mPreSize.height);
                Log.d("puchunjie", "camera picWidth: " + mPicSize.width + "  picHeight :" + mPreSize.height);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeCamera() {
        // 关闭相机
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        return false;
    }

    @Override
    public boolean changeCamera(int cameraID) {
        // 切换摄像头
        if (camera != null) {
            closeCamera();
            openCamera(cameraID);
        }
        return false;
    }

    // 计算picSize
    private Camera.Size calculatePicSize(List<Camera.Size> supportedPictureSizes, float rate, int mPreSize) {
        //   计算预览界面宽高  和宽高比
        Log.d("puchunjie", "supportedPictureSizes:" + supportedPictureSizes.toString());
        Collections.sort(supportedPictureSizes, comparator);
        int i = 0;
        for (Camera.Size size : supportedPictureSizes) {
            if (size.height >= mPreSize && equalsRate(size, rate)) {
                break;
            }
            i++;
        }
        if (i == supportedPictureSizes.size()) {
            i = 0;
        }
        return supportedPictureSizes.get(i);
    }

    /**
     * 判断Rate
     *
     * @param size
     * @param rate
     * @return
     */
    private boolean equalsRate(Camera.Size size, float rate) {
        // 比较Rate
        float r = size.width / size.height;
        if (Math.abs(r - rate) <= 0.03) {
            // 取绝对值  保证是正整数
            return true;
        } else {
            return false;
        }
    }

    // 计算preSize
    private Camera.Size calculatePreSize(List<Camera.Size> supportedPictureSizes, float rate, int mPreSize) {
        // 排序    对支持的相机的宽高  进行排序
        Collections.sort(supportedPictureSizes, comparator);
        int i = 0;
        for (Camera.Size size : supportedPictureSizes) {
            if (size.height >= mPreSize) {
                break;
            }
            i++;
        }
        if (i == supportedPictureSizes.size()) {
            i = 0;
        }
        return supportedPictureSizes.get(i);
    }

    /**
     * 比较
     */
    private class CameraSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            if (o1.height == o2.height) {
                return 0;
            } else if (o1.height > o2.height) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public void takePhoto(TakePhotoCallBack takePhotoCallBack) {

    }

    @Override
    public void setPreViewTexture(SurfaceTexture texture) {
        if (camera != null) {
            try {
                camera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存了预览宽高信息的数据
    @Override
    public Point getPreviewSize() {
        return mPreData;
    }

    // 保存了pic宽高的数据
    @Override
    public Point getPicViewSize() {
        return mPicData;
    }

    @Override
    public void setCameraConfig(Config config) {
        this.config = config;
    }

    @Override
    public void setPreViewCallBack(final PreviewFrameCallback callBack) {
        // 设置相机预览数据
        if (callBack != null) {
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // 将预览宽高传入进去
                    callBack.onPreviewFrame(data, mPreData.x, mPreData.y);
                    Log.d("puchunjie111", "相机数据==" + data);
                }
            });
        }
    }

    @Override
    public boolean preView() {
        if (camera != null) {
            camera.startPreview();
            return true;
        } else {
            return false;
        }
    }
}

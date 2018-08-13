package com.baidu.camare.helloopengl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mView;
    private List<Data> mDatas;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.lv_view);
        mDatas = new ArrayList<>();
        setData("三角形", Triangle.class);
        setData("正彩色三角形", ColorTriangleActivity.class);
        setData("正方体", TubeActivity.class);
        setData("相机预览", OpenGlCamareActivity.class);
        setData("load obj文件", LoadOBJActivity.class);
        mAdapter = new ListViewAdapter();
        mView.setAdapter(mAdapter);
        // 测试filePath
//        File file = new File("hello");
//        file.getAbsolutePath();
    }


    /*********************************
     *Data数据类
     ***********************************/
    private class Data {
        String tille;
        Class<?> mClass;
    }

    // 设置Data数据
    private void setData(String title, Class<?> mclass) {
        Data aData = new Data();
        aData.tille = title;
        aData.mClass = mclass;
        mDatas.add(aData);
    }


    /*********************************
     *ListView Adapter
     ***********************************/
    private class ListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, null);
                view.setTag(new ListViewHolder(view));
            }
            ListViewHolder holder = (ListViewHolder) view.getTag();
            holder.setData(mDatas, i);
            return view;
        }
    }

    class ListViewHolder {
        private TextView mDescribe;

        public ListViewHolder(View parent) {
            mDescribe = parent.findViewById(R.id.tv_des);
        }

        public void setData(final List<Data> mDatas, final int i) {
            mDescribe.setText(mDatas.get(i).tille);
            mDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, mDatas.get(i).mClass);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        requestAllPermissions(REQUEST_CODE_ASK_ALL_PERMISSIONS);
        super.onResume();
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

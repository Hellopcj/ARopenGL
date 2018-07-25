package com.baidu.camare.helloopengl;

import android.content.Intent;
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
        setData("正彩色三角形",ColorTriangleActivity.class);
        mAdapter = new ListViewAdapter();
        mView.setAdapter(mAdapter);
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
                    Intent intent = new Intent(MainActivity.this,mDatas.get(i).mClass);
                    startActivity(intent);
                }
            });
        }
    }

}

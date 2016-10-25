package com.myapp.mytvtest.mytvtest.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.myapp.mytvtest.mytvtest.R;
import com.myapp.mytvtest.mytvtest.activity.DetailActivity;
import com.myapp.mytvtest.mytvtest.adapter.GridViewAdapter;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.GridViewTV;
import com.open.androidtvwidget.view.MainUpView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/20.
 */

public class DomesticFragment extends Fragment {
    private GridViewTV gridViewTV;
    private MainUpView mainUpView;
    private View mOldView;

    private GridViewAdapter mAdapter;
    private ArrayList<String> titleList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_domestic,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridViewTV= (GridViewTV) view.findViewById(R.id.gridView_domestic);
        mainUpView= (MainUpView) view.findViewById(R.id.mainUpView);
        initData();
        mAdapter=new GridViewAdapter(getContext(),titleList);
        gridViewTV.setAdapter(mAdapter);
        mainUpView.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridget= (EffectNoDrawBridge) mainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(200);//设置动画时间

        mainUpView.setUpRectResource(R.drawable.white_light_10);//设置边框图片
        mainUpView.setDrawUpRectPadding(new Rect(26,26,26,-55));//设置边框间距

        gridViewTV.setSelector(new ColorDrawable(Color.TRANSPARENT));

        //GridView item选中状态的监听，包括边框和放大
        gridViewTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
               if (view!=null){
                   view.bringToFront();
                   mainUpView.setFocusView(view,mOldView,1.1f);//选中的当前view设置边框及放大效果

               }
                mOldView=view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //GridView item点击的监听
        gridViewTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(),"点击了"+(position+1)+"个item",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });

        //载入布局后，默认设置第一个item获取焦点
        gridViewTV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                //默认选中第一个item
                if (gridViewTV.getChildCount()>0){
                    gridViewTV.setSelection(0);
                    View  newView=gridViewTV.getChildAt(0);
                    newView.bringToFront();
                    mainUpView.setFocusView(newView,1.1f);
                    mOldView=gridViewTV.getChildAt(0);
                }
            }
        });
    }

    private void initData() {
        for (int i=0;i<40;i++){
            titleList.add("电影"+(i+1));
        }
    }
}

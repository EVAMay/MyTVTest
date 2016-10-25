package com.myapp.mytvtest.mytvtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.mytvtest.mytvtest.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/24.
 */

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> titleList;

    public GridViewAdapter(Context context, ArrayList<String> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        return titleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        myViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new myViewHolder();
            //绑定布局
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_item,viewGroup,false);
            viewHolder.imageView_gridView_item= (ImageView) convertView.findViewById(R.id.image_gridView_item);
            viewHolder.title_gridView_item= (TextView) convertView.findViewById(R.id.title_gridView_item);
            viewHolder.summary_gridView_item= (TextView) convertView.findViewById(R.id.summary_gridView_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (myViewHolder) convertView.getTag();
            //设置title和image
            viewHolder.title_gridView_item.setText(titleList.get(position));
        }
        return convertView;
    }
    class myViewHolder {
        ImageView imageView_gridView_item;
        TextView title_gridView_item,summary_gridView_item;
    }
}

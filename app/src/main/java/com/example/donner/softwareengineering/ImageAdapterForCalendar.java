package com.example.donner.softwareengineering;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * Created by Donner on 2015-04-09.
 */
public class ImageAdapterForCalendar extends BaseAdapter {

    private Context mContext;

    public ImageAdapterForCalendar(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(57,57));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


    private Integer[] mThumbIds = {
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2,
            R.drawable.gridfill2, R.drawable.gridfill2
    };
}

package kr.co.dogisangel.android.dogisangel.Pro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-08.
 */
public class PromoteDetailAdapter extends PagerAdapter {
    ArrayList<String> items = new ArrayList<String>();

    ProPromoteDetailActivity proPromoteDetailActivity;
    LayoutInflater inflater;
    Context context;

    public PromoteDetailAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        proPromoteDetailActivity = (ProPromoteDetailActivity) context;

    }

    public void setImageResources(ArrayList<String> images) {
        for (int i = 0; i < images.size(); i++) {
            items.add(images.get(i));
        }
        notifyDataSetChanged();
    }

    public void add(String image) {
        items.add(image);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();

    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view;
        ImageView imageView;

        if (inflater != null) {
            view = inflater.inflate(R.layout.detail_main_img, container, false);
            imageView = (ImageView) view.findViewById(R.id.pro_detail_image_view);
            Glide.with(proPromoteDetailActivity).load(items.get(position)).into(imageView);

        } else {
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.detail_main_img, container, false);
        }


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

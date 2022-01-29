package kr.co.dogisangel.android.dogisangel.MainRanking;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.MainActivity;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-04.
 */
public class RankingAdapter extends PagerAdapter {
    ArrayList<String> items = new ArrayList<String>();

    LayoutInflater inflater;
    Context context;
    MainActivity mainActivity;
    public RankingAdapter(Context context){
        inflater = LayoutInflater.from(context);
       mainActivity = (MainActivity)context;
    }


    public void setImageResources(ArrayList<String> images) {
        for (int i = 0; i < images.size(); i++) {
            items.add(images.get(i));
        }
        notifyDataSetChanged();
    }

    public void add(String item) {
        items.add(item);
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
            view=inflater.inflate(R.layout.viewpager_ranking, container, false);
            imageView = (ImageView)view.findViewById(R.id.main_ImageView);

            Glide.with(mainActivity).load(items.get(position)).into(imageView);

        }else{
            inflater = LayoutInflater.from(context);
            view = (TextView)inflater.inflate(R.layout.viewpager_ranking, container, false);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

}

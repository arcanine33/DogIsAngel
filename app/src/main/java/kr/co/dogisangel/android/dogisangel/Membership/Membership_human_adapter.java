package kr.co.dogisangel.android.dogisangel.Membership;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-13.
 */
public class Membership_human_adapter extends BaseAdapter {
    private Context context;
    ArrayList<Membership_human_add> membership_list;
    private LayoutInflater inflater;

    public Membership_human_adapter(Context context, ArrayList<Membership_human_add> membership_list) {
        this.context = context;
        this.membership_list = membership_list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return membership_list.size();
    }

    @Override
    public Object getItem(int position) {
        return membership_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.membership_human_add, parent, false);
        } else {
            view = convertView;
        }
        return view;
    }
}


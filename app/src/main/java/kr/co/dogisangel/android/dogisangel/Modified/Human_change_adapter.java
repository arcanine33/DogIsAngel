package kr.co.dogisangel.android.dogisangel.Modified;

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
public class Human_change_adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Human_change_add> list;
    private LayoutInflater inflater;

    public Human_change_adapter(Context context, ArrayList<Human_change_add> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.human_change_add, parent, false);
        } else {
            view = convertView;
        }
/*

        EditText human_add_edit = (EditText) view.findViewById(R.id.human_add_edit);
        RadioButton human_add_man = (RadioButton) view.findViewById(R.id.human_add_man);
        RadioButton human_add_woman = (RadioButton) view.findViewById(R.id.human_add_woman);
        Spinner human_add_age = (Spinner) view.findViewById(R.id.human_add_age);
        Spinner human_add_type = (Spinner) view.findViewById(R.id.human_add_type);
*/



        return view;
    }
}

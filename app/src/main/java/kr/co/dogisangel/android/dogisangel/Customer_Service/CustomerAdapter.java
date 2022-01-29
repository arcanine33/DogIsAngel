package kr.co.dogisangel.android.dogisangel.Customer_Service;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import kr.co.dogisangel.android.dogisangel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidstudio on 2016-08-07.
 */
public class CustomerAdapter extends BaseExpandableListAdapter {
    List<kr.co.dogisangel.android.dogisangel.Customer_Service.GroupItem> items = new ArrayList<kr.co.dogisangel.android.dogisangel.Customer_Service.GroupItem>();

    public void put(String groupName, String childName) {
        kr.co.dogisangel.android.dogisangel.Customer_Service.GroupItem match = null;
        for (kr.co.dogisangel.android.dogisangel.Customer_Service.GroupItem g : items) {
            if (g.groupName.equals(groupName)) {
                match = g;
                break;
            }
        }
        if (match == null) {
            match = new kr.co.dogisangel.android.dogisangel.Customer_Service.GroupItem();
            match.groupName = groupName;
            items.add(match);
        }

        if (!TextUtils.isEmpty(childName)) {
            kr.co.dogisangel.android.dogisangel.Customer_Service.ChildItem child = new kr.co.dogisangel.android.dogisangel.Customer_Service.ChildItem();
            child.childName = childName;
            match.children.add(child);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((long)groupPosition)<<32|0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((long)groupPosition)<<32|childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        kr.co.dogisangel.android.dogisangel.Customer_Service.GroupView view;
        if (convertView == null) {
            view = new kr.co.dogisangel.android.dogisangel.Customer_Service.GroupView(parent.getContext());
        } else {
            view = (kr.co.dogisangel.android.dogisangel.Customer_Service.GroupView) convertView;
        }
        view.setGroupItem(items.get(groupPosition));
        if (groupPosition == 0) {
            view.iconimage.setImageResource(R.drawable.servicecenter1_q);
        } else if(groupPosition == 1){
            view.iconimage.setImageResource(R.drawable.servicecenter1_q);
        }else if(groupPosition == 2){
            view.iconimage.setImageResource(R.drawable.servicecenter1_q);
        }
        else if(groupPosition == 3){
            view.iconimage.setImageResource(R.drawable.servicecenter1_q);
        }
        else if(groupPosition == 4){
            view.iconimage.setImageResource(R.drawable.servicecenter1_q);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        kr.co.dogisangel.android.dogisangel.Customer_Service.ChildView view;
        if (convertView == null) {
            view = new kr.co.dogisangel.android.dogisangel.Customer_Service.ChildView(parent.getContext());
        } else {
            view = (kr.co.dogisangel.android.dogisangel.Customer_Service.ChildView)convertView;
        }
        view.setChildItem(items.get(groupPosition).children.get(childPosition));

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
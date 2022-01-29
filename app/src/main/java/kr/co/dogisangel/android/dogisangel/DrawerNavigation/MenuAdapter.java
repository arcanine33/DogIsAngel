package kr.co.dogisangel.android.dogisangel.DrawerNavigation;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;


import java.util.ArrayList;
import java.util.List;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by dongja94 on 2016-01-18.
 */
public class MenuAdapter extends BaseExpandableListAdapter {
    List<GroupItem> items = new ArrayList<GroupItem>();


    public void put(String groupName, String childName) {
        GroupItem match = null;
        for (GroupItem g : items) {
            if (g.groupName.equals(groupName)) {
                match = g;
                break;
            }
        }
        if (match == null) {
            match = new GroupItem();
            match.groupName = groupName;
            items.add(match);
        }

        if (!TextUtils.isEmpty(childName)) {
            ChildItem child = new ChildItem();
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
        GroupView view;


        if (convertView == null) {
            view = new GroupView(parent.getContext());

        } else {
            view = (GroupView)convertView;
        }


        view.setGroupItem(items.get(groupPosition));
        if (groupPosition == 0) {
            view.iconimage.setImageResource(R.drawable.menu_pro_search);
        } else if(groupPosition == 1){
            view.iconimage.setImageResource(R.drawable.menu_gomin);
        }else if(groupPosition == 2){
            view.iconimage.setImageResource(R.drawable.menu_mypage);
        }else if(groupPosition == 3){
            view.iconimage.setImageResource(R.drawable.ranking_icon);
        }else if(groupPosition == 4){
            view.iconimage.setImageResource(R.drawable.menu_dogtalk);
        }else{
            view.iconimage.setImageResource(R.drawable.menu_plus);
        }

       /* if (getChildrenCount(groupPosition) == 0) {
            groupPosition.setGroupIndicator(null);

        }else{

        }*/

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildView view;
        if (convertView == null) {
            view = new ChildView(parent.getContext());
        } else {
            view = (ChildView)convertView;
        }
        view.setChildItem(items.get(groupPosition).children.get(childPosition));


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

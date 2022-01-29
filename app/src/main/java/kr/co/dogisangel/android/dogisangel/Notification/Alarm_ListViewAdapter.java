package kr.co.dogisangel.android.dogisangel.Notification;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.R;

public class Alarm_ListViewAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_UPDATE = 0 ;
    private static final int ITEM_VIEW_TYPE_TALK = 1 ;
    private static final int ITEM_VIEW_TYPE_REPLY = 2 ;
    private static final int ITEM_VIEW_TYPE_RANK= 3 ;
    private static final int ITEM_VIEW_TYPE_MAX = 4 ;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    public Alarm_ListViewAdapter() {

    }
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX ;
    }

    // position 위치의 아이템 타입 리턴.
    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            ListViewItem listViewItem = listViewItemList.get(position);

            switch (viewType) {
                case ITEM_VIEW_TYPE_UPDATE:
                    convertView = inflater.inflate(R.layout.alarm_update, parent, false);
                    TextView Alarm_text = (TextView) convertView.findViewById(R.id.Alarm_text) ;
                    ImageView Alarm_notic = (ImageView) convertView.findViewById(R.id.Alarm_noticimg) ;
                    ImageView Alarm_new = (ImageView) convertView.findViewById(R.id.Alarm_newimg) ;

                    Alarm_text.setText(listViewItem.getTitleStr());
                    Alarm_notic.setImageDrawable(listViewItem.getIconDrawable());
                    Alarm_new.setImageDrawable(listViewItem.getIconDrawable2());
                    break;

                case ITEM_VIEW_TYPE_TALK :
                    convertView = inflater.inflate(R.layout.alarm_talk,
                            parent, false);

                    TextView alarm_talk_text = (TextView) convertView.findViewById(R.id.alarm_talk_text) ;
                    ImageView alarm_talk_new = (ImageView) convertView.findViewById(R.id.alarm_talk_newimg) ;

                    alarm_talk_text.setText(listViewItem.getTalktext());
                    alarm_talk_new.setImageDrawable(listViewItem.getTalkicon());
                    break;


                case ITEM_VIEW_TYPE_REPLY :
                    convertView = inflater.inflate(R.layout.alarm_reply, parent, false);

                    TextView alatm_reply_text = (TextView) convertView.findViewById(R.id.alatm_reply_text) ;
                    ImageView alarm_reply_new = (ImageView) convertView.findViewById(R.id.alarm_reply_newimg) ;

                    alatm_reply_text.setText(listViewItem.getReplytext());
                    alarm_reply_new.setImageDrawable(listViewItem.getReplyicon());
                    break;


                case  ITEM_VIEW_TYPE_RANK :
                    convertView = inflater.inflate(R.layout.alarm_rank, parent, false);

                    ImageView alarm_rank_img = (ImageView) convertView.findViewById(R.id.alarm_rank_img) ;
                    TextView alarm_rank_text = (TextView) convertView.findViewById(R.id.alarm_rank_text) ;

                    alarm_rank_img.setImageDrawable(listViewItem.getRankicon());
                    alarm_rank_text.setText(listViewItem.getRanktext());
                    break;
            }
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 첫 번째 아이템 추가를 위한 함수.
    public void addTitleItem(String title, Drawable icon, Drawable icon2) {
        ListViewItem item = new ListViewItem() ;

        item.setType(ITEM_VIEW_TYPE_UPDATE) ;
        item.setTitleStr(title); ;
        item.setIconDrawable(icon); ;
        item.setIconDrawable2(icon2); ;

        listViewItemList.add(item) ;
    }

    public void addItem(String talktext, Drawable Talkicon, int type) {
            ListViewItem item = new ListViewItem();
        if(type==1) {

            item.setType(ITEM_VIEW_TYPE_TALK);
            item.setTalktext(talktext);
            item.setTalkicon(Talkicon);
        }else if(type==2){

            item.setType(ITEM_VIEW_TYPE_REPLY) ;
            item.setReplytext(talktext);
            item.setReplyicon(Talkicon);
        }else if(type==3){

            item.setType(ITEM_VIEW_TYPE_RANK) ;
            item.setRanktext(talktext); ;
            item.setRankicon(Talkicon); ;
        }
        listViewItemList.add(item);
    }


    /*
    // 두 번째 아이템 추가를 위한 함수.
    public void addItem(String talktext, Drawable Talkicon, String talk) {
        ListViewItem item = new ListViewItem() ;

        item.setType(ITEM_VIEW_TYPE_TALK) ;
        item.setTalktext(talktext); ;
        item.setTalkicon(Talkicon); ;

        listViewItemList.add(item);
    }
    // 세 번째 아이템 추가를 위한 함수.
    public void addItem(String replytext, Drawable replyicon) {
        ListViewItem item = new ListViewItem() ;

        item.setType(ITEM_VIEW_TYPE_REPLY) ;
        item.setReplytext(replytext);
        item.setReplyicon(replyicon);

        listViewItemList.add(item);
    }


    // 네 번째 아이템 추가를 위한 함수.

    public void addItem(String title, Drawable icon) {
        ListViewItem item = new ListViewItem() ;

        item.setType(ITEM_VIEW_TYPE_RANK) ;
        item.setTitle(title) ;
        item.setIcon(icon) ;

        listViewItemList.add(item);
    }
    */
}
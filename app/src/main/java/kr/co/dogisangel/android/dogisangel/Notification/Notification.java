package kr.co.dogisangel.android.dogisangel.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import kr.co.dogisangel.android.dogisangel.Chatting.Chatting_main;
import kr.co.dogisangel.android.dogisangel.Notify.Notify;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-08.
 */
public class Notification extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.alarm_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);


        ListView listview;
        Alarm_ListViewAdapter Alarm_ListViewAdapter;

        // Adapter 생성
        Alarm_ListViewAdapter = new Alarm_ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.alarm_list);

        // addTitleItem(String title, Drawable icon,Drawable icon2
        Alarm_ListViewAdapter.addTitleItem("새로운 공지사항이 있습니다.", ContextCompat.getDrawable(this, R.drawable.notice_notice), ContextCompat.getDrawable(this, R.drawable.notice_new));

        // 두 번째 아이템 추가.addItem(String talktext, Drawable Talkicon, int type)
        Alarm_ListViewAdapter.addItem("새로운 메세지가 도착했습니다.", ContextCompat.getDrawable(this, R.drawable.notice_new), 1);

        // 세 번째 아이템 추가.
        Alarm_ListViewAdapter.addItem("새로운 댓글이 달렸습니다.", ContextCompat.getDrawable(this, R.drawable.notice_new), 2);

        // 네 번째 아이템 추가
        Alarm_ListViewAdapter.addItem("등업을 하셨습니다.", ContextCompat.getDrawable(this, R.drawable.jeonmoomin_articles_star1), 3);



        listview.setAdapter(Alarm_ListViewAdapter);
        //listview.setOnItemClickListener(itemClickListener);
    }
/*
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {
            Toast.makeText(AlarmActivity.this, "이거 눌림", Toast.LENGTH_SHORT).show();

        }
    };
    */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickRank(View view) {
        Toast.makeText(Notification.this, "등업!", Toast.LENGTH_SHORT).show();
    }

    public void clickReply(View view) {

        Toast.makeText(Notification.this, "메시지!", Toast.LENGTH_SHORT).show();
    }
    public void clickupdate(View view) {

        Intent intent=new Intent(Notification.this,Notify.class);
        startActivity(intent);
    }
    public void clicktalk(View view) {

        Intent intent=new Intent(Notification.this,Chatting_main.class);
        startActivity(intent);
    }

}


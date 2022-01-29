package kr.co.dogisangel.android.dogisangel.Notify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import kr.co.dogisangel.android.dogisangel.R;


public class Notify extends AppCompatActivity {

    ExpandableListView listView;
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);


        listView = (ExpandableListView)findViewById(R.id.notify_expandable);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.notify_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);




        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });



        initData();

    }

    private void initData() {
        mAdapter.put("개가천사 1.0ver 업데이트", "드디어 반려동물 고민상담 및 훈련인 찾기 어플, 개가천사가 오픈했습니다!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

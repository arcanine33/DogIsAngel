package kr.co.dogisangel.android.dogisangel.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import kr.co.dogisangel.android.dogisangel.R;


public class SearchProActivity extends AppCompatActivity implements SearchDialogFragment.OnSelectLocationListener  {


    private String keyword;
    private String expertObj,Search_seleted,ID1,ID2,ID3,ID4,ID5,ID6,ID7,region;

    static ImageView nosearch;
    static FrameLayout search_frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       search_frame = (FrameLayout)findViewById(R.id.search_frame);


        Intent intent = getIntent();
        keyword=intent.getStringExtra("keyword");
        expertObj=intent.getStringExtra("0");
        Search_seleted= intent.getStringExtra("train_keyword");
        region =intent.getStringExtra("region");


        ID1 = intent.getStringExtra("join_id");
        ID2 = intent.getStringExtra("transitionName");

        ID3 = intent.getStringExtra("join_post_title");
        ID4 = intent.getStringExtra("join_dog_size");
        ID5 = intent.getStringExtra("join_my_name");
        ID6 = intent.getStringExtra("join_my_region");
        ID7 = intent.getStringExtra("join_price");

        ID3 = intent.getStringExtra("post_title");
        ID4 = intent.getStringExtra("dog_size");
        ID5 = intent.getStringExtra("my_name");
        ID6 = intent.getStringExtra("my_region");
        ID7 = intent.getStringExtra("price");

        nosearch = (ImageView)findViewById(R.id.nosearch);


        Search_ProFragment searchfragment=new Search_ProFragment();
        searchfragment.keyword=intent.getStringExtra("keyword");
        searchfragment.region=intent.getStringExtra("region");

        searchfragment.train_keyword=intent.getStringExtra("train_keyword");

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.search_frame, searchfragment);
        transaction.commit();

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

    @Override
    public void onProSelectLocation(String location) {

    }

    @Override
    public void onHumanSelectLocation(String location) {

    }
}

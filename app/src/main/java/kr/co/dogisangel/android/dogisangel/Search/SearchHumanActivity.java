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


public class SearchHumanActivity extends AppCompatActivity implements SearchDialogFragment.OnSelectLocationListener  {

    private String keyword;
    private String humanObj,Search_seleted,ID1,ID2,ID3,ID4,ID5,region;

    static ImageView human_nosearch;
    static FrameLayout human_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_human);

        Intent intent = getIntent();
        keyword=intent.getStringExtra("keyword");
        humanObj=intent.getStringExtra("1");
        Search_seleted= intent.getStringExtra("train_keyword");
        region =intent.getStringExtra("region");
//

        ID1 = intent.getStringExtra("gomin_id");
        ID2 = intent.getStringExtra("transitionName");
        ID3 = intent.getStringExtra("gomin_region");
        ID4 = intent.getStringExtra("gomin_nickname");
        ID5 = intent.getStringExtra("gomin_main_title");

        human_nosearch = (ImageView)findViewById(R.id.human_nosearch);




        final Toolbar toolbar = (Toolbar) findViewById(R.id.h_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       human_frame = (FrameLayout) findViewById(R.id.search_human_frame);



        Search_HumanFragment searchhumanfragment=new Search_HumanFragment();
        searchhumanfragment.keyword=intent.getStringExtra("keyword");
        searchhumanfragment.train_keyword=intent.getStringExtra("train_keyword");
        searchhumanfragment.region=intent.getStringExtra("region");

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.search_human_frame,searchhumanfragment);
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

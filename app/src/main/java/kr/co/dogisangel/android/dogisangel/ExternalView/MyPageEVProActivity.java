package kr.co.dogisangel.android.dogisangel.ExternalView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.Chatting.ChattingActivity;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.ProfileObject;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

public class MyPageEVProActivity extends AppCompatActivity {

    String member_id;
    static CircleImageView ev_profile_img;
    static ImageView ev_grade_img;
    static TextView ev_user_name;
    static TextView ev_license_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_ev_pro_default);

        Intent intent = getIntent();
        member_id = intent.getStringExtra("member_id");

        ev_profile_img = (CircleImageView)findViewById(R.id.ev_profile_img);
        ev_grade_img = (ImageView)findViewById(R.id.ev_grade_img);
        ev_user_name = (TextView)findViewById(R.id.ev_user_name);
        ev_license_text = (TextView)findViewById(R.id.ev_license_text);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.ev_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        ViewPager ev_profile_viewpager = (ViewPager) findViewById(R.id.ev_profile_viewpager);
        if (ev_profile_viewpager != null) {
            setupViewPager(ev_profile_viewpager);
        }

        final TabLayout ev_tabLayout = (TabLayout) findViewById(R.id.ev_profile_tab);
        ev_tabLayout.setupWithViewPager(ev_profile_viewpager);


        ev_tabLayout.addTab(ev_tabLayout.newTab().setCustomView(R.layout.ev_tab1_click), 0,true);
        ev_tabLayout.addTab(ev_tabLayout.newTab().setCustomView(R.layout.ev_tab2_unclick),1);

        final ImageView firstImageTab = (ImageView) ev_tabLayout.findViewById(R.id.ev_profile_tab1_click);
        final ImageView secondImageTab = (ImageView) ev_tabLayout.findViewById(R.id.ev_profile_tab2_unclick);


        ev_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                if( tabPosition == 0){
                    firstImageTab.setImageResource(R.drawable.profile1_click);
                    secondImageTab.setImageResource(R.drawable.profile2_unclick);
                }else{
                    firstImageTab.setImageResource(R.drawable.profile1_unclick);
                    secondImageTab.setImageResource(R.drawable.profile2_click);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        new EVActivityAsyncTask().execute();
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

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        MyPageEVProProfile fragment1 = MyPageEVProProfile.newInstance(0);
        fragment1.myPageEVProActivity = this;
        pageAdapter.appendFragment1(fragment1);


        MyPageEVProPost fragment2 = MyPageEVProPost.newInstance(1);
        fragment2.myPageEVProActivity = this;
        pageAdapter.appendFragment2(fragment2);


        viewPager.setAdapter(pageAdapter);
    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
        //private final ArrayList<String> tabTitles = new ArrayList<String>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment1(MyPageEVProProfile fragment) {
            fragments.add(fragment);
           // tabTitles.add(title);
        }

        public void appendFragment2(MyPageEVProPost fragment) {
            fragments.add(fragment);
            //tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }



    public class EVActivityAsyncTask extends AsyncTask<String, Integer, ProfileObject> {
//        NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());


        @Override
        protected ProfileObject doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;
                Request request = new Request.Builder().url(FULL_URL + "otherProfile/" + member_id).build();
                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getProfileObject(new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //networkDialog.show();
        }

        @Override
        protected void onPostExecute(ProfileObject result) {
            //networkDialog.dismiss();

            MyPageEVProActivity.ev_user_name.setText(result.user_name);
            MyPageEVProActivity.ev_license_text.setText(result.license_text);

            Glide.with(MyPageEVProActivity.this).load(result.profile_img).into(ev_profile_img);
            Glide.with(MyPageEVProActivity.this).load(result.grade_img).into(ev_grade_img);

        }
    }
}
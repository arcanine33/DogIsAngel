package kr.co.dogisangel.android.dogisangel;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.Chatting.Chatting_main;
import kr.co.dogisangel.android.dogisangel.Customer_Service.Customer_Service;
import kr.co.dogisangel.android.dogisangel.DogGomin.GominFragment;
import kr.co.dogisangel.android.dogisangel.DrawerNavigation.MenuAdapter;

import kr.co.dogisangel.android.dogisangel.DrawerNavigation.MenuObject;
import kr.co.dogisangel.android.dogisangel.FloatingButton.HumanWriting_Activity;
import kr.co.dogisangel.android.dogisangel.FloatingButton.ProWriting;
import kr.co.dogisangel.android.dogisangel.MainRanking.RankingAdapter;
import kr.co.dogisangel.android.dogisangel.MyPage.MyLikeActivity;
import kr.co.dogisangel.android.dogisangel.MyPage.MyPostActivity;
import kr.co.dogisangel.android.dogisangel.MyPage.ProfileActivity;
import kr.co.dogisangel.android.dogisangel.Notification.Notification;
import kr.co.dogisangel.android.dogisangel.Notify.Notify;
import kr.co.dogisangel.android.dogisangel.Pro.ProFragment;
import kr.co.dogisangel.android.dogisangel.Ranking.ranking_activity;
import kr.co.dogisangel.android.dogisangel.Search.SearchDialogFragment;
import kr.co.dogisangel.android.dogisangel.Setting.Setting;
import kr.co.dogisangel.android.dogisangel.VersionInformation.Version_Information;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;


public class MainActivity extends AppCompatActivity implements SearchDialogFragment.OnSelectLocationListener {
    RankingAdapter mAdapter;
    ExpandableListView expandableListView;
    MenuAdapter menuAdapter;
    RankingAdapter rankingAdapter;
    AppCompatDelegate mDelegate;
    private DrawerLayout mDrawerLayout;


    static CircleImageView profile_img;
    static CustomTextView user_name;

    private long backPressedTime=0;
    private final long FINSH_INTERVAL_TIME=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            getLayoutInflater().setFactory(this);
        } else {
            getLayoutInflater().setFactory2(this);
        }
        mDelegate = getDelegate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mtoolbar);

         final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }



        final TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_click_2), 0,true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_unclick_1),1);

        final ImageView firstImageTab = (ImageView) tabLayout.findViewById(R.id.expert_tab);
        final ImageView secondImageTab = (ImageView) tabLayout.findViewById(R.id.human_tab_2);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                if( tabPosition == 0){
                    firstImageTab.setImageResource(R.drawable.main_tab_click2);
                    secondImageTab.setImageResource(R.drawable.main_tab_unclick1);
                }else{
                    secondImageTab.setImageResource(R.drawable.main_tab_click1);
                    firstImageTab.setImageResource(R.drawable.main_tab_unclick2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewPager ranking = (ViewPager) findViewById(R.id.ranking);
        mAdapter = new RankingAdapter(this);
        ranking.setAdapter(mAdapter);


        initData();


        ImageButton confirmBtn = (ImageButton) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //검색 버튼 눌렀을 때
                getSupportFragmentManager().beginTransaction().
                        add(SearchDialogFragment.newInstance(), "search-").commit();
            }
        });


        /*ImageButton notification_Btn = (ImageButton) findViewById(R.id.notification_Btn);
        notification_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });*/


        final ActionBar ab = getSupportActionBar();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (MySingleton.sharedInstance().qualify.equals("게스트")) {
          //  mDrawerLayout.setVisibility(View.GONE);

        } else {
            ab.setHomeAsUpIndicator(R.drawable.menu);
            ab.setDisplayHomeAsUpEnabled(true);

        }


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert viewPager != null;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 1) {

                    if (MySingleton.sharedInstance().qualify.equals("일반인")) {
                        fab.setVisibility(View.VISIBLE);

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent;
                                intent = new Intent(MainActivity.this, HumanWriting_Activity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        fab.setVisibility(View.GONE);
                    }

                } else {
                    if (MySingleton.sharedInstance().qualify.equals("전문인")) {

                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent;
                                intent = new Intent(MainActivity.this, ProWriting.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        fab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            fab.setVisibility(View.GONE);
           // mDrawerLayout.setVisibility(View.GONE);
        }


        if(viewPager.getCurrentItem() == 0 ){
            if(MySingleton.sharedInstance().qualify.equals("일반인")){
                fab.setVisibility(View.GONE);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    intent = new Intent(MainActivity.this, ProWriting.class);
                    startActivity(intent);
                }
            });
        }

        profile_img = (CircleImageView)findViewById(R.id.header_image);
        user_name = (CustomTextView)findViewById(R.id.header_user_name);


        expandableListView = (ExpandableListView) findViewById(R.id.menu_expand);

        menuAdapter = new MenuAdapter();
        expandableListView.setAdapter(menuAdapter);
        expandableListView.setGroupIndicator(null);


        MenuPut();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {


                Intent intent;
                switch (childPosition) {
                    case 0:
                        intent = new Intent(MainActivity.this, Notify.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, Version_Information.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, Customer_Service.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, Setting.class);
                        startActivity(intent);

                        mDrawerLayout.closeDrawers();
                        break;
                }

                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Intent intent;
                if(MySingleton.sharedInstance().qualify.equals("전문인")){
                    switch (groupPosition) {
                        case 0://내 프로필
                            intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;
                        case 1://내글
                            intent = new Intent(MainActivity.this, MyPostActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;
                        case 2://내찜
                            intent = new Intent(MainActivity.this, MyLikeActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 3://내등급
                            intent = new Intent(MainActivity.this, ranking_activity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;

                    }
                }
                else{
                    switch (groupPosition) {
                        case 0://내 프로필
                            intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;
                        case 1://내글
                            intent = new Intent(MainActivity.this, MyPostActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;
                        case 2://내찜
                            intent = new Intent(MainActivity.this, MyLikeActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            break;

                    }
                    /*case 4://개톡
                        intent = new Intent(MainActivity.this, Chatting_main.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;*/
                }
                return false;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Transition exitTrans = new Explode();

            Transition reenterTrans = new Explode();

            window.setExitTransition(exitTrans);
            window.setExitTransition(reenterTrans);
            window.setAllowEnterTransitionOverlap(true);
            window.setAllowReturnTransitionOverlap(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!MySingleton.sharedInstance().qualify.equals("게스트")){
            new HeaderAsyncTask().execute();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.appendFragment(ProFragment.newInstance(0));
        pageAdapter.appendFragment(GominFragment.newInstance(1));

        viewPager.setAdapter(pageAdapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mAdapter.add("item " + i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void MenuPut() {

        if(MySingleton.sharedInstance().qualify.equals("전문인")){
            menuAdapter.put("내 프로필", null);
            menuAdapter.put("내 글", null);
            menuAdapter.put("내 찜", null);
            menuAdapter.put("내 등급 보기", null);
            menuAdapter.put("더보기", "공지사항");
            menuAdapter.put("더보기", "버전정보");
            menuAdapter.put("더보기", "고객센터");
            menuAdapter.put("더보기", "설정");

        }else{
            menuAdapter.put("내 프로필", null);
            menuAdapter.put("내글", null);
            menuAdapter.put("내찜", null);
            menuAdapter.put("더보기", "공지사항");
            menuAdapter.put("더보기", "버전정보");
            menuAdapter.put("더보기", "고객센터");
            menuAdapter.put("더보기", "설정");
        }
    }

    @Override
    public void onProSelectLocation(String location) {

    }

    @Override
    public void onHumanSelectLocation(String location) {

    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
       // private final ArrayList<String> tabTitles = new ArrayList<String>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            fragments.add(fragment);
           // tabTitles.add(title);
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

    public class HeaderAsyncTask extends AsyncTask<String, Integer, MenuObject> {
        ProgressDialog dialog;


        @Override
        protected MenuObject doInBackground(String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.FULL_URL + "myMenu")
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getMenuObject(
                            new StringBuilder(responseBody.string()));
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
            dialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(MenuObject result) {
            dialog.dismiss();

            if(MySingleton.sharedInstance().qualify.equals("전문인")){
                MainActivity.user_name.setText(result.user_name);
            }else{
                MainActivity.user_name.setText(result.nick_name);
            }

            Glide.with(MainActivity.this).load(result.header_img).into(profile_img);


        }
    }

    @Override
    public void onBackPressed(){
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if(0<=intervalTime && FINSH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed();
        }
        else{
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(), "'뒤로' 버튼 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

}

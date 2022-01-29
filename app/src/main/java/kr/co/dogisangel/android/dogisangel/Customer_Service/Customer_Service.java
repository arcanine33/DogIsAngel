package kr.co.dogisangel.android.dogisangel.Customer_Service;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import kr.co.dogisangel.android.dogisangel.R;
import kr.co.dogisangel.android.dogisangel.Tab.CustomTabLayout;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-07-27.
 */
public class Customer_Service extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_main);


        Toolbar mtoolbar = (Toolbar)findViewById(R.id.customer_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagermy);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        CustomTabLayout tabLayout = (CustomTabLayout) findViewById(R.id.tab_CS);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.appendFragment1(Question_Fragment.newInstance(0), "자주묻는 질문");
        pageAdapter.appendFragment2(Inquire_Fragment.newInstance(1), "문의.신고");
        pageAdapter.appendFragment3(guide_Fragment.newInstance(2), "이용안내");
        viewPager.setAdapter(pageAdapter);
    }


    private static class PageAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        private final ArrayList<String> tabTitles = new ArrayList<String>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment1(Question_Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }
        public void appendFragment2(Inquire_Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }
        public void appendFragment3(guide_Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
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
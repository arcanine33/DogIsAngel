package kr.co.dogisangel.android.dogisangel.Ranking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.DogGomin.GominDetailActivity;
import kr.co.dogisangel.android.dogisangel.GominCardViewObject;
import kr.co.dogisangel.android.dogisangel.MainActivity;
import kr.co.dogisangel.android.dogisangel.MainRanking.RankingAdapter;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-08.
 */
public class ranking_activity extends AppCompatActivity {
    ImageView content_img;
    ImageView comment_img;
    ImageView grade_img;

    static TextView rank;

    String content;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        grade_img = (ImageView) findViewById(R.id.Ranking_main);
        content_img = (ImageView) findViewById(R.id.ranking_post_count);
        comment_img = (ImageView) findViewById(R.id.ranking_comment_count);

        rank = (TextView) findViewById(R.id.ranking_name);


        Toolbar mtoolbar = (Toolbar) findViewById(R.id.ranking_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);


        new AsyncrankingJSONList().execute("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncrankingJSONList().execute("");
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class AsyncrankingJSONList extends AsyncTask<String, Integer, RankingObject> {
        ProgressDialog dialog;
        String targetURL = String.format(NetworkDefineConstant.RANKING);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ranking_activity.this,
                    "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected RankingObject doInBackground(
                String... params) {
            Response response = null;
            try {
                //OKHttp3사용ㄴ
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(targetURL)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                // responseBody.string();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONRankingAllList(new StringBuilder(responseBody.string()));
//                    Log.e("서버데이터",responseBody.string());
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
        protected void onPostExecute(RankingObject result) {
            dialog.dismiss();

            Glide.with(ranking_activity.this).load(result.grade).into(grade_img);
            Glide.with(ranking_activity.this).load(result.content).into(content_img);
            Glide.with(ranking_activity.this).load(result.comment).into(comment_img);


            ranking_activity.rank.setText(result.rank);



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
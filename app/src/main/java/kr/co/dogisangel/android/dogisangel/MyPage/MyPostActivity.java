package kr.co.dogisangel.android.dogisangel.MyPage;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.GominCardViewObject;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.ProCardViewObject;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

public class MyPostActivity extends AppCompatActivity {

    static RecyclerView myPostView;

    SwipeRefreshLayout refreshLayout;
    ProMyPostAdapter proMyPostAdapter;
    HumanMyPostAdapter humanMyPostAdapter;
    ArrayList<ProCardViewObject> proCardViewObjectArrayList = new ArrayList<>();
    ArrayList<GominCardViewObject> gominCardViewObjectArrayList = new ArrayList<>();

    ImageView no_my_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_pro_mypost);
        myPostView = (RecyclerView) findViewById(R.id.mypost_recycleView);
        no_my_post = (ImageView)findViewById(R.id.no_my_post);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.pro_mypost);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);



        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.mypage_pro_refreshlayout);
        refreshLayout.setColorSchemeColors(Color.YELLOW, Color.RED, Color.GREEN);

        if(MySingleton.sharedInstance().qualify.equals("전문인")){

            proMyPostAdapter = new ProMyPostAdapter(this, proCardViewObjectArrayList);
            myPostView.setLayoutManager(new LinearLayoutManager(myPostView.getContext()));
            //myPostView.setAdapter(proMyPostAdapter);
            new ProMyPostAsyncTask().execute();

        }else if(MySingleton.sharedInstance().qualify.equals("일반인")){

            humanMyPostAdapter = new HumanMyPostAdapter(this, gominCardViewObjectArrayList);
            GridLayoutManager layoutManager = new GridLayoutManager(MyPostActivity.this, 2,LinearLayoutManager.VERTICAL, false);
            myPostView.setLayoutManager(layoutManager);
            new HumanMyPostAsyncTask().execute();

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

    public class ProMyPostAsyncTask extends AsyncTask<String, Integer, ArrayList<ProCardViewObject>> {

      //  NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());


        @Override
        protected ArrayList<ProCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(FULL_URL + "expertPost/" + "0")
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONProAllList(
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
          //  networkDialog.show();

        }

        @Override
        protected void onPostExecute(ArrayList<ProCardViewObject> result) {
           // networkDialog.dismiss();
            if (result != null && result.size() > 0) {
                no_my_post.setVisibility(View.GONE);

                proMyPostAdapter.proCardViewObjects = result;
                proMyPostAdapter.notifyDataSetChanged();

                myPostView.setAdapter(proMyPostAdapter);
            }else{
                no_my_post.setVisibility(View.VISIBLE);
                myPostView.setVisibility(View.GONE);
            }
        }
    }

    public class HumanMyPostAsyncTask extends AsyncTask<String, Integer, ArrayList<GominCardViewObject>> {
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        @Override
        protected ArrayList<GominCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(FULL_URL + "gominPost/" +"0")
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONDogGominAllList(new StringBuilder(responseBody.string()));
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
          //  networkDialog.show();

        }

        @Override
        protected void onPostExecute(ArrayList<GominCardViewObject> result) {
           // networkDialog.dismiss();

            if (result != null && result.size() > 0) {
                no_my_post.setVisibility(View.GONE);
                humanMyPostAdapter.gominCardViewObjects = result;
                humanMyPostAdapter.notifyDataSetChanged();

                myPostView.setAdapter(humanMyPostAdapter);
            }else{
                no_my_post.setVisibility(View.VISIBLE);
                myPostView.setVisibility(View.GONE);

            }

        }
    }

}

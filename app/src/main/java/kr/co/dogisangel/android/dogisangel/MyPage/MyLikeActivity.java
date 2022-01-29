package kr.co.dogisangel.android.dogisangel.MyPage;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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

public class MyLikeActivity extends AppCompatActivity {
    RecyclerView mylikeview;

    SwipeRefreshLayout refreshLayout;
    ProMyLikeAdapter proMyLikeAdapter;
    ArrayList<ProCardViewObject> proCardViewObjectArrayList = new ArrayList<>();

    ImageView no_like;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_pro_mylike);

        mylikeview = (RecyclerView) findViewById(R.id.mylike_recycler_view);
        no_like = (ImageView)findViewById(R.id.no_like);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.pro_mylike);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.mypage_pro_likerefreshlayout);
        refreshLayout.setColorSchemeColors(Color.YELLOW, Color.RED, Color.GREEN);


        proMyLikeAdapter = new ProMyLikeAdapter(this, proCardViewObjectArrayList);
        mylikeview.setLayoutManager(new LinearLayoutManager(mylikeview.getContext()));
        mylikeview.setAdapter(proMyLikeAdapter);

        new MyLikeAsyncTask().execute();




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

    public class MyLikeAsyncTask extends AsyncTask<String, Integer, ArrayList<ProCardViewObject>> {
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        @Override
        protected ArrayList<ProCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(FULL_URL + "mylike")
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONProAllList(new StringBuilder(responseBody.string()));
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

                no_like.setVisibility(View.GONE);
                proMyLikeAdapter.proCardViewObjects = result;
                proMyLikeAdapter.notifyDataSetChanged();
                mylikeview.setAdapter(proMyLikeAdapter);


            }else{
                mylikeview.setVisibility(View.GONE);
                no_like.setVisibility(View.VISIBLE);

            }

        }
    }
}

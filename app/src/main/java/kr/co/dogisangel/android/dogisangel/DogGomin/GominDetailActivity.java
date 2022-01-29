package kr.co.dogisangel.android.dogisangel.DogGomin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.GominDetailObject;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.Main_Gomin;


/**
 * Created by ccei on 2016-08-03.
 */
public class GominDetailActivity extends AppCompatActivity {

    public static CustomTextView gomin_detail_main_title, gomin_detail_user_name, gomin_detail_register_date, gomin_detail_content;
    public static CustomTextView gomin_detail_comment_count, gomin_detail_region;
    public static CircleImageView gomin_detail_profile_Img;

    private EditText comment_content;
    private ImageButton comment_Btn;

    public RecyclerView recyclerView;

    GominDetailAdapter gominDetailAdapter;
    GominCommentAdapter gominCommentAdapter;
    String ID;

    int writing_member_id;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doggomin_item_detail);

        gominDetailAdapter = new GominDetailAdapter(this);


        gomin_detail_profile_Img = (CircleImageView)findViewById(R.id.gomin_detail_profile_img);
        gomin_detail_main_title = (CustomTextView)findViewById(R.id.gomin_detail_title);
        gomin_detail_user_name = (CustomTextView)findViewById(R.id.gomin_detail_user_name);
        gomin_detail_register_date = (CustomTextView)findViewById(R.id.gomin_detail_register_date);
        gomin_detail_content = (CustomTextView)findViewById(R.id.gomin_detail_content);
        gomin_detail_comment_count = (CustomTextView)findViewById(R.id.gomin_detail_comment_count);
        gomin_detail_region = (CustomTextView)findViewById(R.id.gomin_detail_user_area);

        comment_content = (EditText)findViewById(R.id.doggomin_comment);
        comment_Btn = (ImageButton)findViewById(R.id.doggomin_comment_Btn);






        gominCommentAdapter = new GominCommentAdapter(this, new ArrayList<CommentObject>());

        recyclerView = (RecyclerView)findViewById(R.id.doggomin_comment_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));
        recyclerView.setAdapter(gominCommentAdapter);



        Intent intent = getIntent();
        ID = intent.getStringExtra("gomin_id");
        final String qualify = intent.getStringExtra("qulify");

        new AsyncGominDetailJSONList(gominDetailAdapter).execute("");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            Transition exitTrans = new Explode();
            Transition reenterTrans = new Explode();
            window.setExitTransition(exitTrans);
            window.setEnterTransition(reenterTrans);
            window.setReenterTransition(reenterTrans);
        }


        final Toolbar toolbar = (Toolbar)findViewById(R.id.doggomin_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ViewPager gomin_detail_viewpager = (ViewPager) findViewById(R.id.gomin_detail_Img_viewpager);
        gomin_detail_viewpager.setAdapter(gominDetailAdapter);

        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            comment_Btn.setVisibility(View.GONE);
            comment_content.setVisibility(View.GONE);
        }

        comment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_check = comment_content.getText().toString().trim();
                new GominCommentSendAsyncTask(comment_check).execute();
                new AsyncGominDetailJSONList(gominDetailAdapter).execute("");
                comment_content.setText("");

            }
        });


    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, String> {
        private String query1; //보낼쿼리

        public deleteAsyncTask(String query1) {
            this.query1 = query1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Response response = null;
            try {
                //업로드는 타임 및 리드타임을 넉넉히 준다.
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;


                //요청 세팅
                Request request = new Request.Builder()
                        .url(Main_Gomin + "/" + query1)
                        .delete()
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public class AsyncGominDetailJSONList extends AsyncTask<String, Integer, GominDetailObject> {
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());
        GominDetailAdapter adapter;

        public AsyncGominDetailJSONList(GominDetailAdapter detailAdapter) {
            adapter = detailAdapter;
        }

        @Override
        protected GominDetailObject doInBackground(
                String... params) {
            Response response = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.Main_Gomin_DETAIL + ID)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONGominDetailAllList(
                            new StringBuilder(responseBody.string()));
                }else{
                    Toast.makeText(GominDetailActivity.this, "뭔가 에러가 났네영", Toast.LENGTH_SHORT ).show();
                    finish();
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
           // networkDialog.show();
        }

        @Override
        protected void onPostExecute(GominDetailObject result) {
           // networkDialog.show();

            if (result == null) {
                Toast.makeText(GominDetailActivity.this, "파일을 받아올 수 없습니다.고민디테일액티비티.", Toast.LENGTH_SHORT ).show();
                finish();
            }else {

                GominDetailActivity.gomin_detail_main_title.setText(result.gomin_detail_main_title);
                GominDetailActivity.gomin_detail_register_date.setText(result.gomin_detail_register_date);
                GominDetailActivity.gomin_detail_user_name.setText(result.gomin_detail_nickname);
                GominDetailActivity.gomin_detail_content.setText(result.gomin_detail_content);
                GominDetailActivity.gomin_detail_comment_count.setText(String.valueOf(result.gomin_detail_comment_count));
                GominDetailActivity.gomin_detail_region.setText(result.gomin_detail_region);

                gominCommentAdapter.commentObjects = result.commentObjectArrayList;
                gominCommentAdapter.notifyDataSetChanged();

                writing_member_id = result.member_id;


                Glide.with(GominDetailActivity.this).load(result.gomin_detail_profile_Img).into(gomin_detail_profile_Img);

                adapter.setImageResources(result.gomin_detail_main_Img);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gomin_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        if(writing_member_id == MySingleton.sharedInstance().member_id) {
            menu.setGroupVisible(R.id.gomin_guest, false);
            menu.setGroupVisible(R.id.gomin_owner, true);
        }else {
            menu.setGroupVisible(R.id.gomin_guest, true);
            menu.setGroupVisible(R.id.gomin_owner, false);
        }

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.gomin_delete_menu:
                new deleteAsyncTask(ID).execute();
                finish();
                new GominFragment.AsyncGominJSONList().execute("");
                return true;

            case R.id.gomin_declare_menu:
                Toast.makeText(GominDetailActivity.this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GominCommentSendAsyncTask extends AsyncTask<Void, Void, ArrayList<CommentObject>> {
        private String comment_content; //보낼쿼리
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        public GominCommentSendAsyncTask(String comment_content) {
            this.comment_content = comment_content;
        }

        @Override
        protected void onPreExecute() {
           // networkDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<CommentObject> doInBackground(Void... voids) {
            Response response = null;
            try {

                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                RequestBody formBody = new FormBody.Builder()
                        .add("comment", comment_content)
                        .build();

                //요청 세팅
                Request request = new Request.Builder()
                        .url(Main_Gomin + "/" + ID + "/comment")
                        .post(formBody) //반드시 post로
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();



                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {

                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<CommentObject> result) {
          //  networkDialog.dismiss();
        }
    }
}

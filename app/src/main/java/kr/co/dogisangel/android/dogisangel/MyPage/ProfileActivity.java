package kr.co.dogisangel.android.dogisangel.MyPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.ProfileObject;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

public class ProfileActivity extends AppCompatActivity {
    EditText profile_comment;

    RecyclerView recyclerView;
    ProfileCommentAdapter profileCommentAdapter;

    EditText comment_content;
    ImageButton comment_Btn;

    ImageView license_img1;
    ImageView license_img2;
    static TextView profile_content, myProfile_comment_count;

    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_pro_profile);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.pro_myprofile);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        Intent intent =getIntent();
        ID = intent.getStringExtra("member_id");

        myProfile_comment_count = (TextView)findViewById(R.id.myprofile_comment_count);




        profileCommentAdapter = new ProfileCommentAdapter(this, new ArrayList<CommentObject>());

        recyclerView = (RecyclerView)findViewById(R.id.profile_comment_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));
        recyclerView.setAdapter(profileCommentAdapter);

        comment_Btn = (ImageButton)findViewById(R.id.profile_comment_Btn);
        comment_content = (EditText)findViewById(R.id.profile_comment);

        comment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_check = comment_content.getText().toString().trim();
                new ProfileCommentSendAsyncTask(comment_check).execute();
                new ProfileAsyncTask().execute();
                comment_content.setText("");

            }
        });

        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            comment_Btn.setVisibility(View.GONE);
            comment_content.setVisibility(View.GONE);
        }

        license_img1 = (ImageView)findViewById(R.id.license_img1);
        license_img2 = (ImageView)findViewById(R.id.license_img2);
        profile_content =(TextView)findViewById(R.id.profile_content);

        license_img1.setVisibility(View.GONE);
        license_img2.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        new ProfileAsyncTask().execute("");

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

    public class ProfileAsyncTask extends AsyncTask<String, Integer, ProfileObject> {
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        @Override
        protected ProfileObject  doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;
                Request request = new Request.Builder()
                        .url(FULL_URL + "profile")
                        .build();
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
          //  networkDialog.show();
        }

        @Override
        protected void onPostExecute(ProfileObject result) {
           // networkDialog.dismiss();

            ProfileActivity.profile_content.setText(result.profile);
            ProfileActivity.myProfile_comment_count.setText(""+ result.comment_count);



            Glide.with(ProfileActivity.this).load(result.license1).into(license_img1);
            Glide.with(ProfileActivity.this).load(result.license2).into(license_img2);

            profileCommentAdapter.commentObjects = result.commentObjectArrayList;
            profileCommentAdapter.notifyDataSetChanged();

            //Glide.with(ProfileActivity.this).load(result.license1).into(license_img1);

        }
    }




    private class ProfileCommentSendAsyncTask extends AsyncTask<Void, Void, ArrayList<CommentObject>> {
        private String comment_content; //보낼쿼리

        public ProfileCommentSendAsyncTask(String comment_content) {
            this.comment_content = comment_content;
        }

        @Override
        protected void onPreExecute() {
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
                        .url(FULL_URL + "profile/" + MySingleton.sharedInstance().member_id + "/comment")
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

        }
    }

}

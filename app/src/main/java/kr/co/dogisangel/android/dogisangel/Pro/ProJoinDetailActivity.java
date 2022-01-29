package kr.co.dogisangel.android.dogisangel.Pro;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.ProDetailObject;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.MAIN_PRO;


/**
 * Created by ccei on 2016-08-15.
 */
public class ProJoinDetailActivity extends AppCompatActivity {
    public static CustomTextView join_detail_dog_size;
    public static CustomTextView join_detail_register_date;
    public static CustomTextView join_detail_title;
    public static CustomTextView join_detail_training_spot;
    public static CustomTextView join_detail_person_number;
    public static CustomTextView join_detail_training_period;
    public static CustomTextView join_origin_price;
    public static CustomTextView join_sale_price;
    public static CustomTextView join_training_type;
    public static CustomTextView join_content;
    public static CircleImageView join_profile_Img;
    public static ImageView join_grade_Img;
    public static CustomTextView join_user_name;
    public static CustomTextView join_profile_content;
    public static CustomTextView join_star_count;
    public static CustomTextView join_comment_count, join_license;

    private EditText comment_content;
    private ImageButton comment_Btn;

    static ToggleButton star_count_Btn;

    String ID;
    int user_member_id;

    RecyclerView recyclerView;


    JoinDetailAdapter joinDetailAdapter;
    JoinCommentAdapter joinCommentAdapter;

    String join_star_value;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onResume() {
        super.onResume();

        //new AsyncJoinDetailJSONList(joinDetailAdapter).execute("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_join_detail);

        joinDetailAdapter = new JoinDetailAdapter(this);


        final LinearLayout toSNSRoot = (LinearLayout)findViewById(R.id.toSNSRoot);

        join_detail_dog_size = (CustomTextView) findViewById(R.id.join_detail_dog_size);
        join_detail_register_date = (CustomTextView) findViewById(R.id.join_detail_register_date);
        join_detail_title = (CustomTextView) findViewById(R.id.join_detail_title);
        join_detail_training_spot = (CustomTextView) findViewById(R.id.join_detail_train_spot);
        join_detail_person_number = (CustomTextView) findViewById(R.id.join_detail_recruit_person);
        join_detail_training_period = (CustomTextView) findViewById(R.id.join_detail_train_period);
        join_origin_price = (CustomTextView) findViewById(R.id.join_detail_origin_cost);
        join_sale_price = (CustomTextView) findViewById(R.id.join_detail_sale_cost);
        join_training_type = (CustomTextView) findViewById(R.id.join_detail_train_type);
        join_content = (CustomTextView) findViewById(R.id.join_detail_content);
        join_profile_Img = (CircleImageView) findViewById(R.id.join_detail_profile_Img);
        join_grade_Img = (ImageView) findViewById(R.id.join_detail_grade_img);
        join_user_name = (CustomTextView) findViewById(R.id.join_detail_user_name);
        join_profile_content = (CustomTextView) findViewById(R.id.join_detail_profile);
        join_star_count = (CustomTextView) findViewById(R.id.join_detail_star_count);
        join_comment_count = (CustomTextView) findViewById(R.id.join_detail_comment_count);
        comment_content = (EditText)findViewById(R.id.join_comment_content);
        comment_Btn = (ImageButton)findViewById(R.id.join_comment_Btn);
        join_license = (CustomTextView)findViewById(R.id.join_license) ;



        ImageButton join_share_Btn = (ImageButton)findViewById(R.id.join_share_Btn) ;

        joinCommentAdapter = new JoinCommentAdapter(this,new ArrayList<CommentObject>());

        recyclerView = (RecyclerView)findViewById(R.id.join_comment_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));
        recyclerView.setAdapter(joinCommentAdapter);

        Intent intent = getIntent();
        ID = intent.getStringExtra("join_id");
        Log.e("공동훈련 글 아이디를 잘 받아오니???", ID);

        new AsyncJoinDetailJSONList(joinDetailAdapter).execute("");
        star_count_Btn = (ToggleButton) findViewById(R.id.join_star_Btn);
        star_count_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinMyLikeAsyncTask(ID).execute();
                new AsyncJoinDetailJSONList(joinDetailAdapter).execute();
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.join_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager main_img = (ViewPager) findViewById(R.id.join_detail_img_viewpager);
        main_img.setAdapter(joinDetailAdapter);

        star_count_Btn.setText(null);
        star_count_Btn.setTextOff(null);
        star_count_Btn.setTextOn(null);


        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            comment_Btn.setVisibility(View.GONE);
            comment_content.setVisibility(View.GONE);
        }


        comment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_check = comment_content.getText().toString().trim();
                new JoinCommentSendAsyncTask(comment_check).execute();
                new AsyncJoinDetailJSONList(joinDetailAdapter).execute("");
                comment_content.setText("");
            }
        });

        join_share_Btn.setOnClickListener(new View.OnClickListener(){           // 공유하기 버튼 클릭
            @Override
            public void onClick(View view) {
                String fileName = "sns_upload_image_file.jpg";
                File snsShareDir = new File(Environment.getExternalStorageDirectory() +
                        "/sns_share_dir_images/");
                FileOutputStream fos;
                if (Build.VERSION.SDK_INT >= 23) {
                    if(isStoragePermissionGranted()) {
                        toSNSRoot.buildDrawingCache();
                        Bitmap captureView = toSNSRoot.getDrawingCache();

                        try {
                            if (!snsShareDir.exists()) {
                                if (!snsShareDir.mkdirs()) {
                                }
                            }
                            File file = new File(snsShareDir, fileName);
                            if(!file.exists()){
                                file.createNewFile();
                            }
                            fos = new FileOutputStream(file);
                            captureView.compress(Bitmap.CompressFormat.JPEG, 100,fos );

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                            Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                            startActivity(target);

                        } catch (Exception e) {
                            Log.e("onTouch", e.toString(), e);
                        }
                    }
                }else{
                    toSNSRoot.buildDrawingCache();
                    Bitmap captureView = toSNSRoot.getDrawingCache();
                    try {
                        if (!snsShareDir.exists()) {
                            if (!snsShareDir.mkdirs()) {
                            }
                        }
                        File file = new File(snsShareDir, fileName);
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(snsShareDir));

                        Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                        startActivity(target);

                    } catch (Exception e) {
                        Log.e("onTouch", e.toString(), e);
                    }
                }

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.join_menu, menu);
       return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        if(user_member_id == MySingleton.sharedInstance().member_id) {
            menu.setGroupVisible(R.id.guest, false);
            menu.setGroupVisible(R.id.owner, true);
        }else {
            menu.setGroupVisible(R.id.guest, true);
            menu.setGroupVisible(R.id.owner, false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.join_delete_menu:
                new deleteAsyncTask(ID).execute();
                finish();
                new ProFragment.AsyncProPromoteJSONList().execute("");
                return true;
            case R.id.declare_menu:
                Toast.makeText(ProJoinDetailActivity.this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            case R.id.recruit_completion:
                new recruitAsyncTask(ID).execute();
                finish();
                new ProFragment.AsyncProPromoteJSONList().execute("");
                return true;
        }


        return super.onOptionsItemSelected(item);
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
                        .url(MAIN_PRO + "/" + query1)
                        .delete()
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    Log.e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    Log.e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                Log.e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bb", uee.toString());
            } catch (Exception e) {
                Log.e("cc", e.toString());
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

    private static class recruitAsyncTask extends AsyncTask<Void, Void, String> {
        private String query1; //보낼쿼리

        public recruitAsyncTask(String query1) {
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
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();


                //요청 세팅
                Request request = new Request.Builder()
                        .url(MAIN_PRO + "/" + query1 + "/state")
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    Log.e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    Log.e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                Log.e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bb", uee.toString());
            } catch (Exception e) {
                Log.e("cc", e.toString());
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

    public class AsyncJoinDetailJSONList extends AsyncTask<String, Integer, ProDetailObject> {

       // NetworkDialog networkDialog;
        JoinDetailAdapter adapter;

        public AsyncJoinDetailJSONList(JoinDetailAdapter joinDetailAdapter) {
            adapter = joinDetailAdapter;
            //networkDialog = new NetworkDialog(ProJoinDetailActivity.this);
        }

        @Override
        protected ProDetailObject doInBackground(String... params) {
            Response response = null;
            ProDetailObject proDetailObject = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.MAIN_PRO_DETAIL + ID)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();


                if (flag) {
                    proDetailObject =  ParseDataParseHandler.getJSONPromoteDetailAllList(new StringBuilder(responseBody.string()));
                    return proDetailObject;

                }
            } catch (UnknownHostException une) {
                Log.e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bbb", uee.toString());
            } catch (Exception e) {
                Log.e("ccc", e.toString());
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
        protected void onPostExecute(ProDetailObject result) {
            //networkDialog.dismiss();

            if (result == null) {
                Toast.makeText(ProJoinDetailActivity.this, "파일을 받아올 수 없습니다.", Toast.LENGTH_SHORT ).show();
                finish();
            }else {
                ProJoinDetailActivity.join_detail_dog_size.setText(result.pro_detail_dog_size);
                ProJoinDetailActivity.join_detail_register_date.setText(result.pro_detail_register_date);
                ProJoinDetailActivity.join_detail_title.setText(result.pro_detail_title);
                ProJoinDetailActivity.join_detail_training_spot.setText(result.pro_detail_train_spot);
                ProJoinDetailActivity.join_detail_person_number.setText(String.valueOf(result.pro_detail_recruit_person));
                ProJoinDetailActivity.join_detail_training_period.setText(String.valueOf(result.pro_detail_train_period));
                ProJoinDetailActivity.join_origin_price.setText(String.valueOf(result.pro_detail_origin_price));
                ProJoinDetailActivity.join_sale_price.setText(String.valueOf(result.pro_detail_price));
                ProJoinDetailActivity.join_training_type.setText(result.pro_detail_train_type);
                ProJoinDetailActivity.join_content.setText(result.pro_detail_content);
                ProJoinDetailActivity.join_user_name.setText(result.pro_detail_name);
                ProJoinDetailActivity.join_profile_content.setText(result.pro_detail_profile);
                ProJoinDetailActivity.join_star_count.setText(String.valueOf(result.pro_detail_star_count));
                ProJoinDetailActivity.join_comment_count.setText(String.valueOf(result.pro_detail_comment_count));
                ProJoinDetailActivity.join_license.setText(result.pro_detail_license);

                join_star_value = result.is_favorite;

                Glide.with(ProJoinDetailActivity.this).load(result.pro_detail_profile_img).into(join_profile_Img);
                Glide.with(ProJoinDetailActivity.this).load(result.pro_detail_grade_img).into(join_grade_Img);

                joinCommentAdapter.commentObjects = result.commentObjectArrayList;
                joinCommentAdapter.notifyDataSetChanged();

                user_member_id = result.member_id;

                if (join_star_value.equals("true")) {
                    star_count_Btn.setChecked(true);
                } else {
                    star_count_Btn.setChecked(false);
                }

                adapter.setImageResources(result.pro_detail_image);
            }

        }
    }
    private class JoinCommentSendAsyncTask extends AsyncTask<Void, Void, ArrayList<CommentObject>> {
        private String comment_content; //보낼쿼리

        public JoinCommentSendAsyncTask(String comment_content) {
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
                        .url(MAIN_PRO + "/" + ID + "/comment")
                        .post(formBody) //반드시 post로
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {

                }

            } catch (UnknownHostException une) {
                Log.e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bb", uee.toString());
            } catch (Exception e) {
                Log.e("cc", e.toString());
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

    private static class JoinMyLikeAsyncTask extends AsyncTask<Void, Void, String> {
        private String query1; //보낼쿼리

        public JoinMyLikeAsyncTask(String query1) {
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
                        .url(MAIN_PRO + "/" + query1 + "/like")
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    Log.e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    Log.e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                Log.e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bb", uee.toString());
            } catch (Exception e) {
                Log.e("cc", e.toString());
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

        }
    }
}




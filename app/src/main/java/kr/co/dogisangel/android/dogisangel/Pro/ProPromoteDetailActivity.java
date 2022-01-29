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
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.Chatting.ChattingActivity;
import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.Edit.EditPromote;
import kr.co.dogisangel.android.dogisangel.ExternalView.MyPageEVProActivity;
import kr.co.dogisangel.android.dogisangel.FloatingButton.TrainingPromote;
import kr.co.dogisangel.android.dogisangel.Login.Login;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MyPage.ProfileActivity;
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

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.MAIN_PRO;


/**
 * Created by ccei on 2016-07-07.
 */
public class ProPromoteDetailActivity extends AppCompatActivity {



    public  CustomTextView post_detail_title, post_detail_dog_size, post_detail_register_date, post_detail_star_count, post_detail_comment_count;
    public  CustomTextView post_detail_train_spot, post_detail_train_period, post_detail_cost, post_detail_train_type, post_detail_content;
    public  CustomTextView post_detail_user_name, post_detail_profile;
    public  ImageView post_detail_grade_img;
    public  CircleImageView post_detail_profile_img;

    public  ToggleButton star_count_Btn;

    private EditText comment_content;
     private TextView post_detail_license;


    RecyclerView recyclerView;

    PromoteDetailAdapter promoteDetailAdapter;
    PromoteCommentAdapter promoteCommentAdapter;
    String ID;

    int writing_member_id;



    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_promote_detail);

        promoteDetailAdapter = new PromoteDetailAdapter(this);



        post_detail_title = (CustomTextView) findViewById(R.id.post_detail_title);
        post_detail_dog_size = (CustomTextView) findViewById(R.id.post_detail_dog_size);
        post_detail_register_date = (CustomTextView) findViewById(R.id.post_detail_register_date);
        post_detail_star_count = (CustomTextView) findViewById(R.id.post_detail_star_count);
        post_detail_comment_count = (CustomTextView) findViewById(R.id.post_detail_comment_count);
        post_detail_train_spot = (CustomTextView) findViewById(R.id.post_detail_train_spot);
        post_detail_train_period = (CustomTextView) findViewById(R.id.post_detail_train_period);
        post_detail_cost = (CustomTextView) findViewById(R.id.post_detail_cost);
        post_detail_train_type = (CustomTextView) findViewById(R.id.post_detail_train_type);
        post_detail_content = (CustomTextView) findViewById(R.id.post_detail_content);
        post_detail_user_name = (CustomTextView)findViewById(R.id.post_detail_user_name);
        post_detail_profile =(CustomTextView)findViewById(R.id.post_detail_profile);
        post_detail_profile_img = (CircleImageView) findViewById(R.id.post_detail_profile_img);
        post_detail_grade_img = (ImageView) findViewById(R.id.post_detail_grade_img);
        post_detail_license = (TextView)findViewById(R.id.post_detail_license);

        ImageButton promote_share_Btn = (ImageButton)findViewById(R.id.promote_share_Btn);

        final LinearLayout toSNSRoot =  (LinearLayout)findViewById(R.id.snsRoot);



        comment_content = (EditText)findViewById(R.id.pro_comment);
        ImageButton comment_Btn = (ImageButton) findViewById(R.id.pro_comment_Btn);

        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            comment_content.setVisibility(View.GONE);
            comment_Btn.setVisibility(View.GONE);
        }

        promoteCommentAdapter = new PromoteCommentAdapter(this,new ArrayList<CommentObject>());

        recyclerView = (RecyclerView)findViewById(R.id.pro_comment_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));
        recyclerView.setAdapter(promoteCommentAdapter);

        Intent intent = getIntent();
        ID = intent.getStringExtra("join_id");
        Log.e("글 아이디를 잘 받아오니???", ID);

        new AsyncPromoteDetailJSONList(promoteDetailAdapter).execute("");

        star_count_Btn = (ToggleButton) findViewById(R.id.star_Btn);
        star_count_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new PromoteMyLikeAsyncTask(ID).execute();
                new AsyncPromoteDetailJSONList(promoteDetailAdapter).execute();

            }
        });
        star_count_Btn.setText(null);
        star_count_Btn.setTextOff(null);
        star_count_Btn.setTextOn(null);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Transition exitTrans = new Explode();
            Transition reenterTrans = new Explode();
            window.setExitTransition(exitTrans);
            window.setEnterTransition(reenterTrans);
            window.setReenterTransition(reenterTrans);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ImageButton ev_promote_profile = (ImageButton) findViewById(R.id.ev_promote_profile);


        ev_promote_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ProPromoteDetailActivity.this,MyPageEVProActivity.class);
                intent.putExtra("member_id", String.valueOf(writing_member_id));
                startActivity(intent);
            }
        });



        ViewPager main_img = (ViewPager) findViewById(R.id.post_detail_img_viewpager);
        main_img.setAdapter(promoteDetailAdapter);

        comment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_check = comment_content.getText().toString().trim();
                new PromoteCommentSendAsyncTask(comment_check).execute();
                new AsyncPromoteDetailJSONList(promoteDetailAdapter).execute("");
                comment_content.setText("");
            }
        });

        promote_share_Btn.setOnClickListener(new View.OnClickListener(){           // 공유하기 버튼 클릭
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
    protected void onResume() {
        super.onResume();

       //new AsyncPromoteDetailJSONList(promoteDetailAdapter).execute("");
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

    private static class PromoteMyLikeAsyncTask extends AsyncTask<Void, Void, String> {
        private String query1; //보낼쿼리

        public PromoteMyLikeAsyncTask(String query1) {
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

    public class AsyncPromoteDetailJSONList extends AsyncTask<String, Integer, ProDetailObject> {
        //NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());
        PromoteDetailAdapter adapter;

        public AsyncPromoteDetailJSONList(PromoteDetailAdapter detailAdapter) {
            adapter = detailAdapter;
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
                Log.e("sdsdsdsdd", ""+proDetailObject);

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
        protected void onPostExecute(ProDetailObject result) {
            //networkDialog.dismiss();

            if (result == null) {
                Toast.makeText(ProPromoteDetailActivity.this, "파일을 받아올 수 없습니다.", Toast.LENGTH_SHORT ).show();
                finish();
            }else {

                ProPromoteDetailActivity.this.post_detail_title.setText(result.pro_detail_title);
                ProPromoteDetailActivity.this.post_detail_dog_size.setText(result.pro_detail_dog_size);
                ProPromoteDetailActivity.this.post_detail_register_date.setText(result.pro_detail_register_date);
                ProPromoteDetailActivity.this.post_detail_train_period.setText(String.valueOf(result.pro_detail_train_period));
                ProPromoteDetailActivity.this.post_detail_train_spot.setText(result.pro_detail_train_spot);
                ProPromoteDetailActivity.this.post_detail_cost.setText(String.valueOf(result.pro_detail_origin_price));
                ProPromoteDetailActivity.this.post_detail_train_type.setText(result.pro_detail_train_type);
                ProPromoteDetailActivity.this.post_detail_comment_count.setText(String.valueOf(result.pro_detail_comment_count));
                ProPromoteDetailActivity.this.post_detail_star_count.setText(String.valueOf(result.pro_detail_star_count));
                ProPromoteDetailActivity.this.post_detail_content.setText(result.pro_detail_content);
                ProPromoteDetailActivity.this.post_detail_user_name.setText(result.pro_detail_name);
                ProPromoteDetailActivity.this.post_detail_profile.setText(result.pro_detail_profile);
                ProPromoteDetailActivity.this.post_detail_license.setText(result.pro_detail_license);


                Glide.with(ProPromoteDetailActivity.this).load(result.pro_detail_profile_img).into(post_detail_profile_img);
                Glide.with(ProPromoteDetailActivity.this).load(result.pro_detail_grade_img).into(post_detail_grade_img);

                if (result.is_favorite.equals("true")) {
                    star_count_Btn.setChecked(true);
                } else {
                    star_count_Btn.setChecked(false);
                }


                promoteCommentAdapter.commentObjects = result.commentObjectArrayList;
                promoteCommentAdapter.notifyDataSetChanged();

                writing_member_id = result.member_id;
                Log.e("나의 멤버아이디가 어떠니", "" + MySingleton.sharedInstance().member_id);
                Log.e("글의 멤버아이디가 어떠니", "" + writing_member_id);


                adapter.setImageResources(result.pro_detail_image);
            }
        }
    }

    private class PromoteCommentSendAsyncTask extends AsyncTask<Void, Void, ArrayList<CommentObject>> {
        private String comment_content; //보낼쿼리

        public PromoteCommentSendAsyncTask(String comment_content) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.promotegominmenu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        if(writing_member_id == MySingleton.sharedInstance().member_id) {
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
                    break;
                case R.id.promote_delete_menu:
                    new deleteAsyncTask(ID).execute();
                    finish();
                    new ProFragment.AsyncProPromoteJSONList().execute("");
                    return true;
                case R.id.promote_edit_menu:
                    Intent intent = new Intent(ProPromoteDetailActivity.this, EditPromote.class);
                    intent.putExtra("post_id", ID);
                    startActivity(intent);

                    break;
                case R.id.promote_declare_menu:
                    Toast.makeText(ProPromoteDetailActivity.this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }

        return super.onOptionsItemSelected(item);
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

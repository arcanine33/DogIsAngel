package kr.co.dogisangel.android.dogisangel.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.LoginObject;
import kr.co.dogisangel.android.dogisangel.MainActivity;
import kr.co.dogisangel.android.dogisangel.Membership.Membership_main;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.NetworkManager;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.PropertyManager;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;


/**
 * Created by ccei on 2016-08-11.
 */
public class Login extends AppCompatActivity {

    private EditText login_ID, login_PW;

    private static final String TAG = "MainActivity";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    //private static final String TAG = "CookiesExample";
    private Handler handler;
    private CheckBox login_checkbox;
    String sfName1 = "myFile";

    private long backPressedTime=0;
    private final long FINSH_INTERVAL_TIME=2000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_ID = (EditText) findViewById(R.id.login_ID);
        login_PW = (EditText) findViewById(R.id.login_PW);


        SharedPreferences sf1 = getSharedPreferences(sfName1, 0);
        String str1 = sf1.getString("login_ID", ""); // 키값으로 꺼냄
        String str2 = sf1.getString("login_PW", ""); // 키값으로 꺼냄
        login_ID.setText(str1);
        login_PW.setText(str2);


        if(str1==null && str2 == null){

        }else{

            final String login_ID_check = login_ID.getText().toString().trim();
            final String login_PW_check = login_PW.getText().toString().trim();
            new LoginSendAsyncTask(login_ID_check, login_PW_check).execute();


        }


        login_checkbox = (CheckBox) findViewById(R.id.login_checkbox);
        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.login_checkbox) {
                    if (isChecked) {


                        SharedPreferences sf1 = getSharedPreferences(sfName1, 0);
                        SharedPreferences.Editor editor = sf1.edit();//저장하려면 editor가 필요
                        String str1 = login_ID.getText().toString(); // 사용자가 입력한 값
                        String str2 = login_PW.getText().toString();
                        editor.putString("login_ID", str1); // 입력
                        editor.putString("login_PW", str2); // 입력
                        editor.commit();

                    } else {

                    }
                }
            }
        });

        ImageButton login_Btn = (ImageButton) findViewById(R.id.login_Btn);
        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login_ID_check = login_ID.getText().toString().trim();
                String login_PW_check = login_PW.getText().toString().trim();

                new LoginSendAsyncTask(login_ID_check, login_PW_check).execute();

            }
        });

        handler = new Handler();

        checkPermission();

        ImageButton guest_Btn = (ImageButton) findViewById(R.id.guest_Btn);
        guest_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton membership_Btn = (ImageButton) findViewById(R.id.membership_Btn);
        membership_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Login.this, Membership_main.class);
                startActivity(intent);
            }
        });

        ImageButton id_password_Btn = (ImageButton) findViewById(R.id.id_password_Btn);
        id_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Login.this, LoginSearchMain.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private class LoginSendAsyncTask extends AsyncTask<Void, Void, LoginObject> {
        private String ID; //보낼쿼리
        private String PW; //보낼쿼리

        public LoginSendAsyncTask(String ID, String PW) {
            this.ID = ID;
            this.PW = PW;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LoginObject doInBackground(Void... voids) {
            Response response = null;
            try {
                //업로드는 타임 및 리드타임을 넉넉히 준다.
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                RequestBody formBody = new FormBody.Builder()
                        .add("id", ID)
                        .add("password", PW)
                        .build();
                //요청 세팅
                Request request = new Request.Builder()
                        .url(FULL_URL + "login")
                        .post(formBody)
                        .build();
                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();

                if (flag) {
                    Log.e("flag", String.valueOf(flag));
                    return ParseDataParseHandler.getJSONLoginObjectAllList(response.body().string());
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
        protected void onPostExecute(LoginObject s) {
            super.onPostExecute(s);
            if (s != null) {
                if (Boolean.parseBoolean(s.message)) {
                    //new MyThread().start();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(Login.this, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                login_ID.requestFocus();
            }
        }
    }


    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우
                } else {

                    Log.d(TAG, "Permission always deny");
                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }
    private TextView resultView;

    class MyThread extends Thread {
        @Override
        public void run() {
            Request request = new Request.Builder().url("http://52.78.102.112:3000/login").build();
            try {

                OkHttpClient client = MySingleton.sharedInstance().httpClient;
                Response response = client.newCall(request).execute();
                String bodyStr = response.body().string();

                if (bodyStr != null) {
                    JSONObject root = new JSONObject(bodyStr);
                    final String visit = root.getString("sessionVisit");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultView.setText("visit : " + visit);
                        }
                    });
                } else {
                    Log.d(TAG, "Response body error");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error : " + e.getMessage());
            }

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
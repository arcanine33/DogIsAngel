package kr.co.dogisangel.android.dogisangel.Membership;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

public class ProStartActivity extends AppCompatActivity {


    private EditText membership_pro_ID, membership_pro_PW, membership_pro_PW_Check;
    private TextView pw_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_start);

        membership_pro_ID = (EditText) findViewById(R.id.membership_ID);
        membership_pro_PW = (EditText) findViewById(R.id.membership_PW);
        membership_pro_PW_Check = (EditText) findViewById(R.id.membership_PW_Check);
        pw_check = (TextView) findViewById(R.id.pw_check);


        ImageButton membership_human = (ImageButton) findViewById(R.id.join_start_next);
        membership_human.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (membership_pro_ID.getText().toString().length() <= 0) {
                    Toast.makeText(ProStartActivity.this, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (membership_pro_PW.getText().toString().length() <= 0) {
                    Toast.makeText(ProStartActivity.this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

                if (membership_pro_PW.getText().toString().equals(membership_pro_PW_Check.getText().toString()) && membership_pro_PW.getText().toString().length() > 0) {
                    String membership_ID_check = membership_pro_ID.getText().toString().trim();
                    new IDCheckAsyncTask(membership_ID_check).execute();
                } else {
                    pw_check.setText("비밀번호가 일치하지 않습니다.");
                    membership_pro_PW_Check.requestFocus();
                }
            }
        });
    }

    private class IDCheckAsyncTask extends AsyncTask<Void, Void, String> {
        private String query1; //보낼쿼리


        public IDCheckAsyncTask(String query1) {
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

                RequestBody formBody = new FormBody.Builder()
                        .add("id", query1)
                        .build();


                //요청 세팅
                Request request = new Request.Builder()
                        .url(FULL_URL + "sign/check")
                        .post(formBody)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    e("response결과", responseCode + "---" + response.message()); //응답에 대한 메세지(OK)
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
            Log.i("아이디를 받아오니?", s);

            if (s.contains("success")) {
                Intent intent;
                intent = new Intent(ProStartActivity.this, Membership_Pro.class);
                intent.putExtra("ID", membership_pro_ID.getText().toString().trim());
                intent.putExtra("PW", membership_pro_PW.getText().toString().trim());
                startActivity(intent);
            } else {
                Toast.makeText(ProStartActivity.this, "아이디가 중복됩니다! 다른 아이디를 사용해주세요 :)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

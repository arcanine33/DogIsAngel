package kr.co.dogisangel.android.dogisangel.ExternalView;


import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
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


public class MyPageEVProProfile extends Fragment {


    static TextView ev_profile;
    ImageButton ev_comment_Btn;
    EditText ev_comment;
    ImageView license_img1, license_img2;
    String member_id;

    RecyclerView recyclerView;
    MyPageEVProActivity myPageEVProActivity;

    EVProfileCommentAdapter evProfileCommentAdapter;

    public MyPageEVProProfile() {}
    static TextView ev_comment_count;

    ArrayList<CommentObject> commentObjectArrayList = new ArrayList<>();

    public static MyPageEVProProfile newInstance(int initValue) {
        MyPageEVProProfile viewFragment = new MyPageEVProProfile();

        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        viewFragment.setArguments(bundle);

        return viewFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pro_profile, container, false);

        member_id = getActivity().getIntent().getStringExtra("member_id");

        evProfileCommentAdapter = new EVProfileCommentAdapter(myPageEVProActivity, commentObjectArrayList);

        recyclerView = (RecyclerView)view.findViewById(R.id.ev_profile_comment_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));
        recyclerView.setAdapter(evProfileCommentAdapter);

        ev_profile = (TextView)view.findViewById(R.id.ev_profile);
        ev_comment = (EditText)view.findViewById(R.id.ev_comment);
        ev_comment_Btn = (ImageButton)view.findViewById(R.id.ev_comment_Btn);
        ev_comment_count = (TextView)view.findViewById(R.id.ev_comment_count);

        license_img1 = (ImageView)view.findViewById(R.id.ev_license_img1);
        license_img2 = (ImageView)view.findViewById(R.id.ev_license_img2);



        ev_comment_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String comment_check = ev_comment.getText().toString().trim();
                new EVProfileCommentSendAsyncTask(comment_check).execute();
                new EVProfileAsyncTask().execute();
                ev_comment.setText("");
            }
        });

        if(MySingleton.sharedInstance().qualify.equals("게스트")){
            ev_comment_Btn.setVisibility(View.GONE);
            ev_comment.setVisibility(View.GONE);
        }



        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        new EVProfileAsyncTask().execute();
    }

    public class EVProfileAsyncTask extends AsyncTask<String, Integer, ProfileObject> {
//        NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //networkDialog.show();
        }

        @Override
        protected ProfileObject  doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;
                Request request = new Request.Builder().url(FULL_URL + "otherProfile/" + member_id).build();
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
        protected void onPostExecute(ProfileObject result) {
            //networkDialog.dismiss();

            MyPageEVProProfile.ev_profile.setText(result.profile);
            MyPageEVProProfile.ev_comment_count.setText(""+result.comment_count);

            Glide.with(getActivity()).load(result.license1).into(license_img1);
            Glide.with(getActivity()).load(result.license2).into(license_img2);


            evProfileCommentAdapter.commentObjects = result.commentObjectArrayList;
            evProfileCommentAdapter.notifyDataSetChanged();


        }
    }

    private class EVProfileCommentSendAsyncTask extends AsyncTask<Void, Void, ArrayList<CommentObject>> {
        private String comment_content; //보낼쿼리
        NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        public EVProfileCommentSendAsyncTask(String comment_content) {
            this.comment_content = comment_content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkDialog.show();
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
                        .url(FULL_URL + "profile/" + member_id + "/comment")
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
            networkDialog.dismiss();
        }
    }
}
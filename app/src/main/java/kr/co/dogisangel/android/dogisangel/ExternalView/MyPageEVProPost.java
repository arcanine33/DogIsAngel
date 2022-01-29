package kr.co.dogisangel.android.dogisangel.ExternalView;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MyPage.MyPostActivity;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
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


public class MyPageEVProPost extends Fragment {
    static RecyclerView ev_recyclerView;

    static EVPostAdapter evPostAdapter;

    public MyPageEVProPost() {}

    MyPageEVProActivity myPageEVProActivity;
    static ImageView no_post;
    static String ev_member_id;

    ArrayList<ProCardViewObject> proCardViewObjectArrayList = new ArrayList<>();

    public static MyPageEVProPost newInstance(int initValue){
        MyPageEVProPost Pro_writeFragment = new MyPageEVProPost();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        Pro_writeFragment.setArguments(bundle);

        return Pro_writeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.mypage_ev_human_recyclerview, container, false);

        myPageEVProActivity = (MyPageEVProActivity) getActivity();

        ev_member_id = getActivity().getIntent().getStringExtra("member_id");

        ev_recyclerView=(RecyclerView)view.findViewById(R.id.ev_mypost_recycleView);
        no_post = (ImageView)view.findViewById(R.id.ev_no_my_post);

        evPostAdapter = new EVPostAdapter(myPageEVProActivity, proCardViewObjectArrayList);
        ev_recyclerView.setLayoutManager(new LinearLayoutManager(ev_recyclerView.getContext()));



        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new EVPostAsyncTask().execute("");
    }

    public static class EVPostAsyncTask extends AsyncTask<String, Integer, ArrayList<ProCardViewObject>> {

//        NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());

        @Override
        protected ArrayList<ProCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(FULL_URL + "otherProfileContent/" + ev_member_id)
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
            //networkDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ProCardViewObject> result) {
            //networkDialog.dismiss();

            if (result != null && result.size() > 0) {
                no_post.setVisibility(View.GONE);



                evPostAdapter.proCardViewObjects = result;
               // EVPostAdapter evPostAdapter = new EVPostAdapter(null, result);
                evPostAdapter.notifyDataSetChanged();
                ev_recyclerView.setAdapter(evPostAdapter);
            }else{
                no_post.setVisibility(View.VISIBLE);
                ev_recyclerView.setVisibility(View.GONE);
            }


        }
    }
}


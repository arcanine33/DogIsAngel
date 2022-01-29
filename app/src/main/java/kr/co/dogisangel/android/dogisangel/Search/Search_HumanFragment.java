package kr.co.dogisangel.android.dogisangel.Search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.DogGomin.GominDetailActivity;
import kr.co.dogisangel.android.dogisangel.GominCardViewObject;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class Search_HumanFragment extends Fragment {
    static SearchHumanActivity owner;
    static RecyclerView recyclerView;

    SwipyRefreshLayout refreshLayout;
    String targetURL = "";
    int pageValue = 0;

    Handler mHandler = new Handler(Looper.getMainLooper());

    DogRecyclerViewAdapter dogRecyclerViewAdapter;


    String train_keyword;
    String keyword;
    String  region;



    public Search_HumanFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doggomin_recyclerview, container, false);


        owner = (SearchHumanActivity)getActivity();


        recyclerView = (RecyclerView) view.findViewById(R.id.dogrecycler_container);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(new DogRecyclerViewAdapter(owner));


        refreshLayout = (SwipyRefreshLayout)view.findViewById(R.id.refreshlayout);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    int page = pageValue;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncGominJSONList().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    pageValue = page;
                    refreshLayout.setRefreshing(true);
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncGominJSONList2().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    refreshLayout.setRefreshing(true);
                }

            }
        });

        dogRecyclerViewAdapter = new DogRecyclerViewAdapter(null, new ArrayList<GominCardViewObject>());
        refreshLayout.setColorSchemeColors(Color.YELLOW, Color.RED, Color.GREEN);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AsyncGominJSONList().execute("");
    }

    public class AsyncGominJSONList extends AsyncTask<String, Integer, ArrayList<GominCardViewObject>> {

        ProgressDialog dialog;


        @Override
        protected ArrayList<GominCardViewObject> doInBackground(
                String... params) {
            Response response = null;
            try {
                //OKHttp3사용
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();


                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SEARCH_GOMIN +"/0?train_keyword=" + train_keyword + "&keyword=" + keyword+"&region="+region)
                        .get()
                        .build();



                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();


                if (flag) {
                    return ParseDataParseHandler.getJSONDogGominAllList(
                            new StringBuilder(responseBody.string()));
                }

            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            }finally{
                if(response != null){
                    response.close();
                }
            }
            return null;

        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<GominCardViewObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                SearchHumanActivity.human_nosearch.setVisibility(View.GONE);
                DogRecyclerViewAdapter dogRecyclerViewAdapter = new DogRecyclerViewAdapter(owner, result);
                dogRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogRecyclerViewAdapter);
            }else{
                SearchHumanActivity.human_nosearch.setVisibility(View.VISIBLE);
                SearchHumanActivity.human_frame.setVisibility(View.GONE);
            }
        }
    }

    public class AsyncGominJSONList2 extends AsyncTask<String, Integer, ArrayList<GominCardViewObject>> {

        ProgressDialog dialog;
        String train_keyword;
        String keyword;
        String  region;



        @Override
        protected ArrayList<GominCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            targetURL = String.format(NetworkDefineConstant.SEARCH_GOMIN +"/0?train_keyword=" + train_keyword + "&keyword=" + keyword +"&region="+region);


            try {
                //OKHttp3사용
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(targetURL)
                        .build();



                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();


                if (flag) {
                    return ParseDataParseHandler.getJSONDogGominAllList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            }finally{
                if(response != null){
                    response.close();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<GominCardViewObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                SearchHumanActivity.human_nosearch.setVisibility(View.GONE);
                dogRecyclerViewAdapter.addItemsData(result);
                dogRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogRecyclerViewAdapter);
            }else{
                SearchHumanActivity.human_nosearch.setVisibility(View.VISIBLE);
                SearchHumanActivity.human_frame.setVisibility(View.GONE);
            }
        }
    }
    public static class DogRecyclerViewAdapter extends RecyclerView.Adapter<DogRecyclerViewAdapter.ViewHolder> {
        public ArrayList<GominCardViewObject> gominCardViewObjects;

        public DogRecyclerViewAdapter (Context context, ArrayList<GominCardViewObject> resources) {
            gominCardViewObjects = resources;
        }

        public void addItemsData(ArrayList<GominCardViewObject> valueObjects) {

            if (valueObjects != null && valueObjects.size() > 0) {
                gominCardViewObjects.addAll(valueObjects);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doggomin_cardview_detail, viewGroup, false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public final ImageView gomin_main_Img;
            public final ImageView gomin_profile_Img;
            public final CustomTextView gomin_user_name;
            public final CustomTextView gomin_region;
            public final CustomTextView gomin_main_title;

            View view;

            public ViewHolder(View itemView){
                super(itemView);
                view = (View)itemView.findViewById(R.id.cardView);
                gomin_main_title = (CustomTextView) itemView.findViewById(R.id.gomin_main_title);
                gomin_main_Img = (ImageView) itemView.findViewById(R.id.gomin_item_image);
                gomin_profile_Img = (ImageView)itemView.findViewById(R.id.gomin_profile_Img);
                gomin_user_name = (CustomTextView)itemView.findViewById(R.id.gomin_user_name);
                gomin_region = (CustomTextView)itemView.findViewById(R.id.gomin_region);

            }
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position){
            final GominCardViewObject gominCardViewObject = gominCardViewObjects.get(position);
            final int id;

            holder.gomin_main_title.setText(gominCardViewObject.gomin_main_title);
            holder.gomin_user_name.setText(gominCardViewObject.gomin_nickname);
            holder.gomin_region.setText(gominCardViewObject.gomin_region);

            Glide.with(owner).load(gominCardViewObject.gomin_main_Img).into(holder.gomin_main_Img);
            Glide.with(owner).load(gominCardViewObject.gomin_profile_Img).into(holder.gomin_profile_Img);
            //id = (gominCardViewObject.gomin_id);




            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(owner, GominDetailActivity.class);

                    intent.putExtra("gomin_id", String.valueOf(gominCardViewObject.gomin_id));
                    intent.putExtra("gomin_region", String.valueOf(gominCardViewObject.gomin_region));
                    intent.putExtra("gomin_nickname", String.valueOf(gominCardViewObject.gomin_nickname));
                    intent.putExtra("gomin_main_title", String.valueOf(gominCardViewObject.gomin_main_title));



                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                            (owner, holder.gomin_main_Img, "transitionName");

                    ActivityCompat.startActivity(owner, intent, options.toBundle());
                }
            });
        }
        @Override
        public int getItemCount(){
            return gominCardViewObjects.size();
        }
    }
}

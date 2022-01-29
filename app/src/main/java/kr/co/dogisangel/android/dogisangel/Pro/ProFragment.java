package kr.co.dogisangel.android.dogisangel.Pro;

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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.MainActivity;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
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

/**
 * Created by ccei on 2016-07-25.
 */
public class ProFragment extends Fragment {

    static MainActivity owner;
    static RecyclerView rv;
    String targetURL = "";

    SwipyRefreshLayout refreshLayout;
    Handler mHandler = new Handler(Looper.getMainLooper());
    ProRecyclerAdapter proRecyclerAdapter;
    static int pageValue = 0;



    public ProFragment() {}

    public static ProFragment newInstance(int initValue) {
        ProFragment viewFragment = new ProFragment();
        return viewFragment;
    }

    /*ProRecyclerAdapter mAdapter;*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pro_recycler_view, container, false);


        owner = (MainActivity) getActivity();

        rv = (RecyclerView) view.findViewById(R.id.pro_recycler_container);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyApplicationContext()));



        refreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.pro_refreshlayout);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    int page = pageValue;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncProPromoteJSONList().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    pageValue = page;
                    refreshLayout.setRefreshing(true);
                }

                else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncProPromoteJSONList2().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    refreshLayout.setRefreshing(true);
                }
            }

        });

        proRecyclerAdapter = new ProRecyclerAdapter(null, new ArrayList<ProCardViewObject>());
        refreshLayout.setColorSchemeColors(Color.YELLOW, Color.RED, Color.GREEN);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new AsyncProPromoteJSONList().execute("");
    }


    public static class AsyncProPromoteJSONList extends AsyncTask<String, Integer, ArrayList<ProCardViewObject>> {



        @Override
        protected ArrayList<ProCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.MAIN_PRO + "/0")
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

        }

        @Override
        protected void onPostExecute(ArrayList<ProCardViewObject> result) {
            if (result == null) {
                Toast.makeText(owner, "파일을 받아올 수 없습니다. 프로 프래그먼트 ", Toast.LENGTH_SHORT ).show();

            }else {
                if (result != null && result.size() > 0) {
                    ProRecyclerAdapter proRecyclerAdapter = new ProRecyclerAdapter(owner, result);
                    proRecyclerAdapter.notifyDataSetChanged();
                    rv.setAdapter(proRecyclerAdapter);
                }
                pageValue = 0;
            }
        }
    }

    public class AsyncProPromoteJSONList2 extends AsyncTask<String, Integer, ArrayList<ProCardViewObject>> {



        @Override
        protected ArrayList<ProCardViewObject> doInBackground(
                String... params) {
            Response response = null;

            targetURL = String.format(NetworkDefineConstant.MAIN_PRO+ "/" + (pageValue++));

            try {
                //OKHttp3사용
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;
                Request request = new Request.Builder()
                        .url(targetURL)
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

        }

        @Override
        protected void onPostExecute(ArrayList<ProCardViewObject> result) {

            if (result == null) {
                Toast.makeText(owner, "파일을 받아올 수 없습니다.", Toast.LENGTH_SHORT ).show();

            }else {

                if (result != null && result.size() > 0) {
                    proRecyclerAdapter.addItemsData(result);
                    proRecyclerAdapter.notifyDataSetChanged();
                    rv.setAdapter(proRecyclerAdapter);
                }
            }
        }
    }
    public static class ProRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_PROMOTE = 100;
        private static final int VIEW_TYPE_JOIN = 500;
        public ArrayList<ProCardViewObject> proCardViewObjects;


        public ProRecyclerAdapter(Context context, ArrayList<ProCardViewObject> resources) {
            this.proCardViewObjects = resources;
        }

        public void addItemsData(ArrayList<ProCardViewObject> valueObjects) {

            if (valueObjects != null && valueObjects.size() > 0) {
                proCardViewObjects.addAll(valueObjects);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


            switch (viewType) {
                case VIEW_TYPE_PROMOTE:
                    View promote_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pro_promote_cardview_detail, viewGroup, false);
                    return new PromoteViewHolder(promote_view);

                case VIEW_TYPE_JOIN:
                    View join_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pro_join_cardview_detail, viewGroup, false);
                    return new JoinViewHolder(join_view);
            }
            return null;
        }


        @Override
        public int getItemViewType(int position) {
            ProCardViewObject proPromoteCardViewObject = proCardViewObjects.get(position);
            int switchFlag = Integer.parseInt(proPromoteCardViewObject.content_type.trim());
            switch (switchFlag) {
                case 0:
                    return VIEW_TYPE_PROMOTE;
                case 1:
                    return VIEW_TYPE_JOIN;

            }
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final ProCardViewObject proCardViewObject = proCardViewObjects.get(position);



            switch (getItemViewType(position)) {

                case VIEW_TYPE_PROMOTE:

                    final PromoteViewHolder promoteViewHolder = (PromoteViewHolder) holder;

                    promoteViewHolder.post_title.setText(proCardViewObject.title);
                    promoteViewHolder.promote_dog_size.setText(proCardViewObject.dog_size);
                    promoteViewHolder.my_name.setText(proCardViewObject.name);
                    promoteViewHolder.my_region.setText(proCardViewObject.region);
                    promoteViewHolder.register_date.setText(proCardViewObject.register_date);
                    promoteViewHolder.star_count.setText(String.valueOf(proCardViewObject.star_count));
                    promoteViewHolder.comment_count.setText(String.valueOf(proCardViewObject.comment_count));
                    promoteViewHolder.price.setText(String.valueOf(proCardViewObject.origin_price));

                    if(proCardViewObject.is_like.equals("true") ){
                        promoteViewHolder.star_count_img.setImageResource(R.drawable.star_count_click);
                    }else{
                        promoteViewHolder.star_count_img.setImageResource(R.drawable.star_count);
                    }

                    Glide.with(owner).load(proCardViewObject.image).into(promoteViewHolder.main_image);
                    Glide.with(owner).load(proCardViewObject.profile_img).into(promoteViewHolder.my_image);
                    Glide.with(owner).load(proCardViewObject.grade_img).into(promoteViewHolder.grade_img);

                    promoteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(owner, ProPromoteDetailActivity.class);
                            intent.putExtra("join_id", String.valueOf(proCardViewObject.id));


                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                                    (owner, (promoteViewHolder.main_image), "transitionName");

                            ActivityCompat.startActivity(owner, intent, options.toBundle());

                        }
                    });
                    break;

                case VIEW_TYPE_JOIN:

                    final JoinViewHolder joinViewHolder = (JoinViewHolder) holder;

                    joinViewHolder.join_post_title.setText(proCardViewObject.title);
                    joinViewHolder.join_dog_size.setText(proCardViewObject.dog_size);
                    joinViewHolder.join_my_name.setText(proCardViewObject.name);
                    joinViewHolder.join_my_region.setText(proCardViewObject.region);
                    joinViewHolder.join_register_date.setText(proCardViewObject.register_date);
                    joinViewHolder.join_star_count.setText(String.valueOf(proCardViewObject.star_count));
                    joinViewHolder.join_comment_count.setText(String.valueOf(proCardViewObject.comment_count));
                    joinViewHolder.join_price.setText(String.valueOf(proCardViewObject.price));
                    joinViewHolder.join_origin_price.setText(String.valueOf(proCardViewObject.origin_price));

                    if(proCardViewObject.is_like.equals("true") ){
                        joinViewHolder.join_star_count_img.setImageResource(R.drawable.star_count_click);
                    }else{
                        joinViewHolder.join_star_count_img.setImageResource(R.drawable.star_count);
                    }



                    if(proCardViewObject.recruit_state.equals("모집중")){
                        Log.e("모집 중 사진", ""+joinViewHolder.recruit_completion_img);
                        joinViewHolder.recruit_completion_img.setVisibility(View.INVISIBLE);

                    }else{
                        joinViewHolder.recruit_completion_img.setVisibility(View.VISIBLE);
                    }




                    Glide.with(owner).load(proCardViewObject.image).into(joinViewHolder.join_main_image);
                    Glide.with(owner).load(proCardViewObject.profile_img).into(joinViewHolder.join_my_image);
                    Glide.with(owner).load(proCardViewObject.grade_img).into(joinViewHolder.join_grade_img);

                    joinViewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(owner, ProJoinDetailActivity.class);
                            intent.putExtra("join_id", String.valueOf(proCardViewObject.id));

                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                                    (owner, (joinViewHolder.join_main_image), "transitionName");

                            ActivityCompat.startActivity(owner, intent, options.toBundle());

                        }
                    });
                    break;


            }
        }


        @Override
        public int getItemCount() {
            return proCardViewObjects.size();
        }


        public class PromoteViewHolder extends RecyclerView.ViewHolder {


            public final CustomTextView promote_dog_size;
            public final CustomTextView post_title;
            public final CustomTextView my_region;
            public final CustomTextView register_date;
            public final CustomTextView price;
            public final CustomTextView my_name;
            public final CustomTextView comment_count;
            public final CustomTextView star_count;


            public final ImageView grade_img;
            public final ImageView my_image;
            public final ImageView main_image;
            public final ImageView star_count_img;

            View view;

            public PromoteViewHolder(View itemView) {
                super(itemView);

                view = (View) itemView.findViewById(R.id.pro_cardView);
                promote_dog_size = (CustomTextView) itemView.findViewById(R.id.promote_dog_size);
                my_name = (CustomTextView) itemView.findViewById(R.id.pro_my_name);
                post_title = (CustomTextView) itemView.findViewById(R.id.post_title);
                my_region = (CustomTextView) itemView.findViewById(R.id.my_region);
                register_date = (CustomTextView) itemView.findViewById(R.id.date);
                price = (CustomTextView) itemView.findViewById(R.id.price);
                comment_count = (CustomTextView) itemView.findViewById(R.id.comment_count);
                star_count = (CustomTextView) itemView.findViewById(R.id.star_count);


                grade_img = (ImageView) itemView.findViewById(R.id.grade_img);
                main_image = (ImageView) itemView.findViewById(R.id.pro_item_image);
                my_image = (ImageView) itemView.findViewById(R.id.pro_my_image);
                star_count_img = (ImageView)itemView.findViewById(R.id.star_count_img);

            }
        }

        public class JoinViewHolder extends RecyclerView.ViewHolder {

            public final CustomTextView join_dog_size;
            public final CustomTextView join_post_title;
            public final CustomTextView join_my_region;
            public final CustomTextView join_register_date;
            public final CustomTextView join_price;
            public final CustomTextView join_origin_price;
            public final CustomTextView join_my_name;
            public final CustomTextView join_comment_count;
            public final CustomTextView join_star_count;

            public final ImageView join_grade_img;
            public final ImageView join_my_image;
            public final ImageView join_main_image;
            public final ImageView join_star_count_img;
            public final ImageView recruit_completion_img;

            View view;

            public JoinViewHolder(View itemView) {
                super(itemView);


                view = (View) itemView.findViewById(R.id.pro_join_cardView);

                join_dog_size = (CustomTextView) itemView.findViewById(R.id.join_dog_size);
                join_my_name = (CustomTextView) itemView.findViewById(R.id.pro_join_my_name);
                join_post_title = (CustomTextView) itemView.findViewById(R.id.join_post_title);
                join_my_region = (CustomTextView) itemView.findViewById(R.id.join_my_region);
                join_register_date = (CustomTextView) itemView.findViewById(R.id.join_date);
                join_price = (CustomTextView) itemView.findViewById(R.id.pro_join_sale_price);
                join_origin_price = (CustomTextView) itemView.findViewById(R.id.join_origin_price);
                join_comment_count = (CustomTextView) itemView.findViewById(R.id.join_comment_count);
                join_star_count = (CustomTextView) itemView.findViewById(R.id.join_star_count);

                join_grade_img = (ImageView) itemView.findViewById(R.id.join_grade_img);
                join_main_image = (ImageView) itemView.findViewById(R.id.pro_join_item_image);
                join_my_image = (ImageView) itemView.findViewById(R.id.join_my_image);
                join_star_count_img = (ImageView)itemView.findViewById(R.id.join_star_count_img);


                recruit_completion_img = (ImageView)itemView.findViewById(R.id.recruit_completion_image);

            }
        }
    }
}






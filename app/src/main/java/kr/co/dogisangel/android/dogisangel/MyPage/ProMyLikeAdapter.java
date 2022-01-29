package kr.co.dogisangel.android.dogisangel.MyPage;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.Pro.ProJoinDetailActivity;
import kr.co.dogisangel.android.dogisangel.Pro.ProPromoteDetailActivity;
import kr.co.dogisangel.android.dogisangel.ProCardViewObject;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-22.
 */
public class ProMyLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PROMOTE = 100;
    private static final int VIEW_TYPE_JOIN = 500;
    public ArrayList<ProCardViewObject> proCardViewObjects;
    MyLikeActivity owner;


    public ProMyLikeAdapter(Context context, ArrayList<ProCardViewObject> resources) {
        owner = (MyLikeActivity)context;
        this.proCardViewObjects = resources;
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
                Log.e("가격", ""+promoteViewHolder.price);



                Glide.with(owner).load(proCardViewObject.image).into(promoteViewHolder.main_image);
                Glide.with(owner).load(proCardViewObject.profile_img).into(promoteViewHolder.my_image);
                Glide.with(owner).load(proCardViewObject.grade_img).into(promoteViewHolder.grade_img);

                if(proCardViewObject.is_like.equals("true") ){
                    promoteViewHolder.promote_star_Img.setImageResource(R.drawable.star_count_click);
                }else{
                    promoteViewHolder.promote_star_Img.setImageResource(R.drawable.star_count);
                }

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


                String recruit_state = proCardViewObject.recruit_state;

                if (recruit_state.equals("모집중")) {
                    Log.e("모집 중 사진", "" + joinViewHolder.recruit_completion_img);
                    joinViewHolder.recruit_completion_img.setVisibility(View.INVISIBLE);

                } else {
                    joinViewHolder.recruit_completion_img.setVisibility(View.VISIBLE);
                }

                if(proCardViewObject.is_like.equals("true") ){
                    joinViewHolder.join_star_Img.setImageResource(R.drawable.star_count_click);
                }else{
                    joinViewHolder.join_star_Img.setImageResource(R.drawable.star_count);
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

        public final ImageView promote_star_Img;

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

            promote_star_Img  =(ImageView)itemView.findViewById(R.id.star_count_img);

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

        public final ImageView recruit_completion_img;

        public final ImageView join_star_Img;

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

            join_star_Img = (ImageView)itemView.findViewById(R.id.join_star_count_img);


            recruit_completion_img = (ImageView) itemView.findViewById(R.id.recruit_completion_image);

        }

    }
}
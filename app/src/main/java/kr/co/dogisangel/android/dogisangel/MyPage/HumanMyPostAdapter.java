package kr.co.dogisangel.android.dogisangel.MyPage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.DogGomin.GominDetailActivity;
import kr.co.dogisangel.android.dogisangel.GominCardViewObject;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-22.
 */
public class HumanMyPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<GominCardViewObject> gominCardViewObjects;
    MyPostActivity owner;

    public HumanMyPostAdapter(Context context, ArrayList<GominCardViewObject> resources) {
        owner = (MyPostActivity)context;
        this.gominCardViewObjects = resources;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doggomin_cardview_detail, viewGroup, false);
        return new HumanMyPostViewHolder(view);
    }

    public class HumanMyPostViewHolder extends RecyclerView.ViewHolder {

        public final ImageView gomin_main_Img;
        public final ImageView gomin_profile_Img;
        public final CustomTextView gomin_user_name;
        public final CustomTextView gomin_region;
        public final CustomTextView gomin_main_title;

        View view;

        public HumanMyPostViewHolder(View itemView) {
            super(itemView);
            view = (View) itemView.findViewById(R.id.cardView);
            gomin_main_title = (CustomTextView) itemView.findViewById(R.id.gomin_main_title);
            gomin_main_Img = (ImageView) itemView.findViewById(R.id.gomin_item_image);
            gomin_profile_Img = (ImageView) itemView.findViewById(R.id.gomin_profile_Img);
            gomin_user_name = (CustomTextView) itemView.findViewById(R.id.gomin_user_name);
            gomin_region = (CustomTextView) itemView.findViewById(R.id.gomin_region);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final GominCardViewObject gominCardViewObject = gominCardViewObjects.get(position);
        final HumanMyPostViewHolder humanMyPostViewHolder = (HumanMyPostViewHolder) holder;

        humanMyPostViewHolder.gomin_main_title.setText(gominCardViewObject.gomin_main_title);
        humanMyPostViewHolder.gomin_user_name.setText(gominCardViewObject.gomin_nickname);
        humanMyPostViewHolder.gomin_region.setText(gominCardViewObject.gomin_region);

        Glide.with(owner).load(gominCardViewObject.gomin_main_Img).into(humanMyPostViewHolder.gomin_main_Img);
        Glide.with(owner).load(gominCardViewObject.gomin_profile_Img).into(humanMyPostViewHolder.gomin_profile_Img);



        humanMyPostViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(owner, GominDetailActivity.class);

                intent.putExtra("gomin_id", String.valueOf(gominCardViewObject.gomin_id));


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                        (owner, humanMyPostViewHolder.gomin_main_Img, "transitionName");

                ActivityCompat.startActivity(owner, intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gominCardViewObjects.size();
    }

}


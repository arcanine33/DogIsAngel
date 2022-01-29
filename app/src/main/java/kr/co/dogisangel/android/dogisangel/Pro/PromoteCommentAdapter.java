package kr.co.dogisangel.android.dogisangel.Pro;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.CommentObject;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-20.
 */
public class PromoteCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CommentObject> commentObjects;

    private static final int PRO_COMMENT_VIEW = 100;
    private static final int HUMAN_COMMENT_VIEW = 500;

    ProPromoteDetailActivity promoteDetailActivity;


    public PromoteCommentAdapter(ProPromoteDetailActivity context, ArrayList<CommentObject> resources){
        this.commentObjects = resources;
        this.promoteDetailActivity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        switch (viewType){
            case PRO_COMMENT_VIEW:
                View pro_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_pro, viewGroup, false);
                return new ProCommentViewHolder(pro_view);
            case HUMAN_COMMENT_VIEW:
                View human_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_human, viewGroup, false);
                return new HumanCommentViewHolder(human_view);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {

        CommentObject commentObject = commentObjects.get(position);
        String switchFlag = commentObject.qualify.trim();

        //String switchFlag = MySingleton.sharedInstance().qualify;

        Log.e("확인", switchFlag);
        switch (switchFlag) {
            case "전문인":
                return PRO_COMMENT_VIEW;
            case "일반인":
                return HUMAN_COMMENT_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CommentObject commentObject = commentObjects.get(position);
        switch (getItemViewType(position)){

            case PRO_COMMENT_VIEW:
                final ProCommentViewHolder proCommentViewHolder = (ProCommentViewHolder) holder;

                proCommentViewHolder.pro_name.setText(commentObject.pro_user_name);
                proCommentViewHolder.comment_content.setText(commentObject.comment_content);
                proCommentViewHolder.register_date.setText(commentObject.register_date);


                Glide.with(promoteDetailActivity).load(commentObject.profile_img).into(proCommentViewHolder.pro_profile_img);
                Glide.with(promoteDetailActivity).load(commentObject.grade_img).into(proCommentViewHolder.grade_img);

                break;

            case HUMAN_COMMENT_VIEW:
                final HumanCommentViewHolder humanCommentViewHolder = (HumanCommentViewHolder) holder;
                humanCommentViewHolder.human_name.setText(commentObject.human_nickname);
                humanCommentViewHolder.comment_content.setText(commentObject.comment_content);
                humanCommentViewHolder.register_date.setText(commentObject.register_date);

                Glide.with(promoteDetailActivity).load(commentObject.profile_img).into(humanCommentViewHolder.human_profile_img);

                break;

        }

    }
    @Override
    public int getItemCount(){return commentObjects.size();}

    public class ProCommentViewHolder extends RecyclerView.ViewHolder {


        public final CircleImageView pro_profile_img;
        public final TextView pro_name;
        public final TextView register_date;
        public final TextView comment_content;
        public final ImageView grade_img;

        View view;

        public ProCommentViewHolder(View itemView) {
            super(itemView);

            view = (View)itemView.findViewById(R.id.comment_pro_view);
            pro_profile_img = (CircleImageView)itemView.findViewById(R.id.comment_pro_profile_img);
            pro_name = (TextView)itemView.findViewById(R.id.comment_pro_name);
            register_date = (TextView)itemView.findViewById(R.id.comment_pro_date);
            comment_content = (TextView)itemView.findViewById(R.id.comment_pro_content);
            grade_img = (ImageView)itemView.findViewById(R.id.comment_pro_grade_img);
        }
    }
    public class HumanCommentViewHolder extends RecyclerView.ViewHolder{

        public final CircleImageView human_profile_img;
        public final TextView human_name;
        public final TextView register_date;
        public final TextView comment_content;


        View view;

        public HumanCommentViewHolder(View itemView){
            super(itemView);

            view = (View)itemView.findViewById(R.id.comment_human_view);
            human_profile_img = (CircleImageView)itemView.findViewById(R.id.comment_human_profile_img);
            human_name = (TextView)itemView.findViewById(R.id.comment_human_name);
            register_date = (TextView)itemView.findViewById(R.id.comment_human_register_date);
            comment_content = (TextView)itemView.findViewById(R.id.comment_human_content);

        }
    }

}

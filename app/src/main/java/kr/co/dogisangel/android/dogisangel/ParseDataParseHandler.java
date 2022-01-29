package kr.co.dogisangel.android.dogisangel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.DrawerNavigation.MenuObject;
import kr.co.dogisangel.android.dogisangel.Edit.EditProObject;
import kr.co.dogisangel.android.dogisangel.Ranking.RankingObject;

/**
 * Created by ccei on 2016-08-07.
 */
public class ParseDataParseHandler {

    public static ArrayList<ProCardViewObject> getJSONProAllList(
            StringBuilder buf) {

        ArrayList<ProCardViewObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<ProCardViewObject>();
            jsonObject = new JSONObject(buf.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {

                ProCardViewObject entity = new ProCardViewObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.content_type = jData.getString("content_type");

                entity.id = jData.getInt("expert_id");
                entity.title = jData.getString("expert_title");
                entity.image = jData.getString("expert_main_Img");
                entity.register_date = jData.getString("expert_register_date");
                entity.dog_size = jData.getString("dog_size");
                entity.comment_count = jData.getInt("expert_comment_count");
                entity.star_count = jData.getInt("expert_star_count");
                entity.price = jData.getInt("discount_cost");
                entity.origin_price = jData.getInt("cost");


                entity.grade_img = jData.getString("grade_Img");
                entity.name = jData.getString("expert_username");
                entity.region = jData.getString("region");
                entity.profile_img = jData.getString("profile_Img");
                entity.member_id = jData.getInt("member_id");

                entity.recruit_state = jData.getString("content_state");

                entity.is_like = jData.getString("islike");


                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "개잘하는 전문인 찾기 JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ProDetailObject getJSONPromoteDetailAllList(StringBuilder buf) {

        ProDetailObject entity = new ProDetailObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;



            entity.pro_detail_content_type = jData.getString("content_type");
            entity.pro_detail_id = jData.getInt("expert_id");
            entity.pro_detail_title = jData.getString("expert_title");

            JSONArray picture = jData.getJSONArray("picture");
            int size = picture.length();
            for (int i = 0; i < size; i++) {
                entity.pro_detail_image.add(picture.getString(i));
            }


            entity.is_favorite = jData.getString("islike");
            entity.pro_detail_register_date = jData.getString("expert_register_date");
            entity.pro_detail_dog_size = jData.getString("dog_size");
            entity.pro_detail_comment_count = jData.getInt("expert_comment_count");
            entity.pro_detail_star_count = jData.getInt("expert_star_count");
            entity.pro_detail_price = jData.getInt("discount_cost");
            entity.pro_detail_origin_price = jData.getInt("cost");
            entity.pro_detail_train_type = jData.getString("train_type");
            entity.pro_detail_train_spot = jData.getString("train_spot");
            entity.pro_detail_content = jData.getString("expert_content");


            entity.pro_detail_grade_img = jData.getString("grade_Img");
            entity.pro_detail_name = jData.getString("expert_username");
            entity.pro_detail_region = jData.getString("region");
            entity.pro_detail_profile_img = jData.getString("profile_Img");
            entity.pro_detail_train_period = jData.getInt("train_period");
            entity.pro_detail_recruit_person = jData.getInt("recruit_person");
            entity.pro_detail_profile = jData.getString("profile");
            entity.pro_detail_license = jData.getString("license");

            entity.member_id = jData.getInt("member_id");
            Log.e("글쓴이의 멤버아디를 받아오련", ""+entity.member_id);

            //프로필들어가는게 안되요


            JSONArray commentNode = jData.getJSONArray("comment");
            ArrayList<CommentObject> commentObjectArrayList = getCommentObject(commentNode);
            entity.commentObjectArrayList = commentObjectArrayList;
            Log.e("코멘트 카운트", "" + commentObjectArrayList.size());


        } catch (JSONException je) {
            Log.e("RequestAllList", "훈련 상세정보 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

    public static ArrayList<GominCardViewObject> getJSONDogGominAllList(
            StringBuilder buf) {

        ArrayList<GominCardViewObject> dogGominAllList = null;
        JSONObject jsonObject = null;
        try {
            dogGominAllList = new ArrayList<GominCardViewObject>();
            jsonObject = new JSONObject(buf.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");


            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {

                GominCardViewObject entity = new GominCardViewObject();
                JSONObject jData = jsonArray.getJSONObject(i);

                entity.gomin_id = jData.getInt("gomin_id");
                entity.gomin_main_Img = jData.getString("gomin_main_Img");
                entity.gomin_main_title = jData.getString("gomin_title");

                entity.gomin_profile_Img = jData.getString("profile_Img");
                entity.gomin_nickname = jData.getString("gomin_nickname");
                entity.gomin_region = jData.getString("region");
                entity.member_id = jData.getInt("member_id");


                dogGominAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "고민나누기 JSON파싱 중 에러발생", je);
        }
        return dogGominAllList;
    }

    public static GominDetailObject getJSONGominDetailAllList(StringBuilder buf) {

        GominDetailObject entity = new GominDetailObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;

            entity.gomin_detail_id = jData.getInt("gomin_id");
            entity.gomin_detail_main_title = jData.getString("gomin_title");
            entity.gomin_detail_profile_Img = jData.getString("profile_Img");
            entity.gomin_detail_register_date = jData.getString("gomin_register_date");
            entity.gomin_detail_nickname = jData.getString("gomin_nickname");
            entity.gomin_detail_comment_count = jData.getInt("gomin_comment_count");
            entity.gomin_detail_region = jData.getString("region");
            entity.gomin_detail_content = jData.getString("gomin_content");

            entity.member_id = jData.getInt("member_id");

            JSONArray picture = jData.getJSONArray("picture");
            int size = picture.length();
            for (int i = 0; i < size; i++) {
                entity.gomin_detail_main_Img.add(picture.getString(i));

            }

            JSONArray commentNode = jData.getJSONArray("comment");
            ArrayList<CommentObject> commentObjectArrayList = getCommentObject(commentNode);
            entity.commentObjectArrayList = commentObjectArrayList;



        } catch (JSONException je) {
            Log.e("RequestAllList", "고민나누기 상세정보 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

    public static LoginObject getJSONLoginObjectAllList(String buf) {

        LoginObject entity = new LoginObject();
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(buf);
            entity.qualify = jsonObject.getString("qualfy");
            entity.message = jsonObject.getString("msg");

            MySingleton.sharedInstance().qualify = jsonObject.getString("qualfy");
            MySingleton.sharedInstance().message = jsonObject.getString("msg");
            MySingleton.sharedInstance().member_id = jsonObject.getInt("member_id");


        } catch (JSONException je) {
            Log.e("RequestAllList", "로그인 중 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

    public static ArrayList<CommentObject> getCommentObject(JSONArray commentNode) {


        ArrayList<CommentObject> commentList = null;

        try {
            commentList = new ArrayList<CommentObject>();

            int jsonObjSize = commentNode.length();
            for (int i = 0; i < jsonObjSize; i++) {

                CommentObject entity = new CommentObject();
                JSONObject jData = commentNode.getJSONObject(i);

                entity.comment_content = jData.getString("comment");
                entity.profile_img = jData.getString("profile_Img");
                entity.grade_img = jData.getString("grade_Img");
                entity.register_date = jData.getString("register_comment");
                entity.pro_user_name = jData.getString("username");
                entity.human_nickname = jData.getString("nickname");
                entity.qualify = jData.getString("qualfy");

                entity.member_id = jData.getInt("member_id");

                commentList.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentList;
    }

    public static ProfileObject getProfileObject(StringBuilder buf) {

        ProfileObject entity = new ProfileObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;

            entity.profile = jData.getString("profile");
            entity.license1 = jData.getString("license1");
            entity.license2 = jData.getString("license2");
            /*entity.license3 = jData.getString("license3");
            entity.license4 = jData.getString("license4");
            entity.license5 = jData.getString("license5");*/
            entity.user_name = jData.getString("username");
            entity.nickname = jData.getString("nickname");
            entity.license_text = jData.getString("license");

            entity.profile_img = jData.getString("profile_Img");
            entity.grade_img = jData.getString("grade_Img");

            JSONArray commentNode = jData.getJSONArray("comment");
            Log.e("코멘트 오브젝트는11? ", "" + jData.getJSONArray("comment"));
            Log.e("코멘트 오브젝트는22? ", "" + commentNode);
            ArrayList<CommentObject> commentObjectArrayList = getCommentObject(commentNode);
            Log.e("코멘트 오브젝트는33? ", "" + commentObjectArrayList);


            entity.commentObjectArrayList = commentObjectArrayList;
            Log.e("코멘트 엔티티에 잘들어갔니? ", "" + entity.commentObjectArrayList);
            Log.e("코멘트 오브젝트는? ", "" + commentObjectArrayList);

            entity.comment_count = jData.getInt("comment_count");


        } catch (JSONException je) {
            Log.e("RequestAllList", "MY PROFILE JSON파싱 중 에러발생", je);
        }

        return entity;
    }
    public static EditProObject getEditProObject(StringBuilder buf) {

        EditProObject entity = new EditProObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;

            entity.edit_title = jData.getString("expert_title");
            entity.edit_dog_size = jData.getString("dog_size");
            entity.edit_train_spot = jData.getString("train_spot");
            entity.edit_train_period = jData.getInt("train_period");
            entity.edit_price = jData.getInt("cost");
            entity.edit_content = jData.getString("expert_content");
            entity.edit_train_type = jData.getString("train_type");







            JSONArray picture = jData.getJSONArray("picture");
            int size = picture.length();
            for (int i = 0; i < size; i++) {
                entity.edit_picture.add(picture.getString(i));

            }

           // entity.train_type = jData.getString("picture");



        } catch (JSONException je) {
            Log.e("RequestAllList", "고민나누기 상세정보 JSON파싱 중 에러발생", je);
        }

        return entity;
    }
    public static MenuObject getMenuObject(StringBuilder buf) {

        MenuObject entity = new MenuObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;

            entity.header_img = jData.getString("profile_Img");
            Log.e("파싱은 받아오나", jData.getString("profile_Img"));
            Log.e("파싱은 받아오나", entity.header_img);
            entity.user_name = jData.getString("username");
            entity.nick_name = jData.getString("nickname");





        } catch (JSONException je) {
            Log.e("RequestAllList", "메뉴 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

    public static RankingObject getJSONRankingAllList(StringBuilder buf) {

        RankingObject entity = new RankingObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;



            entity.content = jData.getString("content");
            entity.comment = jData.getString("comment");
            entity.grade = jData.getString("grade");


           // entity.member_id = jData.getInt("member_id");
           // Log.e("글쓴이의 멤버아디를 받아오련", ""+entity.member_id);

            entity.rank = jData.getString("rank");


        } catch (JSONException je) {
            Log.e("RequestAllList", "랭킹정보 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

}



package kr.co.dogisangel.android.dogisangel;


  /*Created by ccei on 2016-08-12.*/


import org.json.JSONArray;

import java.util.ArrayList;

public class ProDetailObject {

    public int pro_detail_id;
    public String pro_detail_dog_size;
    public String pro_detail_title;
    public ArrayList<String> pro_detail_image = new ArrayList<>();
    public String is_favorite;
    public String pro_detail_register_date;
    public String pro_detail_content;
    public int pro_detail_comment_count;
    public int pro_detail_star_count;
    public int pro_detail_price;
    public int pro_detail_origin_price;
    public int pro_detail_train_period;
    public int pro_detail_recruit_person;
    public String pro_detail_train_type;
    public String pro_detail_train_spot;


    public String pro_detail_grade_img;
    public String pro_detail_profile_img;
    public String pro_detail_name;
    public String pro_detail_region;
    public String pro_detail_content_type;


    public String pro_detail_profile;
    public String pro_detail_license;




    public int member_id;

    public ArrayList<CommentObject> commentObjectArrayList;

    public ProDetailObject() {}

}

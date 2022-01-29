package kr.co.dogisangel.android.dogisangel;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-17.
 */
public class GominDetailObject {

    public int gomin_detail_id;
    public ArrayList<String> gomin_detail_main_Img = new ArrayList<>();
    public String gomin_detail_profile_Img;
    public String gomin_detail_main_title;
    public String gomin_detail_nickname;
    public String gomin_detail_region;
    public String gomin_detail_content;
    public int gomin_detail_comment_count;
    public String gomin_detail_register_date;

    public int member_id;

    public ArrayList<CommentObject> commentObjectArrayList;

    public GominDetailObject() {
    }
}

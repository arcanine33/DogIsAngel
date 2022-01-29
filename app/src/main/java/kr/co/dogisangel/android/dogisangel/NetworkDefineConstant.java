package kr.co.dogisangel.android.dogisangel;

/**
 * Created by ccei on 2016-08-07.
 */
public class NetworkDefineConstant {
    public static final String HOST_URL = "52.79.202.147";
    //웹서버 포트번호
    public static final int PORT_NUMBER = 3000;
    //저장관련 URL 주소

    public static final String FULL_URL = "http://" + HOST_URL + ":"+PORT_NUMBER + "/";

    public static final String MAIN_PRO = "http://" + HOST_URL + ":"+PORT_NUMBER + "/expert";
    public static final String MAIN_PRO_DETAIL = "http://" + HOST_URL + ":"+PORT_NUMBER + "/expert/spec/";
    public static final String Main_Gomin = "http://" + HOST_URL + ":" + PORT_NUMBER + "/gomin";
    public static final String Main_Gomin_DETAIL = "http://" + HOST_URL + ":" + PORT_NUMBER + "/gomin/spec/";
    public static final String RANKING = "http://" + HOST_URL + ":" + PORT_NUMBER + "/myGrade";



    public static final String SEARCH_PRO = "http://" + HOST_URL + ":"+PORT_NUMBER + "/expert";
    public static final String SEARCH_GOMIN = "http://" + HOST_URL + ":"+PORT_NUMBER + "/gomin";

}

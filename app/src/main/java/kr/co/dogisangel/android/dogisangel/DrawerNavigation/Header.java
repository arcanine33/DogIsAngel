package kr.co.dogisangel.android.dogisangel.DrawerNavigation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-25.
 */
public class Header extends Activity {


    static CircleImageView profile_img;
    static CustomTextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_header);

        profile_img = (CircleImageView)findViewById(R.id.header_image);
        user_name = (CustomTextView)findViewById(R.id.header_user_name);




    }
}

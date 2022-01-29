package kr.co.dogisangel.android.dogisangel;

import android.app.Application;
import android.content.Context;

/**
 * Created by ccei on 2016-08-07.
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate(){
        super.onCreate();
        mContext = this;
    }
    public static Context getMyApplicationContext(){
        return mContext;
    }
}

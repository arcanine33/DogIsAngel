package kr.co.dogisangel.android.dogisangel;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by ccei on 2016-08-19.
 */
public class MySingleton {
    private static MySingleton _instance = null;
    public static MySingleton sharedInstance() {
        if ( _instance == null ) {
            _instance = new MySingleton();
        }
        return _instance;
    }

    private MySingleton() {
        // For Persistent Cookie - https://github.com/franmontiel/PersistentCookieJar
        CookieHandler cookieHandler = new CookieManager();
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieHandler);
        httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).cookieJar(cookieJar).build();
    }
    public OkHttpClient httpClient;
    public String qualify = "게스트";
    public int member_id = 0;

    public String message;
}

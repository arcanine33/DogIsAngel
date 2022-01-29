package kr.co.dogisangel.android.dogisangel;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ccei on 2016-08-07.
 */
public class TypefaceManager {
    private static final TypefaceManager instance = new TypefaceManager();

    public static TypefaceManager getInstance(){
        return instance;
    }
    private TypefaceManager(){
    }

    public static final String FONT_NAME_NAUM = "NanumBarunGothic";
    private Typeface NanumBarunGothic;

    public Typeface getTypeface(Context context, String fontName){
        if(FONT_NAME_NAUM.equals(fontName)){

            if(NanumBarunGothic == null){
                NanumBarunGothic = Typeface.createFromAsset(context.getAssets(), "NanumBarunGothic-YetHangul.ttf");
            }
            return NanumBarunGothic;
        }
        return null;
    }
}

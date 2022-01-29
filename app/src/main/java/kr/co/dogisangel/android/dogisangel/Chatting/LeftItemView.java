package kr.co.dogisangel.android.dogisangel.Chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.dogisangel.android.dogisangel.R;


/**
 * Created by androidstudio on 2016-06-16.
 */
public class LeftItemView extends FrameLayout {
    public LeftItemView(Context context) {
        super(context);
        init();
    }

    ImageView iconView;
    TextView messageView;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.chatting_left, this);
        iconView = (ImageView) findViewById(R.id.header_image);

    }


}

package kr.co.dogisangel.android.dogisangel.Chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import kr.co.dogisangel.android.dogisangel.R;


/**
 * Created by androidstudio on 2016-06-16.
 */
public class RightItemView extends FrameLayout {

    TextView messageView;

    public RightItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.chatting_right, this);

    }
}

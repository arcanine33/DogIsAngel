package kr.co.dogisangel.android.dogisangel.Notify;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import kr.co.dogisangel.android.dogisangel.R;


/**
 * Created by dongja94 on 2016-01-18.
 */
public class ChildView extends FrameLayout {
    public ChildView(Context context) {
        super(context);
        init();
    }

    TextView nameView;
    ChildItem item;
    private void init() {
        inflate(getContext(), R.layout.notify_child, this);
        nameView = (TextView)findViewById(R.id.text_name);
    }

    public void setChildItem(ChildItem item) {
        this.item = item;
        nameView.setText(item.childName);
    }

}

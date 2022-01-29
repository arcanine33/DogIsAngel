package kr.co.dogisangel.android.dogisangel.DrawerNavigation;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.R;


/**
 * Created by dongja94 on 2016-01-18.
 */
public class ChildView extends FrameLayout {
    public ChildView(Context context) {
        super(context);
        init();
    }

    CustomTextView nameView;
    ChildItem item;
    private void init() {
        inflate(getContext(), R.layout.menu_child, this);
        nameView = (CustomTextView) findViewById(R.id.menu_child);
    }

    public void setChildItem(ChildItem item) {
        this.item = item;
        nameView.setText(item.childName);
    }

}

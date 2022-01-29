package kr.co.dogisangel.android.dogisangel.Customer_Service;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.dogisangel.android.dogisangel.CustomTextView;
import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by dongja94 on 2016-01-18.
 */
public class GroupView extends FrameLayout {
    public GroupView(Context context) {
        super(context);
        init();
    }

    CustomTextView nameView;
    GroupItem item;
    ImageView iconimage;
    private void init() {
        inflate(getContext(), R.layout.cs_fq_group, this);
        nameView = (CustomTextView) findViewById(R.id.customer_name);
        iconimage = (ImageView)findViewById(R.id.customer_image);
    }

    public void setGroupItem(GroupItem item) {
        this.item = item;
        nameView.setText(item.groupName);

    }

}

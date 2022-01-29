package kr.co.dogisangel.android.dogisangel.Membership;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-13.
 */
public class Membership_human_add extends FrameLayout implements ListAdapter {
    public Membership_human_add(Context context) {
        super(context);
        init();
    }

    EditText human_membership_edit;
    RadioButton human_membership_man,human_membership_woman;
    Spinner human_membership_age, human_membership_type;

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.membership_human_add, this);
        human_membership_edit = (EditText) view.findViewById(R.id.human_membership_edit);
        human_membership_man = (RadioButton) view.findViewById(R.id.human_membership_man);
        human_membership_woman = (RadioButton) view.findViewById(R.id.human_membership_woman);
        human_membership_age = (Spinner) view.findViewById(R.id.human_membership_age);
        human_membership_type = (Spinner) view.findViewById(R.id.human_membership_type);
    }


    //겟터/셋터
    public EditText getHuman_membership_edit() {
        return human_membership_edit;
    }

    public void setHuman_membership_edit(EditText human_membership_edit) {
        this.human_membership_edit = human_membership_edit;
    }

    public RadioButton getHuman_membership_man() {
        return human_membership_man;
    }

    public void setHuman_membership_man(RadioButton human_membership_man) {
        this.human_membership_man = human_membership_man;
    }

    public RadioButton getHuman_membership_woman() {
        return human_membership_woman;
    }

    public void setHuman_membership_woman(RadioButton human_membership_woman) {
        this.human_membership_woman = human_membership_woman;
    }

    public Spinner getHuman_membership_age() {
        return human_membership_age;
    }

    public void setHuman_membership_age(Spinner human_membership_age) {
        this.human_membership_age = human_membership_age;
    }

    public Spinner getHuman_membership_type() {
        return human_membership_type;
    }

    public void setHuman_membership_type(Spinner human_membership_type) {
        this.human_membership_type = human_membership_type;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
//    TalkData2 mData2;
//    public void setData2(TalkData2 talkData2) {
//        mData2 = talkData2;
//        iconViewpage.setImageResource(mData2.iconId2);
//        messageViewpage.setText(mData2.message2);
//    }



}

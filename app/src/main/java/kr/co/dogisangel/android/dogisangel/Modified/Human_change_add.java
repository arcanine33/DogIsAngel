package kr.co.dogisangel.android.dogisangel.Modified;

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
public class Human_change_add extends FrameLayout implements ListAdapter {
    public Human_change_add(Context context) {
        super(context);
        init();
    }

    EditText human_add_edit;
    RadioButton human_add_man,human_add_woman;
    Spinner human_add_age,human_add_type;

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.human_change_add, this);
        human_add_edit = (EditText) view.findViewById(R.id.human_add_edit);
        human_add_man = (RadioButton) view.findViewById(R.id.human_add_man);
        human_add_woman = (RadioButton) view.findViewById(R.id.human_add_woman);
        human_add_age = (Spinner) view.findViewById(R.id.human_add_age);
        human_add_type = (Spinner) view.findViewById(R.id.human_add_type);
    }


    //겟터/셋터
    public EditText getHuman_add_edit() {
        return human_add_edit;
    }

    public void setHuman_add_edit(EditText human_add_edit) {
        this.human_add_edit = human_add_edit;
    }

    public RadioButton getHuman_add_man() {
        return human_add_man;
    }

    public void setHuman_add_man(RadioButton human_add_man) {
        this.human_add_man = human_add_man;
    }

    public RadioButton getHuman_add_woman() {
        return human_add_woman;
    }

    public void setHuman_add_woman(RadioButton human_add_woman) {
        this.human_add_woman = human_add_woman;
    }

    public Spinner getHuman_add_age() {
        return human_add_age;
    }

    public void setHuman_add_age(Spinner human_add_age) {
        this.human_add_age = human_add_age;
    }

    public Spinner getHuman_add_type() {
        return human_add_type;
    }

    public void setHuman_add_type(Spinner human_add_type) {
        this.human_add_type = human_add_type;
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
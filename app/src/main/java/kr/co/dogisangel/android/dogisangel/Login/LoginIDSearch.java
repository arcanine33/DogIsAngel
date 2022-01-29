package kr.co.dogisangel.android.dogisangel.Login;




import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.app.DialogFragment;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by androidstudio on 2016-08-16.
 */
public class LoginIDSearch extends Fragment {

    int year, month, day, hour, minute;
    private TextView tv;
    static LoginSearchMain owner1;

    public LoginIDSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.login_id_search, container, false);

        owner1 = (LoginSearchMain)getActivity();
        // Inflate the layout for this fragment

        ImageButton btn = (ImageButton) v.findViewById(R.id.id_selected);
        tv = (TextView)v.findViewById(R.id.year_contect);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        try {
                            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            tv.setText(String.format("%d          %d           %d", year,monthOfYear+1, dayOfMonth));


                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                datePickerDialog.show();

            }
        });

        ImageButton id_password_Btn = (ImageButton)v.findViewById(R.id.id_search_confirm);
        id_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), ID_search_confirm.class);
                startActivity(intent);

            }
        });
        return v;
    }


}
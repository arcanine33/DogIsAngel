package kr.co.dogisangel.android.dogisangel.Customer_Service;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inquire_Fragment extends Fragment {
    Button custombtn;
    Intent i;

    Uri uri;

    public static Inquire_Fragment newInstance(int initValue) {
        Inquire_Fragment Inquire_Fragment = new Inquire_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        Inquire_Fragment.setArguments(bundle);

        return Inquire_Fragment;
    }

    public Inquire_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.cs_inquire, container, false);
        custombtn = (Button) view.findViewById(R.id.custom_button);


        custombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri = Uri.parse("mailto:hjsert@naver.com"); //이메일과 관련된 Data는 'mailto:'으로 시작. 이후는 이메일 주소

                i = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 Dial Activity의 action값

                startActivity(i);//액티비티 실행

                //GenyMotion 무료버전에서는 실행오류. 실제 디바이스에서는 동작됩니다.



            }
        });
        return view;
    }



}




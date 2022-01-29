package kr.co.dogisangel.android.dogisangel.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import kr.co.dogisangel.android.dogisangel.R;

public class LoginSearchMain extends AppCompatActivity implements View.OnClickListener{
        FrameLayout framelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__start);

        Button firstBtn = (Button) findViewById(R.id.first_btn);
        Button secondBtn = (Button) findViewById(R.id.second_btn);

        firstBtn.setOnClickListener(this);
        secondBtn.setOnClickListener(this);

        framelayout = (FrameLayout) findViewById(R.id.login_frame_layout);
        Fragment fragment = null;
        fragment = new LoginIDSearch();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.login_frame_layout, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v){
        Fragment fragment = null;
        switch (v.getId()){
            case R.id.first_btn:
                fragment = new LoginIDSearch();
                break;
            case R.id.second_btn:
                fragment = new LoginPWSearch();
                break;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.login_frame_layout, fragment);
        transaction.commit();
    }
}

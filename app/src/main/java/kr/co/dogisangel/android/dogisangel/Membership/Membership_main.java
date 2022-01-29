package kr.co.dogisangel.android.dogisangel.Membership;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-14.
 */
public class Membership_main extends AppCompatActivity {
    int a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_main);

        ImageButton membership_pro = (ImageButton) findViewById(R.id.membership_pro_Btn);
        membership_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1;
                intent1 = new Intent(Membership_main.this,ProStartActivity.class);
                startActivity(intent1);

            }
        });

        ImageButton membership_human = (ImageButton) findViewById(R.id.membership_human_Btn);
        membership_human.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(Membership_main.this,HumanStartActivity.class);
                startActivity(intent);

            }
        });
    }


}
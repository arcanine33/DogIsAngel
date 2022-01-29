package kr.co.dogisangel.android.dogisangel.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import kr.co.dogisangel.android.dogisangel.R;

public class Password_search_confirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_password_confirm);

        ImageButton id_password_Btn = (ImageButton)findViewById(R.id.password_search_restart);
        id_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Password_search_confirm.this, Login.class);
                startActivity(intent);

            }
        });
    }
}

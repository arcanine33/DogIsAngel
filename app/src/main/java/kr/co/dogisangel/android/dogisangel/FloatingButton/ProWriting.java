package kr.co.dogisangel.android.dogisangel.FloatingButton;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import kr.co.dogisangel.android.dogisangel.R;


public class ProWriting extends AppCompatActivity {


    Spinner spinnerColor;
    private static final String[] color = {
            "훈련 홍보글","공동 훈련 모집글" };
    private ArrayAdapter<String> adapter;

    //    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_pro);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, color);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerColor = (Spinner) findViewById(R.id.writing_pro_spinner);
        spinnerColor.setAdapter(adapter);
        spinnerColor.setSelection(0);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.prowriting_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        spinnerColor.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Fragment fragment = null;

                switch (spinnerColor.getSelectedItem().toString()) {
                    case "훈련 홍보글":
                        fragment = new TrainingPromote();
                        break;
                    case "공동 훈련 모집글":
                        fragment = new JointTrainingPromote();
                        break;
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentView, fragment);
                transaction.commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

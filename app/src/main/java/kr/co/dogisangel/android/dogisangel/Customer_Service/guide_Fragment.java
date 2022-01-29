package kr.co.dogisangel.android.dogisangel.Customer_Service;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class guide_Fragment extends Fragment {

    public static guide_Fragment newInstance(int initValue) {
        guide_Fragment guide_Fragment = new guide_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        guide_Fragment.setArguments(bundle);

        return guide_Fragment;
    }
    public guide_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cs_agreement_of_utilization, container, false);
    }

}

package kr.co.dogisangel.android.dogisangel.DogGomin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * Created by ccei on 2016-08-11.
 */
public class GominDetailMainImgFragment extends Fragment {

    public GominDetailMainImgFragment() {
        // Required empty public constructor
    }

    public static GominDetailMainImgFragment newInstance(String message) {
        Bundle b = new Bundle();
        b.putString("message", message);

        GominDetailMainImgFragment f = new GominDetailMainImgFragment();
        f.setArguments(b);

        return f;
    }



    String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString("message");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doggomin_item_detail, container, false);


        return view;
    }
}

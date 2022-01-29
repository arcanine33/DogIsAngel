package kr.co.dogisangel.android.dogisangel.Customer_Service;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import kr.co.dogisangel.android.dogisangel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Question_Fragment extends Fragment {

    ExpandableListView listView;
    CustomerAdapter CustomerAdapter;

    public static Question_Fragment newInstance(int initValue) {
        Question_Fragment Question_Fragment = new Question_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        Question_Fragment.setArguments(bundle);

        return Question_Fragment;
    }
    public Question_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.cs_question_, container, false);
        listView = (ExpandableListView)view.findViewById(R.id.customer_expandable);

        CustomerAdapter = new  CustomerAdapter();
        listView.setAdapter(CustomerAdapter);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

       /* Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        listView.setIndicatorBounds(width - getDipsFromPixel(15), width - getDipsFromPixel(14));*/

        initData();
        return view;

    }

    private void initData() {
        CustomerAdapter.put("회원가입시 전문가 신청후 언제쯤 승인이 되나요?", "2일정도 걸립니다! 가입해 주셔서 감사합니다.");
        CustomerAdapter.put("자기 등업 상태는 어떻게 볼 수 있나요?", "메뉴에 있는 내 등급 보기를 클릭하시면 현재의 등급을 확인할 수 있습니다. ");
        CustomerAdapter.put("등업은 어떻게 하는 건가요?"," 등업은 글을 작성하거나 댓글을 달면 자동으로 등급이 올라갑니다.");
        CustomerAdapter.put("등업 기준이 어떻게 되나요.","5등급 가입시 주어지는 기본 단계, 4등급 댓글 5개 글 2개, 3등급 댓글 12개 글 5개, 2등급 댓글 20개 글 10개, 1등급 댓글 30개 글 15개 작성 하셔야 합니다.");
        CustomerAdapter.put("글과 수정과 삭제는 어떻게 하나요.", "본인이 쓴글의 상세페이지 상단에 메뉴를 누르면 삭제와 수정이 가능합니다.");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
package kr.co.dogisangel.android.dogisangel.Search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.ProCardViewObject;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-09.
 */
public class SearchDialogFragment extends DialogFragment {
    private static final String BACKGROUND_IMG = "background_img";
    int a = 0;

    private String basictraining, pottytraining, playtraining, sociability, barkingdog, eatdung, competition, guidance, bitingdog;
    String train_keyword ;


    private Spinner region;

    SearchExpertEntityObject expertObj;
    SearchHumanEntityObject humanObj;

    public interface OnSelectLocationListener {
        void onProSelectLocation(String location);

        void onHumanSelectLocation(String location);
    }

    OnSelectLocationListener selectListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectLocationListener) {
            selectListener = (OnSelectLocationListener) context;
        }
    }


    public SearchDialogFragment() {
    }

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment fragment = new SearchDialogFragment();
        /*Bundle bundle = new Bundle();
        bundle.putParcelable(BACKGROUND_IMG, bitmap);
        fragment.setArguments(bundle);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*if(getArguments() != null) {
            backgroundImg = getArguments().getParcelable(BACKGROUND_IMG);
        }*/
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_gomin_filter, container, false);
        RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.radio_search1);
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.radio_search2);


        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog));


        region = (Spinner) view.findViewById(R.id.search_spinner);
        //getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog));


        final EditText keywordedit = (EditText) view.findViewById(R.id.search_edit);

        final TextView search_pro_hint = (TextView) view.findViewById(R.id.search_pro_hint);
        final TextView  search_human_hint = (TextView) view.findViewById(R.id. search_human_hint);


        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_search);

        final LinearLayout pro_1 = (LinearLayout) view.findViewById(R.id.pro_1);
        final LinearLayout gomin_1 = (LinearLayout) view.findViewById(R.id.gomin_1);


//      //??????????????? = 0, ??????????????? 1
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a = 0;
                expertObj = new SearchExpertEntityObject();
                humanObj = null;
                pro_1.setVisibility(View.VISIBLE);
                gomin_1.setVisibility(View.GONE);
                search_pro_hint.setVisibility(View.VISIBLE);
                search_human_hint.setVisibility(View.GONE);


            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = 1;

                humanObj = new SearchHumanEntityObject();
                expertObj = null;
                a = 1;
                pro_1.setVisibility(View.GONE);
                gomin_1.setVisibility(View.VISIBLE);
                search_pro_hint.setVisibility(View.GONE);
                search_human_hint.setVisibility(View.VISIBLE);

            }
        });





        final ToggleButton search_03_basictraining = (ToggleButton) view.findViewById(R.id.search_03_basictraining);
        final ToggleButton search_03_pottytraining = (ToggleButton) view.findViewById(R.id.search_03_pottytraining);
        final ToggleButton search_03_playtraining = (ToggleButton) view.findViewById(R.id.search_03_playtraining);
        final ToggleButton search_03_sociability = (ToggleButton) view.findViewById(R.id.search_03_sociability);
        final ToggleButton search_03_bitingdog = (ToggleButton) view.findViewById(R.id.search_03_bitingdog);
        final ToggleButton search_03_barkingdog = (ToggleButton) view.findViewById(R.id.search_03_barkingdog);
        final ToggleButton search_03_eatdung = (ToggleButton) view.findViewById(R.id.search_03_eatdung);
        final ToggleButton search_03_competition = (ToggleButton) view.findViewById(R.id.search_03_competition);
        final ToggleButton search_03_guidance = (ToggleButton) view.findViewById(R.id.search_03_guidance);


        search_03_basictraining.setText(null);
        search_03_basictraining.setTextOn(null);
        search_03_basictraining.setTextOff(null);
//
//
        final int image[] = {
                R.drawable.search_03_basictraining,
                R.drawable.search_03_basictraining_tap
        };
        search_03_basictraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                // expertObj = new SearchExpertEntityObject();


                if (isChecked) {
                    search_03_basictraining.setBackgroundResource(image[1]);
                    //      expertObj.basicTrainning = "????????????";

                } else {
                    search_03_basictraining.setBackgroundResource(image[0]);
                }
            }
        });




        search_03_pottytraining.setText(null);
        search_03_pottytraining.setTextOn(null);
        search_03_pottytraining.setTextOff(null);

        final int image1[] = {
                R.drawable.search_03_pottytraining,
                R.drawable.search_03_pottytraining_tap
        };


        search_03_pottytraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_pottytraining.isChecked()) {
                    search_03_pottytraining.setBackgroundResource(image1[1]);
                    //     expertObj.search_03_pottytraining= "????????????";

                } else {
                    search_03_pottytraining.setBackgroundResource(image1[0]);
                }
            }
        });


        search_03_playtraining.setText(null);
        search_03_playtraining.setTextOn(null);
        search_03_playtraining.setTextOff(null);

        final int image2[] = {
                R.drawable.search_03_playtraining,
                R.drawable.search_03_playtraining_tap
        };


        search_03_playtraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_playtraining.isChecked()) {
                    search_03_playtraining.setBackgroundResource(image2[1]);
                    //    expertObj.search_03_playtraining= "????????????";

                } else {
                    search_03_playtraining.setBackgroundResource(image2[0]);
                }
            }
        });

        search_03_sociability.setText(null);
        search_03_sociability.setTextOn(null);
        search_03_sociability.setTextOff(null);

        final int image3[] = {
                R.drawable.search_03_sociability,
                R.drawable.search_03_sociability_tap
        };


        search_03_sociability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_sociability.isChecked()) {
                    search_03_sociability.setBackgroundResource(image3[1]);
                    //   expertObj.search_03_sociability= "?????????";

                } else {
                    search_03_sociability.setBackgroundResource(image3[0]);
                }
            }
        });

        search_03_bitingdog.setText(null);
        search_03_bitingdog.setTextOn(null);
        search_03_bitingdog.setTextOff(null);

        final int image4[] = {
                R.drawable.search_03_bitingdog,
                R.drawable.search_03_bitingdog_tap
        };


        search_03_bitingdog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_bitingdog.isChecked()) {
                    search_03_bitingdog.setBackgroundResource(image4[1]);
                    //  expertObj.search_03_bitingdog= "?????? ??? ??????";

                } else {
                    search_03_bitingdog.setBackgroundResource(image4[0]);
                }
            }
        });

        search_03_barkingdog.setText(null);
        search_03_barkingdog.setTextOn(null);
        search_03_barkingdog.setTextOff(null);

        final int image5[] = {
                R.drawable.search_03_barkingdog,
                R.drawable.search_03_barkingdog_tap
        };

        search_03_barkingdog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_barkingdog.isChecked()) {
                    search_03_barkingdog.setBackgroundResource(image5[1]);
                    //  expertObj.earch_03_barkingdog= "????????? ??????";
                } else {
                    search_03_barkingdog.setBackgroundResource(image5[0]);
                }
            }
        });

        search_03_eatdung.setText(null);
        search_03_eatdung.setTextOn(null);
        search_03_eatdung.setTextOff(null);

        final int image6[] = {
                R.drawable.search_03_eatdung,
                R.drawable.search_03_eatdung_tap
        };


        search_03_eatdung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_eatdung.isChecked()) {
                    search_03_eatdung.setBackgroundResource(image6[1]);
                    //  expertObj.search_03_eatdung= "?????????";
                } else {
                    search_03_eatdung.setBackgroundResource(image6[0]);
                }
            }
        });


        search_03_competition.setText(null);
        search_03_competition.setTextOn(null);
        search_03_competition.setTextOff(null);

        final int image7[] = {
                R.drawable.search_03_competition,
                R.drawable.search_03_competition_tap
        };


        search_03_competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_competition.isChecked()) {
                    search_03_competition.setBackgroundResource(image7[1]);
                    //   expertObj.search_03_competition= "?????? ??????";
                } else {
                    search_03_competition.setBackgroundResource(image7[0]);
                }
            }
        });


        search_03_guidance.setText(null);
        search_03_guidance.setTextOn(null);
        search_03_guidance.setTextOff(null);

        final int image8[] = {
                R.drawable.search_03_guidance,
                R.drawable.search_03_guidance_tap
        };

        search_03_guidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_guidance.isChecked()) {
                    search_03_guidance.setBackgroundResource(image8[1]);
                    //  expertObj.search_03_guidance= "?????????";
                } else {
                    search_03_guidance.setBackgroundResource(image8[0]);
                }
            }
        });


///??????

        final ToggleButton earch_04_eatdung = (ToggleButton) view.findViewById(R.id.earch_04_eatdung);
        final ToggleButton search_04_attack = (ToggleButton) view.findViewById(R.id.search_04_attack);
        final ToggleButton search_04_barkinghabit = (ToggleButton) view.findViewById(R.id.search_04_barkinghabit);
        final ToggleButton search_04_bitinghabit = (ToggleButton) view.findViewById(R.id.search_04_bitinghabit);
        final ToggleButton search_04_dungproblems = (ToggleButton) view.findViewById(R.id.search_04_dungproblems);
        final ToggleButton search_04_sociability = (ToggleButton) view.findViewById(R.id.search_04_sociability);


        earch_04_eatdung.setText(null);
        earch_04_eatdung.setTextOn(null);
        earch_04_eatdung.setTextOff(null);

        final int image_0[] = {
                R.drawable.earch_04_eatdung,
                R.drawable.earch_04_eatdung_tap,
        };
        earch_04_eatdung.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // humanObj = new SearchHumanEntityObject();
                if (earch_04_eatdung.isChecked()) {

                    earch_04_eatdung.setBackgroundResource(image_0[1]);
                    // humanObj.earch_04_eatdung = "?????????";


                } else {
                    earch_04_eatdung.setBackgroundResource(image_0[0]);

                }
            }
        });


        search_04_attack.setText(null);
        search_04_attack.setTextOn(null);
        search_04_attack.setTextOff(null);

        final int image_1[] = {
                R.drawable.search_04_attack,
                R.drawable.search_04_attack_tap
        };


        search_04_attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_04_attack.isChecked()) {
                    search_04_attack.setBackgroundResource(image_1[1]);
                    //  humanObj.search_04_attack = "?????????";

                } else {
                    search_04_attack.setBackgroundResource(image_1[0]);


                }
            }
        });

        search_04_barkinghabit.setText(null);
        search_04_barkinghabit.setTextOn(null);
        search_04_barkinghabit.setTextOff(null);

        final int image_2[] = {
                R.drawable.search_04_barkinghabit,
                R.drawable.search_04_barkinghabit_tap,
        };
        search_04_barkinghabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_04_barkinghabit.isChecked()) {
                    search_04_barkinghabit.setBackgroundResource(image_2[1]);
                    //humanObj.search_04_barkinghabit = "????????????";


                } else {
                    search_04_barkinghabit.setBackgroundResource(image_2[0]);

                }
            }
        });


        search_04_bitinghabit.setText(null);
        search_04_bitinghabit.setTextOn(null);
        search_04_bitinghabit.setTextOff(null);

        final int image_3[] = {
                R.drawable.search_04_bitinghabit,
                R.drawable.search_04_bitinghabit_tap,
        };
        search_04_bitinghabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_04_bitinghabit.isChecked()) {
                    search_04_bitinghabit.setBackgroundResource(image_3[1]);
                    //  humanObj. search_04_bitinghabit = "????????????";


                } else {
                    search_04_bitinghabit.setBackgroundResource(image_3[0]);

                }
            }
        });


        search_04_dungproblems.setText(null);
        search_04_dungproblems.setTextOn(null);
        search_04_dungproblems.setTextOff(null);

        final int image_4[] = {
                R.drawable.search_04_dungproblems,
                R.drawable.search_04_dungproblems_tap,
        };
        search_04_dungproblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_04_dungproblems.isChecked()) {
                    search_04_dungproblems.setBackgroundResource(image_4[1]);
                    // humanObj.search_04_dungproblems = "????????????";


                } else {
                    search_04_dungproblems.setBackgroundResource(image_4[0]);

                }
            }
        });


        search_04_sociability.setText(null);
        search_04_sociability.setTextOn(null);
        search_04_sociability.setTextOff(null);

        final int image_5[] = {
                R.drawable.search_04_sociability,
                R.drawable.search_04_sociability_tap,
        };
        search_04_sociability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_04_sociability.isChecked()) {
                    search_04_sociability.setBackgroundResource(image_5[1]);
                    //humanObj.search_04_sociability = "?????????";


                } else {
                    search_04_sociability.setBackgroundResource(image_5[0]);

                }
            }
        });


        final ImageButton search_clear = (ImageButton) view.findViewById(R.id.search_start);

        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    if (upLoadfiles != null && upLoadfiles.size() > 0) {
                search_clear.setEnabled(false);


                if (a == 0) {
                    dismiss();
                    String keyword = keywordedit.getText().toString().trim();

                    //  String search_type =expertObj.toString().trim();
                    String  region1= region.getSelectedItem().toString().trim();

                    String train_keyword = "";

                    if (search_03_basictraining.isChecked()) {
                        basictraining = "????????????";
                        train_keyword += basictraining;
                    }
                    if (search_03_pottytraining.isChecked()) {
                        pottytraining = "????????????";
                        train_keyword += ","+pottytraining;
                    }
                    if (search_03_barkingdog.isChecked()) {
                        barkingdog = "?????? ??? ??????";
                        train_keyword += ","+barkingdog;
                    }
                    if (search_03_bitingdog.isChecked()) {
                        bitingdog = "?????? ??? ??????";
                        train_keyword += ","+bitingdog;
                    }
                    if (search_03_playtraining.isChecked()) {
                        playtraining = "????????????";
                        train_keyword += ","+playtraining;
                    }
                    if (search_03_sociability.isChecked()) {
                        sociability = "?????????";
                        train_keyword+= ","+sociability;
                    }
                    if (search_03_eatdung.isChecked()) {
                        eatdung = "?????????";
                        train_keyword+= ","+eatdung;
                    }
                    if (search_03_competition.isChecked()) {
                        competition = "?????? ??????";
                        train_keyword+= ","+competition;
                    }
                    if (search_03_guidance.isChecked()) {
                        guidance = "????????? ";
                        train_keyword+= ","+guidance;
                    }

//                    new FileUpLoadAsyncTask(expertObj, keyword
//                            , train_keyword).execute();



                    selectListener.onProSelectLocation(keywordedit.getText().toString());
                    Intent intent = new Intent(getActivity(), SearchProActivity.class);
                    intent.putExtra("keyword", keyword);
                    intent.putExtra("train_keyword",train_keyword);
                    intent.putExtra("region",region1);

                    startActivity(intent);


                } else {
                    dismiss();
                    String keyword = keywordedit.getText().toString().trim();

                    //  String search_type =expertObj.toString().trim();

                    String  region1= region.getSelectedItem().toString().trim();
                    String train_keyword = "";


                    if (earch_04_eatdung.isChecked()) {
                        basictraining = "?????????";
                        train_keyword+=basictraining;
                    }
                    if (search_04_attack.isChecked()) {
                        pottytraining = "?????????";
                        train_keyword += ","+pottytraining;
                    }
                    if (search_04_barkinghabit.isChecked()) {
                        barkingdog = "????????????";
                        train_keyword += ","+barkingdog;
                    }
                    if (search_04_bitinghabit.isChecked()) {
                        bitingdog = "????????????";
                        train_keyword += ","+bitingdog;
                    }
                    if (search_04_dungproblems.isChecked()) {
                        playtraining = "????????????";
                        train_keyword += ","+playtraining;
                    }
                    if (search_04_sociability.isChecked()) {
                        sociability = "?????????";
                        train_keyword += ","+sociability;
                    }

//                    new FileUpLoadAsyncTask(humanObj, keyword
//                            , train_keyword).execute();


                    selectListener.onHumanSelectLocation(keywordedit.getText().toString());
                    Intent intent = new Intent(getActivity(), SearchHumanActivity.class);
                    intent.putExtra("keyword", keyword);
                    intent.putExtra("train_keyword",train_keyword);
                    intent.putExtra("region",region1);
//                    intent.putExtra("1", humanObj.toString());

                    startActivity(intent);
                }

            }
        });

        return view;
    }



    private class FileUpLoadAsyncTask extends AsyncTask<ArrayList<ProCardViewObject>, Void, String> {
        //???????????? Mime Type ??????

        //????????????
        private String search_type; //????????? ??????,Radio
        private ArrayList<String> train_keyword;//?????? ??????
        private String keyword;//Edit??????

        private String region;


        public FileUpLoadAsyncTask(String search_type, String keyword, ArrayList<String> train_keyword,String region) {

            this.search_type = search_type;
            this.train_keyword = train_keyword;
            this.keyword = keyword;

            this.region =  region;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(ArrayList<ProCardViewObject>... arrayLists) {
            Response response = null;
            try {
                //???????????? ?????? ??? ??????????????? ????????? ??????.
                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                //?????? ??????
                Request request = new Request.Builder()
                        .url("http://52.78.102.112:3000/expert/0?type=" + search_type + "&train_keyword=" + train_keyword + "&keyword=" + region+"&region=")
                        .get() //????????? post???
                        .build();
                //?????? ??????
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //?????? ?????? 200??????
                int responseCode = response.code();
                if (flag) {
                    e("response??????", responseCode + "---" + response.message()); //????????? ?????? ?????????(OK)
                    e("response????????????", response.body().string()); //json?????? ??????
                    return "success";
                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "fail";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
        wlp.copyFrom(window.getAttributes());
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
    }

}
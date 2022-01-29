package kr.co.dogisangel.android.dogisangel.Edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.ParseDataParseHandler;
import kr.co.dogisangel.android.dogisangel.Pro.ProFragment;
import kr.co.dogisangel.android.dogisangel.Pro.ProPromoteDetailActivity;
import kr.co.dogisangel.android.dogisangel.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.MAIN_PRO;


public class EditPromote extends Activity {

    String post_id;

    static EditText edit_promote_title, edit_promote_period, edit_promote_price, edit_promote_content;
    static RadioGroup edit_dogsize_radio_group, edit_train_spot_radio_group;
    static RadioButton edit_dogsize_small, edit_dogsize_big, edit_spot_enter, edit_spot_visit;

    static ToggleButton edit_promote_basic, edit_promote_pottytraining, edit_promote_sociability, edit_promote_barkingdog, edit_promote_eatdung, edit_promote_competition, edit_promote_guidance, edit_promote_bitingdog, edit_promote_playtraining;
    ImageView mPhotoImageView;

    static ImageView edit_picture1, edit_picture2, edit_picture3, edit_picture4, edit_picture5;
    static ImageButton edit_Btn;

    String dog_size;
    String train_spot;

    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private UpLoadValueObject image1;
    private UpLoadValueObject image2;
    private UpLoadValueObject image3;
    private UpLoadValueObject image4;
    private UpLoadValueObject image5;

    private static final int IMAGE1 = 1;
    private static final int IMAGE2 = 2;
    private static final int IMAGE3 = 3;
    private static final int IMAGE4 = 4;
    private static final int IMAGE5 = 5;

    private int imageNumber;

    int edit_image1;
    int edit_image2;
    int edit_image3;
    int edit_image4;
    int edit_image5;

    int existing_image1=0;
    int existing_image2=0;
    int existing_image3=0;
    int existing_image4=0;
    int existing_image5=0;


    int new_image1 = 5;
    int new_image2 = 7;
    int new_image3 = 9;
    int new_image4 = 11;
    int new_image5 = 13;

    private ArrayList<String> toggleArrayList = new ArrayList<>();

    private String basictraining, pottytraining, playtraining, sociability, barkingdog, eatdung, competition, guidance, bitingdog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpromote);

        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");

        edit_promote_title = (EditText) findViewById(R.id.edit_promote_title);
        edit_dogsize_radio_group = (RadioGroup) findViewById(R.id.edit_promote_dog_size_radio_group);

        edit_dogsize_small = (RadioButton) findViewById(R.id.edit_promote_small_size);
        edit_dogsize_big = (RadioButton) findViewById(R.id.edit_promote_big_size);

        edit_train_spot_radio_group = (RadioGroup) findViewById(R.id.training_spot_radio_group);
        edit_spot_enter = (RadioButton) findViewById(R.id.edit_initiatory_training);
        edit_spot_visit = (RadioButton) findViewById(R.id.edit_visit_training);

        edit_promote_period = (EditText) findViewById(R.id.edit_training_month);
        edit_promote_price = (EditText) findViewById(R.id.edit_training_price);
        edit_promote_content = (EditText) findViewById(R.id.edit_training_promote_content);


        edit_picture1 = (ImageView) findViewById(R.id.edit_pro_picturepluse1);
        edit_picture2 = (ImageView) findViewById(R.id.edit_pro_picturepluse2);
        edit_picture3 = (ImageView) findViewById(R.id.edit_pro_picturepluse3);
        edit_picture4 = (ImageView) findViewById(R.id.edit_pro_picturepluse4);
        edit_picture5 = (ImageView) findViewById(R.id.edit_pro_picturepluse5);
        edit_Btn = (ImageButton) findViewById(R.id.edit_promote_file_upload_btn);

        edit_picture1.setOnClickListener(new PhotoClickListener(IMAGE1));
        edit_picture2.setOnClickListener(new PhotoClickListener(IMAGE2));
        edit_picture3.setOnClickListener(new PhotoClickListener(IMAGE3));
        edit_picture4.setOnClickListener(new PhotoClickListener(IMAGE4));
        edit_picture5.setOnClickListener(new PhotoClickListener(IMAGE5));

        edit_promote_basic = (ToggleButton) findViewById(R.id.edit_promote_basictraining);
        edit_promote_pottytraining = (ToggleButton) findViewById(R.id.edit_promote_pottytraining);
        edit_promote_playtraining = (ToggleButton) findViewById(R.id.edit_promote_playtraining);
        edit_promote_sociability = (ToggleButton) findViewById(R.id.edit_promote_sociability);
        edit_promote_bitingdog = (ToggleButton) findViewById(R.id.edit_promote_bitingdog);
        edit_promote_barkingdog = (ToggleButton) findViewById(R.id.edit_promote_barkingdog);
        edit_promote_eatdung = (ToggleButton) findViewById(R.id.edit_promote_eatdung);
        edit_promote_competition = (ToggleButton) findViewById(R.id.edit_promote_competition);
        edit_promote_guidance = (ToggleButton) findViewById(R.id.edit_promote_guidance);


        edit_dogsize_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.edit_promote_small_size:
                        dog_size = "소형견";
                        Toast.makeText(EditPromote.this, "소형견을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit_promote_big_size:
                        dog_size = "중*대형견";
                        Toast.makeText(EditPromote.this, "중*대형견을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        edit_train_spot_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.edit_initiatory_training:
                        train_spot = "입소훈련";
                        Toast.makeText(EditPromote.this, "입소훈련을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit_visit_training:
                        train_spot = "방문훈련";
                        Toast.makeText(EditPromote.this, "방문훈련을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        edit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_promote_title.getText().toString().length() == 0) {
                    Toast.makeText(EditPromote.this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    edit_promote_title.requestFocus();
                } else if (edit_dogsize_radio_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(EditPromote.this, "훈련할 강아지 크기를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (edit_train_spot_radio_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(EditPromote.this, "훈련할 장소를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (edit_promote_period.getText().toString().length() == 0) {
                    Toast.makeText(EditPromote.this, "훈련할 기간을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    edit_promote_period.requestFocus();
                } else if (edit_promote_price.getText().toString().length() == 0) {
                    Toast.makeText(EditPromote.this, "훈련할 가격을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    edit_promote_price.requestFocus();
                } else if (edit_promote_content.getText().toString().length() == 0) {
                    Toast.makeText(EditPromote.this, "훈련할 내용을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    edit_promote_content.requestFocus();
                } else {


                    String title_check = edit_promote_title.getText().toString().trim();
                    String dog_size_check = dog_size.trim();
                    String train_spot_check = train_spot.trim();
                    String period_check = edit_promote_period.getText().toString().trim();
                    String price_check = edit_promote_price.getText().toString().trim();
                    String content_check = edit_promote_content.getText().toString().trim();

                    if (image1 != null && existing_image1 == new_image1 ) {
                        upLoadfiles.add(image1);
                        edit_image1 = 1;

                    } else if (image1 != null && existing_image1 != new_image1) {
                        upLoadfiles.add(image1);
                        edit_image1 = 2;
                    } else if (image1 == null) {
                        edit_image1 = 4;
                    }


                    if (image2 != null && existing_image2 == new_image2) {
                        upLoadfiles.add(image2);
                        edit_image2 = 1;
                    } else if (image2 != null && existing_image2 != new_image2) {
                        upLoadfiles.add(image2);
                        edit_image2 = 2;
                    } else if (image2 == null) {
                        edit_image2 = 4;
                    }

                    if (image3 != null && existing_image3 == new_image3) {
                        upLoadfiles.add(image3);
                        edit_image3 = 1;
                    } else if (image3 != null && existing_image3 != new_image3) {
                        upLoadfiles.add(image3);
                        edit_image3 = 2;
                    } else if (image3 == null) {
                        edit_image3 = 4;
                    }

                    if (image4 != null && existing_image4 == new_image4) {
                        upLoadfiles.add(image4);
                        edit_image4 = 1;
                    } else if (image4 != null && existing_image4 != new_image4) {
                        upLoadfiles.add(image4);
                        edit_image4 = 2;
                    } else if (image4 == null) {
                        edit_image4 = 4;
                    }


                    if (image5 != null && existing_image5 == new_image5 ) {
                        upLoadfiles.add(image5);
                        edit_image5 = 1;
                    } else if (image5 != null && existing_image5 != new_image5) {
                        upLoadfiles.add(image5);
                        edit_image5 = 2;
                    } else if (image5 == null) {
                        edit_image5 = 4;
                    }

                    if (edit_promote_basic.isChecked()) {
                        basictraining = "기본훈련";
                        toggleArrayList.add(basictraining);
                    }
                    if (edit_promote_pottytraining.isChecked()) {
                        pottytraining = "배변훈련";
                        toggleArrayList.add(pottytraining);
                    }
                    if (edit_promote_barkingdog.isChecked()) {
                        barkingdog = "짖는 개 훈련";
                        toggleArrayList.add(barkingdog);
                    }
                    if (edit_promote_bitingdog.isChecked()) {
                        bitingdog = "무는 개 훈련";
                        toggleArrayList.add(bitingdog);
                    }
                    if (edit_promote_playtraining.isChecked()) {
                        playtraining = "놀이훈련";
                        toggleArrayList.add(playtraining);
                    }
                    if (edit_promote_sociability.isChecked()) {
                        sociability = "사회성";
                        toggleArrayList.add(sociability);
                    }
                    if (edit_promote_eatdung.isChecked()) {
                        eatdung = "식분증";
                        toggleArrayList.add(eatdung);
                    }
                    if (edit_promote_competition.isChecked()) {
                        competition = "대회 훈련";
                        toggleArrayList.add(competition);
                    }
                    if (edit_promote_guidance.isChecked()) {
                        guidance = "안내견 ";
                        toggleArrayList.add(guidance);
                    }

                    String toggle_check = toggleArrayList.toString().trim();

                    String content_type = "0";


                    new EditUpLoadAsyncTask(title_check, dog_size_check, edit_image1, edit_image2, edit_image3, edit_image4,
                            edit_image5, train_spot_check, period_check, price_check, content_check, toggle_check, content_type).execute(upLoadfiles);
                }

                final int image[] = {
                        R.drawable.search_03_basictraining,
                        R.drawable.search_03_basictraining_tap,
                };
                edit_promote_basic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_basic.isChecked()) {
                            edit_promote_basic.setBackgroundResource(image[1]);

                        } else {
                            edit_promote_basic.setBackgroundResource(image[0]);
                        }
                    }
                });


                edit_promote_pottytraining.setText(null);
                edit_promote_pottytraining.setTextOn(null);
                edit_promote_pottytraining.setTextOff(null);

                final int image1[] = {
                        R.drawable.search_03_pottytraining,
                        R.drawable.search_03_pottytraining_tap
                };


                edit_promote_pottytraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_pottytraining.isChecked()) {
                            edit_promote_pottytraining.setBackgroundResource(image1[1]);
                        } else {
                            edit_promote_pottytraining.setBackgroundResource(image1[0]);
                        }
                    }
                });


                edit_promote_playtraining.setText(null);
                edit_promote_playtraining.setTextOn(null);
                edit_promote_playtraining.setTextOff(null);

                final int image2[] = {
                        R.drawable.search_03_playtraining,
                        R.drawable.search_03_playtraining_tap
                };


                edit_promote_playtraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_playtraining.isChecked()) {
                            edit_promote_playtraining.setBackgroundResource(image2[1]);
                        } else {
                            edit_promote_playtraining.setBackgroundResource(image2[0]);
                        }
                    }
                });

                edit_promote_sociability.setText(null);
                edit_promote_sociability.setTextOn(null);
                edit_promote_sociability.setTextOff(null);

                final int image3[] = {
                        R.drawable.search_03_sociability,
                        R.drawable.search_03_sociability_tap
                };


                edit_promote_sociability.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_sociability.isChecked()) {
                            edit_promote_sociability.setBackgroundResource(image3[1]);

                        } else {
                            edit_promote_sociability.setBackgroundResource(image3[0]);
                        }
                    }
                });

                edit_promote_bitingdog.setText(null);
                edit_promote_bitingdog.setTextOn(null);
                edit_promote_bitingdog.setTextOff(null);

                final int image4[] = {
                        R.drawable.search_03_bitingdog,
                        R.drawable.search_03_bitingdog_tap
                };


                edit_promote_bitingdog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_bitingdog.isChecked()) {
                            edit_promote_bitingdog.setBackgroundResource(image4[1]);

                        } else {
                            edit_promote_bitingdog.setBackgroundResource(image4[0]);
                        }
                    }
                });

                edit_promote_barkingdog.setText(null);
                edit_promote_barkingdog.setTextOn(null);
                edit_promote_barkingdog.setTextOff(null);

                final int image5[] = {
                        R.drawable.search_03_barkingdog,
                        R.drawable.search_03_barkingdog_tap
                };

                edit_promote_barkingdog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_barkingdog.isChecked()) {
                            edit_promote_barkingdog.setBackgroundResource(image5[1]);

                        } else {
                            edit_promote_barkingdog.setBackgroundResource(image5[0]);
                        }
                    }
                });

                edit_promote_eatdung.setText(null);
                edit_promote_eatdung.setTextOn(null);
                edit_promote_eatdung.setTextOff(null);

                final int image6[] = {
                        R.drawable.search_03_eatdung,
                        R.drawable.search_03_eatdung_tap
                };


                edit_promote_eatdung.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_eatdung.isChecked()) {
                            edit_promote_eatdung.setBackgroundResource(image6[1]);

                        } else {
                            edit_promote_eatdung.setBackgroundResource(image6[0]);
                        }
                    }
                });


                edit_promote_competition.setText(null);
                edit_promote_competition.setTextOn(null);
                edit_promote_competition.setTextOff(null);

                final int image7[] = {
                        R.drawable.search_03_competition,
                        R.drawable.search_03_competition_tap
                };


                edit_promote_competition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_competition.isChecked()) {
                            edit_promote_competition.setBackgroundResource(image7[1]);

                        } else {
                            edit_promote_competition.setBackgroundResource(image7[0]);
                        }
                    }
                });


                edit_promote_guidance.setText(null);
                edit_promote_guidance.setTextOn(null);
                edit_promote_guidance.setTextOff(null);

                final int image8[] = {
                        R.drawable.search_03_guidance,
                        R.drawable.search_03_guidance_tap
                };

                edit_promote_guidance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_promote_guidance.isChecked()) {
                            edit_promote_guidance.setBackgroundResource(image8[1]);

                        } else {
                            edit_promote_guidance.setBackgroundResource(image8[0]);
                        }
                    }
                });

            }
        });


    }

    private class EditSendAsyncTask extends AsyncTask<Void, Void, EditProObject> {

        private String query1; //보낼쿼리
       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());


        public EditSendAsyncTask(String query1) {
            this.query1 = query1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //networkDialog.show();
        }

        @Override
        protected EditProObject doInBackground(Void... voids) {
            Response response = null;
            try {

                OkHttpClient toServer = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(MAIN_PRO + "/modi/" + query1)
                        .build();

                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getEditProObject(new StringBuilder(responseBody.string()));
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
            return null;
        }

        @Override
        protected void onPostExecute(EditProObject result) {
            super.onPostExecute(result);
           // networkDialog.dismiss();

            String dog_size2 = result.edit_dog_size;
            if (dog_size2.equals("소형견")) {
                EditPromote.edit_dogsize_small.setChecked(true);
            } else {
                EditPromote.edit_dogsize_big.setChecked(true);
            }

            EditPromote.edit_promote_title.setText(result.edit_title);

            String train_spot = result.edit_train_spot;
            if (train_spot.equals("입소훈련")) {
                EditPromote.edit_spot_enter.setChecked(true);
            } else {
                EditPromote.edit_spot_visit.setChecked(true);
            }

            EditPromote.edit_promote_period.setText(""+result.edit_train_period);
            EditPromote.edit_promote_price.setText(""+result.edit_price);
            EditPromote.edit_promote_content.setText(result.edit_content);

            String[] data = result.edit_train_type.split(",");



            for (int i = 0; i < data.length; i++) {
                if (data[i].trim() != null) {
                    if (data[i].trim().equals("기본훈련")) {
                        edit_promote_basic.setChecked(true);
                    }
                    if (data[i].trim().equals("배변훈련")) {
                        edit_promote_pottytraining.setChecked(true);
                    }
                    if (data[i].trim().equals("짖는 개 훈련")) {
                        edit_promote_barkingdog.setChecked(true);
                    }
                    if (data[i].trim().equals("무는 개 훈련")) {
                        edit_promote_bitingdog.setChecked(true);
                    }
                    if (data[i].trim().equals("놀이훈련")) {
                        edit_promote_playtraining.setChecked(true);
                    }
                    if (data[i].trim().equals("사회성")) {
                        edit_promote_sociability.setChecked(true);
                    }
                    if (data[i].trim().equals("식분증")) {
                        edit_promote_eatdung.setChecked(true);
                    }
                    if (data[i].trim().equals("대회 훈련")) {
                        edit_promote_competition.setChecked(true);
                    }
                    if (data[i].trim().equals("안내견")) {
                        edit_promote_guidance.setChecked(true);
                    }
                }
            }


            if (result.edit_picture.get(0) != null) {
                Glide.with(EditPromote.this).load(result.edit_picture.get(0)).into(edit_picture1);
                //Bitmap photo = extras.getParcelable("data");
                //mPhotoImageView.setImageBitmap(photo);
                existing_image1 = 5;
            }

            if (result.edit_picture.get(1) != null) {
                Glide.with(EditPromote.this).load(result.edit_picture.get(1)).into(edit_picture2);
                existing_image2 = 7;
            }

            if (result.edit_picture.get(2) != null) {
                Glide.with(EditPromote.this).load(result.edit_picture.get(2)).into(edit_picture3);
                existing_image3 = 9;
            }

            if (result.edit_picture.get(3) != null) {
                Glide.with(EditPromote.this).load(result.edit_picture.get(3)).into(edit_picture4);
                existing_image4 = 11;
            }

            if (result.edit_picture.get(4) != null) {
                Glide.with(EditPromote.this).load(result.edit_picture.get(4)).into(edit_picture5);
                existing_image5 = 13;
            }

        }
    }

    private class EditUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        //업로드할 Mime Type 설정
        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
        private String title;
        private String dog_size;
        private int edit_image1;
        private int edit_image2;
        private int edit_image3;
        private int edit_image4;
        private int edit_image5;
        private String edit_train_spot;
        private String period;
        private String cost;
        private String content;
        private String toggle_check;
        private String content_type;


        public EditUpLoadAsyncTask(String title, String dog_size, int edit_image1, int edit_image2, int edit_image3, int edit_image4, int edit_image5,
                                   String edit_train_spot, String period, String cost, String content, String toggle_check,
                                   String content_type) {
            this.title = title;
            this.dog_size = dog_size;
            this.edit_image1 = edit_image1;
            this.edit_image2 = edit_image2;
            this.edit_image3 = edit_image3;
            this.edit_image4 = edit_image4;
            this.edit_image5 = edit_image5;
            this.edit_train_spot = edit_train_spot;
            this.period = period;
            this.cost = cost;
            this.content = content;
            this.toggle_check = toggle_check;
            this.content_type = content_type;
        }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(ArrayList<UpLoadValueObject>... arrayLists) {
        Response response = null;
        try {
            //업로드는 타임 및 리드타임을 넉넉히 준다.
            OkHttpClient toServer = MySingleton.sharedInstance().httpClient;


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("expert_title", title);
            builder.addFormDataPart("dog_size", dog_size);
            builder.addFormDataPart("train_spot", edit_train_spot);
            builder.addFormDataPart("train_period", period);
            builder.addFormDataPart("cost", cost);
            builder.addFormDataPart("expert_content", content);
            builder.addFormDataPart("train_type", toggle_check);
            builder.addFormDataPart("content_type", content_type);


            int fileSize = arrayLists[0].size();

            if (upLoadfiles != null) {

                for (int i = 0; i < fileSize; i++) {
                    File file = arrayLists[0].get(i).file;
                    builder.addFormDataPart("picture", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }
                    /*for(int i = 0; i<fileSize; i++){
                        builder.addFormDataPart("change", "1"+""+edit_image1 );
                    }*/
            }

            builder.addFormDataPart("change", "1" + "" + edit_image1 + ",2" + edit_image2 + ",3" + edit_image3 + ",4" +
                    edit_image4 + ",5" + edit_image5);


            RequestBody fileUploadBody = builder.build();
            //요청 세팅
            Request request = new Request.Builder()
                    .url(MAIN_PRO + "/" + post_id)
                    .put(fileUploadBody) //반드시 post로
                    .build();
            //동기 방식
            response = toServer.newCall(request).execute();

            boolean flag = response.isSuccessful();
            //응답 코드 200등등
            int responseCode = response.code();
            if (flag) {
                e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                e("response응답바디", response.body().string()); //json으로 변신
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

        if (s.equalsIgnoreCase("success")) {

            Toast.makeText(EditPromote.this, "파일업로드에 성공했습니다", Toast.LENGTH_LONG).show();
            int fileSize = upLoadfiles.size();

            for (int i = 0; i < fileSize; i++) {
                UpLoadValueObject fileValue = upLoadfiles.get(i);
                if (fileValue.tempFiles) {
                    fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                }
                finish();

            }
        } else {
            Toast.makeText(EditPromote.this, "파일업로드에 실패했습니다", Toast.LENGTH_LONG).show();
        }
        edit_Btn.setEnabled(true);
    }
}



    @Override
    public void onResume() {
        super.onResume();

        new EditSendAsyncTask(post_id).execute();

        if (!isSDCardAvailable()) {
            Toast.makeText(this, "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        //checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplication(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UpLoadValueObject {
        File file; //업로드할 파일
        boolean tempFiles; //임시파일 유무

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }

    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 카메라에서 이미지 가져오기
     */
    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //업로드할 파일의 이름
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("파일 업로드", ""+requestCode);
        Log.e("파일 업로드22", ""+resultCode);
        if (resultCode != Activity.RESULT_OK) {
            if (imageNumber == IMAGE1) {
                image1 =null;
            } else if (imageNumber == IMAGE2) {
                image2 =null;
            } else if (imageNumber == IMAGE3) {
                image3 = null;
            } else if (imageNumber == IMAGE4) {
                image4 = null;
            } else if (imageNumber == IMAGE5) {
                image5 = null;
            }
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {

                // 크롭된 이미지를 세팅
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    //new BitmapBytesTranscoder().transcode(new BitmapResource(photo, new Bitmp))
                    // Glide.with(EditPromote.this)
                    mPhotoImageView.setImageBitmap(photo);

                    if(imageNumber == IMAGE1){
                        new_image1 = 0;
                    }else if (imageNumber == IMAGE2) {
                        new_image2 =0;
                    } else if (imageNumber == IMAGE3) {
                        new_image3 = 0;
                    } else if (imageNumber == IMAGE4) {
                        new_image4 = 0;
                    } else if (imageNumber == IMAGE5) {
                        new_image5 = 0;
                    }
                }
                break;
            }
            case PICK_FROM_ALBUM: {

                currentSelectedUri = data.getData();
                if (currentSelectedUri == null) {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("임시이미지파일저장", "저장됨");
                    } else {
                        Log.e("임시이미지파일저장", "실패");
                    }
                }
                if (findImageFileNameFromUri(currentSelectedUri)) {
                    if (imageNumber == IMAGE1) {
                        image1 = new UpLoadValueObject(new File(currentFileName), false);
                    } else if (imageNumber == IMAGE2) {
                        image2 = new UpLoadValueObject(new File(currentFileName), false);
                    } else if (imageNumber == IMAGE3) {
                        image3 = new UpLoadValueObject(new File(currentFileName), false);
                    } else if (imageNumber == IMAGE4) {
                        image4 = new UpLoadValueObject(new File(currentFileName), false);
                    } else if (imageNumber == IMAGE5) {
                        image5 = new UpLoadValueObject(new File(currentFileName), false);
                    }else{
                        Toast.makeText(EditPromote.this, "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
                    }
                }
                cropIntent(currentSelectedUri);
                break;

            }

            case PICK_FROM_CAMERA: {


                if (imageNumber == IMAGE1) {
                    image1 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageNumber == IMAGE2) {
                    image2 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageNumber == IMAGE3) {
                    image3 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageNumber == IMAGE4) {
                    image4 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageNumber == IMAGE5) {
                    image5 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                }else{
                    Toast.makeText(EditPromote.this, "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
                }


                //카메라캡쳐를 이용해 가져온 이미지
                //upLoadfiles.add(new UpLoadValueObject(new File(myImageDir, currentFileName), false));
                cropIntent(currentSelectedUri);
                break;
            }
        }
    }
    private  void  cropIntent(Uri cropUri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CROP_FROM_CAMERA);

    }
    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            currentFileName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.
            File tempFile = File.createTempFile(
                    currentFileName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
            upLoadfiles.add(new UpLoadValueObject(tempFile, true));

            if (bitmapStream != null) {
                bitmapStream.close();
            }
            currentSelectedUri = Uri.fromFile(tempFile);
            flag = true;
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }

    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currentFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                Log.e("fileName", String.valueOf(currentFileName));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }

    class PhotoClickListener implements View.OnClickListener {
        int type;

        public PhotoClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            AlertDialog dialog = null;

            mPhotoImageView = (ImageView) v;
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    imageNumber = type;
                    doTakePhotoAction();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    imageNumber = type;
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            dialog = new AlertDialog.Builder(EditPromote.this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("앨범선택", albumListener)
                    .setNegativeButton("사진촬영", cameraListener)
                    .setNeutralButton("취소", cancelListener).create();

            dialog.show();
        }
    }
}


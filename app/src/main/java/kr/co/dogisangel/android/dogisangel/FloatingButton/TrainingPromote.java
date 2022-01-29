package kr.co.dogisangel.android.dogisangel.FloatingButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.Pro.ProFragment;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.MAIN_PRO;


public class TrainingPromote extends Fragment {

    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int IMAGE1 = 1;
    private static final int IMAGE2 = 2;
    private static final int IMAGE3 = 3;
    private static final int IMAGE4 = 4;
    private static final int IMAGE5 = 5;
    ProWriting owner;
    /**
     * 카메라에서 이미지 가져오기
     */
    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    private ImageView mPhotoImageView;
    private ImageView pro_picturepluse1, pro_picturepluse2, pro_picturepluse3, pro_picturepluse4, pro_picturepluse5;
    private Button pro_file_upload_btn;
    private EditText promote_title, promote_month, promote_price, promote_content;

    private UpLoadValueObject image1;
    private UpLoadValueObject image2;
    private UpLoadValueObject image3;
    private UpLoadValueObject image4;
    private UpLoadValueObject image5;



    private ArrayList<String> toggleArrayList = new ArrayList<>();
    private RadioGroup dogsize_radio_group;
    private RadioGroup training_spot_radio_group;
    private String dog_size;
    private String spot;
    private String basictraining, pottytraining, playtraining, sociability, barkingdog, eatdung, competition, guidance, bitingdog;



    private int imageNumber;

    ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();


    public TrainingPromote() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.writing_training_promote, container, false);
        owner = (ProWriting) getActivity();

        pro_picturepluse1 = (ImageView) view.findViewById(R.id.pro_picturepluse1);
        pro_picturepluse2 = (ImageView) view.findViewById(R.id.pro_picturepluse2);
        pro_picturepluse3 = (ImageView) view.findViewById(R.id.pro_picturepluse3);
        pro_picturepluse4 = (ImageView) view.findViewById(R.id.pro_picturepluse4);
        pro_picturepluse5 = (ImageView) view.findViewById(R.id.pro_picturepluse5);



        pro_picturepluse1.setOnClickListener(new PhotoClickListener(IMAGE1));
        pro_picturepluse2.setOnClickListener(new PhotoClickListener(IMAGE2));
        pro_picturepluse3.setOnClickListener(new PhotoClickListener(IMAGE3));
        pro_picturepluse4.setOnClickListener(new PhotoClickListener(IMAGE4));
        pro_picturepluse5.setOnClickListener(new PhotoClickListener(IMAGE5));

        promote_title = (EditText) view.findViewById(R.id.training_promote_title);
        promote_month = (EditText) view.findViewById(R.id.training_month);
        promote_price = (EditText) view.findViewById(R.id.training_price);
        promote_content = (EditText) view.findViewById(R.id.training_promote_content);

        promote_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.training_promote_content) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
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

        dogsize_radio_group = (RadioGroup) view.findViewById(R.id.promote_dog_size_radio_group);
        dogsize_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.promote_small_size:
                        dog_size = "소형견";
                        Toast.makeText(getActivity(), "소형견을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.promote_big_size:
                        dog_size = "중*대형견";
                        Toast.makeText(getActivity(), "중*대형견을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        training_spot_radio_group = (RadioGroup) view.findViewById(R.id.training_spot_radio_group);
        training_spot_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.initiatory_training:
                        spot = "입소훈련";
                        Toast.makeText(getActivity(), "입소훈련을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.visit_training:
                        spot = "방문훈련";
                        Toast.makeText(getActivity(), "방문훈련을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        pro_file_upload_btn = (Button) view.findViewById(R.id.pro_promote_file_upload_btn);


        pro_file_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (promote_title.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    promote_title.requestFocus();
                } else if (dogsize_radio_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "훈련할 강아지 크기를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (training_spot_radio_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "훈련할 장소를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (promote_month.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "훈련할 기간을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    promote_month.requestFocus();
                } else if (promote_price.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "훈련할 가격을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    promote_price.requestFocus();
                } else if (promote_content.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "훈련할 내용을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    promote_content.requestFocus();
                } else {

                    pro_file_upload_btn.setEnabled(false);

                    String title = promote_title.getText().toString().trim();
                    String content = promote_content.getText().toString().trim();
                    String month = promote_month.getText().toString().trim();
                    String price = promote_price.getText().toString().trim();
                    String dog_size_check = dog_size.trim();
                    String training_spot_check = spot.trim();

                    String content_type = "0";

                    if (search_03_basictraining.isChecked()) {
                        basictraining = "기본훈련";
                        toggleArrayList.add(basictraining);
                    }
                    if (search_03_pottytraining.isChecked()) {
                        pottytraining = "배변훈련";
                        toggleArrayList.add(pottytraining);
                    }
                    if (search_03_barkingdog.isChecked()) {
                        barkingdog = "짖는 개 훈련";
                        toggleArrayList.add(barkingdog);
                    }
                    if (search_03_bitingdog.isChecked()) {
                        bitingdog = "무는 개 훈련";
                        toggleArrayList.add(bitingdog);
                    }
                    if (search_03_playtraining.isChecked()) {
                        playtraining = "놀이훈련";
                        toggleArrayList.add(playtraining);
                    }
                    if (search_03_sociability.isChecked()) {
                        sociability = "사회성";
                        toggleArrayList.add(sociability);
                    }
                    if (search_03_eatdung.isChecked()) {
                        eatdung = "식분증";
                        toggleArrayList.add(eatdung);
                    }
                    if (search_03_competition.isChecked()) {
                        competition = "대회 훈련";
                        toggleArrayList.add(competition);
                    }
                    if (search_03_guidance.isChecked()) {
                        guidance = "안내견 ";
                        toggleArrayList.add(guidance);
                    }

                    String toggle_check = toggleArrayList.toString().trim();



                    if(image1 != null) {
                        upLoadfiles.add(image1);
                    }
                    if(image2 != null) {
                        upLoadfiles.add(image2);
                    }
                    if(image3 != null) {
                        upLoadfiles.add(image3);
                    }
                    if(image4 != null) {
                        upLoadfiles.add(image4);
                    }
                    if(image5 != null) {
                        upLoadfiles.add(image5);
                    }

                    new FileUpLoadAsyncTask(title, content, month, price, content_type,
                            dog_size_check, training_spot_check, toggle_check).execute(upLoadfiles);
                }

            }
        });

        search_03_basictraining.setText(null);
        search_03_basictraining.setTextOn(null);
        search_03_basictraining.setTextOff(null);

        final int image[] = {
                R.drawable.search_03_basictraining,
                R.drawable.search_03_basictraining_tap,
        };
        search_03_basictraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_03_basictraining.isChecked()) {
                    search_03_basictraining.setBackgroundResource(image[1]);

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

                } else {
                    search_03_guidance.setBackgroundResource(image8[0]);
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        String currentAppPackage = getActivity().getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);


        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getActivity(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
                    mPhotoImageView.setImageBitmap(photo);

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
                        Toast.makeText(getActivity(), "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
                    }


                //카메라캡쳐를 이용해 가져온 이미지
                //upLoadfiles.add(new UpLoadValueObject(new File(myImageDir, currentFileName), false));
                cropIntent(currentSelectedUri);
                break;
            }
        }
    }

   /* private void setData(Uri uri){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(uri, "image" );

        startActivityForResult(intent, CROP_FROM_CAMERA);
    }*/

    private void cropIntent(Uri cropUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image");

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
            cursor = owner.getContentResolver().query(
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

            dialog = new AlertDialog.Builder(owner)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("앨범선택", albumListener)
                    .setNegativeButton("사진촬영", cameraListener)
                    .setNeutralButton("취소", cancelListener).create();

            dialog.show();
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

    private class FileUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        //업로드할 Mime Type 설정
        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
        private String title; //보낼쿼리
        private String content; //보낼쿼리
        private String month; //보낼쿼리
        private String price; //보낼쿼리
        private String content_type;//보낼쿼리
        private String dog_size_check;
        private String training_spot_check;
        private String toggle_check;

       // NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());


        public FileUpLoadAsyncTask(String title, String content, String month, String price, String content_type,
                                   String dog_size_check, String training_spot_check, String toggle_check) {
            this.title = title;
            this.content = content;
            this.month = month;
            this.price = price;
            this.content_type = content_type;
            this.dog_size_check = dog_size_check;
            this.training_spot_check = training_spot_check;
            this.toggle_check = toggle_check;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // networkDialog.show();
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
                builder.addFormDataPart("expert_content", content);
                builder.addFormDataPart("train_period", month);
                builder.addFormDataPart("cost", price);


                builder.addFormDataPart("content_type", content_type);

                builder.addFormDataPart("dog_size", dog_size_check);
                builder.addFormDataPart("train_spot", training_spot_check);
                builder.addFormDataPart("train_type", toggle_check);


                int fileSize = arrayLists[0].size();

                if (upLoadfiles != null){

                    for (int i = 0; i < fileSize; i++) {
                        File file = arrayLists[0].get(i).file;
                        builder.addFormDataPart("picture", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                    }
                }


                RequestBody fileUploadBody = builder.build();
                //요청 세팅
                Request request = new Request.Builder()
                        .url(MAIN_PRO)
                        .post(fileUploadBody) //반드시 post로
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

                Toast.makeText(getActivity(), "파일업로드에 성공했습니다", Toast.LENGTH_LONG).show();
                int fileSize = upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                }
                getActivity().finish();
                new ProFragment.AsyncProPromoteJSONList().execute();

            } else {
                Toast.makeText(getActivity(), "파일업로드에 실패했습니다", Toast.LENGTH_LONG).show();
            }
            pro_file_upload_btn.setEnabled(true);
        }
    }
}



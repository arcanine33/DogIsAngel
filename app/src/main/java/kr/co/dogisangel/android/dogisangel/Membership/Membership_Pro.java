package kr.co.dogisangel.android.dogisangel.Membership;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.Login.Login;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

/**
 * Created by ccei on 2016-08-12.
 */
public class Membership_Pro extends Activity  {

    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PROFILE_IMG = 1;
    //private static final int LICENSE_IMG = 2;

    private static final int IMAGE1 = 1;
    private static final int IMAGE2 = 2;
    private static final int IMAGE3 = 3;
    private static final int IMAGE4 = 4;
    private static final int IMAGE5 = 5;


    private static EditText membership_pro_name;
    private static EditText membership_pro_profile, membership_pro_other_address;
    private static Spinner membership_pro_region;
    private static TextView membership_pro_birthday;

    String ID, PW;
    int year, month, day;

    private UpLoadValueObject image1;
    private UpLoadValueObject image2;
    private UpLoadValueObject image3;
    private UpLoadValueObject image4;
    private UpLoadValueObject image5;




    Uri currentSelectedUri;
    File myImageDir;
    String currentFileName;
    private ImageView mPhotoImageView;
    private ImageView memberhip_pro_picturepluse1, memberhip_pro_picturepluse2, memberhip_pro_picturepluse3,
            memberhip_pro_picturepluse4, memberhip_pro_picturepluse5, memberhip_pro_main;
    private ImageButton memberhip_fileUploadBtn;
    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();
    private UpLoadValueObject profileFile;
    private RadioGroup membership_pro_license_radio_group;
    private String license;
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String month;
            String day;
            if (monthOfYear + 1 < 10) {
                month = "0" + String.valueOf(monthOfYear + 1);
            } else {
                month = String.valueOf(monthOfYear + 1);
            }

            if (dayOfMonth < 10) {
                day = "0" + String.valueOf(dayOfMonth);
            } else {
                day = String.valueOf(dayOfMonth);
            }

            membership_pro_birthday.setText(String.format("%d          %s           %s", year, month, day));
        }
    };
    private int imageType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_pro);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        PW = intent.getStringExtra("PW");


        memberhip_pro_picturepluse1 = (ImageView) findViewById(R.id.memberhip_pro_picturepluse1);
        memberhip_pro_picturepluse2 = (ImageView) findViewById(R.id.memberhip_pro_picturepluse2);
        memberhip_pro_picturepluse3 = (ImageView) findViewById(R.id.memberhip_pro_picturepluse3);
        memberhip_pro_picturepluse4 = (ImageView) findViewById(R.id.memberhip_pro_picturepluse4);
        memberhip_pro_picturepluse5 = (ImageView) findViewById(R.id.memberhip_pro_picturepluse5);
        memberhip_pro_main = (ImageView) findViewById(R.id.membership_pro_profile_img);

        memberhip_pro_picturepluse1.setOnClickListener(new PhotoClickListener(IMAGE1));
        memberhip_pro_picturepluse2.setOnClickListener(new PhotoClickListener(IMAGE2));
        memberhip_pro_picturepluse3.setOnClickListener(new PhotoClickListener(IMAGE3));
        memberhip_pro_picturepluse4.setOnClickListener(new PhotoClickListener(IMAGE4));
        memberhip_pro_picturepluse5.setOnClickListener(new PhotoClickListener(IMAGE5));
        memberhip_pro_main.setOnClickListener(new PhotoClickListener(PROFILE_IMG));

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        membership_pro_region = (Spinner) findViewById(R.id.membership_pro_region);

        membership_pro_name = (EditText) findViewById(R.id.membership_pro_name);
        membership_pro_profile = (EditText) findViewById(R.id.membership_pro_profile);
        membership_pro_other_address = (EditText) findViewById(R.id.membership_pro_other_address);
        membership_pro_license_radio_group = (RadioGroup) findViewById(R.id.membership_pro_license);
        membership_pro_license_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.membership_pro_first:
                        license = "1급";
                        Toast.makeText(Membership_Pro.this, "1급을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.membership_pro_second:
                        license = "2급";
                        Toast.makeText(Membership_Pro.this, "2급을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.membership_pro_third:
                        license = "3급";
                        Toast.makeText(Membership_Pro.this, "3급을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        membership_pro_birthday = (TextView) findViewById(R.id.membership_pro_birthday);


        findViewById(R.id.year_selected).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Membership_Pro.this, dateSetListener, year, month, day).show();
            }
        });


        memberhip_fileUploadBtn = (ImageButton) findViewById(R.id.memberhip_fileUploadBtn);
        memberhip_fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (membership_pro_name.getText().toString().length() == 0) {
                    Toast.makeText(Membership_Pro.this, "이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    membership_pro_name.requestFocus();
                } else if (membership_pro_birthday.getText().toString().length() == 0) {
                    Toast.makeText(Membership_Pro.this, "생년월일을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    membership_pro_birthday.requestFocus();
                }else if(membership_pro_license_radio_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Membership_Pro.this, "자격증 종류를 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
                else {

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

                    if (upLoadfiles != null && upLoadfiles.size() > 0) {
                        memberhip_fileUploadBtn.setEnabled(false);

                        String pro_name = membership_pro_name.getText().toString().trim();
                        String pro_other_address = membership_pro_other_address.getText().toString().trim();
                        String pro_profile = membership_pro_profile.getText().toString().trim();

                        String birthday_check = membership_pro_birthday.getText().toString().replaceAll(" ", "");
                        String pro_region_check = membership_pro_region.getSelectedItem().toString().trim();
                        String license_check = license.trim();
                        String ID_check = ID;
                        String PW_check = PW;

                        new FileUpLoadAsyncTask(pro_name, pro_other_address, pro_profile, birthday_check,
                                pro_region_check, ID_check, PW_check, license_check).execute(upLoadfiles);

                    } else {
                        Toast.makeText(Membership_Pro.this, "자격증 사진을 반드시 올려주세요!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

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

    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {

            if (imageType == IMAGE1) {
                image1 =null;
            } else if (imageType == IMAGE2) {
                image2 =null;
            } else if (imageType == IMAGE3) {
                image3 = null;
            } else if (imageType == IMAGE4) {
                image4 = null;
            } else if (imageType == IMAGE5) {
                image5 = null;
            }
            return;
        }
        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                Log.e("크롭1", "CROP_FROM_CAMERA");
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
                if (currentSelectedUri != null) {
                    //실제 Image의 full path name을 얻어온다.
                    if (findImageFileNameFromUri(currentSelectedUri)) {
                        //ArrayList에 업로드할  객체를 추가한다.
                        //프로필 이미지 , 라이센스 이미지 구별? 이미지타입?
                        if (imageType == IMAGE1) {
                            image1 = new UpLoadValueObject(new File(currentFileName), false);
                            Log.i("사진이 들어갔니", ""+image1);
                        } else if (imageType == IMAGE2) {
                            image2 = new UpLoadValueObject(new File(currentFileName), false);
                        } else if (imageType == IMAGE3) {
                            image3 = new UpLoadValueObject(new File(currentFileName), false);
                        } else if (imageType == IMAGE4) {
                            image4 = new UpLoadValueObject(new File(currentFileName), false);
                        } else if (imageType == IMAGE5) {
                            image5 = new UpLoadValueObject(new File(currentFileName), false);
                        }else{
                            profileFile = new UpLoadValueObject(new File(currentFileName), false);
                        }
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("임시이미지파일저장", "저장됨");
                    } else {
                        Log.e("임시이미지파일저장", "실패");
                    }
                }

                cropIntent(currentSelectedUri);
                break;
            }

            case PICK_FROM_CAMERA: {


                //카메라캡쳐를 이용해 가져온 이미지
                //upLoadfiles.add(new UpLoadValueObject(new File(myImageDir, currentFileName), false));

                if (imageType == IMAGE1) {
                    image1 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageType == IMAGE2) {
                    image2 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageType == IMAGE3) {
                    image3 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageType == IMAGE4) {
                    image4 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                } else if (imageType == IMAGE5) {
                    image5 = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                }else{
                    profileFile = new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                }

                cropIntent(currentSelectedUri);

                break;
            }
        }
    }

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
    class PhotoClickListener implements View.OnClickListener{
        int type;

        public PhotoClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            mPhotoImageView = (ImageView) view;
            AlertDialog dialog = null;

            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    imageType = type;
                    doTakePhotoAction();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    imageType = type;
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            dialog = new AlertDialog.Builder(Membership_Pro.this)
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

        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

        private String pro_name; //보낼쿼리
        private String pro_other_address; //보낼쿼리
        private String pro_profile; //보낼쿼리
        private String birthday_check;
        private String region_check; //보낼쿼리
        private String ID_check;
        private String PW_check;
        private String license_check;

        public FileUpLoadAsyncTask(String pro_name, String pro_other_address ,String pro_profile,String birthday_check, String region_check, String ID_check, String PW_check, String license_check) {

            this.pro_name = pro_name;
            this.pro_profile = pro_profile;
            this.pro_other_address = pro_other_address;
            this.region_check = region_check;
            this.birthday_check = birthday_check;
            this.ID_check = ID_check;
            this.PW_check = PW_check;
            this.license_check = license_check;
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
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("username", pro_name);
                builder.addFormDataPart("profile", pro_profile);
                builder.addFormDataPart("detail_region", pro_other_address);
                builder.addFormDataPart("region", region_check);
                builder.addFormDataPart("birthday", birthday_check);
                builder.addFormDataPart("id", ID_check);
                builder.addFormDataPart("password", PW_check);
                builder.addFormDataPart("license", license_check);


                int fileSize = arrayLists[0].size();

                for (int i = 0; i < fileSize; i++) {
                    File file = arrayLists[0].get(i).file;
                    builder.addFormDataPart("license", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }


                //File file2 = arrayLists[0].get(0).file;

                if(profileFile != null){
                File file2 = profileFile.file;
                builder.addFormDataPart("profile_Img", file2.getName(), RequestBody.create(IMAGE_MIME_TYPE, file2));
                }

                RequestBody fileUploadBody = builder.build();
                //요청 세팅
                Request request = new Request.Builder()
                        .url(FULL_URL + "sign")
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
                Toast.makeText(Membership_Pro.this, "회원가입에 성공했습니다", Toast.LENGTH_LONG).show();
                int fileSize = upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }

                    finish();

                    Intent intent;
                    intent = new Intent(Membership_Pro.this, Login.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(Membership_Pro.this, "회원가입에 실패했습니다", Toast.LENGTH_LONG).show();
            }
            memberhip_fileUploadBtn.setEnabled(true);
        }
    }


}



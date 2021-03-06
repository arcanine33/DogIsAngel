package kr.co.dogisangel.android.dogisangel.Membership;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.Login.Login;
import kr.co.dogisangel.android.dogisangel.MainActivity;
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
public class Membership_Human extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static EditText membership_human_name, membership_human_nickname;
    private static EditText membership_human_profile, membership_human_other_address;
    private static Spinner membership_human_region;
    private static TextView membership_human_birthday;

    int year, month, day, hour, minute;
    ListView member_list;
    Membership_human_adapter membership_adapter;
    ArrayList<Membership_human_add> membership_list;
    ImageButton human_membership_pluse;
    String ID, PW;



    private ImageView mPhotoImageView;
    private ImageView member_hum_main;
    private ImageButton member_hum_fileUploadBtn;
    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String month;
            String day;
            if(monthOfYear+1 < 10){
                month = "0" + String.valueOf(monthOfYear+1);
            }else{
                month = String.valueOf(monthOfYear+1);
            }

            if(dayOfMonth <10){
                day ="0" + String.valueOf(dayOfMonth);
            }else{
                day = String.valueOf(dayOfMonth);
            }

            membership_human_birthday.setText(String.format("%d          %s           %s", year, month, day));
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_human);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        PW = intent.getStringExtra("PW");

        membership_human_name = (EditText) findViewById(R.id.membership_human_name);
        membership_human_nickname = (EditText) findViewById(R.id.membership_human_nickname);
        membership_human_profile = (EditText) findViewById(R.id.membership_human_profile);
        membership_human_other_address = (EditText) findViewById(R.id.membership_human_other_address);
        membership_human_region = (Spinner) findViewById(R.id.membership_human_region);
        membership_human_birthday = (TextView) findViewById(R.id.membership_human_birthday);


        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);




        findViewById(R.id.member_human_selected).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Membership_Human.this, dateSetListener, year, month, day).show();
            }
        });


        member_hum_main = (ImageView) findViewById(R.id.member_hum_main);
        member_hum_main.setOnClickListener(this);


        member_hum_fileUploadBtn = (ImageButton) findViewById(R.id.member_hum_fileUploadBtn);
        member_hum_fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(membership_human_name.getText().toString().length() == 0){
                    Toast.makeText(Membership_Human.this, "????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                    membership_human_name.requestFocus();
                }else if(membership_human_nickname.getText().toString().length() == 0){
                    Toast.makeText(Membership_Human.this, "???????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                    membership_human_nickname.requestFocus();
                }else if(membership_human_birthday.getText().toString().length() == 0) {
                    Toast.makeText(Membership_Human.this, "????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                    membership_human_birthday.requestFocus();
                }else {
                    if (upLoadfiles != null && upLoadfiles.size() > 0) {
                        member_hum_fileUploadBtn.setEnabled(false);

                        String human_name = membership_human_name.getText().toString().trim();
                        String human_nickname = membership_human_nickname.getText().toString().trim();
                        String human_profile = membership_human_profile.getText().toString().trim();
                        String human_other_address = membership_human_other_address.getText().toString().trim();
                        String region_check = membership_human_region.getSelectedItem().toString().trim();
                        String birthday_check = membership_human_birthday.getText().toString().replaceAll(" ", "");
                        String ID_check = ID;
                        String PW_check = PW;


                        new FileUpLoadAsyncTask(human_name, human_nickname, human_profile, human_other_address, region_check, birthday_check, ID_check, PW_check).execute(upLoadfiles);
                    } else {
                        Toast.makeText(Membership_Human.this, "???????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isSDCardAvailable()) {
            Toast.makeText(this, "SD ????????? ?????? ?????? ?????????.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        //checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplication(), " ????????? ??????????????? ?????? ???", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //???????????? ????????? ??????
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //???????????? ???????????? OK?????? ??????
                } else {

                    Log.d(TAG, "Permission always deny");
                    //???????????? ???????????? ???????????? ??????
                }
                break;
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
     * ??????????????? ????????? ????????????
     */
    Uri currentSelectedUri; //???????????? ?????? ???????????? ?????? Uri
    File myImageDir; //???????????? ?????? ????????? ????????? ????????????
    String currentFileName;  //????????????

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //???????????? ????????? ??????
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    /**
     * ???????????? ????????? ????????????
     */
    private void doTakeAlbumAction() {
        // ?????? ??????
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {

                // ????????? ???????????? ??????
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
                    //?????? Image??? full path name??? ????????????.
                    if (findImageFileNameFromUri(currentSelectedUri)) {
                        //ArrayList??? ????????????  ????????? ????????????.
                        upLoadfiles.add(new UpLoadValueObject(new File(currentFileName), false));
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("???????????????????????????", "?????????");
                    } else {
                        Log.e("???????????????????????????", "??????");
                    }
                }
                cropIntent(currentSelectedUri);
                break;

            }

            case PICK_FROM_CAMERA: {

                //?????????????????? ????????? ????????? ?????????
                upLoadfiles.add(new UpLoadValueObject(new File(myImageDir, currentFileName), false));
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
            //??????????????? ????????????.
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
            Log.e("????????? ????????????", i.toString(), i);
        }
        return flag;
    }

    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //?????? Image Uri??? ????????????
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key?????? ??????
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB??? ????????? ?????????.
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

    @Override
    public void onClick(View v) {
        AlertDialog dialog = null;
        mPhotoImageView = (ImageView) v;
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        dialog = new AlertDialog.Builder(this)
                .setTitle("???????????? ????????? ??????")
                .setPositiveButton("????????????", albumListener)
                .setNegativeButton("????????????", cameraListener)
                .setNeutralButton("??????", cancelListener).create();

        dialog.show();
    }


    class UpLoadValueObject {
        File file; //???????????? ??????
        File licence;
        boolean tempFiles; //???????????? ??????

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }

    private class FileUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        private String human_name; //????????????
        private String human_nickname; //????????????
        private String human_profile; //????????????
        private String human_other_address; //????????????
        private String region_check; //????????????
        private String birthday_check;
        private String ID_check;
        private String PW_check;


        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");



        public FileUpLoadAsyncTask(String human_name, String human_nickname, String human_profile, String human_other_address, String region_check, String birthday_check, String ID_check, String PW_check) {
            this.human_name = human_name;
            this.human_nickname = human_nickname;
            this.human_profile = human_profile;
            this.human_other_address = human_other_address;
            this.region_check = region_check;
            this.birthday_check = birthday_check;
            this.ID_check = ID_check;
            this.PW_check = PW_check;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ArrayList<UpLoadValueObject>... arrayLists) {
            Response response = null;
            try {
                //???????????? ?????? ??? ??????????????? ????????? ??????.
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("username", human_name);
                builder.addFormDataPart("nickname", human_nickname);
                builder.addFormDataPart("profile", human_profile);
                builder.addFormDataPart("detail_region", human_other_address);
                builder.addFormDataPart("region", region_check);
                builder.addFormDataPart("birthday", birthday_check);
                builder.addFormDataPart("id", ID_check);
                builder.addFormDataPart("password", PW_check);




                int fileSize = arrayLists[0].size();

                for (int i = 0; i < fileSize; i++) {
                    File file = arrayLists[0].get(i).file;
                    builder.addFormDataPart("profile_Img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }


                RequestBody fileUploadBody = builder.build();
                //?????? ??????
                Request request = new Request.Builder()
                        .url(FULL_URL + "sign")
                        .post(fileUploadBody) //????????? post???
                        .build();
                //?????? ??????
                response = toServer.newCall(request).execute();
                   /* //????????? ??????
                          toServer.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response res) throws IOException {

                            }
                        });*/
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
            if(s.equalsIgnoreCase("success")){
                Toast.makeText(Membership_Human.this, "??????????????? ??????????????????", Toast.LENGTH_LONG).show();
                int  fileSize = upLoadfiles.size();

                for(int i = 0 ; i < fileSize ; i++){
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if(fileValue.tempFiles){
                        fileValue.file.deleteOnExit(); //??????????????? ????????????
                    }

                    finish();

                    Intent intent;
                    intent = new Intent(Membership_Human.this,Login.class);
                    startActivity(intent);
                }
            }else{
                Toast.makeText(Membership_Human.this, "??????????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
            member_hum_fileUploadBtn.setEnabled(true);
        }
    }

}



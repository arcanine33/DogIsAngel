package kr.co.dogisangel.android.dogisangel.Modified;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.co.dogisangel.android.dogisangel.NetworkDefineConstant.FULL_URL;

public class Pro_changeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private ImageView mPhotoImageView;
    private ImageView pro_change_picturepluse1, pro_change_picturepluse2, pro_change_picturepluse3,
            pro_change_picturepluse4, pro_change_picturepluse5,memberhip_pro_main;
    private ImageButton pro_change_fileUploadBtn;
    private EditText writeInput1, writeInput2;

    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();

    class UpLoadValueObject {
        File file; //???????????? ??????
        boolean tempFiles; //???????????? ??????

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modified_pro);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.modified_pro_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);


        memberhip_pro_main = (ImageView) findViewById(R.id.pro_change_main);
        pro_change_picturepluse1 = (ImageView) findViewById(R.id.pro_change_picturepluse1);
        pro_change_picturepluse2 = (ImageView) findViewById(R.id.pro_change_picturepluse2);
        pro_change_picturepluse3 = (ImageView) findViewById(R.id.pro_change_picturepluse3);
        pro_change_picturepluse4 = (ImageView) findViewById(R.id.pro_change_picturepluse4);
        pro_change_picturepluse5 = (ImageView) findViewById(R.id.pro_change_picturepluse5);

        pro_change_picturepluse1 .setOnClickListener(this);
        pro_change_picturepluse2 .setOnClickListener(this);
        pro_change_picturepluse3 .setOnClickListener(this);
        pro_change_picturepluse4 .setOnClickListener(this);
        pro_change_picturepluse5 .setOnClickListener(this);





        pro_change_fileUploadBtn = (ImageButton)findViewById(R.id.pro_change_fileUploadBtn);

        pro_change_fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (upLoadfiles != null && upLoadfiles.size() > 0) {
                    pro_change_fileUploadBtn.setEnabled(false);
//                    String query1 = writeInput1.getText().toString().trim();
//                    String query2 = writeInput2.getText().toString().trim();
//                    new FileUpLoadAsyncTask(query1, query2).execute(upLoadfiles);
                } else {
                    Toast.makeText(Pro_changeActivity.this, "???????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                }

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

        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplication(), " ????????? ??????????????? ?????? ???", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    /**
     * ???????????? ????????? ????????????
     */
    private void doTakeAlbumAction() {
        // ?????? ??????
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
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
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 0, bitmapStream);
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

    private class FileUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        //        private String query1; //????????????
//        private String query2; //????????????
        //???????????? Mime Type ??????
        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");


        public FileUpLoadAsyncTask() {
        }

        public FileUpLoadAsyncTask(String query1, String query2) {
//            this.query1 = query1;
//            this.query2 = query2;
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
//                builder.addFormDataPart("title", query1);
//                builder.addFormDataPart("content", query2);
                int fileSize = arrayLists[0].size();

                for (int i = 0; i < fileSize; i++) {
                    File file = arrayLists[0].get(i).file;
                    builder.addFormDataPart("picture", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }
                RequestBody fileUploadBody = builder.build();
                //?????? ??????
                Request request = new Request.Builder()
                        .url(FULL_URL + "file")
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
                Toast.makeText(Pro_changeActivity.this, "?????????????????? ??????????????????", Toast.LENGTH_LONG).show();
                int  fileSize = upLoadfiles.size();

                for(int i = 0 ; i < fileSize ; i++){
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if(fileValue.tempFiles){
                        fileValue.file.deleteOnExit(); //??????????????? ????????????
                    }
                    finish();
                }
            }else{
                Toast.makeText(Pro_changeActivity.this, "?????????????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
            pro_change_fileUploadBtn.setEnabled(true);
        }
    }

}



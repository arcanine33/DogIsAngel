package kr.co.dogisangel.android.dogisangel.FloatingButton;

import android.Manifest;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.dogisangel.android.dogisangel.DogGomin.GominFragment;
import kr.co.dogisangel.android.dogisangel.MyApplication;
import kr.co.dogisangel.android.dogisangel.MySingleton;
import kr.co.dogisangel.android.dogisangel.NetworkDefineConstant;
import kr.co.dogisangel.android.dogisangel.NetworkDialog;
import kr.co.dogisangel.android.dogisangel.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

public class HumanWriting_Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int IMAGE1 = 1;
    private static final int IMAGE2 = 2;
    private static final int IMAGE3 = 3;
    private static final int IMAGE4 = 4;
    private static final int IMAGE5 = 5;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    /**
     * 카메라에서 이미지 가져오기
     */
    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    private ImageView mPhotoImageView;
    private ImageView picturepluse1, picturepluse2, picturepluse3, picturepluse4, picturepluse5;
    private Button fileUploadBtn;
    private EditText writeInput1, writeInput2;
    private UpLoadValueObject image1;
    private UpLoadValueObject image2;
    private UpLoadValueObject image3;
    private UpLoadValueObject image4;
    private UpLoadValueObject image5;
    private int imageNumber;
    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();
    private ArrayList<String> toggleArrayList = new ArrayList<>();

    private String eatdung, attack, barkinghabit, bitinghabit, dungproblems, sociability;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_human);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.humanwriting_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        picturepluse1 = (ImageView) findViewById(R.id.picturepluse1);
        picturepluse2 = (ImageView) findViewById(R.id.picturepluse2);
        picturepluse3 = (ImageView) findViewById(R.id.picturepluse3);
        picturepluse4 = (ImageView) findViewById(R.id.picturepluse4);
        picturepluse5 = (ImageView) findViewById(R.id.picturepluse5);

        picturepluse1.setOnClickListener(new PhotoClickListener(IMAGE1));
        picturepluse2.setOnClickListener(new PhotoClickListener(IMAGE2));
        picturepluse3.setOnClickListener(new PhotoClickListener(IMAGE3));
        picturepluse4.setOnClickListener(new PhotoClickListener(IMAGE4));
        picturepluse5.setOnClickListener(new PhotoClickListener(IMAGE5));

        writeInput1 = (EditText) findViewById(R.id.writeinput1);
        writeInput2 = (EditText) findViewById(R.id.writeinput2);

        writeInput2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.writeinput2) {
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


        final ToggleButton gomin_04_eatdung = (ToggleButton) findViewById(R.id.gomin_04_eatdung);
        final ToggleButton gomin_04_attack = (ToggleButton) findViewById(R.id.gomin_04_attack);
        final ToggleButton gomin_04_barkinghabit = (ToggleButton) findViewById(R.id.gomin_04_barkinghabit);
        final ToggleButton gomin_04_bitinghabit = (ToggleButton) findViewById(R.id.gomin_04_bitinghabit);
        final ToggleButton gomin_04_dungproblems = (ToggleButton) findViewById(R.id.gomin_04_dungproblems);
        final ToggleButton gomin_04_sociability = (ToggleButton) findViewById(R.id.gomin_04_sociability);

        fileUploadBtn = (Button) findViewById(R.id.file_upload_btn);

        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fileUploadBtn.setEnabled(false);
                String query1 = writeInput1.getText().toString().trim();
                String query2 = writeInput2.getText().toString().trim();

                if (gomin_04_eatdung.isChecked()) {
                    eatdung = "배변문제";
                    toggleArrayList.add(eatdung);
                }
                if (gomin_04_attack.isChecked()) {
                    attack = "공격성";
                    toggleArrayList.add(attack);
                }
                if (gomin_04_barkinghabit.isChecked()) {
                    barkinghabit = "짖는버릇";
                    toggleArrayList.add(barkinghabit);
                }
                if (gomin_04_bitinghabit.isChecked()) {
                    bitinghabit = "무는버릇";
                    toggleArrayList.add(bitinghabit);
                }
                if (gomin_04_dungproblems.isChecked()) {
                    dungproblems = "식분증";
                    toggleArrayList.add(dungproblems);
                }
                if (gomin_04_sociability.isChecked()) {
                    sociability = "사회성";
                    toggleArrayList.add(sociability);
                }

                String toggle_check = toggleArrayList.toString().trim();

                if (image1 != null) {
                    upLoadfiles.add(image1);
                }
                if (image2 != null) {
                    upLoadfiles.add(image2);
                }
                if (image3 != null) {
                    upLoadfiles.add(image3);
                }
                if (image4 != null) {
                    upLoadfiles.add(image4);
                }
                if (image5 != null) {
                    upLoadfiles.add(image5);
                }


                new FileUpLoadAsyncTask(query1, query2, toggle_check).execute(upLoadfiles);


            }
        });




        gomin_04_eatdung.setText(null);
        gomin_04_eatdung.setTextOn(null);
        gomin_04_eatdung.setTextOff(null);

        final int image0[] = {
                R.drawable.earch_04_eatdung,
                R.drawable.earch_04_eatdung_tap,
        };
        gomin_04_eatdung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_eatdung.isChecked()) {
                    gomin_04_eatdung.setBackgroundResource(image0[1]);


                } else {
                    gomin_04_eatdung.setBackgroundResource(image0[0]);

                }
            }
        });


        gomin_04_attack.setText(null);
        gomin_04_attack.setTextOn(null);
        gomin_04_attack.setTextOff(null);

        final int image1[] = {
                R.drawable.search_04_attack,
                R.drawable.search_04_attack_tap
        };


        gomin_04_attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_attack.isChecked()) {
                    gomin_04_attack.setBackgroundResource(image1[1]);

                } else {
                    gomin_04_attack.setBackgroundResource(image1[0]);


                }
            }
        });

        gomin_04_barkinghabit.setText(null);
        gomin_04_barkinghabit.setTextOn(null);
        gomin_04_barkinghabit.setTextOff(null);

        final int image2[] = {
                R.drawable.search_04_barkinghabit,
                R.drawable.search_04_barkinghabit_tap,
        };
        gomin_04_barkinghabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_barkinghabit.isChecked()) {
                    gomin_04_barkinghabit.setBackgroundResource(image2[1]);


                } else {
                    gomin_04_barkinghabit.setBackgroundResource(image2[0]);

                }
            }
        });


        gomin_04_bitinghabit.setText(null);
        gomin_04_bitinghabit.setTextOn(null);
        gomin_04_bitinghabit.setTextOff(null);

        final int image3[] = {
                R.drawable.search_04_bitinghabit,
                R.drawable.search_04_bitinghabit_tap,
        };
        gomin_04_bitinghabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_bitinghabit.isChecked()) {
                    gomin_04_bitinghabit.setBackgroundResource(image3[1]);


                } else {
                    gomin_04_bitinghabit.setBackgroundResource(image3[0]);

                }
            }
        });


        gomin_04_dungproblems.setText(null);
        gomin_04_dungproblems.setTextOn(null);
        gomin_04_dungproblems.setTextOff(null);

        final int image4[] = {
                R.drawable.search_04_dungproblems,
                R.drawable.search_04_dungproblems_tap,
        };
        gomin_04_dungproblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_dungproblems.isChecked()) {
                    gomin_04_dungproblems.setBackgroundResource(image4[1]);


                } else {
                    gomin_04_dungproblems.setBackgroundResource(image4[0]);

                }
            }
        });


        gomin_04_sociability.setText(null);
        gomin_04_sociability.setTextOn(null);
        gomin_04_sociability.setTextOff(null);

        final int image5[] = {
                R.drawable.search_04_sociability,
                R.drawable.search_04_sociability_tap,
        };
        gomin_04_sociability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gomin_04_sociability.isChecked()) {
                    gomin_04_sociability.setBackgroundResource(image5[1]);


                } else {
                    gomin_04_sociability.setBackgroundResource(image5[0]);

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
                        Toast.makeText(HumanWriting_Activity.this, "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
                    }
                }
                cropIntent(currentSelectedUri);
                break;

            }

            case PICK_FROM_CAMERA: {

                //카메라캡쳐를 이용해 가져온 이미지
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
                    Toast.makeText(HumanWriting_Activity.this, "무언가 에러가 났엉ㅋ", Toast.LENGTH_LONG).show();
                }

                cropIntent(currentSelectedUri);
                break;
            }
        }
    }

    private void cropIntent(Uri cropUri) {
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

            dialog = new AlertDialog.Builder(HumanWriting_Activity.this)
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
        private String query1; //보낼쿼리
        private String query2; //보낼쿼리
        private String toggle_check;
      //  NetworkDialog networkDialog = new NetworkDialog(MyApplication.getMyApplicationContext());


        public FileUpLoadAsyncTask(String query1, String query2, String toggle_check) {
            this.query1 = query1;
            this.query2 = query2;
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
                builder.addFormDataPart("gomin_title", query1);
                builder.addFormDataPart("gomin_content", query2);
                builder.addFormDataPart("gomin_keyword", toggle_check);
                int fileSize = arrayLists[0].size();

                for (int i = 0; i < fileSize; i++) {
                    File file = arrayLists[0].get(i).file;
                    builder.addFormDataPart("picture", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }
                RequestBody fileUploadBody = builder.build();
                //요청 세팅
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.Main_Gomin)
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
         //   networkDialog.dismiss();
            if (s.equalsIgnoreCase("success")) {

                int fileSize = upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                }
                finish();
                new GominFragment.AsyncGominJSONList().execute();
            } else {
                Toast.makeText(HumanWriting_Activity.this, "파일업로드에 실패했습니다", Toast.LENGTH_LONG).show();
            }
            fileUploadBtn.setEnabled(true);
        }
    }
}
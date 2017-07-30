package com.dev.muslim.pantaumeter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class InputMeteran extends AppCompatActivity {
    private static final String TAG = "InputMeteran";
    TextView id_pel;
    ImageView imageView;
    EditText txtStandIni,txtKelainan;
    Button btnUpload;

    //utilitys for capture
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "PantauMeter";
    private Uri fileUri; // file url to store image/video

    //upload
//  private static final String SERVER_PATH = "http://10.0.3.2:8080/manajemen_pelanggan/api/upload/";

    private static final String SERVER_PATH = "http://192.168.1.9:8080/manajemen_pelanggan/api/upload/";
    private static final int READ_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meteran);
        id_pel= (TextView)findViewById(R.id.idPelanggan);
        imageView = (ImageView)findViewById(R.id.fotoMeter);
        txtStandIni = (EditText)findViewById(R.id.standIni);
        txtKelainan= (EditText)findViewById(R.id.kelainan);
        btnUpload= (Button)findViewById(R.id.btnUpload);


        Intent intent= getIntent();
        id_pel.setText(String.valueOf(intent.getIntExtra("no_pel",0)));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new UploadFileToServer().execute();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

            imageView.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));

    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;


        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

    public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private final ProgressDialog dialog = new ProgressDialog(InputMeteran.this);

        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {

//          uploadImage(); // inside the method paste your file uploading code
            uploadImage2();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
//            progressBar.setVisibility(View.VISIBLE);
            // updating percentage value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");

            Log.d("Loading",String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            // Here if you wish to do future process for ex. move to another activity do here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    //upload image using loopj
    private void uploadImage2(){
        Bitmap bitmap;

//        bitmap=BitmapFactory.decodeFile(fileUri.getPath());
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
//        byte[] data = bos.toByteArray();

        File myFile = new  File(fileUri.getPath());

        SyncHttpClient client = new SyncHttpClient();
        RequestParams params= new RequestParams();
        try {
            params.put("file", photoCompressor(myFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        params.setUseJsonStreamer(false);
        params.put("id_pel",String.valueOf(id_pel.getText()));
        params.put("stand",String.valueOf(txtStandIni.getText()));
        params.put("kelainan",String.valueOf(txtKelainan.getText()));

        client.post(getApplicationContext(),SERVER_PATH, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response"," Success "+response.toString());
//                Toast.makeText(getApplicationContext(),"Success"+response.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response"," Failed "+responseString.toString());
//                Toast.makeText(getBaseContext(),"Failed "+responseString.toString(),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void uploadImage() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            String filePath = getRealPathFromURIPath(fileUri, InputMeteran.this);

            File filePhoto = new File(fileUri.getPath());
            Log.d("FileName", "Filename " + filePhoto.getName());
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoCompressor(filePhoto));
//          RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", photoCompressor(filePhoto).getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), photoCompressor(filePhoto).getName());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
            Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);

            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Log.d("Response", response.raw().message());
                    Log.d("Response", "Success " + response.body().getSuccess());
                    Toast.makeText(getBaseContext(), "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();

                }
                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Log.d("Response", "Error " + t.getMessage());
                    Toast.makeText(getBaseContext(), "Canot Upload " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your file storage so that it can read photos", READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public File photoCompressor(File photoFile) {

        Bitmap b = BitmapFactory.decodeFile(photoFile.getAbsolutePath());


        int originalWidth = b.getWidth();
        int originalHeight = b.getHeight();
        int boundWidth = 800;
        int boundHeight = 800;
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        //check if the image needs to be scale width
        if (originalWidth > boundWidth) {
            //scale width to fit
            newWidth = boundWidth;
            //scale height to maintain aspect ratio
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        //now check if we need to scale even the new height
        if (newHeight > boundHeight) {
            //scale height to fit instead
            newHeight = boundHeight;
            //scale width to maintain aspect ratio
            newWidth = (newHeight * originalWidth) / originalHeight;
        }
        Log.i(TAG, "Original Image:" + originalHeight + " x" + originalWidth);
        Log.i(TAG, "New Image:" + newHeight + " x" + newWidth);
        try {
            Bitmap out = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
            FileOutputStream fOut;
            fOut = new FileOutputStream(photoFile);
            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (OutOfMemoryError exception) {
            Log.e(TAG, "OutofMemory excpetion" + exception);
            exception.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found excpetion" + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IO exception excpetion" + e);
            e.printStackTrace();
        }
        return photoFile;
    }

}

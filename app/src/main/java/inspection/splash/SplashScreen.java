package inspection.splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.location.LocationRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import inspection.home.RCPOLICYActivity;
import inspection.utility.BaseActivity;
import inspection.utility.ILocationStateListener;
import inspection.utility.LocationTracker;
import inspection.utility.Utility;

import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.Constants;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;


public class SplashScreen extends BaseActivity implements View.OnClickListener, IResponseSubcribe, ILocationStateListener {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 1111;
    Button btnStartVideo;
    String[] perms = {"android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.CALL_PHONE",
            "android.permission.READ_PHONE_STATE",


    };
    File file;
    //VideoRequestEntity videoRequestEntity;
    LocationTracker locationTracker;
    LocationRequest locationRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FrontRearFacade frontRearFacade;
    ImageView car_img_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash_screen_inspect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        car_img_gif = (ImageView)findViewById(R.id.car_gif_img);
        Glide.with(this).load(R.drawable.main).into(car_img_gif);

        btnStartVideo = (Button) findViewById(R.id.btnStartVideo);
        btnStartVideo.setOnClickListener(this);
        setSupportActionBar(toolbar);
        locationTracker = new LocationTracker(this);


      //

        if (!checkPermission()) {
            requestPermission();
        } else {
            //location callback method
            locationTracker.setLocationStateListener(this);

            //GoogleApiClient initialisation and location update
            locationTracker.init();

            //GoogleApiclient connect
            locationTracker.onResume();
        }

        file = new File(Environment.getExternalStorageDirectory(), "/POLICYBOSS");
        String baseDir = file.getAbsolutePath();
        //String fileName = "POLICYBOSS.mp4";
        List<String> results = Utility.getListOfFilesPath(baseDir);
        //Utility.setListFrontEntity();


        initSharedPreference();


        //Umesh  26-07
//        GifImageView ivImage = (GifImageView) findViewById(R.id.car_gif);
//        ivImage.setGifImageResource(R.drawable.main);
//        ivImage.setBackgroundResource(R.drawable.splash_gif_animation);
//        Button play = findViewById(R.id.btnplay);
//        AnimationDrawable splashAnimation = (AnimationDrawable) ivImage.getBackground();
//        splashAnimation.start();

        ////////////////


//        file = new File(Environment.getExternalStorageDirectory(), "/POLICYBOSS");
//        String baseDir = file.getAbsolutePath();
//        //String fileName = "POLICYBOSS.mp4";
//        List<String> results = Utility.getListOfFilesPath(baseDir);
//        Utility.setListFrontEntity();
//        File[] file1 = Utility.getListOfFiles(Utility.createDirIfNotExists().getAbsolutePath());
//        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file1[0]);
////
//        //for image
//        MultipartBody.Part imgFile = MultipartBody.Part.createFormData("video", file1[0].getName(), imgBody);
//        HashMap<String, String> body = new HashMap<String, String>();
//        body.put("document_name", "asda");
//        body.put("vehicle_no", "MH45AP0077");
//        new DocumentController(this).uploadDocuments(imgFile, body, this);
//        new DocumentController(this).uploadVideo(imgFile, body, this);
        //for video
        // RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), file1[0]);
        //MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", file1[0].getName(), videoBody);

//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), file1[0]);
//        //  MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
//        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", file1[0].getName(), videoBody);
//
//        HashMap<String, String> vbody = new HashMap<String, String>();
//        vbody.put("document_name", "video_new");
//        vbody.put("vehicle_no", "MH14MW4588");
//        new DocumentController(this).uploadVideo(vFile, vbody, this);

    }

    private void initSharedPreference() {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (Constants.getSharedPreference(this).getBoolean(Constants.SHARED_PREF_ALL_MASTER, true)) {
            frontRearFacade = new FrontRearFacade(this);
            List<FrontRearEntity> frontRearEntities = frontRearFacade.setListFrontEntity();
            frontRearFacade.storeFrontRear(frontRearEntities);

            List<FrontRearEntity> leftEntities = frontRearFacade.setListLeftEntity();
            frontRearFacade.storeLeft(leftEntities);

            List<FrontRearEntity> rightEntities = frontRearFacade.setListRightEntity();
            frontRearFacade.storeRight(rightEntities);

            List<FrontRearEntity> glassEntities = frontRearFacade.setListGlassEntity();
            frontRearFacade.storeGlass(glassEntities);
            editor.putBoolean(Constants.SHARED_PREF_ALL_MASTER, false).commit();
        }
    }

    private boolean checkPermission() {

        int RECORD_AUDIO = ContextCompat.checkSelfPermission(getApplicationContext(), perms[0]);
        int CAMERA = ContextCompat.checkSelfPermission(getApplicationContext(), perms[1]);
        int WRITE_EXTERNAL = ContextCompat.checkSelfPermission(getApplicationContext(), perms[2]);
        int FINE_LOCATION = ContextCompat.checkSelfPermission(getApplicationContext(), perms[3]);
        int READ_SMS = ContextCompat.checkSelfPermission(getApplicationContext(), perms[4]);
        int RECEIVE_SMS = ContextCompat.checkSelfPermission(getApplicationContext(), perms[5]);

        int CALL_PHONE = ContextCompat.checkSelfPermission(getApplicationContext(), perms[6]);
        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(getApplicationContext(), perms[7]);


        //int fineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), perms[7]);
        return RECORD_AUDIO == PackageManager.PERMISSION_GRANTED
                && CAMERA == PackageManager.PERMISSION_GRANTED
                && WRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED
                && FINE_LOCATION == PackageManager.PERMISSION_GRANTED
                && READ_SMS == PackageManager.PERMISSION_GRANTED
                && RECEIVE_SMS == PackageManager.PERMISSION_GRANTED
                && CALL_PHONE == PackageManager.PERMISSION_GRANTED
                && READ_PHONE_STATE == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("MainActivity", "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0) {

                    boolean recordAudio = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean readSms = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean writeSms = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    boolean callPhone = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean phoneState = grantResults[5] == PackageManager.PERMISSION_GRANTED;


                    if (recordAudio && writeExternal && camera && fineLocation && readSms && writeSms && callPhone && phoneState ) {
                        // you can do all necessary steps
                        // new Dialer().getObject().getLeadData(String.valueOf(Utility.EmpCode), this, this);
                        //MainActivity.performClick();
                        //startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        //Toast.makeText(this, "All permission Granted", Toast.LENGTH_SHORT).show();
                    } else {

                        //Permission Denied, You cannot access location data and camera
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            showMessageOKCancel("You need to grant all permissions", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });

                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setTitle("Exit")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnStartVideo) {
            startActivity(new Intent(SplashScreen.this, RCPOLICYActivity.class));
          //  startActivity(new Intent(this, PhotoCaptureActivity.class));
           // startActivity(new Intent(this,MainActivity.class));
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void doFileUpload() {
        String baseDir = file.getAbsolutePath();
        //String fileName = "POLICYBOSS.mp4";
        List<String> results = Utility.getListOfFilesPath(baseDir);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "rn";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
        String urlString = "http://your_website.com/upload_audio_test/upload_audio.php";
        try {
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(results.get(0));
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeBytes("Content-Disposition: form-data; name="uploadedfile";filename="" + selectedPath + """ + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;

            while ((str = inStream.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);
            }
            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected() {
        Location location = locationTracker.mLocation;
    }

    @Override
    public void onConnectionFailed() {

    }

}

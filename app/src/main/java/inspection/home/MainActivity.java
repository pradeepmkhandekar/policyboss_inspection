package inspection.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import inspection.splash.SplashScreen;
import inspection.utility.BaseActivity;
import inspection.utility.Utility;
import okhttp3.MultipartBody;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;


public class MainActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener, IResponseSubcribe {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 1111;
    protected PowerManager.WakeLock mWakeLock;
    boolean recording = false;
    boolean usecamera = true;
    boolean previewRunning = false;
    SurfaceView surfaceView;
    Button btnStop;
    TextView tvRound, tvStart, tvTimer, tvRegNo, tvTimeStamp;
    File root;
    File file;
    Boolean isSDPresent;
    SimpleDateFormat simpleDateFormat;
    String timeStamp;
    EditText etMbNo, etCarNo;
    ProgressDialog uploadingDilog = null;
    String[] perms = {"android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    Animation leftToRight;
    CountDownTimer countDownTimer;
    LinearLayout linearLayout;
    int step = 3;
    private MediaRecorder recorder;
    private SurfaceHolder surfaceHolder;
    private CamcorderProfile camcorderProfile;
    private Camera camera;
    RelativeLayout rlCamview;

    HashMap<String, String> body;
    MultipartBody.Part part;
    RegisterFacade registerFacade;

    public static float megabytesAvailable(File f) {
        StatFs stat = new StatFs(f.getPath());
        long bytesAvailable = (long) stat.getBlockSize()
                * (long) stat.getAvailableBlocks();
        return bytesAvailable / (1024.f * 1024.f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        setContentView(R.layout.activity_main);
        registerFacade = new RegisterFacade(this);
        initWidgets();
        setOnClickListener();
        if (!checkPermission()) {
            requestPermission();
        } else {
            initComs();
        }
        leftToRight = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.left_to_right);
        leftToRight.setFillAfter(true);
        file = Utility.createVideoDirIfNotExists();
        btnStop.performClick();

    }


    public void startAnimation() {
        tvRound.setVisibility(View.VISIBLE);
        tvRound.setAnimation(leftToRight);
        tvRound.startAnimation(leftToRight);
    }

    public void stopAnimation() {
        tvRound.clearAnimation();
    }

        private void initWidgets() {
        uploadingDilog = new ProgressDialog(this);
        uploadingDilog.setMessage("Please Wait Video Uploading...");
        rlCamview = (RelativeLayout) findViewById(R.id.rlCamview);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        btnStop = (Button) findViewById(R.id.btnStop);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvRound = (TextView) findViewById(R.id.tvRound);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvRegNo = (TextView) findViewById(R.id.tvRegNo);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        btnStop.setOnClickListener(this);
    }

    private void initComs() {
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        timeStamp = simpleDateFormat.format(new Date());
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        //  camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        surfaceView = (SurfaceView) findViewById(R.id.preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        isSDPresent = android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);

    }

    private void prepareRecorder() {
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(surfaceHolder.getSurface());
        if (usecamera) {
            camera.lock();
            camera.unlock();
            recorder.setCamera(camera);
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        recorder.setProfile(camcorderProfile);

        /*recorder.setOutputFile(file.getAbsolutePath()+ File.separator +
                new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+ ".mp4");*/
        File dir = new File(file.getAbsolutePath());
        if (dir.exists() && dir.isDirectory()) {
            try {
                FileUtils.cleanDirectory(dir);
            } catch (Exception ex) {

            }

        }
        recorder.setOutputFile(file.getAbsolutePath() + File.separator +
                "inspectvideo" + ".mp4");
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (usecamera) {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        if (!recording && usecamera) {
            if (previewRunning) {
                camera.stopPreview();
            }

            try {

                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

                // You need to choose the most appropriate previewSize for your app
                Camera.Size previewSize = previewSizes.get(0);// .... select one of previewSizes here

                parameters.setPreviewSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
                camera.startPreview();

                previewRunning = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }

        if (recorder != null)
            recorder.release();

        if (usecamera) {
            previewRunning = false;
            camera.lock();
            camera.release();
        }
        finish();
    }

    private boolean checkPermission() {

        int RECORD_AUDIO = ContextCompat.checkSelfPermission(getApplicationContext(), perms[0]);
        int CAMERA = ContextCompat.checkSelfPermission(getApplicationContext(), perms[1]);
        int WRITE_EXTERNAL = ContextCompat.checkSelfPermission(getApplicationContext(), perms[2]);
        int READ_EXTERNAL = ContextCompat.checkSelfPermission(getApplicationContext(), perms[3]);

        //int fineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), perms[7]);
        return RECORD_AUDIO == PackageManager.PERMISSION_GRANTED
                && CAMERA == PackageManager.PERMISSION_GRANTED
                && WRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED
                && READ_EXTERNAL == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0) {

                    boolean recordAudio = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternal = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    if (recordAudio && writeExternal && camera && readExternal) {
                        // you can do all necessary steps
                        // new Dialer().getObject().getLeadData(String.valueOf(Utility.EmpCode), this, this);
                        //MainActivity.performClick();
                        initComs();
                        Toast.makeText(this, "All permission Granted", Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStop:
                if (step == 1) {
                    //takeScreenshot("RCCopy");
                    tvStart.setText("Start the Engine & record the Odometer");
                    step = 2;
                } else if (step == 2) {
                    //takeScreenshot("Odometer");

                    tvStart.setText("Record the Engine & Chasssis number");
                    step = 3;
                } else if (step == 3) {
                    //takeScreenshot("EngineChasssis");
                    showStartAlert("1", "Record 360 Degree view of your car", "OK");
                    tvStart.setText("Click Done after recording 360 degree view");
                    step = 4;
                } else if (step == 4) {
                    stopRecording();
                    btnStop.setText("DONE");
                    stopAnimation();
                }
                /*if (btnStop.getText().equals("START")) {
                    startRecording();
                    btnStop.setText("STOP");
                } else if (btnStop.getText().equals("STOP")) {
                    stopRecording();
                    btnStop.setText("DONE");
                    stopAnimation();
                } else if (btnStop.getText().equals("DONE")) {
                    finish();
                }*/
                break;
        }
    }

    int seconds1 = 0;

    public void startCountDownTimer(long time) {
        tvTimer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(time, 1000) {
            private final int interval = 1000;
            long incTime = 0;

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext

                incTime = (incTime + interval);
                int seconds = (int) (incTime / 1000);
                seconds1 = seconds;

                int minutes = seconds / 60;
                if (minutes == 00) {
                    btnStop.setEnabled(true);
                    // Toast.makeText(MainActivity.this, "One Minutes", Toast.LENGTH_SHORT).show();
                }
                seconds = seconds % 60;
                tvTimer.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
                tvTimeStamp.setText(simpleDateFormat.format(new Date()));
            }

            public void onFinish() {
                //btnStop.performClick();
            }

        };
        countDownTimer.start();
    }

    public int stopCountDownTimer() {
        int data = 0;
        if (seconds1 >= 50 && seconds1 <= 70)
        // if(seconds1>=10 && seconds1<=30 )
        {
            tvTimer.setVisibility(View.GONE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            data = seconds1;
        } else {
            data = seconds1;

        }

        return data;
    }

    public void startRecording() {
        startCountDownTimer(Utility.TIME);
        startAnimation();
        prepareRecorder();
        if (!recording) {
            recording = true;
            recorder.start();
        }
    }

    private void stopRecording() {
        int data = stopCountDownTimer();
        if (data >= 50 && data <= 70)      //05
        //if (data>=10 && data<=30)
        {
            //showFinishAlert("1", "Success!", "Ok");
            if (recording) {
                try {
                    recorder.stop();
                    if (usecamera) {
                        try {
                            camera.reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // recorder.release();
                    recording = false;
                    // Let's prepareRecorder so we can record again
                    //prepareRecorder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //new LongOperation().execute("");
            showFinishAlert("1", "Video prepared successfully !", "Ok");

        } else if (data < 50) {     // 05

            ShowDialog("You can not upload this video due to short  video capture. Do you want to retake video?");

        } else {

            ShowDialog("You can not upload this video due to long  video capture. Do you want to retake video?");

        }

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
//        cancelDialog();
//        if (response instanceof DocumentResponse) {
//            Toast.makeText(this, "Video Uploaded Succesfully ", Toast.LENGTH_SHORT).show();
//            //startActivity(new Intent(MainActivity.this, DeclareSelfActivity2.class));
//            startActivity(new Intent(MainActivity.this, DeclareSelfActivity2.class));
//			 // startActivity(new Intent(MainActivity.this, DeclareSelfActivity.class));
//        }
    }

    @Override
    public void OnFailure(Throwable t) {
//        cancelDialog();
//        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }


    private void showAlert(final String typ, String title, String btnName) {

        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);
            String strBody = "";

            if (typ.equals("1")) {
                strBody = "Keep your Car Registration Certificate(RC) and \nprevious year policy ready.";

            } else if (typ.equals("2")) {
                strBody = "Please keep the bonnet open and sit on the Driver \nseat. Kindly sure all window glasses of your\ncar are up during the video inspection process.";
            } else if (typ.equals("3")) {
                strBody = "Click Record button and follow the instructions\nShown at the bottom of the next screen. Click DONE\nWhen you complete each instruction.";
            }
            builder.setMessage(strBody);

            final String positiveText = btnName;
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (positiveText.equals("RECORD")) {
                                startCountDownTimer(Utility.TIME);
                                startRecording();
                            }

                            if (typ.equals("1")) {
                                showAlert("2", "Step 2:", "YES ITS READY");
                            } else if (typ.equals("2")) {
                                showAlert("3", "Step 3:", "RECORD");
                            }

                        }
                    });
            //   final android.support.v7.app.AlertDialog dialog = builder.create();
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFinishAlert(final String typ, String title, String btnName) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);
            String strBody = "";

            if (typ.equals("1")) {
                strBody = "Do you want to upload the video?? ";

            } else if (typ.equals("2")) {
                strBody = "Please keep the bonnet open and sit on the Driver \nseat. Kindly sure all window glasses of your\ncar are up during the video inspection process.";
            } else if (typ.equals("3")) {
                strBody = "Click Record button and follow the instructions\nShown at the bottom of the next screen. Click DONE\nWhen you complete each instruction.";
            }
            builder.setMessage(strBody);

            final String positiveText = btnName;
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            File[] files = Utility.getListOfFiles(file.getAbsolutePath());
//                            part = Utility.getMultipartVideo(files[0]);
//                            body = registerFacade.getHashMap("policyboss");
//                            showDialog("Uploading Video...");
//                            new DocumentController(MainActivity.this).uploadVideo(part, body, MainActivity.this);
                            startActivity(new Intent(MainActivity.this, PreviewVideoActivity.class));
                            finish();
                        }
                    });
            builder.setNegativeButton("Retake",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startRecording();
                        }
                    });
            final AlertDialog dialog = builder.create();
            //  final android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

//    public void uploadFile(File fileName){
//
//       // Toast.makeText(this, "TEst"+fileName, Toast.LENGTH_SHORT).show();
//
//        uploadingDilog.show();
//        new LongOperation().execute();
//
//
//    }


    private void showStartAlert(final String typ, String title, String btnName) {

        try {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.layout_dialog);
            Button text = dialog.findViewById(R.id.tvOk);
            ImageView image = (ImageView) dialog.findViewById(R.id.ivGif);
            Glide.with(this)
                    .load(R.drawable.car_gif)
                    .into(image);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = 1500;
            ; // Width
            lp.height = lp.WRAP_CONTENT; // Height
            dialogWindow.setAttributes(lp);

            dialog.show();
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    linearLayout.setVisibility(View.VISIBLE);
                    startRecording();
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);
            String strBody = "";

            if (typ.equals("1")) {
                strBody = "Stand in front side of your car and try to match your speed with frames shown at the top of your screen";

            } else if (typ.equals("2")) {
                strBody = "Please keep the bonnet open and sit on the Driver \nseat. Kindly sure all window glasses of your\ncar are up during the video inspection process.";
            } else if (typ.equals("3")) {
                strBody = "Click Record button and follow the instructions\nShown at the bottom of the next screen. Click DONE\nWhen you complete each instruction.";
            }
            builder.setMessage(strBody);

            final String positiveText = btnName;
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linearLayout.setVisibility(View.VISIBLE);
                            startRecording();
                            dialog.dismiss();
                        }
                    });
            final android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void takeScreenshot(String name) {
        try {
            camera.takePicture(null, null, mPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File dir = Utility.createDirIfNotExists();
            String fileName = "picture" + step + ".jpg";
            fileName = fileName.replaceAll("\\s+", "");
            File imageFile = new File(dir, fileName);

            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    public void ShowDialog(String value) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Message");

        builder.setMessage(value);
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                // dialog.dismiss();
                // startRecording();
                // startRecording();
                // dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ShowDialogForConfirm("Are you sure to stop inspection?");
                // startActivity(new Intent(MainActivity.this, CompanyProfofile.class));
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void ShowDialogForConfirm(String value) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Message");

        builder.setMessage(value);
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, SplashScreen.class));//CompanyProfofile
                // startActivity(new Intent(MainActivity.this,MainActivity.class));
                // dialog.dismiss();
                // startRecording();
                // startRecording();
                // dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        android.app.AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

 /*
    private class LongOperation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

          FTPClient client=new FTPClient();



            try {
                File file =new File(MasterData.leftimage);


                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
              //  client.changeDirectory("/wwwhome/");
               // client.changeDirectory("/");
                client.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            uploadingDilog.hide();
            startActivity(new Intent(MainActivity.this, DeclareSelfActivity2.class));
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
*/
}
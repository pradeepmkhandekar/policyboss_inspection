package inspection.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;


import java.io.File;
import java.util.HashMap;


import inspection.selfdeclaration.DeclareSelfActivity2;
import inspection.utility.BaseActivity;
import inspection.utility.Utility;
import okhttp3.MultipartBody;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.documents.DocumentController;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;
import policyboss.com.inspect.core.inspection.response.DocumentResponse;





public class PreviewVideoActivity extends BaseActivity implements View.OnClickListener, IResponseSubcribe {

    protected PowerManager.WakeLock mWakeLock;
    LinearLayout leanstartvideoandclick, leancapture, leanokandretake;
    Button btnplay, btnOKVIDEO, btnRETAKEVIDEO;
    VideoView videoView, videoPlay;
    String encodedString = null;
    ProgressDialog uploadingDilog = null;
    HashMap<String, String> body;
    MultipartBody.Part part;
    RegisterFacade registerFacade;
    File file;
ImageView car_gif_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_preview_video);

        car_gif_img = (ImageView)findViewById(R.id.car_gif_img);
        Glide.with(this).load(R.drawable.main).into(car_gif_img);
        //GifImageView ivImage = (GifImageView) findViewById(R.id.car_gif);
       // ivImage.setGifImageResource(R.drawable.main);
       // ivImage.setBackgroundResource(R.drawable.splash_gif_animation);

     //   AnimationDrawable splashAnimation = (AnimationDrawable) ivImage.getBackground();
       // splashAnimation.start();
        Button play = findViewById(R.id.btnplay);
        registerFacade = new RegisterFacade(this);
        file = Utility.createVideoDirIfNotExists();
        Init();
        ButtonEvent();

        try {
            File[] files = Utility.getListOfFiles(file.getAbsolutePath());
            File videoFile = new File(files[0].getAbsolutePath());

            if (!videoFile.exists()) {
                showAlert("Video file is not exist");
            } else {
                encodedString = files[0].getAbsolutePath();
            }

            String mUrl = encodedString;
            VideoView mVideoView = (VideoView) findViewById(R.id.videoView);
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mUrl, MediaStore.Images.Thumbnails.MINI_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
            mVideoView.setBackgroundDrawable(bitmapDrawable);


            mVideoView.requestFocus();

            // btnStartVideo.setVisibility(View.GONE);
            leanstartvideoandclick.setVisibility(View.GONE);
            leancapture.setVisibility(View.VISIBLE);
            leanokandretake.setVisibility(View.VISIBLE);

            //mVideoView.start();


        } catch (Exception ex) {

        }


    }

    private void Init() {

        uploadingDilog = new ProgressDialog(this);
        uploadingDilog.setMessage("Please Wait Video Uploading...");
        uploadingDilog.setCancelable(false);
        leanstartvideoandclick = findViewById(R.id.leanstartvideoandclick);
        leancapture = findViewById(R.id.leancapture);
        leanokandretake = findViewById(R.id.leanokandretake);
//        btnStartVideo = findViewById(R.id.btnStartVideo);
        btnplay = findViewById(R.id.btnplay);
        btnOKVIDEO = findViewById(R.id.btnOKVIDEO);
        btnRETAKEVIDEO = findViewById(R.id.btnRETAKEVIDEO);
        videoView = findViewById(R.id.videoView);

    }

    private void ButtonEvent() {
//        btnStartVideo.setOnClickListener(this);
        btnOKVIDEO.setOnClickListener(this);
        btnRETAKEVIDEO.setOnClickListener(this);
        btnplay.setOnClickListener(this);
    }



    public void showVideo() {
        File[] files = Utility.getListOfFiles(file.getAbsolutePath());
        File videoFile = new File(files[0].getAbsolutePath());

        if (!videoFile.exists()) {
          showAlert("Video file is not exist");
        } else {
            encodedString = files[0].getAbsolutePath();
        }
        final Dialog dialog = new Dialog(PreviewVideoActivity.this);
        dialog.setContentView(R.layout.layout_video_play);
        // Button btnClose=dialog.findViewById(R.id.btnCloseDilog);
        VideoView mVideoView = dialog.findViewById(R.id.video_view);


        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = lp.MATCH_PARENT;// Width
        lp.height = lp.MATCH_PARENT; // Height
        dialogWindow.setAttributes(lp);
        MediaController videoMediaController = new MediaController(this);
        mVideoView.setVideoPath(encodedString);
        videoMediaController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(videoMediaController);
        mVideoView.requestFocus();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
            }
        });
        Log.e("Vishnu", String.valueOf(videoView.getDuration()));
        // Toast.makeText(, videoView.getDuration(), Toast.LENGTH_SHORT).show();
        mVideoView.start();
        dialog.show();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnStartVideo:

                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnRETAKEVIDEO:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnOKVIDEO:

              //  uploadingDilog.show();
                showDialog("Please Wait Video Uploading...");

                File[] files = Utility.getListOfFiles(file.getAbsolutePath());
                File videoFile = new File(files[0].getAbsolutePath());

                if (!videoFile.exists()) {
                    showAlert("Video file is not exist");
                } else {
                   // encodedString = files[0].getAbsolutePath();

                    part = Utility.getMultipartVideo(files[0]);

                    ///  Static Data
                    body = registerFacade.getHashMap("policyboss", "mh04gj4131", "MH00123");   // 05 temp
                    new DocumentController(PreviewVideoActivity.this).uploadVideo(part, body, PreviewVideoActivity.this);
                }
                   startActivity(new Intent(PreviewVideoActivity.this, DeclareSelfActivity2.class));


                break;
            case R.id.btnplay:
                showVideo();
                //  startActivity(new Intent(this, OtpPage.class));
                break;

        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof DocumentResponse) {
            Toast.makeText(this, "Video Uploaded Succesfully ", Toast.LENGTH_SHORT).show();
          //  startActivity(new Intent(PreviewVideoActivity.this, DeclareSelfActivity2.class));
        }
    }

    @Override
    public void OnFailure(Throwable t) {

        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

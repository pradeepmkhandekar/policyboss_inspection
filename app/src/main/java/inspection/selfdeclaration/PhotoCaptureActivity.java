package inspection.selfdeclaration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;


import inspection.activities.ThankYouActivity;
import inspection.utility.BaseActivity;
import inspection.utility.Utility;
import okhttp3.MultipartBody;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.documents.DocumentController;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;
import policyboss.com.inspect.core.inspection.response.DocumentResponse;


public class PhotoCaptureActivity extends BaseActivity implements View.OnClickListener, IResponseSubcribe {

    private static final int CAMERA_REQUEST_FRONT = 1888;
    private static final int CAMERA_REQUEST_REAR = 1889;
    private static final int CAMERA_REQUEST_LEFT = 1890;
    private static final int CAMERA_REQUEST_RIGHT = 1891;
    private static final int CAMERA_REQUEST_CORNER1 = 1892;
    private static final int CAMERA_REQUEST_CORNER2 = 1893;
    private static final int CAMERA_REQUEST_CORNER3 = 1894;
    private static final int CAMERA_REQUEST_CORNER4 = 1895;
    private static final int CAMERA_REQUEST_UNDER = 1896;
    private static final int CAMERA_REQUEST_ROOF = 1897;
    private static final int SELECT_PICTURE = 1800;

    ImageView ivFront, ivRear, ivLeft, ivRight, ivCorner1, ivCorner2, ivCorner3, ivCorner4, ivTop, ivBottom;
    Button btnCont;
    private String userChoosenTask;
    HashMap<String, String> body;
    MultipartBody.Part part;
    File file;
    RegisterFacade registerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_photo_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFacade = new RegisterFacade(this);
        init_widgets();
        setListener();
    }

    private void setListener() {
        ivFront.setOnClickListener(this);
        ivRear.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivCorner1.setOnClickListener(this);
        ivCorner2.setOnClickListener(this);
        ivCorner3.setOnClickListener(this);
        ivCorner4.setOnClickListener(this);
        ivTop.setOnClickListener(this);
        ivBottom.setOnClickListener(this);
        btnCont.setOnClickListener(this);
    }

    private void init_widgets() {
        ivFront = (ImageView) findViewById(R.id.ivFront);
        ivRear = (ImageView) findViewById(R.id.ivRear);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivCorner1 = (ImageView) findViewById(R.id.ivCorner1);
        ivCorner2 = (ImageView) findViewById(R.id.ivCorner2);
        ivCorner3 = (ImageView) findViewById(R.id.ivCorner3);
        ivCorner4 = (ImageView) findViewById(R.id.ivCorner4);
        ivTop = (ImageView) findViewById(R.id.ivTop);
        ivBottom = (ImageView) findViewById(R.id.ivBottom);
        btnCont= (Button) findViewById(R.id.btnCont);
    }

    private void selectImage(final int CAMERA_REQUEST) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoCaptureActivity.this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utility.checkPermission(MainActivity.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    launchCamera(CAMERA_REQUEST);

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    openGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void launchCamera(int CAMERA_REQUEST) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {

                case CAMERA_REQUEST_FRONT:
                    setImage(ivFront, mphoto, "FRONT");
                    break;

                case CAMERA_REQUEST_REAR:
                    setImage(ivRear, mphoto, "REAR");
                    break;

                case CAMERA_REQUEST_LEFT:
                    setImage(ivLeft, mphoto, "LEFT");
                    break;
                case CAMERA_REQUEST_RIGHT:
                    setImage(ivRight, mphoto, "RIGHT");
                    break;

                case CAMERA_REQUEST_CORNER1:
                    setImage(ivCorner1, mphoto, "CORNER1");
                    break;

                case CAMERA_REQUEST_CORNER2:
                    setImage(ivCorner2, mphoto, "CORNER2");
                    break;

                case CAMERA_REQUEST_CORNER3:
                    setImage(ivCorner3, mphoto, "CORNER3");
                    break;
                case CAMERA_REQUEST_CORNER4:
                    setImage(ivCorner4, mphoto, "CORNER4");
                    break;
                case CAMERA_REQUEST_UNDER:
                    setImage(ivBottom, mphoto, "UNDER");
                    break;

                case CAMERA_REQUEST_ROOF:
                    setImage(ivTop, mphoto, "ROOF");
                    break;

            }
        }
     /*   if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(mphoto);


           *//* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            basestring = Base64.encodeToString(byteArray, Base64.DEFAULT);*//*
            // String base64String = convertBitmapToBase64(mphoto);

        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);
                // Log.i(TAG, "Image Path : " + path);
                // Set the image in ImageView
                imageView.setImageURI(selectedImageUri);

                updateProfilePic(BitmapUtil.decodeUriAsBitmap(this, selectedImageUri));
            }
        }*/
    }

    public void setImage(ImageView imageView, Bitmap bitmap, String name) {
        imageView.setImageBitmap(bitmap);
        file = saveImageToStorage(bitmap, name);
        part = Utility.getMultipartImage(file);
        body = registerFacade.getHashMap(name);
        new DocumentController(this).uploadDocuments(part, body, this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ivFront) {
            launchCamera(CAMERA_REQUEST_FRONT);

        } else if (i == R.id.ivRear) {
            launchCamera(CAMERA_REQUEST_REAR);

        } else if (i == R.id.ivLeft) {
            launchCamera(CAMERA_REQUEST_LEFT);

        } else if (i == R.id.ivRight) {
            launchCamera(CAMERA_REQUEST_RIGHT);

        } else if (i == R.id.ivCorner1) {
            launchCamera(CAMERA_REQUEST_CORNER1);

        } else if (i == R.id.ivCorner2) {
            launchCamera(CAMERA_REQUEST_CORNER2);

        } else if (i == R.id.ivCorner3) {
            launchCamera(CAMERA_REQUEST_CORNER3);

        } else if (i == R.id.ivCorner4) {
            launchCamera(CAMERA_REQUEST_CORNER4);

        } else if (i == R.id.ivBottom) {
            launchCamera(CAMERA_REQUEST_UNDER);

        } else if (i == R.id.ivTop) {
            launchCamera(CAMERA_REQUEST_ROOF);

        }
        else if (i == R.id.btnCont) {
            startActivity(new Intent(PhotoCaptureActivity.this, ThankYouActivity.class));

        }
    }

    public File saveImageToStorage(Bitmap bitmap, String name) {
        FileOutputStream outStream = null;
        File dir = Utility.createDirIfNotExists();
        //String fileName = String.format("%d.jpg", frontRearEntity.getName() /*+ "-" + System.currentTimeMillis()*/);
        String fileName = name + ".jpg";
        fileName = fileName.replaceAll("\\s+", "");
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof DocumentResponse) {
            Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.action_done) {// Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            startActivity(new Intent(PhotoCaptureActivity.this, ThankYouActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadImageFromFile();
    }

    private void loadImageFromFile() {

        Bitmap bitmap = Utility.checkIfFileExhist("FRONT");
        if (bitmap != null)
            ivFront.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("REAR");
        if (bitmap != null)
            ivRear.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("LEFT");
        if (bitmap != null)
            ivLeft.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("RIGHT");
        if (bitmap != null)
            ivRight.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("CORNER1");
        if (bitmap != null)
            ivCorner1.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("CORNER2");
        if (bitmap != null)
            ivCorner2.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("CORNER3");
        if (bitmap != null)
            ivCorner3.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("CORNER4");
        if (bitmap != null)
            ivCorner4.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("UNDER");
        if (bitmap != null)
            ivBottom.setImageBitmap(bitmap);

        bitmap = Utility.checkIfFileExhist("ROOF");
        if (bitmap != null)
            ivTop.setImageBitmap(bitmap);
    }
}

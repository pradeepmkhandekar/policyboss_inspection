package inspection.home;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


import inspection.utility.BaseActivity;
import inspection.utility.Utility;
import okhttp3.MultipartBody;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.documents.DocumentController;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;
import policyboss.com.inspect.core.inspection.response.DocumentResponse;

public class UploadDocumentActivity extends BaseActivity implements View.OnClickListener, IResponseSubcribe {

    ImageView ivRc, ivPolicy, ivChassis;
    Button btnRc, btnPolicy, btnChasis, btnCont;
    String userChoosenTask;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1800;
    int type;
    RegisterFacade registerFacade;
    HashMap<String, String> body;
    MultipartBody.Part part;
    File file;
    Boolean isRcClicked, isPolicyClicked, isChassisClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_upload_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFacade = new RegisterFacade(this);
        initWidgets();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadImageFromFile();
    }

    private void loadImageFromFile() {
        Bitmap rccopy = Utility.checkIfFileExhist("rccopy");
        if (rccopy != null)
            ivRc.setImageBitmap(rccopy);
        Bitmap policycopy = Utility.checkIfFileExhist("policycopy");
        if (policycopy != null)
            ivPolicy.setImageBitmap(policycopy);
        Bitmap chassis = Utility.checkIfFileExhist("chassis");
        if (chassis != null)
            ivChassis.setImageBitmap(chassis);
    }

    private void setListener() {
        btnChasis.setOnClickListener(this);
        btnPolicy.setOnClickListener(this);
        btnRc.setOnClickListener(this);
        btnCont.setOnClickListener(this);
    }

    private void initWidgets() {
        ivRc = (ImageView) findViewById(R.id.ivRc);
        ivPolicy = (ImageView) findViewById(R.id.ivPolicy);
        ivChassis = (ImageView) findViewById(R.id.ivChassis);
        btnRc = (Button) findViewById(R.id.btnRc);
        btnPolicy = (Button) findViewById(R.id.btnPolicy);
        btnChasis = (Button) findViewById(R.id.btnChasis);
        btnCont = (Button) findViewById(R.id.btnCont);

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnRc) {
            type = 0;
            selectImage();

        } else if (i == R.id.btnPolicy) {
            type = 1;
            launchCamera();

        } else if (i == R.id.btnChasis) {
            type = 2;
            selectImage();

        } else if (i == R.id.btnCont) {
            startActivity(new Intent(this, MainActivity.class));
               /* if (isChassisClicked && isPolicyClicked && isRcClicked)

                else
                    Toast.makeText(this, "Please click pictures of all documents", Toast.LENGTH_SHORT).show();
                break;*/
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocumentActivity.this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utility.checkPermission(MainActivity.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    launchCamera();

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

    private void launchCamera() {
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
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            switch (type) {
                case 0:
                    isRcClicked = true;
                    ivRc.setImageBitmap(mphoto);
                    file = saveImageToStorage(mphoto, "rccopy");
                    part = Utility.getMultipartImage(file);
                    body = Utility.getBody(this, "rccopy");
                    new DocumentController(this).uploadDocuments(part, body, this);
                    break;
                case 1:
                    isPolicyClicked = true;
                    ivPolicy.setImageBitmap(mphoto);
                    file = saveImageToStorage(mphoto, "policycopy");
                    part = Utility.getMultipartImage(file);
                    new DocumentController(this).uploadDocuments(part, body, this);
                    break;
                case 2:
                    isChassisClicked = true;
                    ivChassis.setImageBitmap(mphoto);
                    file = saveImageToStorage(mphoto, "chassis");
                    part = Utility.getMultipartImage(file);
                    body = registerFacade.getHashMap("chassis");
                    new DocumentController(this).uploadDocuments(part, body, this);
                    break;
            }
            //imageView.setImageBitmap(mphoto);


           /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            basestring = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
            // String base64String = convertBitmapToBase64(mphoto);

        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Bitmap mphoto = null;
            try {
                mphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                switch (type) {
                    case 0:
                        isRcClicked = true;
                        ivRc.setImageBitmap(mphoto);
                        file = saveImageToStorage(mphoto, "rccopy");
                        part = Utility.getMultipartImage(file);
                        body = Utility.getBody(this, "rccopy");
                        new DocumentController(this).uploadDocuments(part, body, this);
                        break;
                    case 1:
                        isPolicyClicked = true;
                        ivPolicy.setImageBitmap(mphoto);
                        file = saveImageToStorage(mphoto, "policycopy");
                        part = Utility.getMultipartImage(file);
                        new DocumentController(this).uploadDocuments(part, body, this);
                        break;
                    case 2:
                        isChassisClicked = true;
                        ivChassis.setImageBitmap(mphoto);
                        file = saveImageToStorage(mphoto, "chassis");
                        part = Utility.getMultipartImage(file);
                        body = registerFacade.getHashMap("chassis");
                        new DocumentController(this).uploadDocuments(part, body, this);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Get the path from the Uri
            //String path = getPathFromURI(selectedImageUri);

        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof DocumentResponse) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}

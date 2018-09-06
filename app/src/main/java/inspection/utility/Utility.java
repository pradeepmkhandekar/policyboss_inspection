package inspection.utility;



/**
 * Created by Rajeev Ranjan on 04/12/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Nilesh Birhade on 11-01-2018.
 */

public class Utility {
    public static List<FrontRearEntity> frontRearEntities = new ArrayList<FrontRearEntity>();
    public static List<FrontRearEntity> leftRearEntities = new ArrayList<FrontRearEntity>();
    public static List<FrontRearEntity> righttRearEntities = new ArrayList<FrontRearEntity>();
    public static List<FrontRearEntity> glassRearEntities = new ArrayList<FrontRearEntity>();
    public static long TIME = 4 * 60000;

    public static String getBase64String(String filePath) {
        return Base64.encodeToString(getByteArray(filePath), 0);
    }

    public static byte[] getByteArray(String filePath) {
        File file = new File(filePath);
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static List<String> getListOfFilesPath(String folderPath) {
        List<String> results = new ArrayList<String>();
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null && listOfFiles.length > 0) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].length() > 0) {
                    Log.d("File ", listOfFiles[i].getName());
                    results.add(folderPath + "/" + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    Log.d("Directory ", listOfFiles[i].getName());
                }
            }
        }
        return results;
    }

    public static File[] getListOfFiles(String folderPath) {
        List<String> results = new ArrayList<String>();
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();



        return listOfFiles;
    }

    public static File createDirIfNotExists() {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), "/POLICYBOSS");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return file;
    }

    public static File createVideoDirIfNotExists() {
        boolean ret = true;

        // File file = new File(Environment.getExternalStorageDirectory(), "/POLICYBOSS/VIDEO");
        File file = new File(Environment.getExternalStorageDirectory(), "/POLICYBOSS");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return file;
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
                byte[] byteArray = os.toByteArray();
                return Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDcimDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "RESTORED";
    }

    public static void setListFrontEntity() {
        frontRearEntities.add(new FrontRearEntity("Front Bumper", 0));
        frontRearEntities.add(new FrontRearEntity("Indicator Light", 1));
        frontRearEntities.add(new FrontRearEntity("Front Panel", 2));
        frontRearEntities.add(new FrontRearEntity("Dicky", 3));
        frontRearEntities.add(new FrontRearEntity("Grill", 4));
        frontRearEntities.add(new FrontRearEntity("Indicator Light", 5));
        frontRearEntities.add(new FrontRearEntity("Bonnet", 6));
        frontRearEntities.add(new FrontRearEntity("Rear Bumper", 7));
        frontRearEntities.add(new FrontRearEntity("Head Lamp(LT)", 8));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(LT)", 9));
        frontRearEntities.add(new FrontRearEntity("Left Apron", 10));
        frontRearEntities.add(new FrontRearEntity("Tail Lamp(LT)", 11));
        frontRearEntities.add(new FrontRearEntity("Head Lamp(RT)", 12));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(RT)", 13));
        frontRearEntities.add(new FrontRearEntity("Right Apron", 14));
        frontRearEntities.add(new FrontRearEntity("Tail Lamp(RT)", 15));
    }

    public static List<FrontRearEntity> getListFrontEntity() {
        return frontRearEntities;
    }

    public static MultipartBody.Part getMultipartImage(File file) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData("doc", file.getName(), imgBody);
        return imgFile;
    }

    public static MultipartBody.Part getMultipartVideo(File file) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData("video", file.getName(), imgBody);
        return imgFile;
    }

    public static HashMap<String, String> getBody(Context context, String docName) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("document_name", docName);
        body.put("vehicle_no", new RegisterFacade(context).getUser().getVehicle_no());
        body.put("vehicle_id", new RegisterFacade(context).getUser().getVehicle_id());
        return body;
    }

    public static Bitmap checkIfFileExhist(String name) {
        Bitmap myBitmap = null;
        File dir = Utility.createDirIfNotExists();
        String fileName = name + ".jpg";
        fileName = fileName.replaceAll("\\s+", "");
        File imgFile = new File(dir, fileName);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

}

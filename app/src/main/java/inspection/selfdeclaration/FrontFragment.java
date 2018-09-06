package inspection.selfdeclaration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;


import inspection.selfdeclaration.adapters.FrontAdapter;
import inspection.utility.BaseFragment;
import inspection.utility.SimpleDividerItemDecoration;
import inspection.utility.Utility;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Rajeev Ranjan on 11/12/2017.
 */

public class FrontFragment extends BaseFragment {
    RecyclerView frontRecycler;
    FrontAdapter mAdapter;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1800;
    List<FrontRearEntity> frontRearEntities;
    FrontRearFacade frontRearFacade;

    public FrontFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_front, container, false);
        intit_View(view);
        frontRearFacade = new FrontRearFacade(getActivity());
        frontRearEntities = frontRearFacade.getFrontRearList();
        mAdapter = new FrontAdapter(this, frontRearEntities);
        frontRecycler.setAdapter(mAdapter);
        return view;
    }

    private void intit_View(View view) {
        frontRecycler = (RecyclerView) view.findViewById(R.id.rvFront);
        frontRecycler.setHasFixedSize(true);
        frontRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        frontRecycler.setLayoutManager(mLayoutManager);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            frontRearEntity.setBitmap(mphoto);
            mAdapter.updateAdapter(frontRearEntity, position);
            saveImageToStorage(frontRearEntity);
            /*imageView.setImageBitmap(mphoto);


           *//* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            basestring = Base64.encodeToString(byteArray, Base64.DEFAULT);*//*
            // String base64String = convertBitmapToBase64(mphoto);*/

        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                //String path = getPathFromURI(selectedImageUri);
                // Log.i(TAG, "Image Path : " + path);
                // Set the image in ImageView

                try {
                    InputStream image_stream = null;
                    image_stream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
                    frontRearEntity.setBitmap(bitmap);
                    mAdapter.updateAdapter(frontRearEntity, position);
                    saveImageToStorage(frontRearEntity);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveImageToStorage(FrontRearEntity frontRearEntity) {
        FileOutputStream outStream = null;
        File dir = Utility.createDirIfNotExists();
        //String fileName = String.format("%d.jpg", frontRearEntity.getName() /*+ "-" + System.currentTimeMillis()*/);
        String fileName = frontRearEntity.getName() + ".jpg";
        fileName = fileName.replaceAll("\\s+", "");
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            frontRearEntity.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

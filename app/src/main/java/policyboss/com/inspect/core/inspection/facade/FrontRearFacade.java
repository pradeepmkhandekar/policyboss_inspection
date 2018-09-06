package policyboss.com.inspect.core.inspection.facade;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import policyboss.com.inspect.core.inspection.Constants;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;


/**
 * Created by Rajeev Ranjan on 12/12/2017.
 */

public class FrontRearFacade {

    String TAG = "FrontRearFacade";
    Context mContext;

    public FrontRearFacade(Context context) {
        mContext = context;
    }

    //region FRONT REAR

    public void storeFrontRear(List<FrontRearEntity> frontRearEntities) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.FRONTREAR, gson.toJson(frontRearEntities));
        editor.commit();
    }

    public List<FrontRearEntity> getFrontRearList() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        String frontRearList = sharedPreferences.getString(Constants.FRONTREAR, null);
        if (!frontRearList.matches("")) {
            Type type = new TypeToken<List<FrontRearEntity>>() {}.getType();
            List<FrontRearEntity> frontRearEntities = new Gson().fromJson(frontRearList, type);
            return frontRearEntities;
        }
        return null;
    }
    public List<FrontRearEntity> setListFrontEntity() {
        List<FrontRearEntity> frontRearEntities = new ArrayList<FrontRearEntity>();
        frontRearEntities.add(new FrontRearEntity("Front Bumper", 0));
        frontRearEntities.add(new FrontRearEntity("Front Panel", 1));
        frontRearEntities.add(new FrontRearEntity("Indicator Light(RT)", 2));
        frontRearEntities.add(new FrontRearEntity("Head Lamp(LT)", 3));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(LT)", 4));
        frontRearEntities.add(new FrontRearEntity("Left Apron", 5));

        frontRearEntities.add(new FrontRearEntity("Indicator Light(LT)", 6));
        frontRearEntities.add(new FrontRearEntity("Grill", 7));
        frontRearEntities.add(new FrontRearEntity("Bonnet", 8));
        frontRearEntities.add(new FrontRearEntity("Head Lamp(RT)", 9));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(RT)", 10));
        frontRearEntities.add(new FrontRearEntity("Right Apron", 11));


        /*frontRearEntities.add(new FrontRearEntity("Head Lamp(LT)", 8));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(LT)", 9));
        frontRearEntities.add(new FrontRearEntity("Left Apron", 10));
        frontRearEntities.add(new FrontRearEntity("Tail Lamp(LT)", 11));
        frontRearEntities.add(new FrontRearEntity("Head Lamp(RT)", 12));
        frontRearEntities.add(new FrontRearEntity("Fog Lamp(RT)", 13));
        frontRearEntities.add(new FrontRearEntity("Right Apron", 14));
        frontRearEntities.add(new FrontRearEntity("Tail Lamp(RT)", 15));*/
        return frontRearEntities;
    }
    public List<FrontRearEntity> setListRearEntity(){
        List<FrontRearEntity> rearEntities = new ArrayList<FrontRearEntity>();

        rearEntities.add(new FrontRearEntity("Rear Bumper", 0));

        rearEntities.add(new FrontRearEntity("Dickey Door", 1));
        rearEntities.add(new FrontRearEntity("Tail Lamp(RT)", 2));
        rearEntities.add(new FrontRearEntity("Dickey", 3));
        rearEntities.add(new FrontRearEntity("Tail Lamp(LT)", 4));
        return rearEntities;
    }
    public List<FrontRearEntity> setListLeftEntity(){
        List<FrontRearEntity> leftEntities = new ArrayList<FrontRearEntity>();
        leftEntities.add(new FrontRearEntity("LT Front Door", 0));
        leftEntities.add(new FrontRearEntity("LT Qtr Panel", 1));
        leftEntities.add(new FrontRearEntity("LT Rear Door", 2));
        leftEntities.add(new FrontRearEntity("LT Running Board", 3));
        leftEntities.add(new FrontRearEntity("LT Pillar Door (A)", 4));
        leftEntities.add(new FrontRearEntity("LT Pillar Center (B)", 5));
        leftEntities.add(new FrontRearEntity("LT Pillar Rear (C)", 6));
        return leftEntities;

    }
    public List<FrontRearEntity> setListRightEntity() {
        List<FrontRearEntity> frontRearEntities = new ArrayList<FrontRearEntity>();
        frontRearEntities.add(new FrontRearEntity("RT Qtr Panel", 0));
        frontRearEntities.add(new FrontRearEntity("Floor/Silencer", 1));
        frontRearEntities.add(new FrontRearEntity("RT Rear Pillar(C)", 2));
        frontRearEntities.add(new FrontRearEntity("RT Front Door", 3));
        frontRearEntities.add(new FrontRearEntity("RT Front Fender", 4));
        frontRearEntities.add(new FrontRearEntity("RT Centre Pillar(B)", 5));
        frontRearEntities.add(new FrontRearEntity("RT Rear Door", 6));
        frontRearEntities.add(new FrontRearEntity("Rear View Mirror (LT)", 7));
        frontRearEntities.add(new FrontRearEntity("RT Running Board", 8));
        frontRearEntities.add(new FrontRearEntity("RT Front Pillar(A)", 9));
        return frontRearEntities;
    }


    public List<FrontRearEntity> setListGlassEntity() {
        List<FrontRearEntity> frontRearEntities = new ArrayList<FrontRearEntity>();

        frontRearEntities.add(new FrontRearEntity("Back Glass", 0));
        frontRearEntities.add(new FrontRearEntity("Rim", 1));
        frontRearEntities.add(new FrontRearEntity("Front Windshield", 2));
        frontRearEntities.add(new FrontRearEntity("Under Carriage", 3));
        frontRearEntities.add(new FrontRearEntity("RF Door Glass", 4));
        frontRearEntities.add(new FrontRearEntity("Top Roof", 5));
        frontRearEntities.add(new FrontRearEntity("Dashboard", 6));
        frontRearEntities.add(new FrontRearEntity("Engine", 7));
        frontRearEntities.add(new FrontRearEntity("Suspension", 8));
        frontRearEntities.add(new FrontRearEntity("Radiator", 9));
        frontRearEntities.add(new FrontRearEntity("Drive Shaft", 10));
        frontRearEntities.add(new FrontRearEntity("Brakes", 11));
        frontRearEntities.add(new FrontRearEntity("RR Door Glass", 12));
        frontRearEntities.add(new FrontRearEntity("Roof Lining", 13));
        frontRearEntities.add(new FrontRearEntity("LF Door Glass", 14));
        frontRearEntities.add(new FrontRearEntity("Seats Front", 15));
        frontRearEntities.add(new FrontRearEntity("LR Door Glass", 16));
        frontRearEntities.add(new FrontRearEntity("Seats Rear", 17));
        frontRearEntities.add(new FrontRearEntity("Instrument Meters", 18));
        frontRearEntities.add(new FrontRearEntity("Gearbox", 19));
        frontRearEntities.add(new FrontRearEntity("Steering System", 20));
        frontRearEntities.add(new FrontRearEntity("Air Conditioner", 21));
        frontRearEntities.add(new FrontRearEntity("Wheels", 22));
        frontRearEntities.add(new FrontRearEntity("Music System", 23));
        return frontRearEntities;
    }

    public void updateFrontEntity(FrontRearEntity frontRearEntity, String value) {
        List<FrontRearEntity> frontRearEntities = getFrontRearList();
        for (int i = 0; i < frontRearEntities.size(); i++) {
            FrontRearEntity entity = frontRearEntities.get(i);
            if (entity.getId() == frontRearEntity.getId()) {
                frontRearEntities.get(i).setValue(value);
                storeFrontRear(frontRearEntities);
                break;
            }

        }
    }

    //endregion

    //region LEFT

    public void storeLeft(List<FrontRearEntity> frontRearEntities) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.LEFT, gson.toJson(frontRearEntities));
        editor.commit();
    }

    public List<FrontRearEntity> getLeftList() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);


        String frontRearList = sharedPreferences.getString(Constants.LEFT, null);
        if (!frontRearList.matches("")) {
            Type type = new TypeToken<List<FrontRearEntity>>() {
            }.getType();
            List<FrontRearEntity> frontRearEntities = new Gson().fromJson(frontRearList, type);
            return frontRearEntities;
        }
        return null;
    }

    /*public List<FrontRearEntity> setListLeftEntity() {
        List<FrontRearEntity> frontRearEntities = new ArrayList<FrontRearEntity>();
        frontRearEntities.add(new FrontRearEntity("Front Fender(LT)", 0));
        frontRearEntities.add(new FrontRearEntity("Front Door(LT)", 1));
        frontRearEntities.add(new FrontRearEntity("Rear Door(LT)", 2));
        frontRearEntities.add(new FrontRearEntity("Running Board(LT)", 3));
        frontRearEntities.add(new FrontRearEntity("LT Pillar Door(A)", 4));
        frontRearEntities.add(new FrontRearEntity("LT Pillar Centre(B)", 5));
        frontRearEntities.add(new FrontRearEntity("LT Pillar Rear(C)", 6));
        frontRearEntities.add(new FrontRearEntity("Qtr Panel(LT)", 7));
        return frontRearEntities;
    }*/

    public void updateLeftEntity(FrontRearEntity frontRearEntity, String value) {
        List<FrontRearEntity> frontRearEntities = getLeftList();
        for (int i = 0; i < frontRearEntities.size(); i++) {
            FrontRearEntity entity = frontRearEntities.get(i);
            if (entity.getId() == frontRearEntity.getId()) {
                frontRearEntities.get(i).setValue(value);
                storeLeft(frontRearEntities);
                break;
            }

        }
    }

    //endregion

    //region RIGHT

    public void storeRight(List<FrontRearEntity> frontRearEntities) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.RIGHT, gson.toJson(frontRearEntities));
        editor.commit();
    }

    public List<FrontRearEntity> getRightList() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        String frontRearList = sharedPreferences.getString(Constants.RIGHT, null);
        if (!frontRearList.matches("")) {
            Type type = new TypeToken<List<FrontRearEntity>>() {
            }.getType();
            List<FrontRearEntity> frontRearEntities = new Gson().fromJson(frontRearList, type);
            return frontRearEntities;
        }
        return null;
    }



    public void updateRightEntity(FrontRearEntity frontRearEntity, String value) {
        List<FrontRearEntity> frontRearEntities = getRightList();
        for (int i = 0; i < frontRearEntities.size(); i++) {
            FrontRearEntity entity = frontRearEntities.get(i);
            if (entity.getId() == frontRearEntity.getId()) {
                frontRearEntities.get(i).setValue(value);
                storeRight(frontRearEntities);
                break;
            }

        }
    }

    //endregion

    //region GLASS

    public void storeGlass(List<FrontRearEntity> frontRearEntities) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.GLASS, gson.toJson(frontRearEntities));
        editor.commit();
    }

    public List<FrontRearEntity> getGlassList() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);


        String frontRearList = sharedPreferences.getString(Constants.GLASS, null);
        if (!frontRearList.matches("")) {
            Type type = new TypeToken<List<FrontRearEntity>>() {
            }.getType();
            List<FrontRearEntity> frontRearEntities = new Gson().fromJson(frontRearList, type);
            return frontRearEntities;
        }
        return null;
    }


    public void updateGlassEntity(FrontRearEntity frontRearEntity, String value) {
        List<FrontRearEntity> frontRearEntities = getGlassList();
        for (int i = 0; i < frontRearEntities.size(); i++) {
            FrontRearEntity entity = frontRearEntities.get(i);
            if (entity.getId() == frontRearEntity.getId()) {
                frontRearEntities.get(i).setValue(value);
                storeGlass(frontRearEntities);
                break;
            }

        }
    }

    //endregion
}

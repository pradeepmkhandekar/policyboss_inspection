package policyboss.com.inspect.core.inspection.facade;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

import policyboss.com.inspect.core.inspection.Constants;
import policyboss.com.inspect.core.inspection.response.VehicleRegResponse;


/**
 * Created by Rajeev Ranjan on 18/12/2017.
 */

public class RegisterFacade {
    String TAG = "RegisterFacade";
    Context mContext;

    public RegisterFacade(Context context) {
        mContext = context;
    }

    public void storeUser(VehicleRegResponse.VehicleRegEntity vehicleRegEntity) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.REGISTER, gson.toJson(vehicleRegEntity));
        editor.commit();
    }

    public VehicleRegResponse.VehicleRegEntity getUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, mContext.MODE_PRIVATE);
        String user = sharedPreferences.getString(Constants.REGISTER, "");
        if (!user.matches("")) {
            Gson gson = new Gson();
            VehicleRegResponse.VehicleRegEntity entity = gson.fromJson(user, VehicleRegResponse.VehicleRegEntity.class);
            Log.d(TAG, "get User");
            return entity;
        }
        return null;

    }

    public HashMap<String, String> getHashMap(String document_name) {
        VehicleRegResponse.VehicleRegEntity vehicleRegEntity = getUser();
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("document_name", document_name);
        body.put("vehicle_no", vehicleRegEntity.getVehicle_no());
        body.put("vehicle_id", vehicleRegEntity.getVehicle_id());
        return body;
    }

    public HashMap<String, String> getHashMap(String document_name,String VehicleNo,String VehicleID) {
        VehicleRegResponse.VehicleRegEntity vehicleRegEntity = getUser();
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("document_name", document_name);
        body.put("vehicle_no", VehicleNo);
        body.put("vehicle_id",VehicleID);
        return body;
    }
}

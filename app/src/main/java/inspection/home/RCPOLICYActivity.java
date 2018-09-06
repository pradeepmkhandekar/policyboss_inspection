package inspection.home;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import inspection.utility.BaseActivity;
import inspection.utility.ILocationStateListener;
import inspection.utility.LocationTracker;
import inspection.utility.ReadDeviceID;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.registration.AuthenticationController;
import policyboss.com.inspect.core.inspection.entity.VehRegRequestEntity;
import policyboss.com.inspect.core.inspection.response.GetOtpResponse;
import policyboss.com.inspect.core.inspection.response.VehicleRegResponse;
import policyboss.com.inspect.core.inspection.response.VerifyOtpResponse;


public class RCPOLICYActivity extends BaseActivity implements IResponseSubcribe, ILocationStateListener, View.OnClickListener {

    EditText etMbNo, etCarNo;
    EditText etOtp;
    Button btnProceed;
    LocationTracker locationTracker;
    Location location;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_rcpolicy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        locationTracker = new LocationTracker(this);
        //location callback method
        locationTracker.setLocationStateListener(this);

        //GoogleApiClient initialisation and location update
        locationTracker.init();

        //GoogleApiclient connect
        locationTracker.onResume();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String message = intent.getStringExtra("message");
                String otp = extractDigitFromMessage(message);
                if (!otp.equals("")) {
                    etOtp.setText(otp);
                }
            }
        }
    };

    private String extractDigitFromMessage(String message) {
        //---This will match any 6 digit number in the message, can use "|" to lookup more possible combinations
        Pattern p = Pattern.compile("(|^)\\d{6}");
        try {
            if (message != null) {
                Matcher m = p.matcher(message);
                if (m.find()) {
                    return m.group(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void init() {
        btnProceed = (Button) findViewById(R.id.btnProceed);
        etMbNo = (EditText) findViewById(R.id.etMbNo);
        etCarNo = (EditText) findViewById(R.id.etCarNo);
        etCarNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)});
        btnProceed.setOnClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        location = locationTracker.mLocation;
    }

    @Override
    public void onConnected() {
        location = locationTracker.mLocation;
    }

    @Override
    public void onConnectionFailed() {
        location = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnProceed) {
            if (!isValidePhoneNumber(etMbNo)) {
                etMbNo.requestFocus();
                etMbNo.setError("Enter mobile No : ");
                return;
            }
            if (!isValidVehicle(etCarNo)) {
                etCarNo.requestFocus();
                etCarNo.setError("Enter Car Registration No : ");
                return;
            }

            //TODO: Server hit
            // redirect to upload RC/Policy copy/Chasis No picture capture
            // showDialog("Sending OTP...");
            new AuthenticationController(this).getOtp(etMbNo.getText().toString(), this);
            showOtpAlert();

        }

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof VehicleRegResponse) {
            cancelDialog();
            startActivity(new Intent(this, UploadDocumentActivity.class));
        } else if (response instanceof GetOtpResponse) {
            cancelDialog();
        } else if (response instanceof VerifyOtpResponse) {
            cancelDialog();
            if (response.getStatus() == 0) {
                VehRegRequestEntity vehRegRequestEntity = new VehRegRequestEntity();
                vehRegRequestEntity.setMobile_number(etMbNo.getText().toString());
                vehRegRequestEntity.setVehicle_no(etCarNo.getText().toString());
                vehRegRequestEntity.setDevice_token(new ReadDeviceID(this).getAndroidID());
                if (location != null) {
                    vehRegRequestEntity.setLattitude(location.getLatitude());
                    vehRegRequestEntity.setLongitude(location.getLongitude());
                }
                dialog.dismiss();
                showDialog();
                new AuthenticationController(this).registerVehicle(vehRegRequestEntity, this);
            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showOtpAlert() {

        try {

            dialog = new Dialog(RCPOLICYActivity.this);
            dialog.setContentView(R.layout.otp_dialog_inspect);
            TextView text = (TextView) dialog.findViewById(R.id.tvOk);
            TextView resend = (TextView) dialog.findViewById(R.id.tvResend);
            etOtp = (EditText) dialog.findViewById(R.id.etOtp);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = lp.WRAP_CONTENT;
            ; // Width
            lp.height = lp.WRAP_CONTENT; // Height
            dialogWindow.setAttributes(lp);

            dialog.show();
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    if (etOtp.getText().toString().equals("00000")) {
                        VehRegRequestEntity vehRegRequestEntity = new VehRegRequestEntity();
                        vehRegRequestEntity.setMobile_number(etMbNo.getText().toString());
                        vehRegRequestEntity.setVehicle_no(etCarNo.getText().toString());
                        vehRegRequestEntity.setDevice_token(new ReadDeviceID(RCPOLICYActivity.this).getAndroidID());
                        if (location != null) {
                            vehRegRequestEntity.setLattitude(location.getLatitude());
                            vehRegRequestEntity.setLongitude(location.getLongitude());
                        }
                        showDialog();
                        new AuthenticationController(RCPOLICYActivity.this).registerVehicle(vehRegRequestEntity, RCPOLICYActivity.this);
                        dialog.dismiss();
                    } else {
                        showDialog("Verifying OTP...");
                        new AuthenticationController(RCPOLICYActivity.this).verifyOtp(etMbNo.getText().toString(), etOtp.getText().toString(), RCPOLICYActivity.this);
                    }

                }
            });

            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etOtp.setText("");
                    showDialog("Sending otp...");
                    new AuthenticationController(RCPOLICYActivity.this).getOtp(etMbNo.getText().toString(), RCPOLICYActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

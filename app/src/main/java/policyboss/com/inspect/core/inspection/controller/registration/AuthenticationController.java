package policyboss.com.inspect.core.inspection.controller.registration;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.entity.VehRegRequestEntity;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;
import policyboss.com.inspect.core.inspection.requestbuilder.AuthenticationRequestBuilder;
import policyboss.com.inspect.core.inspection.response.GetOtpResponse;
import policyboss.com.inspect.core.inspection.response.VehicleRegResponse;
import policyboss.com.inspect.core.inspection.response.VerifyOtpResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nilesh Birhade on 14-12-2017.
 */

public class AuthenticationController implements IAuthentication {

    AuthenticationRequestBuilder.AuthenticationNetworkService authenticationNetworkService;
    Context mContext;

    public AuthenticationController(Context context) {
        authenticationNetworkService = new AuthenticationRequestBuilder().getService();
        mContext = context;

    }

    @Override
    public void registerVehicle(VehRegRequestEntity regRequestEntity, final IResponseSubcribe iResponseSubcriber) {

        authenticationNetworkService.registerVehicle(regRequestEntity).enqueue(new Callback<VehicleRegResponse>() {
            @Override
            public void onResponse(Call<VehicleRegResponse> call, Response<VehicleRegResponse> response) {
                if (response.isSuccessful()) {
                    if (iResponseSubcriber != null) {
                        if (response.body().getStatus() == 0) {
                            new RegisterFacade(mContext).storeUser(response.body().getResult());
                            iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                        } else {
                            iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                        }
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to connect to server,Try after sometime..."));
                }
            }

            @Override
            public void onFailure(Call<VehicleRegResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }
            }
        });
    }

    @Override
    public void getOtp(String mobile, final IResponseSubcribe iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("mobile", mobile);
        authenticationNetworkService.getOtp(body).enqueue(new Callback<GetOtpResponse>() {
            @Override
            public void onResponse(Call<GetOtpResponse> call, Response<GetOtpResponse> response) {
                if (response.isSuccessful()) {
                    if (iResponseSubcriber != null) {
                        if (response.body().getStatus() == 0) {
                            iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                        } else {
                            iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                        }
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to connect to server,Try after sometime..."));
                }
            }

            @Override
            public void onFailure(Call<GetOtpResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }
            }
        });
    }

    @Override
    public void verifyOtp(String mobile, String verify_otp, final IResponseSubcribe iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("mobile", mobile);
        body.put("verify_otp", verify_otp);
        authenticationNetworkService.verifyOtp(body).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                if (response.isSuccessful()) {
                    if (iResponseSubcriber != null) {
                        if (response.body().getStatus() == 0) {
                            iResponseSubcriber.OnSuccess(response.body(), response.message());
                        } else {
                            iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                        }
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.message()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to connect to server,Try after sometime..."));
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }
            }
        });
    }


}

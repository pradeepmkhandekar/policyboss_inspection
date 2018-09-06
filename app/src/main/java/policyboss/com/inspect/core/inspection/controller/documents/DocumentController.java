package policyboss.com.inspect.core.inspection.controller.documents;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.MultipartBody;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.entity.VehDetailRequestEntity;
import policyboss.com.inspect.core.inspection.entity.VehSelfDeclarationEntity;
import policyboss.com.inspect.core.inspection.requestbuilder.DocumentsRequestBuilder;
import policyboss.com.inspect.core.inspection.response.DocumentResponse;
import policyboss.com.inspect.core.inspection.response.VehicleDetailResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nilesh Birhade on 14-12-2017.
 */

public class DocumentController implements IDocuments {

    DocumentsRequestBuilder.DocumentsNetworkService documentsNetworkService;
    Context mContext;

    public DocumentController(Context context) {
        documentsNetworkService = new DocumentsRequestBuilder().getService();
        mContext = context;
    }


    @Override
    public void uploadDocuments(MultipartBody.Part document, HashMap<String, String> body, final IResponseSubcribe iResponseSubcriber) {

        documentsNetworkService.uploadDocument(document, body).enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
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
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
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
    public void uploadVideo(MultipartBody.Part document, HashMap<String, String> body, final IResponseSubcribe iResponseSubcriber) {

        documentsNetworkService.uploadVideo(document, body).enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
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
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
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
    public void selfDeclaration(VehSelfDeclarationEntity selfDeclarationEntity, final IResponseSubcribe iResponseSubcriber) {

        documentsNetworkService.selfDeclaration(selfDeclarationEntity).enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
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
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }else  {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }
            }
        });
    }

    @Override
    public void vehicleDetail(VehDetailRequestEntity vehDetailRequestEntity, final IResponseSubcribe iResponseSubcriber) {

        documentsNetworkService.vehicleDetails(vehDetailRequestEntity).enqueue(new Callback<VehicleDetailResponse>() {
            @Override
            public void onResponse(Call<VehicleDetailResponse> call, Response<VehicleDetailResponse> response) {
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
            public void onFailure(Call<VehicleDetailResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }else  {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                }
            }
        });
    }
}

package policyboss.com.inspect.core.inspection.controller.documents;

import java.util.HashMap;

import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.entity.VehDetailRequestEntity;
import policyboss.com.inspect.core.inspection.entity.VehSelfDeclarationEntity;
import okhttp3.MultipartBody;

/**
 * Created by Nilesh Birhade on 14-12-2017.
 */

public interface IDocuments {

    void uploadDocuments(MultipartBody.Part document, HashMap<String, String> body, final IResponseSubcribe iResponseSubcriber);

    void uploadVideo(MultipartBody.Part video, HashMap<String, String> body, final IResponseSubcribe iResponseSubcriber);

    void selfDeclaration(VehSelfDeclarationEntity selfDeclarationEntity, IResponseSubcribe iResponseSubcriber);

    void vehicleDetail(VehDetailRequestEntity vehDetailRequestEntity, IResponseSubcribe iResponseSubcriber);
}

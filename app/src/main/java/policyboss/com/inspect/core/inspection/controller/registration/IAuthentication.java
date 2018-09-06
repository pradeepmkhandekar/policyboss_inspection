package policyboss.com.inspect.core.inspection.controller.registration;


import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.entity.VehRegRequestEntity;

/**
 * Created by Nilesh Birhade on 14-12-2017.
 */

public interface IAuthentication {

    void registerVehicle(VehRegRequestEntity regRequestEntity, final IResponseSubcribe iResponseSubcriber);

    void getOtp(String mobile, final IResponseSubcribe iResponseSubcriber);

    void verifyOtp(String mobile, String verify_otp, final IResponseSubcribe iResponseSubcriber);
}

package policyboss.com.inspect.core.inspection.entity;

/**
 * Created by Nilesh Birhade on 13-12-2017.
 */

public class VehRegRequestEntity {

    /**
     * mobile_number : 9544558740
     * vehicle_no : MH  09 AB 1025
     * lattitude : 65.3233232
     * longitude : 90.4343443
     * device_token : daldjaldjaldaqw33-49234-9324-
     */

    private String mobile_number;
    private String vehicle_no;
    private double lattitude;
    private double longitude;
    private String device_token;

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}

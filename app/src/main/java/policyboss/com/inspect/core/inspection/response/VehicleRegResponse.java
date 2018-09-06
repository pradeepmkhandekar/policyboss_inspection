package policyboss.com.inspect.core.inspection.response;

import policyboss.com.inspect.core.inspection.APIResponse;

/**
 * Created by Nilesh Birhade on 13-12-2017.
 */

public class VehicleRegResponse extends APIResponse {


    /**
     * status : 0
     * message : success
     * result : {"mobile_number":9544558740,"vehicle_no":"MH  09 AB 1025"}
     */


    private VehicleRegEntity result;



    public VehicleRegEntity getResult() {
        return result;
    }

    public void setResult(VehicleRegEntity result) {
        this.result = result;
    }

    public static class VehicleRegEntity {
        /**
         * mobile_number : 9544558740
         * vehicle_no : MH  09 AB 1025
         */

        private long mobile_number;
        private String vehicle_no;
        private String vehicle_id;

        public String getVehicle_id() {
            return vehicle_id;
        }

        public void setVehicle_id(String vehicle_id) {
            this.vehicle_id = vehicle_id;
        }

        public long getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(long mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getVehicle_no() {
            return vehicle_no;
        }

        public void setVehicle_no(String vehicle_no) {
            this.vehicle_no = vehicle_no;
        }
    }
}

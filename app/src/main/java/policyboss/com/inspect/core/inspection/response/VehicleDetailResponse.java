package policyboss.com.inspect.core.inspection.response;

import policyboss.com.inspect.core.inspection.APIResponse;

/**
 * Created by IN-RB on 21-07-2018.
 */


public class VehicleDetailResponse extends APIResponse {


    /**
     * result : {"vehicle_front_detail_id":10,"vehicle_rear_details_id":10,"vehicle_left_details_id":10,"vehicle_right_details_id":10,"vehicle_glass_details_id":10}
     */

    private VehicleDetailEntity result;

    public VehicleDetailEntity getResult() {
        return result;
    }

    public void setResult(VehicleDetailEntity result) {
        this.result = result;
    }

    public static class VehicleDetailEntity {
        /**
         * vehicle_front_detail_id : 10
         * vehicle_rear_details_id : 10
         * vehicle_left_details_id : 10
         * vehicle_right_details_id : 10
         * vehicle_glass_details_id : 10
         */

        private int vehicle_front_detail_id;
        private int vehicle_rear_details_id;
        private int vehicle_left_details_id;
        private int vehicle_right_details_id;
        private int vehicle_glass_details_id;

        public int getVehicle_front_detail_id() {
            return vehicle_front_detail_id;
        }

        public void setVehicle_front_detail_id(int vehicle_front_detail_id) {
            this.vehicle_front_detail_id = vehicle_front_detail_id;
        }

        public int getVehicle_rear_details_id() {
            return vehicle_rear_details_id;
        }

        public void setVehicle_rear_details_id(int vehicle_rear_details_id) {
            this.vehicle_rear_details_id = vehicle_rear_details_id;
        }

        public int getVehicle_left_details_id() {
            return vehicle_left_details_id;
        }

        public void setVehicle_left_details_id(int vehicle_left_details_id) {
            this.vehicle_left_details_id = vehicle_left_details_id;
        }

        public int getVehicle_right_details_id() {
            return vehicle_right_details_id;
        }

        public void setVehicle_right_details_id(int vehicle_right_details_id) {
            this.vehicle_right_details_id = vehicle_right_details_id;
        }

        public int getVehicle_glass_details_id() {
            return vehicle_glass_details_id;
        }

        public void setVehicle_glass_details_id(int vehicle_glass_details_id) {
            this.vehicle_glass_details_id = vehicle_glass_details_id;
        }
    }
}

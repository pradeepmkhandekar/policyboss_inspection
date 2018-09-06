package policyboss.com.inspect.core.inspection.entity;

/**
 * Created by Nilesh Birhade on 15-12-2017.
 */

public class VehSelfDeclarationEntity {

    /**
     * vehicle_no : MH 01 AB 1010
     * front_bumper : 1
     * grill : 1
     * head_lamp_lt : 1
     * head_lamp_rt : 1
     * indicator_light_lt : 1
     * fog_light_lt : 1
     * fog_light_rt : 1
     * front_panel : 1
     * bonnet : 1
     * left_apron : 1
     * right_apron : 2
     * dicky : 3
     * rear_bumper : 1
     * tail_lamp_lt : 1
     * tail_lamp_rt : 1
     * vehicle_condition : 1
     * indicator_light_rt : 1
     * back_glass : 1
     * glass_laminated : 1
     * rf_door_glass : 1
     * rr_door_glass : 1
     * lf_door_glass : 1
     * lr_door_glass : 1
     * rim : 1
     * under_carriage : 1
     * lt_front_fender : 1
     * lt_front_door : 1
     * lt_rear_door : 1
     * lt_running_board : 1
     * lt_pillar_door : 1
     * lt_pillar_center : 1
     * lt_pillar_rear : 1
     * lt_qtr_panel : 1
     * rt_qtr_panel : 1
     * rt_rear_door : 1
     * rt_front_door : 1
     * rt_front_pillar_A : 1
     * rt_center_pillar_B : 1
     * rt_running_board : 1
     * rt_front_fender : 1
     * floor : 1
     * rear_view_mirror_lt : 1
     * rear_view_mirror_rt : 1
     * tyres : 1
     * lt_rear_tyre : 1
     * lt_front_tyre : 1
     * rt_rear_tyre : 1
     * rt_front_tyre : 1
     * rt_center_pillar_C : 1
     * rt_rear_pillar_C : 1
     * vehicle_id : 10
     */

    private String vehicle_no;
    private String  vehicle_id;
    //Front
    private String front_front_bumper;
    private String front_front_panel;
    private String front_indicator_light_RT;
    private String front_head_lamp_LT;
    private String front_fog_lamp_LT;
    private String front_left_apron;
    private String front_indicator_light_LT;
    private String front_grill;
    private String  front_bonnet;
    private String front_head_lamp_RT;
    private String  front_fog_lamp_RT;
    private String  front_right_apron;

    //  (Rear)
    private String rear_rear_bumper;
    private String  rear_dickey_door;
    private String rear_tail_lamp_RT;
    private String  rear_dicky;
    private String rear_tail_lamp_LT;

    //(Left)
    private String  lt_front_door;
    private String   lt_qtr_panel;
    private String   lt_raer_door;
    private String   lt_running_board;
    private String   lt_pillar_board;
    private String   lt_pillar_door_A;
    private String   lt_pillar_center_B;
    private String   lt_pillar_rear_C;


    //  (Right)
    private String  rt_qtr_panel;
    private String  rt_floor_silencer;
    private String  rt_rer_pillar_C;
    private String  rt_front_door;
    private String   rt_front_fender;
    private String   rt_centre_pillar_B;
    private String   rt_rear_door;
    private String   rt_rear_view_mirror_LT;
    private String    rt_running_board;
    private String  rt_front_pillar_A;

    //(Glass Others)
    private String  glass_black_glass;
    private String  glass_rim;
    private String  glass_front_windshield;
    private String  glass_under_carriage;
    private String  glass_rf_door_glass;
    private String  glass_top_roof;
    private String  glass_dashboard;
    private String  glass_engine;
    private String glass_suspension;
    private String  glass_radiator;
    private String  glass_drive_shaft;
    private String  glass_brakes;
    private String   glass_rr_door_glass;
    private String  glass_roof_lining;
    private String glass_lt_door_glass;
    private String  glass_seats_front;
    private String  glass_lr_door_glass;
    private String  glass_seats_rear;
    private String  glass_instrument_meters;
    private String   glass_gear_box;
    private String   glass_steering_system;
    private String   glass_air_conditioner;
    private String    glass_wheels;
    private String    glass_music_system;


    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getFront_front_bumper() {
        return front_front_bumper;
    }

    public void setFront_front_bumper(String front_front_bumper) {
        this.front_front_bumper = front_front_bumper;
    }

    public String getFront_front_panel() {
        return front_front_panel;
    }

    public void setFront_front_panel(String front_front_panel) {
        this.front_front_panel = front_front_panel;
    }

    public String getFront_indicator_light_RT() {
        return front_indicator_light_RT;
    }

    public void setFront_indicator_light_RT(String front_indicator_light_RT) {
        this.front_indicator_light_RT = front_indicator_light_RT;
    }

    public String getFront_head_lamp_LT() {
        return front_head_lamp_LT;
    }

    public void setFront_head_lamp_LT(String front_head_lamp_LT) {
        this.front_head_lamp_LT = front_head_lamp_LT;
    }

    public String getFront_fog_lamp_LT() {
        return front_fog_lamp_LT;
    }

    public void setFront_fog_lamp_LT(String front_fog_lamp_LT) {
        this.front_fog_lamp_LT = front_fog_lamp_LT;
    }

    public String getFront_left_apron() {
        return front_left_apron;
    }

    public void setFront_left_apron(String front_left_apron) {
        this.front_left_apron = front_left_apron;
    }

    public String getFront_indicator_light_LT() {
        return front_indicator_light_LT;
    }

    public void setFront_indicator_light_LT(String front_indicator_light_LT) {
        this.front_indicator_light_LT = front_indicator_light_LT;
    }

    public String getFront_grill() {
        return front_grill;
    }

    public void setFront_grill(String front_grill) {
        this.front_grill = front_grill;
    }

    public String getFront_bonnet() {
        return front_bonnet;
    }

    public void setFront_bonnet(String front_bonnet) {
        this.front_bonnet = front_bonnet;
    }

    public String getFront_head_lamp_RT() {
        return front_head_lamp_RT;
    }

    public void setFront_head_lamp_RT(String front_head_lamp_RT) {
        this.front_head_lamp_RT = front_head_lamp_RT;
    }

    public String getFront_fog_lamp_RT() {
        return front_fog_lamp_RT;
    }

    public void setFront_fog_lamp_RT(String front_fog_lamp_RT) {
        this.front_fog_lamp_RT = front_fog_lamp_RT;
    }

    public String getFront_right_apron() {
        return front_right_apron;
    }

    public void setFront_right_apron(String front_right_apron) {
        this.front_right_apron = front_right_apron;
    }

    public String getRear_rear_bumper() {
        return rear_rear_bumper;
    }

    public void setRear_rear_bumper(String rear_rear_bumper) {
        this.rear_rear_bumper = rear_rear_bumper;
    }

    public String getRear_dickey_door() {
        return rear_dickey_door;
    }

    public void setRear_dickey_door(String rear_dickey_door) {
        this.rear_dickey_door = rear_dickey_door;
    }

    public String getRear_tail_lamp_RT() {
        return rear_tail_lamp_RT;
    }

    public void setRear_tail_lamp_RT(String rear_tail_lamp_RT) {
        this.rear_tail_lamp_RT = rear_tail_lamp_RT;
    }

    public String getRear_dicky() {
        return rear_dicky;
    }

    public void setRear_dicky(String rear_dicky) {
        this.rear_dicky = rear_dicky;
    }

    public String getRear_tail_lamp_LT() {
        return rear_tail_lamp_LT;
    }

    public void setRear_tail_lamp_LT(String rear_tail_lamp_LT) {
        this.rear_tail_lamp_LT = rear_tail_lamp_LT;
    }

    public String getLt_front_door() {
        return lt_front_door;
    }

    public void setLt_front_door(String lt_front_door) {
        this.lt_front_door = lt_front_door;
    }

    public String getLt_qtr_panel() {
        return lt_qtr_panel;
    }

    public void setLt_qtr_panel(String lt_qtr_panel) {
        this.lt_qtr_panel = lt_qtr_panel;
    }

    public String getLt_raer_door() {
        return lt_raer_door;
    }

    public void setLt_raer_door(String lt_raer_door) {
        this.lt_raer_door = lt_raer_door;
    }

    public String getLt_running_board() {
        return lt_running_board;
    }

    public void setLt_running_board(String lt_running_board) {
        this.lt_running_board = lt_running_board;
    }

    public String getLt_pillar_board() {
        return lt_pillar_board;
    }

    public void setLt_pillar_board(String lt_pillar_board) {
        this.lt_pillar_board = lt_pillar_board;
    }

    public String getLt_pillar_door_A() {
        return lt_pillar_door_A;
    }

    public void setLt_pillar_door_A(String lt_pillar_door_A) {
        this.lt_pillar_door_A = lt_pillar_door_A;
    }

    public String getLt_pillar_center_B() {
        return lt_pillar_center_B;
    }

    public void setLt_pillar_center_B(String lt_pillar_center_B) {
        this.lt_pillar_center_B = lt_pillar_center_B;
    }

    public String getLt_pillar_rear_C() {
        return lt_pillar_rear_C;
    }

    public void setLt_pillar_rear_C(String lt_pillar_rear_C) {
        this.lt_pillar_rear_C = lt_pillar_rear_C;
    }

    public String getRt_qtr_panel() {
        return rt_qtr_panel;
    }

    public void setRt_qtr_panel(String rt_qtr_panel) {
        this.rt_qtr_panel = rt_qtr_panel;
    }

    public String getRt_floor_silencer() {
        return rt_floor_silencer;
    }

    public void setRt_floor_silencer(String rt_floor_silencer) {
        this.rt_floor_silencer = rt_floor_silencer;
    }

    public String getRt_rer_pillar_C() {
        return rt_rer_pillar_C;
    }

    public void setRt_rer_pillar_C(String rt_rer_pillar_C) {
        this.rt_rer_pillar_C = rt_rer_pillar_C;
    }

    public String getRt_front_door() {
        return rt_front_door;
    }

    public void setRt_front_door(String rt_front_door) {
        this.rt_front_door = rt_front_door;
    }

    public String getRt_front_fender() {
        return rt_front_fender;
    }

    public void setRt_front_fender(String rt_front_fender) {
        this.rt_front_fender = rt_front_fender;
    }

    public String getRt_centre_pillar_B() {
        return rt_centre_pillar_B;
    }

    public void setRt_centre_pillar_B(String rt_centre_pillar_B) {
        this.rt_centre_pillar_B = rt_centre_pillar_B;
    }

    public String getRt_rear_door() {
        return rt_rear_door;
    }

    public void setRt_rear_door(String rt_rear_door) {
        this.rt_rear_door = rt_rear_door;
    }

    public String getRt_rear_view_mirror_LT() {
        return rt_rear_view_mirror_LT;
    }

    public void setRt_rear_view_mirror_LT(String rt_rear_view_mirror_LT) {
        this.rt_rear_view_mirror_LT = rt_rear_view_mirror_LT;
    }

    public String getRt_running_board() {
        return rt_running_board;
    }

    public void setRt_running_board(String rt_running_board) {
        this.rt_running_board = rt_running_board;
    }

    public String getRt_front_pillar_A() {
        return rt_front_pillar_A;
    }

    public void setRt_front_pillar_A(String rt_front_pillar_A) {
        this.rt_front_pillar_A = rt_front_pillar_A;
    }

    public String getGlass_black_glass() {
        return glass_black_glass;
    }

    public void setGlass_black_glass(String glass_black_glass) {
        this.glass_black_glass = glass_black_glass;
    }

    public String getGlass_rim() {
        return glass_rim;
    }

    public void setGlass_rim(String glass_rim) {
        this.glass_rim = glass_rim;
    }

    public String getGlass_front_windshield() {
        return glass_front_windshield;
    }

    public void setGlass_front_windshield(String glass_front_windshield) {
        this.glass_front_windshield = glass_front_windshield;
    }

    public String getGlass_under_carriage() {
        return glass_under_carriage;
    }

    public void setGlass_under_carriage(String glass_under_carriage) {
        this.glass_under_carriage = glass_under_carriage;
    }

    public String getGlass_rf_door_glass() {
        return glass_rf_door_glass;
    }

    public void setGlass_rf_door_glass(String glass_rf_door_glass) {
        this.glass_rf_door_glass = glass_rf_door_glass;
    }

    public String getGlass_top_roof() {
        return glass_top_roof;
    }

    public void setGlass_top_roof(String glass_top_roof) {
        this.glass_top_roof = glass_top_roof;
    }

    public String getGlass_dashboard() {
        return glass_dashboard;
    }

    public void setGlass_dashboard(String glass_dashboard) {
        this.glass_dashboard = glass_dashboard;
    }

    public String getGlass_engine() {
        return glass_engine;
    }

    public void setGlass_engine(String glass_engine) {
        this.glass_engine = glass_engine;
    }

    public String getGlass_suspension() {
        return glass_suspension;
    }

    public void setGlass_suspension(String glass_suspension) {
        this.glass_suspension = glass_suspension;
    }

    public String getGlass_radiator() {
        return glass_radiator;
    }

    public void setGlass_radiator(String glass_radiator) {
        this.glass_radiator = glass_radiator;
    }

    public String getGlass_drive_shaft() {
        return glass_drive_shaft;
    }

    public void setGlass_drive_shaft(String glass_drive_shaft) {
        this.glass_drive_shaft = glass_drive_shaft;
    }

    public String getGlass_brakes() {
        return glass_brakes;
    }

    public void setGlass_brakes(String glass_brakes) {
        this.glass_brakes = glass_brakes;
    }

    public String getGlass_rr_door_glass() {
        return glass_rr_door_glass;
    }

    public void setGlass_rr_door_glass(String glass_rr_door_glass) {
        this.glass_rr_door_glass = glass_rr_door_glass;
    }

    public String getGlass_roof_lining() {
        return glass_roof_lining;
    }

    public void setGlass_roof_lining(String glass_roof_lining) {
        this.glass_roof_lining = glass_roof_lining;
    }

    public String getGlass_lt_door_glass() {
        return glass_lt_door_glass;
    }

    public void setGlass_lt_door_glass(String glass_lt_door_glass) {
        this.glass_lt_door_glass = glass_lt_door_glass;
    }

    public String getGlass_seats_front() {
        return glass_seats_front;
    }

    public void setGlass_seats_front(String glass_seats_front) {
        this.glass_seats_front = glass_seats_front;
    }

    public String getGlass_lr_door_glass() {
        return glass_lr_door_glass;
    }

    public void setGlass_lr_door_glass(String glass_lr_door_glass) {
        this.glass_lr_door_glass = glass_lr_door_glass;
    }

    public String getGlass_seats_rear() {
        return glass_seats_rear;
    }

    public void setGlass_seats_rear(String glass_seats_rear) {
        this.glass_seats_rear = glass_seats_rear;
    }

    public String getGlass_instrument_meters() {
        return glass_instrument_meters;
    }

    public void setGlass_instrument_meters(String glass_instrument_meters) {
        this.glass_instrument_meters = glass_instrument_meters;
    }

    public String getGlass_gear_box() {
        return glass_gear_box;
    }

    public void setGlass_gear_box(String glass_gear_box) {
        this.glass_gear_box = glass_gear_box;
    }

    public String getGlass_steering_system() {
        return glass_steering_system;
    }

    public void setGlass_steering_system(String glass_steering_system) {
        this.glass_steering_system = glass_steering_system;
    }

    public String getGlass_air_conditioner() {
        return glass_air_conditioner;
    }

    public void setGlass_air_conditioner(String glass_air_conditioner) {
        this.glass_air_conditioner = glass_air_conditioner;
    }

    public String getGlass_wheels() {
        return glass_wheels;
    }

    public void setGlass_wheels(String glass_wheels) {
        this.glass_wheels = glass_wheels;
    }

    public String getGlass_music_system() {
        return glass_music_system;
    }

    public void setGlass_music_system(String glass_music_system) {
        this.glass_music_system = glass_music_system;
    }



}

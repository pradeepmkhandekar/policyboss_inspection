package inspection.selfdeclaration;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import inspection.selfdeclaration.adapters.FrontAdapter;
import inspection.selfdeclaration.adapters.GlassAdapter;
import inspection.selfdeclaration.adapters.LeftAdapter;
import inspection.selfdeclaration.adapters.RightAdapter;
import inspection.utility.BaseActivity;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.documents.DocumentController;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.entity.VehSelfDeclarationEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;
import policyboss.com.inspect.core.inspection.facade.RegisterFacade;
import policyboss.com.inspect.core.inspection.response.DocumentResponse;


public class SelfDeclareActivity extends BaseActivity implements View.OnClickListener, IResponseSubcribe {
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1800;
    // tags used to attach the fragments
    private static final String TAG_FRONT = "front";
    private static final String TAG_LEFT = "left";
    private static final String TAG_RIGHT = "right";
    private static final String TAG_GLASS = "glass";
    public static String CURRENT_TAG = TAG_FRONT;
    public static int selection = 0;
    EditText et_doctype;
    ImageView imageView;
    Button button;
    String basestring = "";
    Toolbar toolbar;
    Button btnFront, btnLeft, btnRight, btnGlass;
    private String userChoosenTask;
    private Handler mHandler;
    FrontRearEntity frontRearEntity;
    int position;
    RegisterFacade registerFacade;

    RecyclerView rvCommon;
    List<FrontRearEntity> frontRearEntities;
    FrontRearFacade frontRearFacade;
    FrontAdapter frontAdapter;
    LeftAdapter leftAdapter;
    RightAdapter rightAdapter;
    GlassAdapter glassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.content_self_declare);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        frontRearFacade = new FrontRearFacade(this);
        registerFacade = new RegisterFacade(this);
        initWidgets();
        setListener();
        mHandler = new Handler();
        setDefaultAdapter(0);
        //loadHomeFragment(TAG_FRONT);
    }

    private void setDefaultAdapter(int selection) {
        switch (selection) {
            case 0:
                frontRearEntities = frontRearFacade.getFrontRearList();
                frontAdapter = new FrontAdapter(this, frontRearEntities);
                rvCommon.setAdapter(frontAdapter);
                break;
            case 1:
                frontRearEntities = frontRearFacade.getLeftList();
                leftAdapter = new LeftAdapter(this, frontRearEntities);
                rvCommon.setAdapter(leftAdapter);
                break;
            case 2:
                frontRearEntities = frontRearFacade.getRightList();
                rightAdapter = new RightAdapter(this, frontRearEntities);
                rvCommon.setAdapter(rightAdapter);
                break;
            case 3:
                frontRearEntities = frontRearFacade.getGlassList();
                glassAdapter = new GlassAdapter(this, frontRearEntities);
                rvCommon.setAdapter(glassAdapter);
                break;
        }

    }

    private void setListener() {
        button.setOnClickListener(this);
        btnGlass.setOnClickListener(this);
        btnFront.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    private void initWidgets() {

        //et_doctype = (EditText) findViewById(R.id.et_doctype);
        btnFront = (Button) findViewById(R.id.btnFront);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnGlass = (Button) findViewById(R.id.btnGlass);
        button = (Button) this.findViewById(R.id.btn_submit);

        rvCommon = (RecyclerView) findViewById(R.id.rvCommon);
        rvCommon.setHasFixedSize(true);
        // rvCommon.addItemDecoration(new SimpleDividerItemDecoration(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvCommon.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_submit) {
            VehSelfDeclarationEntity vehSelfDeclarationEntity;
            vehSelfDeclarationEntity = getAllDetails();
            showDialog();
            new DocumentController(this).selfDeclaration(vehSelfDeclarationEntity, this);

        } else if (i == R.id.btnFront) {
            selection = 0;
            //loadHomeFragment(TAG_FRONT);
            setDefaultAdapter(selection);

        } else if (i == R.id.btnLeft) {
            selection = 1;
            //loadHomeFragment(TAG_LEFT);
            setDefaultAdapter(selection);

        } else if (i == R.id.btnRight) {
            selection = 2;
            //loadHomeFragment(TAG_RIGHT);
            setDefaultAdapter(selection);

        } else if (i == R.id.btnGlass) {
            selection = 3;
            //loadHomeFragment(TAG_GLASS);
            setDefaultAdapter(selection);

        }
    }

    private VehSelfDeclarationEntity getAllDetails() {
        VehSelfDeclarationEntity vehSelfDeclarationEntity = new VehSelfDeclarationEntity();
      /*  vehSelfDeclarationEntity.setVehicle_id(registerFacade.getUser().getVehicle_id());
        vehSelfDeclarationEntity.setVehicle_no(registerFacade.getUser().getVehicle_no());

        List<FrontRearEntity> frontRearEntities = frontRearFacade.getFrontRearList();
        vehSelfDeclarationEntity.setFront_bumper(getId("Front Bumper", frontRearEntities));
        vehSelfDeclarationEntity.setIndicator_light_lt(getId("Indicator Light(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setFront_panel(getId("Front Panel", frontRearEntities));
        vehSelfDeclarationEntity.setDicky(getId("Dicky", frontRearEntities));
        vehSelfDeclarationEntity.setGrill(getId("Grill", frontRearEntities));
        vehSelfDeclarationEntity.setIndicator_light_rt(getId("Indicator Light(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setBonnet(getId("Bonnet", frontRearEntities));
        vehSelfDeclarationEntity.setRear_bumper(getId("Rear Bumper", frontRearEntities));
        vehSelfDeclarationEntity.setHead_lamp_lt(getId("Head Lamp(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setFog_light_lt(getId("Fog Lamp(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setLeft_apron(getId("Left Apron", frontRearEntities));
        vehSelfDeclarationEntity.setTail_lamp_lt(getId("Tail Lamp(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setHead_lamp_rt(getId("Head Lamp(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setFog_light_rt(getId("Fog Lamp(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setRight_apron(getId("Right Apron", frontRearEntities));
        vehSelfDeclarationEntity.setTail_lamp_rt(getId("Tail Lamp(RT)", frontRearEntities));

        frontRearEntities = frontRearFacade.getLeftList();
        vehSelfDeclarationEntity.setLt_front_fender(getId("Front Fender(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_front_door(getId("Front Door(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_rear_door(getId("Rear Door(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_running_board(getId("Running Board(LT)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_pillar_door(getId("LT Pillar Door(A)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_pillar_center(getId("LT Pillar Centre(B)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_pillar_rear(getId("LT Pillar Rear(C)", frontRearEntities));
        vehSelfDeclarationEntity.setLt_qtr_panel(getId("Qtr Panel(LT)", frontRearEntities));

        frontRearEntities = frontRearFacade.getRightList();
        vehSelfDeclarationEntity.setRt_qtr_panel(getId("Qtr Panel(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_rear_door(getId("Rear Door(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_front_door(getId("Front Door(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_front_pillar_A(getId("RT Front Pillar(A)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_center_pillar_B(getId("RT Centre Pillar(B)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_rear_pillar_C(getId("RT Rear Pillar(C)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_running_board(getId("Running Board(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setRt_front_fender(getId("Front Fender(RT)", frontRearEntities));
        vehSelfDeclarationEntity.setFloor(getId("Floor/Silencer", frontRearEntities));
        vehSelfDeclarationEntity.setRear_view_mirror_lt(getId("Rear View Mirror (LT)", frontRearEntities));
        vehSelfDeclarationEntity.setRear_view_mirror_rt(getId("Rear View Mirror (RT)", frontRearEntities));
        vehSelfDeclarationEntity.setTyres(getId("Tyres", frontRearEntities));

        frontRearEntities = frontRearFacade.getGlassList();
        vehSelfDeclarationEntity.setBack_glass(getId("Back Glass", frontRearEntities));
        vehSelfDeclarationEntity.setGlass_laminated(getId("Front ws Glass Laminated", frontRearEntities));
        vehSelfDeclarationEntity.setRf_door_glass(getId("RF Door Glass", frontRearEntities));
        vehSelfDeclarationEntity.setRr_door_glass(getId("RR Door Glass", frontRearEntities));
        vehSelfDeclarationEntity.setLf_door_glass(getId("LF Door Glass", frontRearEntities));
        vehSelfDeclarationEntity.setLr_door_glass(getId("LR Door Glass", frontRearEntities));
        vehSelfDeclarationEntity.setRim(getId("Rim", frontRearEntities));
        vehSelfDeclarationEntity.setUnder_carriage(getId("Under Carriage", frontRearEntities));


        // not defined
        vehSelfDeclarationEntity.setLt_front_tyre(1);
        vehSelfDeclarationEntity.setLt_rear_tyre(1);
        vehSelfDeclarationEntity.setRt_rear_tyre(1);
        vehSelfDeclarationEntity.setRt_front_tyre(1);
        vehSelfDeclarationEntity.setVehicle_condition(1);
*/
        return vehSelfDeclarationEntity;
    }

    private void loadHomeFragment(String title) {
        // selecting appropriate nav menu item
        /*selectNavMenu();*/

        // set toolbar title
        setToolbarTitle(title);

        /*// if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }
*/
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                //fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        // toggleFab();

        //Closing drawer on item click
       /* drawer.closeDrawers();*/

        // refresh toolbar menu
      /*  invalidateOptionsMenu();*/
    }

    private Fragment getHomeFragment() {
        Fragment fragment = null;
        switch (selection) {
            case 0:
                // home
                fragment = new FrontFragment();
                return fragment;
            case 1:
                // left
                fragment = new LeftFragment();
                return fragment;
            case 2:
                // right
                fragment = new RightFragment();
                return fragment;
            case 3:
                // glass fragment
                fragment = new GlassFragment();
                return fragment;

            default:
                fragment = new FrontFragment();
                return fragment;
        }
    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
        //getSupportActionBar().setTitle(title);
    }

    public int getId(String value, List<FrontRearEntity> frontRearEntities) {
        for (FrontRearEntity frontRearEntity : frontRearEntities) {
            if (frontRearEntity.getName().equals(value)) {

                if (frontRearEntity.getValue().matches("INTACT")) {
                    return 3;
                } else if (frontRearEntity.getValue().matches("SCRATCH")) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof DocumentResponse) {
            cancelDialog();
            if (response.getStatus() == 0)
                startActivity(new Intent(this, PhotoCaptureActivity.class));
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

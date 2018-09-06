package inspection.selfdeclaration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import inspection.Communicator;
import inspection.Constants;
import inspection.selfdeclaration.adapters.FrontAdapterNew;
import inspection.utility.BaseActivity;
import inspection.utility.SimpleDividerItemDecoration;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.APIResponse;
import policyboss.com.inspect.core.inspection.IResponseSubcribe;
import policyboss.com.inspect.core.inspection.controller.documents.DocumentController;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.entity.VehDetailRequestEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;
import policyboss.com.inspect.core.inspection.response.VehicleDetailResponse;

import static inspection.Constants.FRONT_CLICK;
import static inspection.Constants.GLASS_CLICK;
import static inspection.Constants.LEFT_CLICK;
import static inspection.Constants.REAR_CLICK;
import static inspection.Constants.RIGHT_CLICK;


public class DeclareSelfActivity2 extends BaseActivity implements View.OnClickListener, Communicator, IResponseSubcribe {
    FrontRearFacade frontRearFacade;
    RecyclerView rvCommon;
    FrontAdapterNew frontAdapter;
    TextView cracked, setCondition, declaration, safe, scratch, broken, dented, no_selected_Item, total_Item, frontName, condition_txt;
    public static List<FrontRearEntity> frontRearEntities = null;
    public static List<FrontRearEntity> rearEntities = null;
    public static List<FrontRearEntity> leftEntities = null;
    public static List<FrontRearEntity> rightEntities = null;
    public static List<FrontRearEntity> glassEntities = null;
    LinearLayout defect_condition_layout, rearLayout, selCon_lay, frontLayout,
            leftLayout, rightLayout, glassLayout, done_layout;
    String URLAddress="http://180.179.67.67:8083/BIRLAPIVC/uploadInspection";
    String encodedUrl;
    ProgressDialog progressDialog=null;
    Button btnDone;
    String frontBumper, frontPanel,frontIndicatorLightRt,frontHeadLempLeft,frontFogLampLeft,frontLftApron, fronLeftInd, frontGrill , frontBonet, frontHeadLempRight,  frontFogLampRight,  frontRightApron;
    String rearBumper, rearDicky, rearDickydoor, reartaillemleft, reartraillampright;
    String leftfrontdoor, leftpilar1, leftpilar2, leftreardoor, leftpiler3, leftrunninboard, leftpannel;
    String rightPannel, rightrtcentpiller1, rightfloorsilencer, rightreardoor, rightrearpilar2, rightrearviewmooror, rightfrontdoor, rightrunningboard, rightrearviewmirrrob, rightfrontpilarA, rightfrontfender;
    String gbackglass, grrdoorglass, grim, grooflining, gfrontwindshield, glfdoorglass, gundercarrigae, gseatsfront, grfdoorglass, glrdoorglass, gtoproof;
    String gseatsrear, gdashboard, ginstrumentmeter, gengine, ggearbox, gsuspension, gsteeringsystem, gradiator, gairconditioner, gdrivesaft, gwheels, gbrakes, gmusicsystem;
    private List<View> mCheckedView = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();
    int ID = 0;
    int count = 1;
    LinearLayoutManager mLayoutManager;

    Bitmap myLogo = null;
    ImageView front_Image, checked_front_img, checked_rear_img, checked_left_img, checked_right_img, checked_glass_img;
    private boolean CHECKED_FRONT_CLICK;
    String mGameStze1;
    static final String FTP_HOST ="180.179.67.67";//"ftp.drivehq.com";//"180.179.67.69";//;

    /*********  FTP USERNAME ***********/
    static final String FTP_USER ="ftpadmin";// "architk";//"ftpadmin";//

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS ="Ftp@gf$56";//"12345678";// "Admin$$123";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_declare_self2);
        init();
        setLisner();
        // uploadFile();

        Drawable vectorDrawable = ResourcesCompat.getDrawable(this.getResources(), R.drawable.checked_icon_21, null);
        myLogo = convertToBitmap(vectorDrawable, 45, 45);
        no_selected_Item = (TextView) findViewById(R.id.no_selected_Item);
        total_Item = findViewById(R.id.total_Item);
        front_Image = (ImageView) findViewById(R.id.front_Image);
        selCon_lay = (LinearLayout) findViewById(R.id.selCon_lay);
        //  condition_txt =  (TextView)findViewById(R.id.condition_txt);

        done_layout =  findViewById(R.id.done_layout);
        declaration = findViewById(R.id.declaration);
        checked_front_img =  findViewById(R.id.checked_front_img);
        checked_rear_img = (ImageView) findViewById(R.id.checked_rear_img);
        checked_left_img = (ImageView) findViewById(R.id.checked_left_img);
        checked_right_img = (ImageView) findViewById(R.id.checked_right_img);
        checked_glass_img = (ImageView) findViewById(R.id.checked_glass_img);


        checked_front_img.setVisibility(View.INVISIBLE);
        setCondition = (TextView) findViewById(R.id.setCondition_txt);
        cracked = (TextView) findViewById(R.id.cracked);
        safe = (TextView) findViewById(R.id.safe);
        scratch = (TextView) findViewById(R.id.scratch);
        broken = (TextView) findViewById(R.id.broken);
        dented = (TextView) findViewById(R.id.dented);
        defect_condition_layout = (LinearLayout) findViewById(R.id.defect_condition_layout);

        Constants.changeFontMedium_TextView(declaration, this);
        rvCommon = (RecyclerView) findViewById(R.id.recycler_view);
        rvCommon.setHasFixedSize(true);
        rvCommon.addItemDecoration(new SimpleDividerItemDecoration(this));
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCommon.setLayoutManager(mLayoutManager);

        FRONT_CLICK = true;

        frontLayout.setBackgroundColor(Color.parseColor("#dedede"));
        checkedClicked(rearLayout, leftLayout, rightLayout, glassLayout, safe, scratch, broken, dented, cracked);
        frontArticle();

        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safe.setBackgroundResource(R.drawable.cardlike_sel);
                safe.setTextColor(Color.parseColor("#FFFFFF"));
                checkedClicked(scratch, broken, dented, cracked);

                if (FRONT_CLICK) {
                    new FrontOperation("Safe").execute("");
                } else if (REAR_CLICK) {
                    new RearOperation("Safe").execute("");
                } else if (LEFT_CLICK) {
                    new LeftOperation("Safe").execute("");
                } else if (RIGHT_CLICK) {
                    new RightOperation("Safe").execute("");
                } else if (GLASS_CLICK) {
                    new GlassOperation("Safe").execute("");
                }

            }
        });
        scratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scratch.setTextColor(Color.parseColor("#FFFFFF"));
                scratch.setBackgroundResource(R.drawable.cardlike_sel);
                checkedClicked(safe, broken, dented, cracked);


                if (FRONT_CLICK) {
                    new FrontOperation("Scratch").execute("");
                } else if (REAR_CLICK) {
                    new RearOperation("Scratch").execute("");
                } else if (LEFT_CLICK) {
                    new LeftOperation("Scratch").execute("");
                } else if (RIGHT_CLICK) {
                    new RightOperation("Scratch").execute("");
                } else if (GLASS_CLICK) {
                    new GlassOperation("Scratch").execute("");
                }
            }
        });
        broken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broken.setTextColor(Color.parseColor("#FFFFFF"));
                broken.setBackgroundResource(R.drawable.cardlike_sel);
                checkedClicked(safe, scratch, dented, cracked);

                if (FRONT_CLICK) {
                    new FrontOperation("Broken").execute("");
                } else if (REAR_CLICK) {
                    new RearOperation("Broken").execute("");
                } else if (LEFT_CLICK) {
                    new LeftOperation("Broken").execute("");
                } else if (RIGHT_CLICK) {
                    new RightOperation("Broken").execute("");
                } else if (GLASS_CLICK) {
                    new GlassOperation("Broken").execute("");
                }


            }
        });
        dented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dented.setTextColor(Color.parseColor("#FFFFFF"));
                dented.setBackgroundResource(R.drawable.cardlike_sel);
                checkedClicked(safe, scratch, broken, cracked);
                if (FRONT_CLICK) {
                    new FrontOperation("Dented").execute("");
                } else if (REAR_CLICK) {
                    new RearOperation("Dented").execute("");
                } else if (LEFT_CLICK) {
                    new LeftOperation("Dented").execute("");
                } else if (RIGHT_CLICK) {
                    new RightOperation("Dented").execute("");
                } else if (GLASS_CLICK) {
                    new GlassOperation("Dented").execute("");
                }

            }
        });
        cracked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cracked.setTextColor(Color.parseColor("#FFFFFF"));
                cracked.setBackgroundResource(R.drawable.cardlike_sel);
                checkedClicked(safe, scratch, broken, dented);

                if (FRONT_CLICK) {
                    new FrontOperation("Cracked").execute("");
                } else if (REAR_CLICK) {
                    new RearOperation("Cracked").execute("");
                } else if (LEFT_CLICK) {
                    new LeftOperation("Cracked").execute("");
                } else if (RIGHT_CLICK) {
                    new RightOperation("Cracked").execute("");
                } else if (GLASS_CLICK) {
                    new GlassOperation("Cracked").execute("");
                }

            }
        });


    }

    private void init() {
        progressDialog=new ProgressDialog(DeclareSelfActivity2.this);
        progressDialog.setMessage("Please Wait Data Is Uploading...");
        progressDialog.setCancelable(false);
        btnDone = (Button) findViewById(R.id.btnDone);
        frontLayout = (LinearLayout) findViewById(R.id.frontLayout);
        rearLayout = (LinearLayout) findViewById(R.id.rearLayout);
        leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        glassLayout = (LinearLayout) findViewById(R.id.glassLayout);
    }

    public void ShowDialog(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage(value);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void getFrontObject(FrontRearEntity frontRearEntity) {
        String str2 = frontRearEntity.getName();
        setCondition.setText(str2);
        ID = frontRearEntity.getId();
        defect_condition_layout.setVisibility(View.VISIBLE);
        selCon_lay.setVisibility(View.VISIBLE);
        checkedClicked(safe, scratch, broken, dented, cracked);



/*
        for (int i = 0 ;i < frontRearEntities.size();i++){

            if (frontRearEntities.get(i).getValue().equalsIgnoreCase("Select")){
                Constants.SHOW_BUTTON = ;
            }
        }
        frontRearEntities = null;
        rearEntities = null;
        leftEntities = null;
        List<FrontRearEntity> rightEntities = null;
        List<FrontRearEntity> glassEntities = null;
*/

        if (FRONT_CLICK) {
            callFrontAdapter();
        } else if (REAR_CLICK) {
            callRearAdapter();
        } else if (LEFT_CLICK) {
            callLeftAdapter();
        } else if (RIGHT_CLICK) {
            callRightAdapter();
        } else if (GLASS_CLICK) {
            callGlassAdapter();
        }


        frontAdapter.notifyDataSetChanged();


        //


        if (frontRearEntity.getValue().equalsIgnoreCase("Safe")) {
            safe.setBackgroundResource(R.drawable.cardlike_sel);
            safe.setTextColor(Color.parseColor("#FFFFFF"));

        } else if (frontRearEntity.getValue().equalsIgnoreCase("Scratch")) {
            scratch.setBackgroundResource(R.drawable.cardlike_sel);
            scratch.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (frontRearEntity.getValue().equalsIgnoreCase("Broken")) {
            broken.setBackgroundResource(R.drawable.cardlike_sel);
            broken.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (frontRearEntity.getValue().equalsIgnoreCase("Dented")) {
            dented.setBackgroundResource(R.drawable.cardlike_sel);
            dented.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (frontRearEntity.getValue().equalsIgnoreCase("Cracked")) {
            cracked.setBackgroundResource(R.drawable.cardlike_sel);
            cracked.setTextColor(Color.parseColor("#FFFFFF"));
        }
        // mLayoutManager.scrollToPositionWithOffset(ID, 20);

    }

    public void updateFrontEntity(FrontRearEntity frontRearEntity, String value) {

        //frontRearEntities
        try {
            // List<FrontRearEntity> frontRearEntities = getFrontRearList();
            for (int i = 0; i < frontRearEntities.size(); i++) {
                FrontRearEntity entity = frontRearEntities.get(i);
                if (entity.getId() == frontRearEntity.getId()) {
                    frontRearEntities.get(i).setValue(value);
                    frontRearEntities.get(i).setBitmap(myLogo);
                    // storeFrontRear(frontRearEntities);
                    frontAdapter.updateAdapter(frontRearEntities.get(i), frontRearEntities.get(i).getId());
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeFrontRear(List<FrontRearEntity> frontRearEntities) {
//        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, this.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        editor.putString(Constants.FRONTREAR, gson.toJson(frontRearEntities));
//        editor.apply();
    }

    public List<FrontRearEntity> getFrontRearList() {
//        SharedPreferences sharedPreferences = this.getSharedPreferences
//                (Constants.SHARED_PREFERENCE_POLICYBOSS, this.MODE_PRIVATE);
//        String frontRearList = sharedPreferences.getString(Constants.FRONTREAR, null);
//        if (!frontRearList.matches("")) {
//            Type type = new TypeToken<List<FrontRearEntity>>() {
//            }.getType();
//            List<FrontRearEntity> frontRearEntities = new Gson().fromJson(frontRearList, type);
//            return frontRearEntities;
//        }
        return null;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return mutableBitmap;
    }

    public void clearConditionList(View... emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);

        int index = 0;
        for (View view : mEmptyViews) {
            if (view instanceof TextView) {
                if (((TextView) view).getText().toString().equalsIgnoreCase("cracked")) {
                    view.setBackgroundResource(R.drawable.cardlike);
                } else {
                    view.setBackgroundResource(R.drawable.right_border_transparent);
                }
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
                Constants.changeFontMedium_TextView((TextView) mEmptyViews.get(index++), this);
            }
        }

    }

    public void checkedClicked(View... emptyViews) {
        mCheckedView = Arrays.asList(emptyViews);
        int index = -1;
        for (View view : mCheckedView) {
            index++;
            if (view instanceof LinearLayout) {
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                switch (view.getId()) {
                    case R.id.glassLayout:
                        view.setBackgroundResource(R.drawable.left_border_transparent);
                        break;
                }
            }

      /*      else if(view instanceof ImageView){

                switch (view.getId()){
                    case R.id.checked_front_img:
                        int isVisible = view.getVisibility();
                        if (isVisible == 1){
                            Constants.CHECKED_FRONT_CLICK = true;
                        }
                        else{
                            Constants. CHECKED_FRONT_CLICK= false;
                        }
                        break;
                    case R.id.checked_rear_img:
                        int rear_img_isVisible = view.getVisibility();
                        if (rear_img_isVisible == 1){
                            Constants.CHECKED_REAR_CLICK = true;
                        }
                        else{
                            Constants.CHECKED_REAR_CLICK = true;
                        }
                        break;
                    case R.id.checked_left_img:
                        int left_img_isVisible = view.getVisibility();
                        if (left_img_isVisible == 1){
                            Constants.CHECKED_LEFT_CLICK = true;
                        }
                        else{
                            Constants.CHECKED_LEFT_CLICK = false;
                        }
                        break;
                    case R.id.checked_right_img:
                        int right_isVisible = view.getVisibility();
                        if (right_isVisible == 1){
                            Constants.CHECKED_RIGHT_CLICK = true;
                        }
                        else{
                            Constants.CHECKED_RIGHT_CLICK= false;
                        }
                        break;
                    case R.id.checked_glass_img:
                        int glass_isVisible = view.getVisibility();
                        if (glass_isVisible == 1){
                            Constants.CHECKED_GLASS_CLICK = true;
                        }
                        else{
                            Constants.CHECKED_GLASS_CLICK =  false;
                        }
                        break;
                }


                if (Constants.CHECKED_FRONT_CLICK && Constants.CHECKED_REAR_CLICK &&
                        Constants.CHECKED_LEFT_CLICK && Constants.CHECKED_RIGHT_CLICK &&Constants.CHECKED_GLASS_CLICK){

                    Toast.makeText(this,"DONE",Toast.LENGTH_SHORT).show();
                }


            }*/

            else if (view instanceof TextView) {
                if (((TextView) view).getText().toString().equalsIgnoreCase("cracked")) {
                    view.setBackgroundResource(R.drawable.cardlike);
                } else {
                    view.setBackgroundResource(R.drawable.right_border_transparent);
                }
                ((TextView) view).setTextColor(Color.parseColor("#000000"));
                try {
                    Constants.changeFontMedium_TextView((TextView) mCheckedView.get(index), this);
                } catch (Exception e) {

                    Log.e("Error2 ", e.getMessage().toString());
                    e.printStackTrace();

                }
            }

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.frontLayout:
                FRONT_CLICK = true;
                REAR_CLICK = false;
                LEFT_CLICK = false;
                RIGHT_CLICK = false;
                GLASS_CLICK = false;
                defect_condition_layout.setVisibility(View.GONE);
                setCondition.setText("");
                frontLayout.setBackgroundColor(Color.parseColor("#dedede"));
                checkedClicked(rearLayout, leftLayout, rightLayout, glassLayout, safe, scratch, broken, dented, cracked);
                frontArticle();
                break;
            case R.id.rearLayout:
                FRONT_CLICK = false;
                REAR_CLICK = true;
                LEFT_CLICK = false;
                RIGHT_CLICK = false;
                GLASS_CLICK = false;
                setCondition.setText("");
                defect_condition_layout.setVisibility(View.GONE);
                rearLayout.setBackgroundColor(Color.parseColor("#dedede"));
                checkedClicked(frontLayout, leftLayout, rightLayout, glassLayout, safe, scratch, broken, dented, cracked);
                rearArticle();
                break;
            case R.id.leftLayout:
                FRONT_CLICK = false;
                REAR_CLICK = false;
                LEFT_CLICK = true;
                RIGHT_CLICK = false;
                GLASS_CLICK = false;
                setCondition.setText("");
                defect_condition_layout.setVisibility(View.GONE);
                leftLayout.setBackgroundColor(Color.parseColor("#dedede"));
                checkedClicked(rearLayout, frontLayout, rightLayout, glassLayout, safe, scratch, broken, dented, cracked);
                leftArticle();
                break;
            case R.id.rightLayout:

                FRONT_CLICK = false;
                REAR_CLICK = false;
                LEFT_CLICK = false;
                RIGHT_CLICK = true;
                GLASS_CLICK = false;
                setCondition.setText("");
                defect_condition_layout.setVisibility(View.GONE);
                rightLayout.setBackgroundColor(Color.parseColor("#dedede"));
                checkedClicked(rearLayout, leftLayout, frontLayout, glassLayout, safe, scratch, broken, dented, cracked);
                rightArticle();
                break;
            case R.id.glassLayout:
                FRONT_CLICK = false;
                REAR_CLICK = false;
                LEFT_CLICK = false;
                RIGHT_CLICK = false;
                GLASS_CLICK = true;
                setCondition.setText("");
                defect_condition_layout.setVisibility(View.GONE);
                glassLayout.setBackgroundResource(R.drawable.left_border_transparent_grey_back);
                checkedClicked(rearLayout, leftLayout, rightLayout, frontLayout, safe, scratch, broken, dented, cracked);
                glassArticle();
                break;

            case R.id.btnDone:
                // progressDialog.show();

                VehDetailRequestEntity vehSelfDeclarationEntity;
                vehSelfDeclarationEntity = getAllDetails();
                showDialog();
                new DocumentController(this).vehicleDetail(vehSelfDeclarationEntity, this);



//                if(isInternetOn())
//                {
//                  //  progressDialog.show();
//                 //   new UploadDataOnServer(URLAddress, encodedUrl, "POST").execute();
//
//
//
//                    /*if (MasterData.Stage=="1")
//                    {
//                        progressDialog.show();
//                        new UploadDataOnServer(URLAddress, encodedUrl, "POST").execute();
//                    }
//                    else
//                    {
//                        ShowDialog("Video Is Uploading please Wait...");
//                    }*/
//
//                }
//                else
//                {
//                    ShowDialog("No Internet Connectivity, Please Connect Internet");
//                }
//

                break;
        }


    }

    private VehDetailRequestEntity getAllDetails() {

        frontBumper=frontRearEntities.get(0).getValue();
        frontPanel=frontRearEntities.get(1).getValue();
        frontIndicatorLightRt=frontRearEntities.get(2).getValue();
        frontHeadLempLeft=frontRearEntities.get(3).getValue();

        frontFogLampLeft=frontRearEntities.get(4).getValue();
        frontLftApron=frontRearEntities.get(5).getValue();
        fronLeftInd=frontRearEntities.get(6).getValue();
        frontGrill=frontRearEntities.get(7).getValue();

        frontBonet=frontRearEntities.get(8).getValue();
        frontHeadLempRight=frontRearEntities.get(9).getValue();
        frontFogLampRight=frontRearEntities.get(10).getValue();
        frontRightApron=frontRearEntities.get(11).getValue();



        rearBumper=rearEntities.get(0).getValue();
        rearDicky=rearEntities.get(1).getValue();
        reartraillampright=rearEntities.get(2).getValue();
        rearDickydoor=rearEntities.get(3).getValue();
        reartaillemleft=rearEntities.get(4).getValue();


        leftfrontdoor= leftEntities.get(0).getValue();
        leftpannel= leftEntities.get(1).getValue();
        leftreardoor= leftEntities.get(2).getValue();
        leftrunninboard= leftEntities.get(3).getValue();
        leftpilar1= leftEntities.get(4).getValue();
        leftpilar2= leftEntities.get(5).getValue();
        leftpiler3= leftEntities.get(6).getValue();





        rightPannel= rightEntities.get(0).getValue();
        rightfloorsilencer= rightEntities.get(1).getValue();
        rightrearpilar2= rightEntities.get(2).getValue();
        rightfrontdoor= rightEntities.get(3).getValue();

        rightfrontfender= rightEntities.get(4).getValue();
        rightrtcentpiller1= rightEntities.get(5).getValue();

        rightreardoor= rightEntities.get(6).getValue();
        rightrearviewmooror= rightEntities.get(7).getValue();
        rightrunningboard= rightEntities.get(8).getValue();
        rightfrontpilarA= rightEntities.get(9).getValue();





        gbackglass=glassEntities.get(0).getValue();
        grim=glassEntities.get(1).getValue();
        gfrontwindshield=glassEntities.get(2).getValue();
        gundercarrigae=glassEntities.get(3).getValue();
        grfdoorglass=glassEntities.get(4).getValue();
        gtoproof=glassEntities.get(5).getValue();

        gdashboard=glassEntities.get(6).getValue();
        gengine=glassEntities.get(7).getValue();
        gsuspension=glassEntities.get(8).getValue();
        gradiator=glassEntities.get(9).getValue();
        gdrivesaft=glassEntities.get(10).getValue();
        gbrakes=glassEntities.get(11).getValue();

        grrdoorglass=glassEntities.get(12).getValue();
        grooflining=glassEntities.get(13).getValue();
        glfdoorglass=glassEntities.get(14).getValue();
        gseatsfront=glassEntities.get(15).getValue();
        glrdoorglass=glassEntities.get(16).getValue();
        gseatsrear=glassEntities.get(17).getValue();

        ginstrumentmeter=glassEntities.get(18).getValue();
        ggearbox=glassEntities.get(19).getValue();
        gsteeringsystem=glassEntities.get(20).getValue();
        gairconditioner=glassEntities.get(21).getValue();
        gwheels=glassEntities.get(22).getValue();
        gmusicsystem=glassEntities.get(23).getValue();

        VehDetailRequestEntity vehSelfDeclarationEntity = new VehDetailRequestEntity();
     //   vehSelfDeclarationEntity.setVehicle_id(registerFacade.getUser().getVehicle_id());
      //  vehSelfDeclarationEntity.setVehicle_no(registerFacade.getUser().getVehicle_no());
        vehSelfDeclarationEntity.setVehicle_id("123");
          vehSelfDeclarationEntity.setVehicle_no("test");
       // List<FrontRearEntity> frontRearEntities = frontRearFacade.getFrontRearList();

        vehSelfDeclarationEntity.setFront_front_bumper(frontBumper);
        vehSelfDeclarationEntity.setFront_front_panel(frontPanel);
        vehSelfDeclarationEntity.setFront_indicator_light_RT(frontIndicatorLightRt);

        vehSelfDeclarationEntity.setFront_head_lamp_LT(frontHeadLempLeft);

        vehSelfDeclarationEntity.setFront_fog_lamp_LT(frontFogLampLeft);
        vehSelfDeclarationEntity.setFront_left_apron(frontLftApron);
        vehSelfDeclarationEntity.setFront_indicator_light_LT(fronLeftInd);
        vehSelfDeclarationEntity.setFront_grill(frontGrill);
        vehSelfDeclarationEntity.setFront_bonnet(frontBonet);
        vehSelfDeclarationEntity.setFront_head_lamp_RT(frontHeadLempRight);

        vehSelfDeclarationEntity.setFront_fog_lamp_RT(frontFogLampRight);
        vehSelfDeclarationEntity.setFront_right_apron(frontRightApron);

      //rear
        vehSelfDeclarationEntity.setRear_rear_bumper(rearBumper);
        vehSelfDeclarationEntity.setRear_dickey_door(rearDicky);
        vehSelfDeclarationEntity.setRear_tail_lamp_RT(reartraillampright);
        vehSelfDeclarationEntity.setRear_dicky(rearDickydoor);
        vehSelfDeclarationEntity.setRear_tail_lamp_LT(reartaillemleft);
    //    frontRearEntities = frontRearFacade.getLeftList();

        vehSelfDeclarationEntity.setLt_front_door(leftfrontdoor);
        vehSelfDeclarationEntity.setLt_qtr_panel(leftpannel);
        vehSelfDeclarationEntity.setLt_rear_door(leftreardoor);
        vehSelfDeclarationEntity.setLt_running_board(leftrunninboard);
        vehSelfDeclarationEntity.setLt_pillar_board("");//Not found
        vehSelfDeclarationEntity.setLt_pillar_door_A(leftpilar1);
        vehSelfDeclarationEntity.setLt_pillar_center_B(leftpilar2);
        vehSelfDeclarationEntity.setLt_pillar_rear_C(leftpiler3);




       // frontRearEntities = frontRearFacade.getRightList();
        vehSelfDeclarationEntity.setRt_qtr_panel(rightPannel);
        vehSelfDeclarationEntity.setRt_floor_silencer(rightfloorsilencer);
        vehSelfDeclarationEntity.setRt_rear_pillar_C(rightrearpilar2);
        vehSelfDeclarationEntity.setRt_front_door(rightfrontdoor);
        vehSelfDeclarationEntity.setRt_front_fender(rightfrontfender);
        vehSelfDeclarationEntity.setRt_centre_pillar_B(rightrtcentpiller1);
        vehSelfDeclarationEntity.setRt_rear_door(rightreardoor);
        vehSelfDeclarationEntity.setRt_rear_view_mirror_LT(rightrearviewmooror);
        vehSelfDeclarationEntity.setRt_running_board(rightrunningboard);
        vehSelfDeclarationEntity.setRt_front_pillar_A(rightfrontpilarA);

      //  vehSelfDeclarationEntity.setRear_view_mirror_rt(getId("Rear View Mirror (RT)", frontRearEntities));
       // vehSelfDeclarationEntity.setTyres(getId("Tyres", frontRearEntities));

        //frontRearEntities = frontRearFacade.getGlassList();
        vehSelfDeclarationEntity.setGlass_back_glass(gbackglass);
        vehSelfDeclarationEntity.setGlass_rim(grim);
        vehSelfDeclarationEntity.setGlass_front_windshield(gfrontwindshield);
        vehSelfDeclarationEntity.setGlass_under_carriage(gundercarrigae);
        vehSelfDeclarationEntity.setGlass_rf_door_glass(grfdoorglass);
        vehSelfDeclarationEntity.setGlass_top_roof(gtoproof);
        vehSelfDeclarationEntity.setGlass_dashboard(gdashboard);
        vehSelfDeclarationEntity.setGlass_engine(gengine);
        vehSelfDeclarationEntity.setGlass_suspension(gsuspension);
        vehSelfDeclarationEntity.setGlass_radiator(gradiator);
        vehSelfDeclarationEntity.setGlass_drive_shaft(gdrivesaft);
        vehSelfDeclarationEntity.setGlass_brakes(gbrakes);
        vehSelfDeclarationEntity.setGlass_rr_door_glass(grrdoorglass);
        vehSelfDeclarationEntity.setGlass_roof_lining(grooflining);


        vehSelfDeclarationEntity.setGlass_lt_door_glass(glfdoorglass);
        vehSelfDeclarationEntity.setGlass_seats_front(gseatsfront);
        vehSelfDeclarationEntity.setGlass_lr_door_glass(glrdoorglass);
        vehSelfDeclarationEntity.setGlass_seats_rear(gseatsrear);

        vehSelfDeclarationEntity.setGlass_instrument_meters(ginstrumentmeter);
        vehSelfDeclarationEntity.setGlass_gear_box(ggearbox);
        vehSelfDeclarationEntity.setGlass_steering_system(gsteeringsystem);
        vehSelfDeclarationEntity.setGlass_air_conditioner(gairconditioner);

        vehSelfDeclarationEntity.setGlass_wheels(gwheels);
        vehSelfDeclarationEntity.setGlass_music_system(gmusicsystem);

        // not defined
//        vehSelfDeclarationEntity.setLt_front_tyre(1);
//        vehSelfDeclarationEntity.setLt_rear_tyre(1);
//        vehSelfDeclarationEntity.setRt_rear_tyre(1);
//        vehSelfDeclarationEntity.setRt_front_tyre(1);
//        vehSelfDeclarationEntity.setVehicle_condition(1);

        return vehSelfDeclarationEntity;
    }
    private class FrontOperation extends AsyncTask<String, Void, String> {
        String Condition_Selected = "";

        FrontOperation(String value) {
            Condition_Selected = value;
        }

        @Override
        protected String doInBackground(String... params) {
            String posi = "";
            for (int i = 0; i < frontRearEntities.size(); i++) {
                if (frontRearEntities.get(i).getId() == ID) {
                    FrontRearEntity entity = frontRearEntities.get(i);
                    if (entity.getId() == frontRearEntities.get(i).getId()) {
                        frontRearEntities.get(i).setValue(Condition_Selected);
                        frontRearEntities.get(i).setBitmap(myLogo);
                        frontRearEntities.get(i).setItem_selected(true);
                        posi = String.valueOf(frontRearEntities.get(i).getId());
                        // storeFrontRear(frontRearEntities);
                        frontAdapter.updateAdapter(frontRearEntities.get(i), frontRearEntities.get(i).getId());
                        break;
                    }
                }
            }
            return posi;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //     mLayoutManager.scrollToPositionWithOffset(Integer.parseInt(result), 20);
                    int count = 1;
                    for (int i = 0; i < frontRearEntities.size(); i++) {
                        if (frontRearEntities.get(i).isItem_selected()) {
                            no_selected_Item.setText(String.valueOf(count++));
                        }
                    }
                    if ((count - 1) == frontRearEntities.size()) {

                        checked_front_img.setVisibility(View.VISIBLE);
                        CHECKED_FRONT_CLICK = true;
                    } else {
                        CHECKED_FRONT_CLICK = false;
                        checked_front_img.setVisibility(View.INVISIBLE);
                    }
                    showDoneButton();
                }
            }, 300);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class RearOperation extends AsyncTask<String, Void, String> {
        String Condition_Selected = "";

        RearOperation(String value) {
            Condition_Selected = value;
        }

        @Override
        protected String doInBackground(String... params) {
            String posi = "";
            for (int i = 0; i < rearEntities.size(); i++) {
                if (rearEntities.get(i).getId() == ID) {
                    FrontRearEntity entity = rearEntities.get(i);
                    if (entity.getId() == rearEntities.get(i).getId()) {
                        rearEntities.get(i).setValue(Condition_Selected);
                        rearEntities.get(i).setBitmap(myLogo);
                        rearEntities.get(i).setItem_selected(true);
                        posi = String.valueOf(rearEntities.get(i).getId());
                        // storeFrontRear(frontRearEntities);
                        frontAdapter.updateAdapter(rearEntities.get(i), rearEntities.get(i).getId());
                        break;
                    }
                }
            }
            return posi;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //     mLayoutManager.scrollToPositionWithOffset(Integer.parseInt(result), 20);
                    int count = 1;
                    for (int i = 0; i < rearEntities.size(); i++) {
                        if (rearEntities.get(i).isItem_selected()) {
                            no_selected_Item.setText(String.valueOf(count++));
                        }
                    }
                    if ((count - 1) == rearEntities.size()) {
                        Constants.CHECKED_REAR_CLICK = true;
                        checked_rear_img.setVisibility(View.VISIBLE);
                    } else {
                        Constants.CHECKED_REAR_CLICK = false;
                        checked_rear_img.setVisibility(View.INVISIBLE);
                    }
                    showDoneButton();
                }
            }, 300);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class LeftOperation extends AsyncTask<String, Void, String> {
        String Condition_Selected = "";

        LeftOperation(String value) {
            Condition_Selected = value;
        }

        @Override
        protected String doInBackground(String... params) {
            String posi = "";
            for (int i = 0; i < leftEntities.size(); i++) {
                if (leftEntities.get(i).getId() == ID) {
                    FrontRearEntity entity = leftEntities.get(i);
                    if (entity.getId() == leftEntities.get(i).getId()) {
                        leftEntities.get(i).setValue(Condition_Selected);
                        leftEntities.get(i).setBitmap(myLogo);
                        leftEntities.get(i).setItem_selected(true);
                        posi = String.valueOf(leftEntities.get(i).getId());
                        // storeFrontRear(frontRearEntities);
                        frontAdapter.updateAdapter(leftEntities.get(i), leftEntities.get(i).getId());
                        break;
                    }
                }
            }
            return posi;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //     mLayoutManager.scrollToPositionWithOffset(Integer.parseInt(result), 20);
                    int count = 1;
                    for (int i = 0; i < leftEntities.size(); i++) {


                        if (leftEntities.get(i).isItem_selected()) {
                            no_selected_Item.setText(String.valueOf(count++));
                        }
                    }
                    if ((count - 1) == leftEntities.size()) {
                        Constants.CHECKED_LEFT_CLICK = true;
                        checked_left_img.setVisibility(View.VISIBLE);
                    } else {
                        Constants.CHECKED_LEFT_CLICK = false;
                        checked_left_img.setVisibility(View.INVISIBLE);
                    }
                    showDoneButton();
                }
            }, 300);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class RightOperation extends AsyncTask<String, Void, String> {
        String Condition_Selected = "";

        RightOperation(String value) {
            Condition_Selected = value;
        }

        @Override
        protected String doInBackground(String... params) {
            String posi = "";
            for (int i = 0; i < rightEntities.size(); i++) {
                if (rightEntities.get(i).getId() == ID) {
                    FrontRearEntity entity = rightEntities.get(i);
                    if (entity.getId() == rightEntities.get(i).getId()) {
                        rightEntities.get(i).setValue(Condition_Selected);
                        rightEntities.get(i).setBitmap(myLogo);
                        rightEntities.get(i).setItem_selected(true);
                        posi = String.valueOf(rightEntities.get(i).getId());
                        // storeFrontRear(frontRearEntities);
                        frontAdapter.updateAdapter(rightEntities.get(i), rightEntities.get(i).getId());
                        break;
                    }
                }
            }
            return posi;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //     mLayoutManager.scrollToPositionWithOffset(Integer.parseInt(result), 20);
                    int count = 1;
                    for (int i = 0; i < rightEntities.size(); i++) {

                        if (rightEntities.get(i).isItem_selected()) {
                            no_selected_Item.setText(String.valueOf(count++));
                        }
                    }
                    if ((count - 1) == rightEntities.size()) {

                        checked_right_img.setVisibility(View.VISIBLE);
                        Constants.CHECKED_RIGHT_CLICK = true;
                    } else {
                        Constants.CHECKED_RIGHT_CLICK = false;
                        checked_right_img.setVisibility(View.INVISIBLE);
                    }
                    showDoneButton();
                }
            }, 300);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GlassOperation extends AsyncTask<String, Void, String> {
        String Condition_Selected = "";

        GlassOperation(String value) {
            Condition_Selected = value;
        }

        @Override
        protected String doInBackground(String... params) {
            String posi = "";
            for (int i = 0; i < glassEntities.size(); i++) {
                if (glassEntities.get(i).getId() == ID) {
                    FrontRearEntity entity = glassEntities.get(i);
                    if (entity.getId() == glassEntities.get(i).getId()) {
                        glassEntities.get(i).setValue(Condition_Selected);
                        glassEntities.get(i).setBitmap(myLogo);
                        glassEntities.get(i).setItem_selected(true);
                        posi = String.valueOf(glassEntities.get(i).getId());
                        // storeFrontRear(frontRearEntities);
                        frontAdapter.updateAdapter(glassEntities.get(i), glassEntities.get(i).getId());
                        break;
                    }
                }
            }
            return posi;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //     mLayoutManager.scrollToPositionWithOffset(Integer.parseInt(result), 20);
                    int count = 1;
                    for (int i = 0; i < glassEntities.size(); i++) {

                        //Not used now
                        if (glassEntities.get(i).isItem_selected()) {
                            no_selected_Item.setText(String.valueOf(count++));
                        }
                    }
                    if ((count - 1) == glassEntities.size()) {
                        Constants.CHECKED_GLASS_CLICK = true;
                        checked_glass_img.setVisibility(View.VISIBLE);
                    } else {
                        Constants.CHECKED_GLASS_CLICK = false;
                        checked_glass_img.setVisibility(View.INVISIBLE);
                    }
                    showDoneButton();
                }
            }, 300);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    public void frontArticle() {


        frontRearFacade = new FrontRearFacade(this);
        if (frontRearEntities == null || frontRearEntities.isEmpty()) {
            frontRearEntities = frontRearFacade.setListFrontEntity();
        }
        int total = frontRearEntities.size();
        total_Item.setText(String.valueOf(total));
        for (int i = 0; i < frontRearEntities.size(); i++) {
            if (frontRearEntities.get(i).isItem_selected()) {
                no_selected_Item.setText(String.valueOf(count++));
            }
        }
        frontAdapter = new FrontAdapterNew(this, frontRearEntities, this);
        rvCommon.setAdapter(frontAdapter);
    }

    public void rearArticle() {


        frontRearFacade = new FrontRearFacade(this);
        if (rearEntities == null || rearEntities.isEmpty()) {
            rearEntities = frontRearFacade.setListRearEntity();
        }
        int total = rearEntities.size();
        total_Item.setText(String.valueOf(total));
        frontAdapter = new FrontAdapterNew(this, rearEntities, this);
        rvCommon.setAdapter(frontAdapter);
    }

    public void leftArticle() {


        frontRearFacade = new FrontRearFacade(this);
        if (leftEntities == null || leftEntities.isEmpty()) {
            leftEntities = frontRearFacade.setListLeftEntity();
        }
        int total = leftEntities.size();
        total_Item.setText(String.valueOf(total));
        frontAdapter = new FrontAdapterNew(this, leftEntities, this);
        rvCommon.setAdapter(frontAdapter);
    }

    public void rightArticle() {


        frontRearFacade = new FrontRearFacade(this);
        if (rightEntities == null || rightEntities.isEmpty() ) {
            rightEntities = frontRearFacade.setListRightEntity();
        }
        int total = rightEntities.size();
        total_Item.setText(String.valueOf(total));
        frontAdapter = new FrontAdapterNew(this, rightEntities, this);
        rvCommon.setAdapter(frontAdapter);
    }

    public void glassArticle() {
        frontRearFacade = new FrontRearFacade(this);
        if (glassEntities == null || glassEntities.isEmpty()) {
            glassEntities = frontRearFacade.setListGlassEntity();
        }
        int total = glassEntities.size();
        total_Item.setText(String.valueOf(total));
        frontAdapter = new FrontAdapterNew(this, glassEntities, this);
        rvCommon.setAdapter(frontAdapter);
    }

    private void setLisner() {

        btnDone.setOnClickListener(this);

        frontLayout.setOnClickListener(this);
        rearLayout.setOnClickListener(this);
        leftLayout.setOnClickListener(this);
        rightLayout.setOnClickListener(this);
        glassLayout.setOnClickListener(this);
        Constants.CHECKED_FRONT_CLICK = false;
        Constants.CHECKED_REAR_CLICK = false;
        Constants.CHECKED_LEFT_CLICK = false;
        Constants.CHECKED_RIGHT_CLICK = false;
        Constants.CHECKED_GLASS_CLICK = false;

    }

    public void callFrontAdapter() {

        for (int i = 0; i < frontRearEntities.size(); i++) {
            if (frontRearEntities.get(i).getId() == ID) {
                frontRearEntities.get(i).setSelected_Name(true);
            } else {
                frontRearEntities.get(i).setSelected_Name(false);
            }

            // if you want to show checked item in bold
         /*   if (!frontRearEntities.get(i).isItem_selected()){
                   // ! item is not checked
                if (frontRearEntities.get(i).getId() == ID)
                {
                    frontRearEntities.get(i).setSelected_Name(true);
                }
                else{
                    frontRearEntities.get(i).setSelected_Name(false);
                }
            }*/
        }

    }

    public void callRearAdapter() {

        for (int i = 0; i < rearEntities.size(); i++) {
            if (rearEntities.get(i).getId() == ID) {
                rearEntities.get(i).setSelected_Name(true);
            } else {
                rearEntities.get(i).setSelected_Name(false);
            }


        }

    }

    public void callLeftAdapter() {

        for (int i = 0; i < leftEntities.size(); i++) {
            if (leftEntities.get(i).getId() == ID) {
                leftEntities.get(i).setSelected_Name(true);
            } else {
                leftEntities.get(i).setSelected_Name(false);
            }
        }

    }

    public void callRightAdapter() {

        for (int i = 0; i < rightEntities.size(); i++) {
            if (rightEntities.get(i).getId() == ID) {
                rightEntities.get(i).setSelected_Name(true);
            } else {
                rightEntities.get(i).setSelected_Name(false);
            }


        }

    }

    public void callGlassAdapter() {

        for (int i = 0; i < glassEntities.size(); i++) {
            if (glassEntities.get(i).getId() == ID) {
                glassEntities.get(i).setSelected_Name(true);
            } else {
                glassEntities.get(i).setSelected_Name(false);
            }


        }

    }

    public void showDoneButton() {

        if (CHECKED_FRONT_CLICK && Constants.CHECKED_REAR_CLICK && Constants.CHECKED_LEFT_CLICK &&
                Constants.CHECKED_RIGHT_CLICK && Constants.CHECKED_GLASS_CLICK) {
            done_layout.setVisibility(View.VISIBLE);

        } else {
            done_layout.setVisibility(View.GONE);
        }

    }

/*
    public class UploadDataOnServer extends AsyncTask<String, InpsectionObj, JSONObject> {

        String url = "";
        String pair;
        String method = "";

        public UploadDataOnServer(String apiUrl, String shareSMS, String method2) {
            url = apiUrl;
            pair = shareSMS;
            method = method2;
            // TODO Auto-generated constructor stub
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {

            InputStream inputStream = null;
            String result = "";
            JSONObject js1 = null;
            try
            {
                JSONObject jsonBody = new JSONObject();

                InpsectionObj obj=new InpsectionObj();
                // MasterData.inspectrefno="420";
                jsonBody.put("inspectrefno", MasterData.inspectrefno);



                //Upload photo
                jsonBody.put("rcimage", MasterData.rcimage);
                jsonBody.put("policyimage", MasterData.policyimage);
                jsonBody.put("chasisimage", MasterData.chasisimage);
                jsonBody.put("odoimage", MasterData.odoimage);
                jsonBody.put("customername", MasterData.customername);
                //End Here

                //Self Data
                jsonBody.put("insuredmobileno", MasterData.insuredmobileno);
                jsonBody.put("vehicleregno", MasterData.vehicleregno);
                jsonBody.put("inspectionby", MasterData.inspectionby);
                jsonBody.put("chasisno", MasterData.chasisno);

                jsonBody.put("prevpolno", MasterData.prevpolno);
                jsonBody.put("makeclass", MasterData.makeclass);

                jsonBody.put("engineno", MasterData.engineno);
                jsonBody.put("frompoldate", MasterData.frompoldate);
                jsonBody.put("topoldate", MasterData.topoldate);


                jsonBody.put("registrationdate", MasterData.registrationdate);
                jsonBody.put("fuelused", MasterData.fuelused);

                jsonBody.put("vehiclecalss", MasterData.vehiclecalss);
                jsonBody.put("previnsurancename", MasterData.previnsurancename);
                jsonBody.put("insepctiondatetime", MasterData.insepctiondatetime);
                jsonBody.put("inspectionvideo", "VideoInspection");


                //Self Declare form data front end


                //  front=frontBumper+","+frontRightApron+","+frontGrill;
                jsonBody.put("frontBumperstatus", frontBumper.toString());
                jsonBody.put("frontpanelstatus", frontPanel.toString());
                jsonBody.put("indicatorlightrtstatus", frontIndicatorLightRt.toString());
                jsonBody.put("headlampltstatus", frontHeadLempLeft.toString());

                jsonBody.put("foglampltstatus", frontFogLampLeft.toString());
                jsonBody.put("leftapronstatus", frontLftApron.toString());
                jsonBody.put("indicatorlightltstatus", fronLeftInd.toString());
                jsonBody.put("grillstatus", frontGrill.toString());

                jsonBody.put("bonnetstatus", frontBonet.toString());
                jsonBody.put("headlamprtstatus", frontHeadLempRight.toString());
                jsonBody.put("foglamprtstatus", frontFogLampRight.toString());
                jsonBody.put("rightapronstatus", frontRightApron.toString());

                jsonBody.put("inspectionby","inspectionby");
             //   jsonBody.put("inspectionby",MasterData.inspectionby);
                jsonBody.put("inspectdate",new Date().toString());
                //##End here

                //Rear data

                jsonBody.put("rearbumperstatus", rearBumper.toString());
                jsonBody.put("dickeydoorstatus", rearDickydoor.toString());
                jsonBody.put("taillamprtstatus", reartraillampright.toString());
                jsonBody.put("dickystatus", rearDicky.toString());
                jsonBody.put("taillampltstatus", reartaillemleft.toString());

                //End Here
                //Left
                jsonBody.put("ltfrontdoorstattus", leftfrontdoor.toString());
                jsonBody.put("ltpillardoorastatus", leftpilar1.toString());
                jsonBody.put("ltpillarcenterbstatus", leftpilar2.toString());
                jsonBody.put("ltreardoorstatus", leftreardoor.toString());
                jsonBody.put("ltpillarrearcstatus", leftpiler3.toString());
                jsonBody.put("ltrunningboardstatus", leftrunninboard.toString());
                jsonBody.put("ltqtrpanelstatus", leftpannel.toString());

                //ENd

                //Right


                jsonBody.put("rtqtrpanelstatus", rightPannel.toString());
                jsonBody.put("floorsilencerstatus", rightfloorsilencer.toString());
                jsonBody.put("rtrearpillarcstatus", rightrearpilar2.toString());
                jsonBody.put("rtfrontdoorstatus", rightfrontdoor.toString());
                jsonBody.put("fenderstatus", rightfrontfender.toString());
                jsonBody.put("rtcenterpillarbstatus", rightrtcentpiller1.toString());
                jsonBody.put("rtreardoorstatus", rightreardoor.toString());
                jsonBody.put("rearviewmirrorltstatus", rightrearviewmooror.toString());
                jsonBody.put("rtrunningboardstatus", rightrunningboard.toString());
                jsonBody.put("rtfrontpillarastatus", rightfrontpilarA.toString());

                //End here

                //Glass
                jsonBody.put("backglassstatus", gbackglass.toString());
                jsonBody.put("rrdoorglassstatus", grrdoorglass.toString());
                jsonBody.put("rimstatus", grim.toString());
                jsonBody.put("roofliningstatus", grooflining.toString());
                jsonBody.put("frontwindshieldstatus", gfrontwindshield.toString());
                jsonBody.put("lfdoorglassstatus", glfdoorglass.toString());

                jsonBody.put("undercarriagestatus", gundercarrigae.toString());
                jsonBody.put("seatsfrontstatus", gseatsfront.toString());
                jsonBody.put("rfdoorglassstatus", grfdoorglass.toString());
                jsonBody.put("lrdoorglassstatus", glrdoorglass.toString());
                jsonBody.put("toproofstatus", gtoproof.toString());

                jsonBody.put("seatsrearstatus", gseatsrear.toString());
                jsonBody.put("dashboardstatus", gdashboard.toString());
                jsonBody.put("instrumentmetersstatus", ginstrumentmeter.toString());
                jsonBody.put("enginestatus", gengine.toString());
                jsonBody.put("gearboxstatus", ggearbox.toString());
                jsonBody.put("suspensionstatus", gsuspension.toString());

                jsonBody.put("steeringsystemstatus", gsteeringsystem.toString());
                jsonBody.put("radiatorstatus", gradiator.toString());
                jsonBody.put("airconditionerstatus", gairconditioner.toString());
                jsonBody.put("driveshaftstatus", gairconditioner.toString());
                jsonBody.put("wheelsstatus", gwheels.toString());
                jsonBody.put("brakesstatus", gbrakes.toString());
                jsonBody.put("musicsystemstatus", gmusicsystem.toString());
                Gson gson = new Gson();
                String serialize = jsonBody.toString();
                //obj.setVehicleNo(MasterData.vehicleregno);
                try {
                    pair = URLEncoder.encode(serialize, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.addHeader("content-type", "application/json;charset=UTF-8");
                    String json = "";
                    js1 = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    //  jsonObject.accumulate("postDataString", pair);
                    StringEntity se = new StringEntity(pair);


                    StringEntity jsonparam = new StringEntity(serialize);
                    jsonparam.setChunked(true);
                    httpPost.setEntity(jsonparam);
                    HttpResponse httpResponse = httpclient.execute(httpPost);

                    inputStream = httpResponse.getEntity().getContent();

                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(inputStream, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        inputStream.close();

                        js1 = new JSONObject(sb.toString());





                    } catch (Exception e) {
                        Log.e("Buffer Error",
                                "Error converting result " + e.toString());
                    }

                    System.out.println(inputStream.toString());

                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }
            }

            catch (JSONException ex)
            {

            }
            return js1;

        }

        protected void onPostExecute(JSONObject json) {
            // String Registration_Date = null,engin_Number=null,Make_Name=null,Chassis_Number=null,Model_Name=null,fuel_Type=null;

            Intent intent = new Intent(getApplicationContext(), ThankYouActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            progressDialog.hide();
            if (json != null) {



            }
            else
            {

            }

        }
    }
    */
    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        Boolean result=false;
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            result=true;

        }
        else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            result=false;
        }
        return  result;
    }



    @Override
    public void onBackPressed() {
        ShowDialogForExit("Are You Sure To Exit?");
    }

    public  void ShowDialogForExit(String value)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage(value);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void uploadFile() {

//        // Toast.makeText(this, "TEst"+fileName, Toast.LENGTH_SHORT).show();
//
//        //  uploadingDilog.show();
//        new DeclareSelfActivity2.LongOperation().execute();


    }
    /*
    private class LongOperation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            FTPClient client = new FTPClient();


            try {
                File file = new File(MasterData.leftimage);


                client.connect(FTP_HOST, 21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
                //  client.changeDirectory("/wwwhome/");
                client.changeDirectory("/");
                client.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //   uploadingDilog.hide();
            //  startActivity(new Intent(VideoPage.this, DeclareSelfActivity2.class));
            // MasterData.Stage="1";
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // MasterData.Stage="0";
        }
    }
    */
    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof VehicleDetailResponse) {
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

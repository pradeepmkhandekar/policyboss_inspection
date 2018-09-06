package inspection.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import inspection.splash.SplashScreen;
import policyboss.com.inspect.R;


public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_thank_you);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView=findViewById(R.id.txtThankyou);
        Button button=findViewById(R.id.btnEndInspection);
        Button btnEndInspection1=findViewById(R.id.btnEndInspection1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                //startActivity(intent);
                 finish();

            }
        });

        btnEndInspection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent=new Intent(getApplicationContext(),SplashScreen.class);
//                    MasterData.leftimage=null;
//                    MasterData.Stage="0";
//                    DeclareSelfActivity2.frontRearEntities.clear();
//                    DeclareSelfActivity2.glassEntities.clear();
//                    DeclareSelfActivity2.rearEntities.clear();
//                    DeclareSelfActivity2.rightEntities.clear();
//                    DeclareSelfActivity2.leftEntities.clear();
                    finish();
                    startActivity(intent);

            }
        });
    //    textView.setText("Thank You\nSelf Inspection Application with\nReference Number "+ MasterData.inspectrefno+" has been\nsubmitted successfully.\nOur team will review and do needful for policy issuance.\nYou may contact us at 1800-xxx-xxxxx in case of any query.");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
////        switch (item.getItemId()) {
////
////            case R.id.action_home:
////
////                Intent intent = new Intent(this, HomeActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent);
////
////                finish();
////                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}

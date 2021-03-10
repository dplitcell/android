package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {
    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "Back not allowed! Press again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        String sessionId = appParameters.getSessionID();
        if (sessionId.matches("0")) {
            Intent newIntent = new Intent(this, Welcome.class);
            startActivity(newIntent);
        }
        String name = appParameters.getUserName();
        String msgDisplay = "Hi! " + name.trim();
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(msgDisplay);
        TextView tvLogout = (TextView) findViewById(R.id.tvLogout);
        msgDisplay = "Not you? Logout";
        SpannableString ssLogout = new SpannableString(msgDisplay);
        ClickableSpan csLogout = new ClickableSpan () {
            @Override
            public void onClick(View textView) {
                appParameters.doLogout(MenuActivity.this);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ssLogout.setSpan(csLogout,0, msgDisplay.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogout.setText(ssLogout);
        tvLogout.setMovementMethod(LinkMovementMethod.getInstance());

        Button btEmpData = (Button) findViewById(R.id.btEmpData);
        btEmpData.setBackground(appParameters.getButtonDefaultShape());
        Button btMonthRepo = (Button) findViewById(R.id.btMonthRepo);
        btMonthRepo.setBackground(appParameters.getButtonDefaultShape());
        Button btYearRepo = (Button) findViewById(R.id.btYearRepo);
        btYearRepo.setBackground(appParameters.getButtonDefaultShape());
        Button btOtherRepo = (Button) findViewById(R.id.btOtherRepo);
        btOtherRepo.setBackground(appParameters.getButtonDefaultShape());
        Button btSrvReq = (Button) findViewById(R.id.btSrvReq);
        btSrvReq.setBackground(appParameters.getButtonDefaultShape());
        Button btAboutApp = (Button) findViewById(R.id.btAboutApp);
        btAboutApp.setBackground(appParameters.getButtonDefaultShape());
        LinearLayout llESSMenu = (LinearLayout) findViewById(R.id.llESSMenu);

        if (!appParameters.getModuleNo().matches("01"))
            llESSMenu.setVisibility(LinearLayout.GONE);
        else
            llESSMenu.setVisibility(LinearLayout.VISIBLE);

        btEmpData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Profile";
                String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\", \"user_id\":\""+appParameters.getUserID()+"\"}";
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI (MenuActivity.this, callUrl, inputJsonObject, appParameters, MyProfile.class, false, MenuActivity.class, true, false, 15000);
            }
        });

        btMonthRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MenuActivity.this, SubMenu.class);
                in.putExtra("menu_of", "M");
                startActivity(in);
            }
        });

        btYearRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MenuActivity.this, SubMenu.class);
                in.putExtra("menu_of", "Y");
                startActivity(in);
            }
        });

        btOtherRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MenuActivity.this, SubMenu.class);
                in.putExtra("menu_of", "O");
                startActivity(in);
            }
        });

        btSrvReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MenuActivity.this, SubMenu.class);
                in.putExtra("menu_of", "S");
                startActivity(in);
            }
        });

        btAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/About";
                String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\"}";
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI (MenuActivity.this, callUrl, inputJsonObject, appParameters, AboutApp.class, false, MenuActivity.class, true, false, 15000);
            }
        });
    }
}
package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyProfile extends AppCompatActivity {
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
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        String sessionId = appParameters.getSessionID();
        if (sessionId.matches("0")) {
            Intent newIntent = new Intent(MyProfile.this, Welcome.class);
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
                appParameters.doLogout(MyProfile.this);
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
        initTable();

        Button btMenuData = (Button) findViewById(R.id.btMenuData);
        btMenuData.setBackground(appParameters.getButtonDefaultShape());
        btMenuData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyProfile.this, MenuActivity.class);
                startActivity(in);
            }
        });
    }

    public void initTable() {
        AppParameters appParameters = (AppParameters) getApplicationContext();
        TableLayout tvMyProfile = (TableLayout) findViewById(R.id.tvMyProfile);
        JSONObject callResponseJSON = appParameters.getCallResponse();
        JSONArray paramJsonArray = null;
        String paramVal = null;
        try {
            paramJsonArray = callResponseJSON.getJSONArray("param_list");
            int length = paramJsonArray.length();
            for (int i = 0; i < length; i++) {
                paramVal = paramJsonArray.getString(i);
                TableRow rowMyProfile = new TableRow(this);
                TableRow.LayoutParams lpMyProfile = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                lpMyProfile.setMargins(15, 15, 15, 15);

                rowMyProfile.setLayoutParams(lpMyProfile);

                if (i % 3 == 0) rowMyProfile.setBackgroundColor(Color.argb(100, 210, 255, 210));
                if (i % 3 == 1) rowMyProfile.setBackgroundColor(Color.argb(100, 255, 210, 210));
                if (i % 3 == 2) rowMyProfile.setBackgroundColor(Color.argb(100, 210, 210, 255));
                TextView tvHead = new TextView(this);
                tvHead.setPadding(5, 5, 5, 5);
                tvHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                TextView tvSep = new TextView(this);
                tvSep.setPadding(5, 5, 5, 5);
                tvSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
                TextView tvVal = new TextView(this);
                tvVal.setPadding(5, 5, 5, 5);
                tvVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                tvHead.setText(paramVal.substring(0, paramVal.indexOf(":")));
                tvSep.setText("::");
                tvVal.setText(paramVal.substring(paramVal.indexOf(":") + 1));
                rowMyProfile.addView(tvHead);
                rowMyProfile.addView(tvSep);
                rowMyProfile.addView(tvVal);
                tvMyProfile.addView(rowMyProfile, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MyProfile.this, "Error in Server Response", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(MyProfile.this, MenuActivity.class);
            startActivity(newIntent);
        }
    }
}
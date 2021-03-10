package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class ViewSR extends AppCompatActivity {
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
        setContentView(R.layout.activity_viewsr);
        getSupportActionBar().hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        final String sessionId = appParameters.getSessionID();
        if (sessionId.matches("0")) {
            Intent newIntent = new Intent(ViewSR.this, Welcome.class);
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
                appParameters.doLogout(ViewSR.this);
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

        final TextView tvMenu = (TextView) findViewById(R.id.tvMenu);
        TableLayout tblView = (TableLayout) findViewById(R.id.tblView);
        JSONObject callResponseJSON = appParameters.getCallResponse();
        JSONArray paramJsonArray = null;

        try {
            tvMenu.setText(callResponseJSON.getString("heading").replace("<br>", "\n"));
            paramJsonArray = callResponseJSON.getJSONArray("param_list");
            int length = paramJsonArray.length();
            List<String> listDisplay = new ArrayList<String>(length);
            String param = null;
            String displayVal = null;
            String refUrl = null;
            int iLoop = 0;
            for (iLoop = 0; iLoop < length; iLoop++) {
                param = paramJsonArray.getString(iLoop);
                listDisplay.add(param);
                TableRow rowTable = new TableRow(this);
                rowTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
                rowTable.setWeightSum(18);

                if (iLoop % 3 == 0) rowTable.setBackgroundColor(Color.argb(100, 210, 255, 210));
                if (iLoop % 3 == 1) rowTable.setBackgroundColor(Color.argb(100, 255, 210, 210));
                if (iLoop % 3 == 2) rowTable.setBackgroundColor(Color.argb(100, 210, 210, 255));

                displayVal = param.substring(0, param.indexOf("#1#")).replace("SR Date: ", " ").replace("-20", "-").replace("SR No: ", " ").replace("<br>", "\n").trim();
                refUrl = param.substring(param.indexOf("#4#")+3, param.indexOf("#5#"));
                TextView tvSerial = new TextView(this);
                tvSerial.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                tvSerial.setText(displayVal);
                if (iLoop == 0)
                    tvSerial.setTextColor(Color.BLACK);
                else {
                    tvSerial.setTextColor(Color.BLUE);
                    tvSerial.setLinkTextColor(Color.parseColor("#9C27B0"));
                }
                if(refUrl.indexOf("TypeId")!=-1){
                    final String btSrlSRNo = refUrl.substring(refUrl.indexOf("ReqId=")+6);
                    final SpannableString ssSerial = new SpannableString(displayVal);
                    ClickableSpan csSerial = new ClickableSpan () {
                        @Override
                        public void onClick(View textView) {
                            JSONObject btSrlJsonObj = new JSONObject();
                            try {
                                btSrlJsonObj.put("session_id", sessionId);
                                btSrlJsonObj.put("type_id", "D");
                                btSrlJsonObj.put("sr_no", btSrlSRNo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String callUrl = "/ViewSR";
                            appParameters.callAPI(ViewSR.this, callUrl, btSrlJsonObj, appParameters, ViewSR.class, false, MenuActivity.class, true, true, 30000);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    ssSerial.setSpan(csSerial,0, displayVal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvSerial.setText(ssSerial);
                    tvSerial.setMovementMethod(LinkMovementMethod.getInstance());
                }
                rowTable.addView(tvSerial);

                displayVal = param.substring(param.indexOf("#1#")+3,param.indexOf("#2#"));
                TextView tvDet = new TextView(this);
                if (iLoop == 0)
                    tvDet.setTextColor(Color.BLACK);
                else
                    tvDet.setTextColor(Color.RED);
                tvDet.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,5f));
                tvDet.setText(displayVal.replace("<br>", "\n"));
                rowTable.addView(tvDet);

                displayVal = param.substring(param.indexOf("#2#")+3,param.indexOf("#3#"));
                TextView tvRem = new TextView(this);
                if (iLoop == 0)
                    tvRem.setTextColor(Color.BLACK);
                else
                    tvRem.setTextColor(Color.BLUE);
                tvRem.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,5f));
                tvRem.setText(displayVal.replace("<br>", "\n"));
                rowTable.addView(tvRem);

                displayVal = param.substring(param.indexOf("#3#")+3,param.indexOf("#4#")).replace("<br>", "\n");
                refUrl = param.substring(param.indexOf("#5#")+3);
                TextView tvAct = new TextView(this);
                if (iLoop == 0)
                    tvAct.setTextColor(Color.BLACK);
                else {
                    tvAct.setTextColor(Color.RED);
                    tvAct.setLinkTextColor(Color.parseColor("#9C27B0"));
                }
                tvAct.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,5f));
                tvAct.setText(displayVal);

                if(refUrl.indexOf("TypeId")!=-1){
                    final String btActSRNo = refUrl.substring(refUrl.indexOf("ReqId=")+6);
                    final SpannableString ssActSRNo = new SpannableString(displayVal);
                    ClickableSpan csActSRNo = new ClickableSpan () {
                        @Override
                        public void onClick(View textView) {
                            JSONObject btActJsonObj = new JSONObject();
                            try {
                                btActJsonObj.put("session_id", sessionId);
                                btActJsonObj.put("type_id", "U");
                                btActJsonObj.put("sr_no", btActSRNo);
                                btActJsonObj.put("emp_no", appParameters.getUserID());
                                btActJsonObj.put("module", appParameters.getModuleNo());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String callUrl = "/CreateSR";
                            appParameters.callAPI(ViewSR.this, callUrl, btActJsonObj, appParameters, CreateSR.class, false, MenuActivity.class, true, true, 15000);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    ssActSRNo.setSpan(csActSRNo,0, displayVal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvAct.setText(ssActSRNo);
                    tvAct.setMovementMethod(LinkMovementMethod.getInstance());
                }
                rowTable.addView(tvAct);

                tblView.addView(rowTable);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        TableRow rowTable = new TableRow(this);
        rowTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
        rowTable.setWeightSum(4);
        Button btMenuData = new Button(this);
        btMenuData.setBackground(appParameters.getButtonDefaultShape());
        btMenuData.setText("GO BACK");
        btMenuData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ViewSR.this, MenuActivity.class);
                startActivity(in);
            }
        });
        Button btLogout = new Button(this);
        btLogout.setBackground(appParameters.getButtonDefaultShape());
        btLogout.setText("LOGOUT");
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences appSharedVal = getSharedPreferences(appParameters.getSharedMem(), MODE_PRIVATE);
                SharedPreferences.Editor appSharedEdit = appSharedVal.edit();
                appSharedEdit.remove("key_val");
                appSharedEdit.apply();
                String callUrl = "/Logout";
                String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\"}";
                appParameters.setSessionID("0");
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI(ViewSR.this, callUrl, inputJsonObject, appParameters, Login.class, false, Login.class, true, true, 5000);
            }
        });
        TableRow.LayoutParams margeLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2f);
        rowTable.addView(btMenuData, margeLayout);
        rowTable.addView(btLogout,margeLayout);
        tblView.addView(rowTable);
 */
        Button btMenuData = (Button) findViewById(R.id.btMenuData);
        btMenuData.setBackground(appParameters.getButtonDefaultShape());
        btMenuData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ViewSR.this, MenuActivity.class);
                startActivity(in);
            }
        });
    }
}
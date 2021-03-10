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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ReportGen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
        setContentView(R.layout.activity_reportgen);
        getSupportActionBar().hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        String sessionId = appParameters.getSessionID();
        if (sessionId.matches("0")) {
            Intent newIntent = new Intent(ReportGen.this, Welcome.class);
            startActivity(newIntent);
        }
        String name = appParameters.getUserName();
        JSONObject reportJson = appParameters.getReportParam();
        String reportType = null;
        String paramType = null;
        try {
            reportType = reportJson.getString("report_type");
            paramType = reportJson.getString("param_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String msgDisplay = "Hi! " + name.trim();
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(msgDisplay);
        TextView tvLogout = (TextView) findViewById(R.id.tvLogout);
        msgDisplay = "Not you? Logout";
        SpannableString ssLogout = new SpannableString(msgDisplay);
        ClickableSpan csLogout = new ClickableSpan () {
            @Override
            public void onClick(View textView) {
                appParameters.doLogout(ReportGen.this);
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
        tvMenu.setText(reportType);
        final TextView tvParam = (TextView) findViewById(R.id.tvParam);
        tvParam.setText(paramType);
        Button btDownload = (Button) findViewById(R.id.btDownload);
        btDownload.setBackground(appParameters.getButtonDefaultShape());
        Button btMenuData = (Button) findViewById(R.id.btMenuData);
        btMenuData.setBackground(appParameters.getButtonDefaultShape());
        Spinner spParam = findViewById(R.id.spParam);
        spParam.setOnItemSelectedListener(this);

        JSONObject callResponseJSON = appParameters.getCallResponse();
        JSONArray paramJsonArray = null;
        List<String> paramLists = null;
        try {
            paramJsonArray = callResponseJSON.getJSONArray("param_list");
            int length = paramJsonArray.length();
            paramLists = new ArrayList<String>(length);
            for (int i = 0; i < length; i++) {
                paramLists.add(paramJsonArray.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ReportGen.this, "Error in Server Response", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(ReportGen.this, MenuActivity.class);
            startActivity(newIntent);
        }

        ArrayAdapter arrayAdapterParam = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paramLists);
        arrayAdapterParam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParam.setAdapter(arrayAdapterParam);

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppParameters appParameters = (AppParameters) getApplicationContext();
                String url = "/Report";
                appParameters.callAPI(ReportGen.this, url, appParameters.getReportParam(), appParameters, MenuActivity.class, true, MenuActivity.class, true, true, 30000);
            }
        });

        btMenuData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ReportGen.this, MenuActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AppParameters appParameters = (AppParameters) getApplicationContext();
        JSONObject reportJson = appParameters.getReportParam();
        try {
            reportJson.remove("param_val");
            reportJson.put("param_val", adapterView.getItemAtPosition(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appParameters.setReportParam(reportJson);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        AppParameters appParameters = (AppParameters) getApplicationContext();
        JSONObject reportJson = appParameters.getReportParam();
        try {
            reportJson.remove("param_val");
            reportJson.put("param_val", adapterView.getItemAtPosition(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appParameters.setReportParam(reportJson);
    }
}
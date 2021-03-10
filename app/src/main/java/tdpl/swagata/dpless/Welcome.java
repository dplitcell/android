package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Welcome extends AppCompatActivity {
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
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            Objects.requireNonNull(getSupportActionBar()).hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        appParameters.setInitials();
        Bundle extras = getIntent().getExtras();
        String inputString = null;
        if (extras != null)
            inputString = extras.getString("json_param");
        if (inputString != null) {
            try {
                JSONObject inputJSON = new JSONObject(inputString);
                appParameters.callAPI(this, inputJSON.getString("ws_url"), inputJSON, appParameters, Class.forName(inputJSON.getString("target_class")), Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, 10000);
            } catch (JSONException | ClassNotFoundException e) {
                e.printStackTrace();
                Intent loginActivity = new Intent(Welcome.this, Login.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginActivity);
                finish();
            }
        } else {
            SharedPreferences appSharedVal = getSharedPreferences(appParameters.getSharedMem(), MODE_PRIVATE);
            String keyVal = appSharedVal.getString("key_val", "0");
            if (keyVal.matches("0")) {
                Intent loginActivity = new Intent(Welcome.this, Login.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginActivity);
                finish();
            } else {
                JSONObject inputJsonObject = new JSONObject();
                try {
                    inputJsonObject.put("user", "0");
                    inputJsonObject.put("pass", "0");
                    inputJsonObject.put("module", appParameters.getModuleNo());
                    inputJsonObject.put("key_flag", "1");
                    inputJsonObject.put("key_val", keyVal);
                    inputJsonObject.put("rem_flag", "1");
                    appParameters.setSessionID("0");
                    appParameters.callAPI(Welcome.this, "/Login", inputJsonObject, appParameters, MenuActivity.class, false, Login.class, true, true, 5000);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent loginActivity = new Intent(Welcome.this, Login.class);
                    loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginActivity);
                    finish();
                }
            }
        }
    }
}
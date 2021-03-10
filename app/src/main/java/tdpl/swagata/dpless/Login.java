package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    int moduleID = 0;
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
        setContentView(R.layout.activity_login);
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        getSupportActionBar().hide();

        final EditText etLSUsrID = (EditText) findViewById(R.id.etLSUsrID);
        final EditText etLSPassWd = (EditText) findViewById(R.id.etLSPassWd);
        Button btLSLogin = (Button) findViewById(R.id.btLSLogin);
        btLSLogin.setBackground(appParameters.getButtonDefaultShape());
        final TextView tvLoginAs1 = (TextView) findViewById(R.id.tvLoginAs1);
        final TextView tvLoginAs2 = (TextView) findViewById(R.id.tvLoginAs2);
        final TextView tvLSTitle = (TextView) findViewById(R.id.tvLSTitle);
        final CheckBox cbRemember = (CheckBox) findViewById(R.id.cbRemember);

        etLSUsrID.setInputType(InputType.TYPE_CLASS_PHONE);
        etLSUsrID.requestFocus();
        etLSUsrID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        cbRemember.setChecked(false);
        tvLSTitle.setText("Login as Indentor");

        final SpannableString[] ssLoginAs = {new SpannableString("Login as Indentor (Employee)"), new SpannableString("Login as Service Provider"), new SpannableString("Login as Service Supervisor")};

        ClickableSpan csLoginAs[] = {
            new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    tvLoginAs1.setText(ssLoginAs[1]);
                    tvLoginAs2.setText(ssLoginAs[2]);
                    tvLSTitle.setText("Login as Indentor");
                    moduleID = 0;
                    etLSUsrID.setInputType(InputType.TYPE_CLASS_PHONE);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            },
            new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    tvLoginAs1.setText(ssLoginAs[0]);
                    tvLoginAs2.setText(ssLoginAs[2]);
                    moduleID = 1;
                    tvLSTitle.setText("Login as Provider");
                    etLSUsrID.setInputType(InputType.TYPE_CLASS_TEXT);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            },
            new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    tvLoginAs1.setText(ssLoginAs[0]);
                    tvLoginAs2.setText(ssLoginAs[1]);
                    tvLSTitle.setText("Login as Supervisor");
                    moduleID = 2;
                    etLSUsrID.setInputType(InputType.TYPE_CLASS_TEXT);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }
        };

        ssLoginAs[0].setSpan(csLoginAs[0],9, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssLoginAs[1].setSpan(csLoginAs[1],9, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssLoginAs[2].setSpan(csLoginAs[2],9, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoginAs1.setText(ssLoginAs[1]);
        tvLoginAs2.setText(ssLoginAs[2]);
        tvLoginAs1.setMovementMethod(LinkMovementMethod.getInstance());
        tvLoginAs2.setMovementMethod(LinkMovementMethod.getInstance());

        btLSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = etLSUsrID.getText().toString().trim();
                String passWd = etLSPassWd.getText().toString().trim();
                EditText etErr = null;
                Boolean validInput = !userID.isEmpty();
                String errMsg = "";
                if (!validInput) {
                    errMsg = "Enter the valid User ID";
                    etErr = etLSUsrID;
                } else {
                    validInput = !passWd.isEmpty();
                    if (!validInput) {
                        errMsg = "Enter the valid Password";
                        etErr = etLSPassWd;
                    }
                }
                if (!validInput) {
                    Toast.makeText(Login.this, errMsg,Toast.LENGTH_SHORT);
                    etErr.requestFocus();
                    etErr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    });
                } else {
                    String remFlag = "0";
                    if (cbRemember.isChecked()) remFlag = "1";
                    String module = appParameters.getModuleNo();
                    switch (moduleID) {
                        case 0:
                            module = "01";
                            break;
                        case 1:
                            module = "02";
                            break;
                        case 2:
                            module = "03";
                            break;
                    }
                    JSONObject inputJsonObject = new JSONObject();
                    try {
                        inputJsonObject.put("user", userID);
                        inputJsonObject.put("pass", passWd);
                        inputJsonObject.put("module", module);
                        inputJsonObject.put("key_flag", "0");
                        inputJsonObject.put("key_val", "0");
                        inputJsonObject.put("rem_flag", remFlag);
                        //appParameters.setSessionID("1");
                        appParameters.callAPI(Login.this, "/Login", inputJsonObject, appParameters, MenuActivity.class, false, Login.class, true, false, 5000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
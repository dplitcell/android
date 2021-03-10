package tdpl.swagata.dpless;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CreateSR extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    public List<String> listCat = null;
    public List<String> listCatId = null;
    public List<String> listArea = null;
    public List<String> listAreaId = null;
    public List<String> listSub = null;
    public List<String> listSubId = null;
    public List<String> listStat = null;
    public List<String> listStatId = null;
    public List<String> listAreaSel = null;
    public List<String> listAreaIdSel = null;
    public List<String> listSubSel = null;
    public List<String> listSubIdSel = null;
    public String selSubId = null;
    public String selStatus = null;
    public EditText etLoc = null;
    public EditText etMob = null;
    public EditText etEmail = null;
    public EditText etReq = null;
    public Spinner spArea = null;
    public Spinner spSub = null;
    public String locVal = null;
    public String mobVal = null;
    public String emailVal = null;
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
        setContentView(R.layout.activity_createsr);
        getSupportActionBar().hide();
        final AppParameters appParameters = (AppParameters) getApplicationContext();
        final String sessionId = appParameters.getSessionID();
        if (sessionId.matches("0")) {
            Intent newIntent = new Intent(CreateSR.this, Welcome.class);
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
                appParameters.doLogout(CreateSR.this);
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
        TableLayout tblCreate = (TableLayout) findViewById(R.id.tblCreate);
        JSONObject callResponseJSON = appParameters.getCallResponse();

        Spinner spCat = null;
        Spinner spStat = null;

       JSONArray paramJsonArray = null;
        try {
            tvMenu.setText(callResponseJSON.getString("heading"));

            final String typeId = callResponseJSON.getString("type_id");
            final String srNo = callResponseJSON.getString("sr_no");
            int inputType = 0;
            String param = null;
            int length = 0;

            paramJsonArray = callResponseJSON.getJSONArray("param_cat");
            length = paramJsonArray.length();
            listCat = new ArrayList<String>(length);
            listCatId = new ArrayList<String>(length);
            for (int i = 0; i < length; i++) {
                param = paramJsonArray.getString(i);
                listCatId.add(param.substring(0,  param.indexOf(":")));
                listCat.add(param.substring(param.indexOf(":")+1));
            }

            paramJsonArray = callResponseJSON.getJSONArray("param_area");
            length = paramJsonArray.length();
            listArea = new ArrayList<String>(length);
            listAreaId = new ArrayList<String>(length);
            listAreaSel = new ArrayList<String>();
            listAreaIdSel = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                param = paramJsonArray.getString(i);
                listAreaId.add(param.substring(0,  param.indexOf("#")));
                listArea.add(param.substring(param.indexOf("#")+1));
                if(listCatId.get(0).matches(param.substring(0,  param.indexOf(":")))){
                    listAreaIdSel.add(param.substring(0,  param.indexOf("#")));
                    listAreaSel.add(param.substring(param.indexOf("#")+1));
                }
            }

            paramJsonArray = callResponseJSON.getJSONArray("param_sub");
            length = paramJsonArray.length();
            listSub = new ArrayList<String>(length);
            listSubId = new ArrayList<String>(length);
            listSubSel = new ArrayList<String>();
            listSubIdSel = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                param = paramJsonArray.getString(i);
                listSubId.add(param.substring(0,  param.indexOf("$")));
                listSub.add(param.substring(param.indexOf("$")+1));
                if(listAreaId.get(0).matches(param.substring(0,  param.indexOf("#")))){
                    listSubIdSel.add(param.substring(0,  param.indexOf("$")));
                    listSubSel.add(param.substring(param.indexOf("$")+1));
                }
            }
            selSubId = listSubIdSel.get(0);

            paramJsonArray = callResponseJSON.getJSONArray("param_status");
            length = paramJsonArray.length();
            listStat = new ArrayList<String>(length);
            listStatId = new ArrayList<String>(length);
            for (int i = 0; i < length; i++) {
                param = paramJsonArray.getString(i);
                listStatId.add(param.substring(0,  param.indexOf(":")));
                listStat.add(param.substring(param.indexOf(":")+1));
            }
            selStatus = listStatId.get(0);

            String labelText = "Employee No";
            String initValue = callResponseJSON.getString("cont_ngs");
            final String contNgs = initValue;
            initTable(tblCreate, labelText, initValue,1);

            labelText = "Name";
            initValue = callResponseJSON.getString("cont_pers");
            initTable(tblCreate, labelText, initValue, 2);

            labelText = "Designation";
            initValue = callResponseJSON.getString("cont_desig");
            initTable(tblCreate, labelText, initValue, 3);

            labelText = "Unit";
            initValue = callResponseJSON.getString("group_desc");
            initTable(tblCreate, labelText, initValue, 4);

            labelText = "Head";
            initValue = callResponseJSON.getString("sub_group_desc");
            initTable(tblCreate, labelText, initValue, 5);

            labelText = "Location";
            if(typeId.matches("U")) {
                locVal = callResponseJSON.getString("cont_loc");
                initTable(tblCreate, labelText, locVal, 6);
            }
            if (typeId.matches("C")){
                etLoc = new EditText(this);
                etLoc.requestFocus();
                etLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
                inputType = InputType.TYPE_CLASS_TEXT;
                initTable(tblCreate, labelText, etLoc, " ", 6, inputType, 1);
            }

            labelText = "Mobile No";
            mobVal = callResponseJSON.getString("cont_mob");
            if(typeId.matches("U")) {
                initTable(tblCreate, labelText, mobVal, 7);
            }
            if (typeId.matches("C")){
                etMob = new EditText(this);
                inputType = InputType.TYPE_CLASS_PHONE;
                initTable(tblCreate, labelText, etMob, mobVal, 7, inputType, 1);
            }

            labelText = "Email ID";
            emailVal = callResponseJSON.getString("cont_email");
            if(typeId.matches("U")) {
                initTable(tblCreate, labelText, emailVal, 8);
            }
            if (typeId.matches("C")){
                etEmail = new EditText(this);
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                initTable(tblCreate, labelText, etEmail, emailVal, 8, inputType, 1);
            }

            labelText = "Service Head";
            spCat = new Spinner(this);
            spCat.setOnItemSelectedListener(this);
            initTable(tblCreate, labelText, spCat, listCat,9,101);

            labelText = "Service Area";
            spArea = new Spinner(this);
            spArea.setOnItemSelectedListener(this);
            initTable(tblCreate, labelText, spArea, listAreaSel,10,102);

            labelText = "Sub Area";
            spSub = new Spinner(this);
            spSub.setOnItemSelectedListener(this);
            initTable(tblCreate, labelText, spSub, listSubSel,11,103);

            labelText = "Request Details";
            initValue = " ";
            etReq = new EditText(this);
            if (typeId.matches("U")) {
                etReq.requestFocus();
                etReq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
            }
            inputType = InputType.TYPE_CLASS_TEXT;
            //inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
            initTable(tblCreate, labelText, etReq, initValue, 12, inputType, 3);

            labelText = "Request Status";
            spStat = new Spinner(this);
            spStat.setOnItemSelectedListener(this);
            initTable(tblCreate, labelText, spStat, listStat,13,104);

            TableRow rowTable = new TableRow(this);
            TableLayout.LayoutParams tblRowParam = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            tblRowParam.setMargins(0,5,0,5);
            rowTable.setLayoutParams(tblRowParam);
            rowTable.setWeightSum(2);
/*            TableRow.LayoutParams lpMyProfile = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lpMyProfile.setMargins(15, 15, 15, 15);
            rowTable.setLayoutParams(lpMyProfile);*/
            Button btSubmit = new Button(this);
            btSubmit.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            btSubmit.setTextColor(Color.BLUE);
            btSubmit.setBackground(appParameters.getButtonDefaultShape());
            btSubmit.setText("Submit");
            btSubmit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText etErr = null;
                    Boolean validInput = !etReq.getText().toString().trim().isEmpty();
                    String errMsg = "";
                    if (!validInput) {
                        errMsg = "Enter the Service Description";
                        etErr = etReq;
                    }
                    if (typeId.matches("C") && validInput) {
                        validInput = !etLoc.getText().toString().trim().isEmpty();
                        if (!validInput) {
                            errMsg = "Mention the Location";
                            etErr = etLoc;
                        } else {
                            validInput = etMob.getText().toString().trim().matches("[789]+[0-9]{9}");
                        }
                        if (!validInput) {
                            errMsg = "Enter valid Mobile No";
                            etErr = etMob;
                        } else {
                            validInput = etEmail.getText().toString().trim().matches("[A-Za-z]+[A-Za-z0-9._]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}") || etEmail.getText().toString().trim().isEmpty();
                            if (!validInput) {
                                errMsg = "Enter valid Email ID";
                                etErr = etEmail;
                            }
                        }
                    }
                    if (!validInput) {
                        Toast.makeText(CreateSR.this, errMsg, Toast.LENGTH_SHORT).show();
                        etErr.requestFocus();
                        etErr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                        });
                    } else {
                        JSONObject retJsonObj = new JSONObject();
                        try {
                            retJsonObj.put("session_id", sessionId);
                            retJsonObj.put("type_id", typeId);
                            retJsonObj.put("sr_no", srNo);
                            retJsonObj.put("cont_ngs", contNgs);
                            if (typeId.matches("C")) {
                                retJsonObj.put("cont_loc", etLoc.getText().toString());
                                retJsonObj.put("cont_mob", etMob.getText().toString());
                                retJsonObj.put("cont_email", etEmail.getText().toString());
                            } else {
                                retJsonObj.put("cont_loc", locVal);
                                retJsonObj.put("cont_mob", mobVal);
                                retJsonObj.put("cont_email", emailVal);
                            }
                            retJsonObj.put("sub_area", selSubId);
                            retJsonObj.put("sr_desc", etReq.getText().toString());
                            retJsonObj.put("sr_status", selStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String callUrl = "/SaveSR";
                        appParameters.callAPI(CreateSR.this, callUrl, retJsonObj, appParameters, ViewSR.class, false, CreateSR.class, true, false, 60000);
                    }
                }
            });
/*
            TextView tvSep = new TextView(this);
            tvSep.setPadding(5, 5, 5, 5);
            tvSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            tvSep.setText(" ");
*/
            Button btMenuData = new Button(this);
            btMenuData.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            //btMenuData.setBackgroundColor(Color.argb(100,255,200,100));
            btMenuData.setBackground(appParameters.getButtonDefaultShape());
            btMenuData.setText("GO BACK");
            btMenuData.setTextColor(Color.RED);
            btMenuData.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            btMenuData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent in = new Intent(CreateSR.this, MenuActivity.class);
                startActivity(in);
                }
            });
            rowTable.addView(btSubmit);
//            rowTable.addView(tvSep);
            rowTable.addView(btMenuData);
            tblCreate.addView(rowTable);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Json", e.getMessage());
            Toast.makeText(CreateSR.this, "Error in Server Response", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(CreateSR.this, MenuActivity.class);
            startActivity(newIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("General", e.getMessage());
            Toast.makeText(CreateSR.this, "Error in Server Response", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(CreateSR.this, MenuActivity.class);
            startActivity(newIntent);
        }
    }

    private void initTable(TableLayout tblCreate, String labelText, String initValue, int rowNo) {
        TableRow rowTable = new TableRow(this);
        if (rowNo % 3 == 0) rowTable.setBackgroundColor(Color.argb(100, 210, 255, 210));
        if (rowNo % 3 == 1) rowTable.setBackgroundColor(Color.argb(100, 255, 210, 210));
        if (rowNo % 3 == 2) rowTable.setBackgroundColor(Color.argb(100, 210, 210, 255));
        TableLayout.LayoutParams tblRowParam = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tblRowParam.setMargins(0,5,0,5);
        rowTable.setLayoutParams(tblRowParam);
        rowTable.setWeightSum(3);

        //TableRow.LayoutParams lpMyProfile = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        //lpMyProfile.setMargins(15, 15, 15, 15);
        //rowTable.setLayoutParams(lpMyProfile);

        TextView tvHeading = new TextView(this);
        tvHeading.setTextColor(Color.BLUE);
        tvHeading.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvHeading.setPadding(5, 5, 5, 5);
        tvHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        tvHeading.setText(labelText);
        rowTable.addView(tvHeading);
/*
        TextView tvSep = new TextView(this);
        tvSep.setTextColor(Color.BLACK);
        tvSep.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvSep.setPadding(0, 0, 0, 0);
        tvSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        tvSep.setText("::");
        rowTable.addView(tvSep);
*/
        TextView tvVal = new TextView(this);
        tvVal.setTextColor(Color.RED);
        tvVal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        tvVal.setPadding(5, 5, 5, 5);
        tvVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvVal.setText(initValue);
        rowTable.addView(tvVal);

        tblCreate.addView(rowTable);
    }


    private void initTable(TableLayout tblCreate, String labelText, EditText etVal, String initValue, int rowNo, int inputType, int noOfLines) {
        TableRow rowTable = new TableRow(this);
        if (rowNo % 3 == 0) rowTable.setBackgroundColor(Color.argb(100, 210, 255, 210));
        if (rowNo % 3 == 1) rowTable.setBackgroundColor(Color.argb(100, 255, 210, 210));
        if (rowNo % 3 == 2) rowTable.setBackgroundColor(Color.argb(100, 210, 210, 255));
        TableLayout.LayoutParams tblRowParam = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tblRowParam.setMargins(0,5,0,5);
        rowTable.setLayoutParams(tblRowParam);
        rowTable.setWeightSum(3);

        TextView tvHeading = new TextView(this);
        tvHeading.setTextColor(Color.BLUE);
        tvHeading.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvHeading.setPadding(5, 5, 5, 5);
        tvHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvHeading.setText(labelText);
        rowTable.addView(tvHeading);
/*
        TextView tvSep = new TextView(this);
        tvSep.setTextColor(Color.BLACK);
        tvSep.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvSep.setPadding(0, 0, 0, 0);
        tvSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        tvSep.setText("::");
        rowTable.addView(tvSep);
*/
        etVal.setTextColor(Color.RED);
        etVal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        etVal.setPadding(5, 5, 5, 5);
        etVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        etVal.setText(initValue);
        etVal.setInputType(inputType);
        etVal.setSingleLine(false);
        etVal.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        etVal.setLines(noOfLines);
        rowTable.addView(etVal);

        tblCreate.addView(rowTable);
    }

    private void initTable(TableLayout tblCreate, String labelText, Spinner spVal, List<String> paramLists, int rowNo, int spNo) {
        TableRow rowTable = new TableRow(this);
        if (rowNo % 3 == 0) rowTable.setBackgroundColor(Color.argb(100, 210, 255, 210));
        if (rowNo % 3 == 1) rowTable.setBackgroundColor(Color.argb(100, 255, 210, 210));
        if (rowNo % 3 == 2) rowTable.setBackgroundColor(Color.argb(100, 210, 210, 255));
        TableLayout.LayoutParams tblRowParam = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tblRowParam.setMargins(0,5,0,5);
        rowTable.setLayoutParams(tblRowParam);
        rowTable.setWeightSum(18);

        TextView tvHeading = new TextView(this);
        tvHeading.setTextColor(Color.BLUE);
        tvHeading.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 7f));
        tvHeading.setPadding(5, 5, 5, 5);
        tvHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvHeading.setText(labelText);
        rowTable.addView(tvHeading);
/*
        TextView tvSep = new TextView(this);
        tvSep.setTextColor(Color.BLACK);
        tvSep.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvSep.setPadding(0, 0, 0, 0);
        tvSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        tvSep.setText("::");
        rowTable.addView(tvSep);
*/
        spVal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 10f));
        spVal.setPadding(5, 5, 5, 5);
        spVal.setId(spNo);
        ArrayAdapter arrayAdapterParam = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paramLists);
        arrayAdapterParam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVal.setAdapter(arrayAdapterParam);
        rowTable.addView(spVal);

        tblCreate.addView(rowTable);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("C-Class", adapterView.getClass().getName());
        //((TextView) view).setTextColor(Color.BLUE);

        String param = null;
        int iLoop = 0;
        int length = 0;
        String selCatId = null;
        String selAreaId = null;
        ArrayAdapter arrayAdapterArea = null;
        ArrayAdapter arrayAdapterSub = null;

        switch (adapterView.getId()) {
            case 101:
                selCatId = listCatId.get(i);
                length = listArea.size();
                listAreaSel = new ArrayList<String>();
                listAreaIdSel = new ArrayList<String>();
                for (iLoop = 0; iLoop < length; iLoop++) {
                    param = listAreaId.get(iLoop);
                    if(selCatId.matches(param.substring(0,  param.indexOf(":")))){
                        listAreaIdSel.add(listAreaId.get(iLoop));
                        listAreaSel.add(listArea.get(iLoop));
                    }
                }

                length = listSub.size();
                selAreaId = listAreaIdSel.get(0);
                listSubSel = new ArrayList<String>();
                listSubIdSel = new ArrayList<String>();
                for (iLoop = 0; iLoop < length; iLoop++) {
                    param = listSubId.get(iLoop);
                    if(selAreaId.matches(param.substring(0,  param.indexOf("#")))){
                        listSubIdSel.add(listSubId.get(iLoop));
                        listSubSel.add(listSub.get(iLoop));
                    }
                }
                selSubId = listSubIdSel.get(0);

                arrayAdapterArea = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listAreaSel);
                arrayAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spArea.setAdapter(arrayAdapterArea);

                arrayAdapterSub = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSubSel);
                arrayAdapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSub.setAdapter(arrayAdapterSub);
                break;
            case 102:
                length = listSub.size();
                selAreaId = listAreaIdSel.get(i);
                listSubSel = new ArrayList<String>();
                listSubIdSel = new ArrayList<String>();
                for (iLoop = 0; iLoop < length; iLoop++) {
                    param = listSubId.get(iLoop);
                    if(selAreaId.matches(param.substring(0,  param.indexOf("#")))){
                        listSubIdSel.add(listSubId.get(iLoop));
                        listSubSel.add(listSub.get(iLoop));
                    }
                }
                selSubId = listSubIdSel.get(0);

                arrayAdapterSub = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSubSel);
                arrayAdapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSub.setAdapter(arrayAdapterSub);
                break;
            case 103:
                selSubId = listSubIdSel.get(i);
                break;
            case 104:
                selStatus = listStatId.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i("C-Class", adapterView.getChildAt(0).getClass().getName());
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
    }
}
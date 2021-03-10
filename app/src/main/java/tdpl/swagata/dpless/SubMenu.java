package tdpl.swagata.dpless;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SubMenu extends AppCompatActivity {
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
        setContentView(R.layout.activity_sub_menu);
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
        TextView tvMenu = (TextView) findViewById(R.id.tvMenu);
        TextView tvLogout = (TextView) findViewById(R.id.tvLogout);
        LinearLayout llCreateSR = (LinearLayout) findViewById(R.id.llCreateSR);
        if(appParameters.getModuleNo().matches("03"))
            llCreateSR.setVisibility(LinearLayout.GONE);
        else
            llCreateSR.setVisibility(LinearLayout.VISIBLE);
        LinearLayout llMonthRepo = (LinearLayout) findViewById(R.id.llMonthRepo);
        LinearLayout llYearRepo = (LinearLayout) findViewById(R.id.llYearRepo);
        LinearLayout llOtherRepo = (LinearLayout) findViewById(R.id.llOtherRepo);
        LinearLayout llSrvReq = (LinearLayout) findViewById(R.id.llSrvReq);
        msgDisplay = "Not you? Logout";
        SpannableString ssLogout = new SpannableString(msgDisplay);
        ClickableSpan csLogout = new ClickableSpan () {
            @Override
            public void onClick(View textView) {
                appParameters.doLogout(SubMenu.this);
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String menuOf = extras.getString("menu_of");
            switch (extras.getString("menu_of")) {
                case "M":
                    tvMenu.setText("Month wise Report Menu");
                    llMonthRepo.setVisibility(LinearLayout.VISIBLE);
                    llOtherRepo.setVisibility(LinearLayout.GONE);
                    llSrvReq.setVisibility(LinearLayout.GONE);
                    llYearRepo.setVisibility(LinearLayout.GONE);
                    break;
                case "Y":
                    tvMenu.setText("Year wise Report Menu");
                    llMonthRepo.setVisibility(LinearLayout.GONE);
                    llOtherRepo.setVisibility(LinearLayout.GONE);
                    llSrvReq.setVisibility(LinearLayout.GONE);
                    llYearRepo.setVisibility(LinearLayout.VISIBLE);
                    break;
                case "O":
                    tvMenu.setText("Other Report or Form Menu");
                    llMonthRepo.setVisibility(LinearLayout.GONE);
                    llOtherRepo.setVisibility(LinearLayout.VISIBLE);
                    llSrvReq.setVisibility(LinearLayout.GONE);
                    llYearRepo.setVisibility(LinearLayout.GONE);
                    break;
                case "S":
                    tvMenu.setText("Service Request Maintenance");
                    llMonthRepo.setVisibility(LinearLayout.GONE);
                    llOtherRepo.setVisibility(LinearLayout.GONE);
                    llSrvReq.setVisibility(LinearLayout.VISIBLE);
                    llYearRepo.setVisibility(LinearLayout.GONE);
                    break;
            }
        } else {
            Intent loginActivity = new Intent(this, MenuActivity.class);
            startActivity(loginActivity);
        }


        Button btITDecl = (Button) findViewById(R.id.btITDecl);
        btITDecl.setBackground(appParameters.getButtonDefaultShape());
        Button btITSupDecl = (Button) findViewById(R.id.btITSupDecl);
        btITSupDecl.setBackground(appParameters.getButtonDefaultShape());
        Button btRopaFix = (Button) findViewById(R.id.btRopaFix);
        btRopaFix.setBackground(appParameters.getButtonDefaultShape());
        Button btPaySlip = (Button) findViewById(R.id.btPaySlip);
        btPaySlip.setBackground(appParameters.getButtonDefaultShape());
        Button btITHist = (Button) findViewById(R.id.btITHist);
        btITHist.setBackground(appParameters.getButtonDefaultShape());
        Button btPayCert = (Button) findViewById(R.id.btPayCert);
        btPayCert.setBackground(appParameters.getButtonDefaultShape());
        Button btSalCert = (Button) findViewById(R.id.btSalCert);
        btSalCert.setBackground(appParameters.getButtonDefaultShape());
        Button btITDisp = (Button) findViewById(R.id.btITDisp);
        btITDisp.setBackground(appParameters.getButtonDefaultShape());
        Button btForm16 = (Button) findViewById(R.id.btForm16);
        btForm16.setBackground(appParameters.getButtonDefaultShape());
        Button btTaxSal = (Button) findViewById(R.id.btTaxSal);
        btTaxSal.setBackground(appParameters.getButtonDefaultShape());
        Button btPFLed = (Button) findViewById(R.id.btPFLed);
        btPFLed.setBackground(appParameters.getButtonDefaultShape());
        Button btCreateReq = (Button) findViewById(R.id.btCreateReq);
        btCreateReq.setBackground(appParameters.getButtonDefaultShape());
        Button btViewReq = (Button) findViewById(R.id.btViewReq);
        btViewReq.setBackground(appParameters.getButtonDefaultShape());
        Button btActReq = (Button) findViewById(R.id.btActReq);
        btActReq.setBackground(appParameters.getButtonDefaultShape());
        Button btMenuData = (Button) findViewById(R.id.btMenuData);
        btMenuData.setBackground(appParameters.getButtonDefaultShape());
        btCreateReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String callUrl = "/CreateSR";
                final String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\", \"type_id\":\"C\", \"sr_no\":\"0\"}";
                if(!appParameters.getModuleNo().matches("01")) {
                    AlertDialog.Builder adEmpNoInput = new AlertDialog.Builder(SubMenu.this);
                    adEmpNoInput.setTitle("Employee Details");
                    adEmpNoInput.setMessage("Employee No of the Contact Person");
                    final EditText etEmpNo = new EditText(SubMenu.this);
                    etEmpNo.setInputType(InputType.TYPE_CLASS_PHONE);
                    etEmpNo.setTextColor(Color.RED);
                    etEmpNo.setPadding(30,15,30,15);
                    etEmpNo.setGravity(Gravity.CENTER_HORIZONTAL);
                    etEmpNo.setTextSize(25);
                    etEmpNo.setHint("Enter valid Employee No");
                    etEmpNo.requestFocus();
                    etEmpNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    });
                    adEmpNoInput.setView(etEmpNo);
                    adEmpNoInput.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String empNo = etEmpNo.getText().toString().trim();
                            if (!empNo.isEmpty()) {
                                JSONObject inputJsonObject = null;
                                try {
                                    inputJsonObject = new JSONObject(strInput);
                                    inputJsonObject.put("emp_no", empNo);
                                    inputJsonObject.put("module", appParameters.getModuleNo());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                appParameters.callAPI(SubMenu.this, callUrl, inputJsonObject, appParameters, CreateSR.class, false, MenuActivity.class, true, true, 30000);
                            }
                        }
                    });

                    adEmpNoInput.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    adEmpNoInput.show();

                } else {
                    JSONObject inputJsonObject = null;
                    try {
                        inputJsonObject = new JSONObject(strInput);
                        inputJsonObject.put("emp_no", appParameters.getUserID());
                        inputJsonObject.put("module", appParameters.getModuleNo());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    appParameters.callAPI(SubMenu.this, callUrl, inputJsonObject, appParameters, CreateSR.class, false, MenuActivity.class, true, true, 30000);
                }
            }
        });

        btViewReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/ViewSR";
                String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\", \"type_id\":\"V\", \"sr_no\":\"0\"}";
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI (SubMenu.this, callUrl, inputJsonObject, appParameters, ViewSR.class, false, MenuActivity.class, true, true, 30000);
            }
        });

        btActReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/ViewSR";
                String strInput = "{\"session_id\": \""+ appParameters.getSessionID() + "\", \"type_id\":\"A\", \"sr_no\":\"0\"}";
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI (SubMenu.this, callUrl, inputJsonObject, appParameters, ViewSR.class, false, MenuActivity.class, true, true, 30000);
            }
        });

        btForm16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "FORM16");
                    reportParam.put("report_type", "Generate Form 16");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Financial Year (YYYY-YY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "form16_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Form 16");
                    reportParam.put("rpt_desc", "Downloading the Form 16 of " + appParameters.getUserName());
                    //reportParam.put("sql_query", "select distinct to_char(s.tax_year) || '-' || substr(to_char(s.tax_year + 1),3) as tax_fy from t_dcpy_form16 s where s.tax_year > 2016 and s.tax_year<2020 and s.ngs = "+ appParameters.getUserID() +" order by tax_fy desc");
                    reportParam.put("sql_query", "select distinct to_char(f.tax_year) || '-' || substr(to_char(f.tax_year + 1),3) as tax_fy  from t_dcpy_traces_blob a, t_dcpy_form16 f where a.pan = f.pan_number and a.asondate is not null and f.tax_year = a.finyr_from and f.ngs = "+ appParameters.getUserID() +" order by tax_fy desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 30000);
            }
        });

        btPFLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "PFLED");
                    reportParam.put("report_type", "Generate PF Ledger");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Financial Year (YYYY-YY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "pfled_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "PF Ledger");
                    reportParam.put("rpt_desc", "Downloading the PF Ledger of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select distinct to_char(s.finyear_from) || '-' || substr(to_char(s.finyear_from + 1),3) as tax_fy from t_dcpf_sequence s, vw_dcpf_emp_pfledger d where s.doc_type = 'PFLEDGER' and s.finyear_from = d.finyear_from and d.ngs = "+ appParameters.getUserID() +" order by tax_fy desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 30000);
            }
        });

        btSalCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "SALCERT");
                    reportParam.put("report_type", "Generate Salary Certificate");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Financial Year (YYYY-YY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "taxsal_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Salary Certificate");
                    reportParam.put("rpt_desc", "Downloading the Salary Certificate of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select distinct to_char(taxyear) || '-' || substr(to_char(taxyear + 1),3) as tax_fy from vw_dcpy_dpl_emp_lst where taxyear < (select max(taxyear) from vw_dcpy_dpl_emp_lst) and ngs = "+ appParameters.getUserID() +" order by tax_fy desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });

        btTaxSal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "TAXSAL");
                    reportParam.put("report_type", "Taxable Salary Breakup");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Financial Year (YYYY-YY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "taxsal_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Salary Breakup");
                    reportParam.put("rpt_desc", "Downloading the Taxable Salary Breakup Statement of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select distinct to_char(tax_year) || '-' || substr(to_char(tax_year + 1),3) as tax_fy from vw_dcpy_rpt_taxable_salary_dtl where ngs = "+ appParameters.getUserID() +" order by tax_fy desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });

        btITDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "TAXDISP");
                    reportParam.put("report_type", "Income Tax Computation");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Financial Year (YYYY-YY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "taxdisp_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Tax Computation");
                    reportParam.put("rpt_desc", "Downloading the Tax Computation Statement of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select to_char(tax_year) || '-' || substr(to_char(tax_year + 1),3) from vw_dcpy_rpt_itax_detail where COMPANY_MARKER='1' and NGS= "+ appParameters.getUserID() +" order by tax_year desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });
        btPayCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "PAYCERT");
                    reportParam.put("report_type", "Generate Pay Certificate");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Salary Month (MM-YYYY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "payc_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Pay Certificate");
                    reportParam.put("rpt_desc", "Downloading the Pay Certificate of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select SAL_MONTH || '-' || SAL_YEAR from vw_dcpy_dpl_emp_lst where COMPANY_MARKER='1' and NGS= "+ appParameters.getUserID() +"  order by sal_year*100 + sal_month desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });

        btITHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "TAXHIST");
                    reportParam.put("report_type", "IT Computation History");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Salary Month (MM-YYYY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "ithis_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "IT Computation");
                    reportParam.put("rpt_desc", "Downloading the IT Computation History of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select SAL_MONTH || '-' || SAL_YEAR from vw_dcpy_rpt_itax_detail_hist where COMPANY_MARKER='1' and NGS= "+ appParameters.getUserID() +"  order by sal_year*100 + sal_month desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });

        btPaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Lists";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "PAYSLIP");
                    reportParam.put("report_type", "Generation of Payslip");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "Select Salary Month (MM-YYYY)");
                    reportParam.put("param_val", "0");
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "payslip_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Payslip");
                    reportParam.put("rpt_desc", "Downloading the Payslip of " + appParameters.getUserName());
                    reportParam.put("sql_query", "select t.SAL_MONTH || '-' || t.SAL_YEAR from t_dcpy_emp_pay_dtls t where t.COMPANY_MARKER='1' and t.payroll_done_on is not null and t.NGS= "+ appParameters.getUserID() +" order by t.sal_year*100+t.sal_month desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, ReportGen.class, false, MenuActivity.class, true, true, 15000);
            }
        });

        btRopaFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Report";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "ROPA");
                    reportParam.put("report_type", "ROPA 2020 Fixation");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "User ID");
                    reportParam.put("param_val", appParameters.getUserID());
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "ropa_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "ROPA 2020");
                    reportParam.put("rpt_desc", "Downloading the ROPA 2020 Fixation of " + appParameters.getUserName());
                    reportParam.put("sql_query", " ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, MenuActivity.class, true, MenuActivity.class, true, true, 60000);
            }
        });

        btITSupDecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Report";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "ITDECS");
                    reportParam.put("report_type", "IT Supplimentary Declaration");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "User ID");
                    reportParam.put("param_val", appParameters.getUserID());
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "itsup_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "Supplimentary Form");
                    reportParam.put("rpt_desc", "Downloading the IT Supplimentary Declaration Form of " + appParameters.getUserName());
                    reportParam.put("sql_query", " ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, MenuActivity.class, true, MenuActivity.class, true, true, 30000);
            }
        });

        btITDecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callUrl = "/Report";
                JSONObject reportParam =  new JSONObject();
                try {
                    reportParam.put("report_name", "ITDEC");
                    reportParam.put("report_type", "IT Declaration Form");
                    reportParam.put("user_id", appParameters.getUserID());
                    reportParam.put("param_type", "User ID");
                    reportParam.put("param_val", appParameters.getUserID());
                    reportParam.put("session_id", appParameters.getSessionID());
                    reportParam.put("output_pdf", "itdec_"+appParameters.getUserID()+".pdf");
                    reportParam.put("rpt_title", "IT Form");
                    reportParam.put("rpt_desc", "Downloading the IT Declaration Form of " + appParameters.getUserName());
                    reportParam.put("sql_query", " ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubMenu.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SubMenu.this, MenuActivity.class);
                    startActivity(in);
                }
                appParameters.setReportParam(reportParam);
                appParameters.callAPI (SubMenu.this, callUrl, reportParam, appParameters, MenuActivity.class, true, MenuActivity.class, true, true, 30000);
            }
        });

        btMenuData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SubMenu.this, MenuActivity.class);
                startActivity(in);
            }
        });
    }
}
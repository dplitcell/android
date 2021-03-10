package tdpl.swagata.dpless;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import pl.droidsonroids.gif.GifTextView;

public class AppParameters extends Application {
    private String versionID;
    private String sessionID;
    private String userID;
    private String userName;
    private String urlBase;
    private String appName;
    private String urlWsInit;
    private String moduleNo;
    private String sharedMem;
    private String urlBaseVersion;
    private JSONObject callResponse;
    private JSONObject reportParam;
    private Long lastRequestSendOn;
    public static final String CHANNEL_SMS = "Request Status";

     @Override
    public void onCreate() {
        super.onCreate();

        this.setSharedMem("appVal");

        //This is a Global Parameter and it should be modified on change of version and URL: Start
        //this.setVersionURL("1.0", "http://192.168.0.10", "smsApp", "ws/webSrv");
        //this.setVersionURL("7.0", "http://172.16.1.142:8080", "smsApp", "ws/webSrv");
        this.setVersionURL("8.0", "http://thedpl.in","smsApp", "ws/webSrv");
        //This is a Global Parameter and it should be modified on change of version and URL: End

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelSMS = new NotificationChannel(CHANNEL_SMS,"Request Status",NotificationManager.IMPORTANCE_HIGH);
            channelSMS.setDescription("Service Request Status");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelSMS);
        }
    }

    public GradientDrawable getButtonDefaultShape() {
        GradientDrawable buttonDefaultShape = new GradientDrawable();
        buttonDefaultShape.setCornerRadius(40);
        buttonDefaultShape.setColor(Color.argb(100,100,200,100));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            buttonDefaultShape.setPadding(15,5,15,5);
        }
        return buttonDefaultShape;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getSharedMem() {
        return sharedMem;
    }

    public void setSharedMem(String sharedMem) {
        this.sharedMem = sharedMem;
    }

    public Long getLastRequestSendOn() {
        return lastRequestSendOn;
    }

    public void setLastRequestSendOn(Long lastRequestSendOn, ViewGroup layout, GifTextView gifTextView, LinearLayout llProgBar) {
        this.lastRequestSendOn = lastRequestSendOn;
        if (layout != null) {
            layout.removeViewInLayout(gifTextView);
            layout.removeViewInLayout(llProgBar);
        }
    }

    public JSONObject getReportParam() {
        return reportParam;
    }

    public void setReportParam(JSONObject reportParam) {
        this.reportParam = reportParam;
    }

    public JSONObject getCallResponse() {
        return callResponse;
    }

    public void setCallResponse(JSONObject callResponse) {
        this.callResponse = callResponse;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public String getUrlBaseVersion() {
        return urlBaseVersion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionURL(String versionID, String urlBase, String appName, String urlWsInit) {
        this.versionID = versionID;
        this.urlBase = urlBase;
        this.urlWsInit = urlWsInit;
        this.appName = appName;
        this.urlBaseVersion = urlBase + "/" + appName + "/" + urlWsInit + "/" + versionID;
    }

    public void setInitials() {
        this.setModuleNo("01"); //Default module for Employee ESS
        this.setSessionID("0");
        this.setUserID("0");
        this.setUserName(" ");
        this.setLastRequestSendOn((long) 0, null, null, null);
    }

    public void doLogout(final Context context) {
        final AppParameters appParameters = this;
        AlertDialog.Builder adLogout = new AlertDialog.Builder(context);
        adLogout.setTitle("Logout");
        adLogout.setMessage("Are you sure to logout?");
        adLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SharedPreferences appSharedVal = getSharedPreferences(appParameters.getSharedMem(), MODE_PRIVATE);
                SharedPreferences.Editor appSharedEdit = appSharedVal.edit();
                appSharedEdit.remove("key_val");
                appSharedEdit.apply();
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent alarmReceiver = new Intent(appParameters, AlarmReceiver.class);
                boolean isAlarmExist = (PendingIntent.getBroadcast(appParameters, 1, alarmReceiver, PendingIntent.FLAG_NO_CREATE) != null);
                if (isAlarmExist) {
                    Log.i("AD", "De-Register");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(appParameters, 1, alarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
                String callUrl = "/Logout";
                String strInput = "{\"session_id\": \"" + appParameters.getSessionID() + "\"}";
                appParameters.setSessionID("0");
                JSONObject inputJsonObject = null;
                try {
                    inputJsonObject = new JSONObject(strInput);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appParameters.callAPI(context, callUrl, inputJsonObject, appParameters, Login.class, false, Login.class, true, true, 5000);
            }
        });
        adLogout.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        adLogout.show();
    }

    public void downloadPDF(String url) {
        try {
            String title = this.reportParam.getString("rpt_title");
            String desc = this.reportParam.getString("rpt_desc");

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Log.d("Download Url", this.urlBase + url);
            Uri downloadUri = Uri.parse(this.urlBase + url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            //Restrict the types of networks over which this download may proceed.
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            //Set whether this download may proceed over a roaming connection.
            request.setAllowedOverRoaming(true);
            //Set the title of this download, to be displayed in notifications (if enabled).
            request.setTitle(title);
            //Set a description of this download, to be displayed in notifications (if enabled)
            request.setDescription(desc);

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //Enqueue a new download and same the downloadId
            Log.d("Downloaded ", "Started");
            long downloadId = downloadManager.enqueue(request);

            Log.d("Downloaded ID", Long.toString(downloadId));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Download", reportParam.toString());
        }
    }


    public void callAPI(final Context context, String callUrl, JSONObject inputJsonObject, final AppParameters appParameters, final Class onSuccessClass, final boolean isDownloadActivity, final Class onFailureClass, final boolean onSuccessNavigate, final boolean onFailureNavigate, final int responseTimeOut) {
        if (appParameters.getLastRequestSendOn() == 0) {
            ViewGroup layout = null;
            LinearLayout llProgBar = null;
            GifTextView gifTextView = null;
            try {
                layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (layout != null) {
                llProgBar = new LinearLayout(context);
                gifTextView = new GifTextView(context);
                gifTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                gifTextView.setBackgroundResource(R.drawable.tdpl);
                llProgBar.setOrientation(LinearLayout.VERTICAL);
                llProgBar.setGravity(Gravity.CENTER);
                llProgBar.addView(gifTextView);
                layout.addView(llProgBar);
            }
            Log.i("API", "Call program Started");
            appParameters.setLastRequestSendOn(new Date().getTime(), null, null, null);
            boolean callFromNotify = Boolean.FALSE;
            boolean callForLogin = Boolean.FALSE;
            boolean toDownload = isDownloadActivity;
            try {
                inputJsonObject.put("request_time", appParameters.getLastRequestSendOn().toString());
                if (inputJsonObject.has("session_id"))
                    callFromNotify = inputJsonObject.getString("session_id").matches("1");
                else
                    callForLogin = Boolean.TRUE;
                if (inputJsonObject.has("to_download"))
                    toDownload = inputJsonObject.getString("to_download").matches("1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = this.getUrlBaseVersion() + callUrl;
            final ViewGroup finalLayout = layout;
            final GifTextView finalGifTextView = gifTextView;
            final LinearLayout finalLlProgBar = llProgBar;
            final boolean isNotify = callFromNotify;
            final boolean isLogin = callForLogin;
            final boolean isDownloadble = toDownload;
            JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.PUT, url, inputJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        requestQueue.stop();
                        try {
                            String msg = response.getString("msg");
                            String isError = response.getString("is_error");
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            if (isError.matches("N")) {
                                if (isNotify) {
                                    appParameters.setModuleNo(response.getString("module"));
                                    appParameters.setUserName(response.getString("name"));
                                    appParameters.setSessionID(response.getString("session_id"));
                                    appParameters.setUserID(response.getString("user_id"));
                                }
                                if (isLogin) {
                                    appParameters.setModuleNo(response.getString("module"));
                                    appParameters.setUserName(response.getString("name"));
                                    appParameters.setSessionID(response.getString("session_id"));
                                    appParameters.setUserID(response.getString("user_id"));
                                    String keyVal = response.getString("key_val");
                                    boolean toRemember = !keyVal.matches("0");
                                    if (toRemember){
                                        SharedPreferences appSharedVal = getSharedPreferences(appParameters.getSharedMem(), MODE_PRIVATE);
                                        SharedPreferences.Editor appSharedEdit = appSharedVal.edit();
                                        appSharedEdit.putString("key_val", keyVal);
                                        appSharedEdit.apply();
                                    }
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    Intent alarmReceiver = new Intent(context, AlarmReceiver.class);
                                    boolean isAlarmExist = (PendingIntent.getBroadcast(context, 1, alarmReceiver, PendingIntent.FLAG_NO_CREATE) != null);
                                    if (toRemember && !isAlarmExist) {
                                        Log.i("AD", "Register");
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        calendar.add(Calendar.MINUTE, 2);
                                        //Start- First Stopped Repeat then Set Exact
                                        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 3, pendingIntent);
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        } else if (Build.VERSION.SDK_INT >= 19) {
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        } else {
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        }
                                        //End
                                    }
                                    if (!toRemember && isAlarmExist) {
                                        Log.i("AD", "De-Register");
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.cancel(pendingIntent);
                                        pendingIntent.cancel();
                                    }
                                }
                                if (isDownloadble) {
                                    String repoUrl = response.getString("repo_url");
                                    if (isNotify)
                                        appParameters.setReportParam(response);
                                    appParameters.downloadPDF(repoUrl);
                                    Log.d("Download", "Completed");
                                    Toast.makeText(context, "Downloading....", Toast.LENGTH_SHORT).show();
                                }
                                appParameters.setCallResponse(response);
                                if (onSuccessNavigate) {
                                    Intent newIntent = new Intent(context, onSuccessClass);
                                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    ((Activity)context).finish();
                                }
                            }
                            appParameters.setLastRequestSendOn((long) 0, finalLayout, finalGifTextView, finalLlProgBar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error in Server Response", Toast.LENGTH_SHORT).show();
                            if (onFailureNavigate) {
                                Intent newIntent = new Intent(context, onFailureClass);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                ((Activity)context).finish();
                            }
                            appParameters.setLastRequestSendOn((long) 0, finalLayout, finalGifTextView, finalLlProgBar);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response Error:", error.toString());
                        requestQueue.stop();
                        Toast.makeText(context, "Error in Server Response", Toast.LENGTH_SHORT).show();
                        if (onFailureNavigate) {
                            Intent newIntent = new Intent(context, onFailureClass);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newIntent);
                            ((Activity)context).finish();
                        }
                        appParameters.setLastRequestSendOn((long) 0, finalLayout, finalGifTextView, finalLlProgBar);
                    }
                });
            RetryPolicy retryPolicy = new DefaultRetryPolicy(responseTimeOut, 0, 1);
            jsonObjectRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(jsonObjectRequest);
        }
    }
}

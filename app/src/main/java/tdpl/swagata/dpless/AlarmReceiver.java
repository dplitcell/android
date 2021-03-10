package tdpl.swagata.dpless;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final AppParameters appParameters = (AppParameters) context.getApplicationContext();
        SharedPreferences appSharedVal = context.getSharedPreferences(appParameters.getSharedMem(), Context.MODE_PRIVATE);
        String keyVal = appSharedVal.getString("key_val", "0");
        if (!keyVal.matches("0")) {
            JSONObject inputJsonObject = new JSONObject();
            try {
                inputJsonObject.put("key_val", keyVal);
                inputJsonObject.put("request_time", ((Long) new Date().getTime()).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Start
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent alarmReceiver = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, 3);
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            //End
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            final Context finalContext = context;
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.PUT, appParameters.getUrlBaseVersion() + "/Notify", inputJsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            requestQueue.stop();
                            String isError = "Y";
                            try {
                                isError = response.getString("is_error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                isError = "Y";
                            }
                            if (isError.matches("N")) {
                                JSONArray jsonParamArray;
                                String jsonParam = null;
                                String idNotify = "1";
                                String showTitle = "";
                                String showText = "";
                                JSONObject jsonParamObj = null;
                                try {
                                    jsonParamArray = response.getJSONArray("param_array");
                                    for (int iLoop = 0; iLoop < jsonParamArray.length(); iLoop++) {
                                        jsonParamObj = jsonParamArray.getJSONObject(iLoop);
                                        idNotify = jsonParamObj.getString("notify_id");
                                        showTitle = jsonParamObj.getString("title");
                                        showText = jsonParamObj.getString("text");
                                        jsonParam = jsonParamObj.toString();
                                        NotificationManagerCompat notificationManager;
                                        notificationManager = NotificationManagerCompat.from(finalContext);
                                        Intent activityIntent = new Intent(finalContext, Welcome.class);
                                        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        activityIntent.putExtra("json_param", jsonParam);
                                        PendingIntent contentIntent = PendingIntent.getActivity(finalContext, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        Notification notification = new NotificationCompat.Builder(finalContext, AppParameters.CHANNEL_SMS)
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle(showTitle)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(showText))
                                                .setContentText(showText)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setColor(Color.argb(100,100,100,240))
                                                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                                .setLights(Color.BLUE, 3000, 3000)
                                                .setContentIntent(contentIntent)
                                                .setAutoCancel(true)
                                                .build();
/*
                                        Notification notification = new NotificationCompat.Builder(finalContext, AppParameters.CHANNEL_SMS)
                                           //     .setContentTitle(showTitle)
                                             //   .setContentText(showText)
                                                .setSmallIcon(R.drawable.logo)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setColor(Color.RED)
                                                .setVibrate(new long[] { 1000, 1000, 1000 })
                                                .setLights(Color.BLUE, 3000, 3000)
                                                .setContentIntent(contentIntent)
                                                .setAutoCancel(true)
                                                .setStyle(new NotificationCompat.InboxStyle()
                                                        .addLine(showTitle)
                                                        .addLine("  ")
                                                        .setBigContentTitle(showTitle)
                                                        .setSummaryText("Click here for more Details..."))
                                                .build();

                                         */
                                        notificationManager.notify(Integer.parseInt(idNotify), notification);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            requestQueue.stop();
                        }
                    });
            RetryPolicy retryPolicy = new DefaultRetryPolicy(60000, 0, 1);
            jsonObjectRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(jsonObjectRequest);
        }
    }
}

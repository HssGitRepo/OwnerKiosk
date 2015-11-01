package com.ownerkiosk.sample.ownerkiosk;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class GcmMessageHandler extends IntentService {
    static final String MSG_KEY = "m";
    String mes;
    TextToSpeech tts;
    public static final int notifyID = 9001;
    public GcmMessageHandler() {
        super("GcmIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        Bundle extras = intent.getExtras();
        String messageType = gcm.getMessageType(intent);
        mes = intent.getExtras().getString("message");

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                sendNotification("Message Received from Google GCM Server:\n\n"
                        + extras.get(MSG_KEY));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        resultIntent.putExtra("msg", mes);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
//                resultIntent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder mNotifyBuilder;
//        NotificationManager mNotificationManager;
//
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        mNotifyBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle("Dr.Owl")
//                .setContentText("New Order")
//                .setSound(alarmSound)
//                .setSmallIcon(R.drawable.add);
//
//        mNotifyBuilder.setContentIntent(resultPendingIntent);
//        mNotifyBuilder.setContentText("New Order available in your place!");
//        mNotifyBuilder.setAutoCancel(true);
//        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        Log.e("GCM MESSAGE", "" + mes);

        try {


             tts= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            int result=  tts.setLanguage(Locale.getDefault());
//                    tts.setPitch(1.0f);int result = tts.setLanguage(Locale.US);

                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Toast.makeText(getApplicationContext(),"Voice not supported",Toast.LENGTH_LONG).show();
                            }
                            else {
                                tts.speak("Threat Alert. ", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }

                    }
                });




        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"not supported",Toast.LENGTH_LONG).show();

        }

    }
}
package com.jyotishapp.jyotishi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotificationForCall {

    public SendNotificationForCall(String message,  String heading, String notificationKey){

        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" + message + "'}," +
                    "'include_player_ids':['" + notificationKey + "']," +
                    "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

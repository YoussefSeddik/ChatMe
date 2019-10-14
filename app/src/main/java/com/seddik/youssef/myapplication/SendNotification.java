package com.seddik.youssef.myapplication;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey) {
        try {
            JSONObject content = new JSONObject("{'contents':{'en':'" + message + "'}," +
                    "'include_player_ids':['" + notificationKey + "']," +
                    "'headings':{'en' :'" + heading + "'}}");
            OneSignal.postNotification(content, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.jyotishapp.jyotishi;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;
import android.util.Log;

public class NotificationClass extends Application {

    FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("AAA", "RUNNNING");

        mAuth = FirebaseAuth.getInstance();
        Log.v("AAA", "RUNNNING1");
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationOpener(NotificationClass.this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Log.v("AAA", "RUNNNING2");
        OneSignal.setSubscription(true);
        Log.v("AAA", "RUNNNING3");
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
//                        .child("NotificationKey").setValue(userId);
//            }
//        });
        Log.v("AAA", "RUNNNING4");
    }
}

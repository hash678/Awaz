package com.abbasi.awaz;

import android.util.Log;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Anjum on 7/22/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

        private static final String TAG = "MyFirebaseIIDService";

        @Override
        public void onTokenRefresh() {

            //Getting registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            //Displaying token on logcat
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("IDS");
            refroot.setValue(refreshedToken);

        }

        private void sendRegistrationToServer(String token) {
            //You can implement this method to store the token on your server
            //Not required for current project
        }
}

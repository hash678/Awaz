package com.abbasi.awaz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import viewpager.Tabs;
import auth.phonelogin;


public class MMainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener {
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mFirebaseUser;
  private String mUsername;
 // private String photourl;
  ImageView logo;
  ProgressDialog progress;
  String username;
    String photourl;
    SharedPreferences prefs;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
logo = (ImageView)findViewById(R.id.logo);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
Log.i("TESTER", prefs.getString("username","sam"));
   //   Log.i("Tester",user.getUid().toString());
    if (user != null||   prefs.getString("username","sam")== "null" ){
      getUsername(MMainActivity.this);
Log.i("Tester","gothere");
 // logo.animate().rotationY(360000000).rotation(360000000).setDuration(2000000);

  // User is signed in
  } else {
      startActivity(new Intent(this, phonelogin.class));
      finish();
    }
  }

    @Override
    protected void onPause() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            getUsername(MMainActivity.this);

           // logo.animate().rotation(360000000).setDuration(2000000);

            logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));

            // User is signed in
        } else {
            startActivity(new Intent(this,phonelogin.class));
            finish();
        }
        super.onPause();
    }

    @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sign_out_menu:
        mFirebaseAuth.signOut();

       // Auth.GoogleSignInApi.signOut(mGoogleApiClient);
       // mUsername = ANONYMOUS;
        //startActivity(new Intent(this, SignInActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }

  }public String getUsername(final Context context){

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    Log.i("log001",dataSnapshot.getKey());
username= dataSnapshot.getKey();
                    Intent i = new Intent(MMainActivity.this, Tabs.class);
                    logo.setVisibility(View.INVISIBLE);

                    i.putExtra("Gotusername",username);

                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return username;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Awaz isnt able to connect to the server :(",Toast.LENGTH_SHORT).show();
    }
}

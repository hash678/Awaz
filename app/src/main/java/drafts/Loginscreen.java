
/**
package com.abbasi.awaz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;


 * Created by Anjum on 7/14/2016.

public class Loginscreen extends Activity implements View.OnKeyListener {
    //username
    EditText username;
    //password
    EditText password;
    //Register Button
    TextView changesignuup;
    //Loading dialog
    ProgressDialog progress;
    private FirebaseAuth mAuth;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        Firebase.setAndroidContext(this);
       checkPlayServices();
        username= (EditText)findViewById(R.id.usernameF);
        password=(EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        TextView myTextView=(TextView)findViewById(R.id.title);
        //Set font
      //  Typeface typeFace= Typeface.createFromAsset(getAssets(),"fonts/mn.ttf");
        //myTextView.setTypeface(typeFace);

        ImageView imageView1 = (ImageView)findViewById(R.id.logologin);
        imageView1.animate().rotation(3600).rotationY(360).setDuration(2000);
//If already logged in, redirect
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                startActivity(new Intent(Loginscreen.this,MMainActivity.class));
                 finish();

                    //Firebase ref = new Firebase("https://awaz-56a71.firebaseio.com/");
                   // ref.child("users").setValue(mAuth.getCurrentUser().getEmail());
//                    Log.i("log",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out

                    Log.d("TAG", "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };


        changesignuup=(TextView)findViewById(R.id.chnageSignupMode);
        username.setOnKeyListener(this);
        password.setOnKeyListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void clicked(View view){
       //Hide keyboard on clicking background
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
   //Standard checking playservices version to ensure compatibility
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),"Device not supported :(",Toast.LENGTH_SHORT).show();
                Log.i("Log", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
       //On pressing enter signup
        if(i== KeyEvent.KEYCODE_ENTER&& keyEvent.getAction()== KeyEvent.ACTION_DOWN){
signIn(view);
        }
        return false;
    }
public void changeSignUpMode(View view){
Intent ia = new Intent(this,Register.class);
    startActivity(ia);

}

//Signing in process
    public void signIn(View view){
        Register register = new Register();
        if(!register.isEmailValid(username.getText().toString())){
            Toast.makeText(getApplicationContext(),"Email invalid",Toast.LENGTH_SHORT).show();


        }else {
            //Shows dialogue to user while processing
            progress = ProgressDialog.show(this, "Please wait",
                    "Logging in", true);
            //Firebase signing in
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progress.dismiss();
                            Log.d("Log", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progress.dismiss();
                                Log.w("Log", "signInWithEmail", task.getException());
                                String problem =task.getException().toString().substring(task.getException().toString().indexOf(" "));


                                Toast.makeText(Loginscreen.this,problem,
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
            
        }
    }

public void reset(View view){
    Intent reset = new Intent(Loginscreen.this,resetpass.class);
    startActivity(reset);
    finish();



}
    public void deleteCache() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    }
 */


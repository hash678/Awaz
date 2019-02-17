
/**
package com.abbasi.awaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by anjum on 8/29/2016.


public class resetpass  extends AppCompatActivity{
EditText emailad;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       setContentView(R.layout.resetpass);
        emailad = (EditText)findViewById(R.id.resetemail);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(resetpass.this,MMainActivity.class));
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

    }

    public void onResetBtn(View view){
        if (emailad!=null||emailad.toString().contains("@")){
Log.i("reset","success");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(emailad.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                Log.d("reset", "Email sent.");
                            }else{
                                Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                                Log.d("reset", "Request failed.");


                            }
                        }
                    });
            Firebase ref = new Firebase("https://awaz-56a71.firebaseio.com/");
            ref.resetPassword(emailad.getText().toString(), new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Log.i("reset","success1");
                    Toast.makeText(getApplicationContext(),"Reset email sent",Toast.LENGTH_SHORT).show();
Intent i = new Intent(resetpass.this,phonelogin.class);
                    startActivity(i);
                    finish();
                    // password reset email sent
                }
                @Override
                public void onError(FirebaseError firebaseError) {

                    Log.i("reset","fail");
                    Log.i("reset",firebaseError.toString());
                    Toast.makeText(getApplicationContext(),firebaseError.toString(),Toast.LENGTH_SHORT).show();
                    // error encountered
                }
            });
        }else{

            Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();

        }



    }

}
*/
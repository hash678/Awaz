package drafts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Class Register is the Activity class that shows user registration screen
 * that allows user to register itself on Parse server for this Chat app.

public class Register extends Activity
{


	private EditText user;

	private EditText pwd;

	private EditText email;
private  EditText name;

	private Boolean created=false;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Firebase.setAndroidContext(this);
		TextView myTextView=(TextView)findViewById(R.id.findbuddiestext);

mAuth = FirebaseAuth.getInstance();


	//	user = (EditText) findViewById(R.id.user);
		pwd = (EditText) findViewById(R.id.pwd);
		email = (EditText) findViewById(R.id.email);
	//name = (EditText)findViewById(R.id.name);

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					if(created){
						user.sendEmailVerification();
					}
					Intent intent = new Intent(Register.this,myProfile.class);
					intent.putExtra("edit-profile",false);
					startActivity(intent);
					finish();
					Log.d("Log007", "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					// User is signed out
					Log.d("Log008", "onAuthStateChanged:signed_out");
				}
				// ...
			}
		};

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


	public void onClick(View v) {


	//	final String u = user.getText().toString();
		String p = pwd.getText().toString();
		String e = email.getText().toString();
	//	final String n = name.getText().toString();
		if (p.length() == 0 || e.length() == 0) {
			Toast.makeText(getApplicationContext(),"You've left one field blank",Toast.LENGTH_SHORT).show();

		} else if (!isEmailValid(e)) {
			Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
		} else {


			final ProgressDialog dia = ProgressDialog.show(this, null,
					"Signing up");
			mAuth.createUserWithEmailAndPassword(e, p)
					.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							dia.dismiss();
							if(task.isSuccessful()) {
								created=true;
								Intent intent = new Intent(Register.this,myProfile.class);
								intent.putExtra("edit-profile",false);
								startActivity(intent);
								finish();
								Log.i("Log 001", "createUserWithEmail:onComplete:" + task.isSuccessful());
							}
							// If sign in fails, display a message to the user. If sign in succeeds
							// the auth state listener will be notified and logic to handle the
							// signed in user can be handled in the listener.
							if (!task.isSuccessful()) {
String problem =task.getException().toString().substring(task.getException().toString().indexOf(" "));


							Toast.makeText(Register.this,problem,
									Toast.LENGTH_SHORT).show();

						}

						}
					});


			return;
		}



		/*final ParseUser pu = new ParseUser();
		pu.setEmail(e);
		pu.setPassword(p);
		pu.setUsername(u);
		pu.add("Fname",n);
		pu.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e)
			{

				if (e == null)
				{
					UserList.user = pu;
					startActivity(new Intent(Register.this, UserList.class));
					setResult(RESULT_OK);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Error signing up",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

	}
	public boolean isEmailValid(String email)
	{
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if(matcher.matches())
			return true;
		else
			return false;
	}
}
*/
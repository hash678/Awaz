package drafts;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.abbasi.awaz.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.Manifest;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import auth.phonelogin;


public class  MainActivity extends AppCompatActivity {
    String buddy;
    FirebaseAuth mAuth;
    long progress;
    long total;
    int check=0;
    ProgressBar progressbar;

    String songname;
    int selectfile=0;

    MediaPlayer mPlayer;
    File file;
    String location;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        intiliza();
        buddy = getIntent().getStringExtra("requestbuddy");

       // Bundle bundle = getIntent().getExtras();
       // String buddy = bundle.getString("message");

       // Log.i("Log601",buddy);
    }
    public void intiliza(){
        verifyStoragePermissions(this);
        //logoAnime();
        //buddy = getIntent().getStringExtra("ET");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Tag", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tag", "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Tag", "signInAnonymously", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("log","Failed");
                        }

                        // ...
                    }
                });

        // ...
    }
    public void check(String notname, String notm, Context context,Class classs ){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notf);
        mBuilder.setContentTitle(notname);
        mBuilder.setContentText(notm);

        Intent resultIntent = new Intent(context,classs);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(classs);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);



        NotificationManager mNotificationManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
    public void clickMe(){
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference();
       ref.child("chat").child(buddy).child(ParseUser.getCurrentUser().getUsername()).child("senderUsername").setValue(ParseUser.getCurrentUser().getUsername());
        ref.child("chat").child(buddy).child(ParseUser.getCurrentUser().getUsername()).child("recUsername").setValue(buddy);
        ref.child("chat").child(buddy).child(ParseUser.getCurrentUser().getUsername()).child("songName").setValue(songname);
   ref.child("checking").setValue("Doesworks");
    }
   public void uploadFile(String string){


       FirebaseStorage storage = FirebaseStorage.getInstance();
       StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");
       Uri file = Uri.fromFile(new File(string));
       StorageReference riversRef = storageRef.child(ParseUser.getCurrentUser().getUsername()+"/"+file.getLastPathSegment());

       UploadTask  uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception exception) {
              Log.i("log","failsss");
               Toast.makeText(getApplicationContext(),"Upload failed. Check you connection.",Toast.LENGTH_LONG).show();
           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
               clickMe();
              Toast.makeText(getApplicationContext(),"Song sent.",Toast.LENGTH_SHORT).show();
               progressbar.setProgress(0);
check("Awaz","UPLOAD COMPLETE",MainActivity.this,MainActivity.class);

               Log.i("log","Success");

               Uri downloadUrl = taskSnapshot.getDownloadUrl();
           }
       });
uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
total = taskSnapshot.getTotalByteCount();
        progress = taskSnapshot.getBytesTransferred();
        Integer i = (int) (long) total;
        Integer j = (int) (long) progress;
       progressbar = (ProgressBar)findViewById(R.id.progressBar);
        progressbar.setMax(i);
        progressbar.setProgress(j);


    }
});}
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
    public void sButt(View view) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);


    }
    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(this, phonelogin.class);
        startActivity(login);

finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == RESULT_OK && data != null); {
            Uri selectedAudio = data.getData();
            selectfile = 1;
            mPlayer = MediaPlayer.create(this, selectedAudio);
            location = getRealPathFromURI(this, selectedAudio);

            file = new File(location);
            Uri file1 = Uri.fromFile(new File(location));
            songname = file1.getLastPathSegment();

        }

        }
    public void parseUpload(){

        ParseObject image = new ParseObject("Image");
    }

    public void playButton(View view){
if(check==0) {
    mPlayer.start();
    check=1;
}else{

    mPlayer.pause();
    check=0;

}
}
    public void uploadbu(View view){

        if(location!=null){

            uploadFile(location);
            Toast.makeText(getApplicationContext(), "Upload has started", Toast.LENGTH_SHORT).show();
            ParseObject po = new ParseObject("song");



            po.put("senderUsername", ParseUser.getCurrentUser().getUsername());
            po.put("recUsername",buddy);
            po.put("songName",songname);
            po.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){Log.i("log","success");}else{
                        Log.i("log","Fail");

                    }
                }
            });


        }else{
            Log.i("Log101","No song selected");
        }

     /*   if(selectfile==1) {
    uploadFile(location);
    Log.i("log", location);
    Toast.makeText(getApplicationContext(), "Upload has started", Toast.LENGTH_SHORT).show();
}else{
    Toast.makeText(getApplicationContext(),"No song has been selected",Toast.LENGTH_SHORT).show();

}*/



    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
            return true;
       // }

      //  return super.onOptionsItemSelected(item);
    }

    public void clickMe(View view){
       // Intent chat = new Intent(this, chat.chat.class);
        //chat.putExtra("message", buddy);
        //startActivity(chat);

       // finish();

    }

}

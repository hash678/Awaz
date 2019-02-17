package drafts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;

import viewpager.Tabs;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Anjum on 7/15/2016.
 */
public class SongSend extends Activity {
    String buddy;
    int selectfile=0;
    int complete;
    File file;
    File file1;
    File file2;
    File file3;

Button sbutt1;
    Button sbutt2;
    Button sbutt3;
    Button sbutt4;

    MediaPlayer mPlayer;
    String location;
    String location1;
    String location2;
    String location3;
    String songnames;
    String songnames1;
    String songnames2;
    String songnames3;
    int count=1;
String username;
TextView songname1;
    TextView songname2;
    TextView songname3;
    TextView songname4;

SharedPreferences.Editor editor;
    ProgressBar progressbar;
    ProgressBar progressbar1;
    ProgressBar progressbar2;
    ProgressBar progressbar3;
    int check;

    int selectedsongs=0;
    String theirusername;
    //Saves songs path in shared pref
    public static final String MyPREFERENCES = "MyPrefs" ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendsong);
        username = getIntent().getStringExtra("usernamek");

        theirusername=getIntent().getStringExtra("theirusername");
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
editor = sharedpreferences.edit();
        setTitle("Select song for "+buddy);
        progressbar= (ProgressBar)findViewById(R.id.song1pbar);
        progressbar1= (ProgressBar)findViewById(R.id.song2pbar);
        progressbar2= (ProgressBar)findViewById(R.id.song3pbar);
        progressbar3= (ProgressBar)findViewById(R.id.song4pbar);
        intiliza();
        songname1 = (TextView)findViewById(R.id.songname1);
        songname2 = (TextView)findViewById(R.id.songname2);
        songname3 = (TextView)findViewById(R.id.songname3);
        songname4 = (TextView)findViewById(R.id.songname4);
        sbutt1 = (Button)findViewById(R.id.sbutt1);
        sbutt2 = (Button)findViewById(R.id.sbutt2);
        sbutt3 = (Button)findViewById(R.id.sbutt3);
        sbutt4 = (Button)findViewById(R.id.sbutt4);
    }

public void uploadBtn(View view){
if(selectfile==0){


}else if (selectfile!=complete){

    Toast.makeText(getApplicationContext(),"Please wait for the songs to upload.",Toast.LENGTH_SHORT).show();
}


}

    public void sBut1(View view) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }
    public void sBut2(View view) {

        Intent a = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(a, 2);

    }
    public void sBut3(View view) {

        Intent b = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(b, 3);

    }
    public void sBut4(View view) {

        Intent c = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(c, 4);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            Uri selectedAudio = data.getData();

            //mPlayer = MediaPlayer.create(this, selectedAudio);
            location = getRealPathFromURI(this, selectedAudio);
            file = new File(location);

            Uri file1 = Uri.fromFile(new File(location));
            songnames = file1.getLastPathSegment();
            String path =location.replace(songnames,"");
if(selectfile==0){

    editor.putString("PATH",path);
}
            songname1.setText(songnames);
            sbutt1.setEnabled(false);
            selectfile++;
           uploadFile(location,1);

        }else if (requestCode == 2 && data != null){
            Uri selectedAudio1 = data.getData();

           // mPlayer = MediaPlayer.create(this, selectedAudio1);
            location1 = getRealPathFromURI(this, selectedAudio1);
            file1 = new File(location1);
            Uri file2 = Uri.fromFile(new File(location1));
            songnames1 = file2.getLastPathSegment();
            String path1 =location1.replace(songnames1,"");
            songname2.setText(songnames1);
            sbutt2.setEnabled(false);
            selectfile++;
        uploadFile(location1,2);

        }else if (requestCode == 3&& data != null){
            Uri selectedAudio2 = data.getData();

            // mPlayer = MediaPlayer.create(this, selectedAudio1);
            location2 = getRealPathFromURI(this, selectedAudio2);
            file2 = new File(location2);
            Uri file3 = Uri.fromFile(new File(location2));
            songnames2 = file3.getLastPathSegment();
            String path2 =location2.replace(songnames2,"");
            if(selectfile==0){

                editor.putString("PATH",path2);
            }
            songname3.setText(songnames2);
            sbutt3.setEnabled(false);
            selectfile++;
         uploadFile(location2,3);

        }else if (requestCode == 4 && data != null){
            Uri selectedAudio3 = data.getData();

            // mPlayer = MediaPlayer.create(this, selectedAudio1);
            location3 = getRealPathFromURI(this, selectedAudio3);
            file3 = new File(location);
            Uri file4 = Uri.fromFile(new File(location3));
            songnames3 = file4.getLastPathSegment();
            String path3 =location3.replace(songnames3,"");
            if(selectfile==0){

                editor.putString("PATH",path3);
            }
            songname4.setText(songnames3);
            sbutt4.setEnabled(false);
            selectfile++;
       uploadFile(location3,4);

        }

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
    public void playButton(View view){
        if(check==0) {
            mPlayer.start();
            check=1;
        }else{

            mPlayer.pause();
            check=0;

        }
    }
    public void intiliza(){
        verifyStoragePermissions(this);

    }
    public void uploadFile(String string, final int progressbarnum){


        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");
        final Uri file = Uri.fromFile(new File(string));
        StorageReference riversRef = storageRef.child(username+"/"+"songs"+"/"+file.getLastPathSegment());

        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("log","failsss");
                selectfile--;
                switch(progressbarnum){
                    case 1:
                        progressbar.setAlpha(0);
                        songname1.setText("Failed");
                        return;
                    case 2:

                        progressbar1.setAlpha(0);
                        songname2.setText("Failed");
                        return;
                    case 3:

                        progressbar2.setAlpha(0);
                        songname3.setText("Failed");
                    case 4:

                        progressbar3.setAlpha(0);
                        songname4.setText("Failed");
                        return;

                }
                Toast.makeText(getApplicationContext(),":( Upload failed:"+file.getLastPathSegment(),Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                complete++;
                Toast.makeText(getApplicationContext(),"Song sent :"+file.getLastPathSegment(),Toast.LENGTH_SHORT).show();
                switch(progressbarnum){
                    case 1:
                        progressbar.setAlpha(0);
                        songname1.setText("Done");
                        return;
                    case 2:

                        progressbar1.setAlpha(0);
                        songname2.setText("Done");
                        return;
                    case 3:

                        progressbar2.setAlpha(0);
                        songname3.setText("Done");
                    case 4:

                        progressbar3.setAlpha(0);
                        songname4.setText("Done");
                        return;

                }

              //  check("Awaz","UPLOAD COMPLETE");

                Log.i("log"," Upload complete:"+file.getLastPathSegment());

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long total = taskSnapshot.getTotalByteCount();
                long progress = taskSnapshot.getBytesTransferred();
                Integer i = (int) (long) total;
                Integer j = (int) (long) progress;
switch(progressbarnum){
    case 1:  // progressbar.setMax(i);
        progressbar.setAlpha(1);
        return;
    case 2:
        //progressbar1.setMax(i);
        progressbar1.setAlpha(1);
return;
    case 3:
        //progressbar2.setMax(i);
        progressbar2.setAlpha(1);
    case 4:
       // progressbar3.setMax(i);
        progressbar3.setAlpha(1);
return;

}





            }
        });}
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {

        }
    }
    public void informaboutUpload(View view){


        if(selectfile==0) {

    }else if (selectfile!=complete){

        Toast.makeText(getApplicationContext(),"Please wait for the songs to upload.",Toast.LENGTH_SHORT).show();
    }
        if (selectfile != 0) {
            final DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference();
            InformUplaod iu = new InformUplaod(username, theirusername, selectfile, songnames, songnames1, songnames2, songnames3);
            ref4.child("Sessions").push().setValue(iu);

        }




    }

public void goBack(){
    Intent ints = new Intent(SongSend.this, Tabs.class);
    ints.putExtra("SongSender",true);
    startActivity(ints);


}
}

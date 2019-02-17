package drafts;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;


import java.io.File;

/**
 * Created by Anjum on 7/16/2016.
 */
public class player extends Activity {
    DatabaseReference ref;
    File localFile =null;
    ProgressBar pbar;
    MediaPlayer mPlayer;
    Boolean play;
    int playing=0;
    int num=0;
    int x;
    String myusername;
   String senderusername;
    int numofsongs;
    String songs[];
TextView numofsongz;
    Button sendyoursongs;

    String key;
    DatabaseReference ref3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.recsongs);

        //pbar= (ProgressBar)findViewById(R.id.progressBar4);
        //ref = new Firebase("https://awaz-56a71.firebaseio.com/").child("testing1");

       // get("monstercat-aero-chord-surface.mp3","HassanAbbasi1");
        sendyoursongs= (Button)findViewById(R.id.sendyoursongs);
        myusername = getIntent().getStringExtra("myusername");
        senderusername = getIntent().getStringExtra("senderusernamez");
        key = getIntent().getStringExtra("key");
        ref3 = FirebaseDatabase.getInstance().getReference();

        numofsongz= (TextView)findViewById(R.id.numofsongz);
        checkifsongsalreadysent();
      checkForSong();

    }
    public void playSavedsong(){




    File file = new File(getDataFolder(player.this), songs[0]);
if(file.exists()){
        Log.i("pendinf","There");
   mPlayer = MediaPlayer.create(this, Uri.fromFile(file));
      mPlayer.start();

    }}
    public void check(final File localFile){
        Log.i("log","Got here");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
play= (Boolean) dataSnapshot.getValue();
                if(play){
                    Log.i("log",dataSnapshot.getValue().toString());
             // playSavedsong(localFile);


                }
                else if(!play){
                    Log.i("log","Pause");
mPlayer.pause();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
    public void playpause(View view){
        if(playing==0){
        ref.setValue(true);
        playing=1;
        }else if(playing==1){
            ref.setValue(false);
            playing=0;

        }

    }
    public void get(String name, final String buddy){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");

        StorageReference islandRef;
        islandRef = storageRef.child(buddy+"/"+"songs"+"/"+name);


        File file = new File(getDataFolder(player.this), name);
      Log.i("pending",file.toString());
            localFile = file;
            Log.i("log101","Storagesuccess");




        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i("log101",localFile.toString());

                Toast.makeText(getApplicationContext(),"Download complete",Toast.LENGTH_SHORT).show();
             //  check(localFile);
                num--;
                numofsongz.setText(Integer.toString(num));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("log101","StoragesFAILURE"+localFile.toString());
                Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
              //  num--;
                numofsongz.setText(Integer.toString(num));

            }
        });


        islandRef.getFile(localFile).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

            }
        });


    }
    public void downlaodsongs(View view){
        if(num!=0){

         //   Toast.makeText(getApplicationContext(),"Wait for songs to download",Toast.LENGTH_SHORT).show();
        }else{
      ref3.child("Sessions").child(key).removeValue();
   
        }
playSavedsong();
    }
    public File getDataFolder(Context context) {
        File dataDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(), "myappdata");
            if(!dataDir.isDirectory()) {
                dataDir.mkdirs();
            }
        }

        if(!dataDir.isDirectory()) {
            dataDir = context.getFilesDir();
        }

        return dataDir;

    }
    public void updatePlay(){
      //

    }
   public void checkForSong(){
Log.i("Pending",myusername);
       ref3.child("Sessions").orderByChild("recusername").equalTo(myusername).addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               if(dataSnapshot.getValue()!=null){
                   drafts.InformUplaod iup =  dataSnapshot.getValue(drafts.InformUplaod.class);

                   numofsongs=iup.getNumofsongs();
int y=0;
                 x=1;
                   songs=new String[numofsongs];
                   while(y<numofsongs){
                       songs[y]=dataSnapshot.child("song"+Integer.toString(x)).getValue().toString();

                       num++;
                       numofsongz.setText(Integer.toString(num));
                  get(songs[y],senderusername);
                       x++;
                       y++;
                   }


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
}
    public void checkifsongsalreadysent() {
        ref3.child("Sessions").orderByChild("recusername").equalTo(senderusername).addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
         if(dataSnapshot.child("username").getValue().toString()==myusername){
             sendyoursongs.setEnabled(false);
             sendyoursongs.setAlpha(0);

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

    }
}

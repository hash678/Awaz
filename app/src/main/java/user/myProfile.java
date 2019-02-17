package user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.MMainActivity;
import com.abbasi.awaz.R;

import viewpager.TabFragment1;
import viewpager.Tabs;
import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import auth.SmsReceiver;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anjum on 7/17/2016.
 */
public class myProfile extends AppCompatActivity {
    private String username;
    private String name;
    private EditText usernameT;
    private EditText nameT;
    DatabaseReference ref;
    String uid;
    String location;
    ProgressDialog progress;
    Boolean checkusername;
    String preusername;
Bitmap photo;
    CircleImageView imageView;



@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.myprofile);
        usernameT = (EditText)findViewById(R.id.username);
        nameT = (EditText)findViewById(R.id.name);
    Log.i("Tester",usernameT.getText().toString());
    Log.i("Tester",nameT.getText().toString());
    TextView myTextView = (TextView)findViewById(R.id.enteryourdetails);
    Typeface typeFace= Typeface.createFromAsset(getAssets(),"fonts/AsimovProBd.otf");
    myTextView.setTypeface(typeFace);
    imageView = (CircleImageView) findViewById(R.id.profilepic);
    Bundle bundle = getIntent().getExtras();

    checkusername = bundle.getBoolean("edit-profile",false);
    preusername = bundle.getString("username");
ref = FirebaseDatabase.getInstance().getReference();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    if(checkusername){
        progress = ProgressDialog.show(this, "Please wait",
                "Loading", true);
        usernameT.setAlpha(0);
        usernameT.setEnabled(false);
        usernameT.setText(preusername);

Log.i("HAA",FirebaseAuth.getInstance().getCurrentUser().getProviderData().toString());



     ref.child("users").child(preusername).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             if (dataSnapshot.child("photourl").getValue() != null) {
                 if (!dataSnapshot.child("photourl").getValue().toString().contains("null")) {
                     Glide.with(myProfile.this)
                             .load(dataSnapshot.child("photourl").getValue().toString())
                             .error(R.drawable.userprofileicon) // will be displayed if the image cannot be loaded
                             .crossFade()
                             .into(imageView);

                     nameT.setText(dataSnapshot.child("name").getValue().toString());
                     nameT.setHint("");

                 } else {
                     nameT.setText(dataSnapshot.child("name").getValue().toString());
                 }
             }
             progress.dismiss();
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

    }else{  ComponentName receiver = new ComponentName(myProfile.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Log.i("Sms_bug", "Disabled broadcst receiver");}
    }

    public void onSetbtn(View v){
        Log.i("same",nameT.getText().toString());
//usernameT.getText().toString()!=""&&nameT.getText().toString()!=""
        if(!(nameT.getText().toString().matches("")&&nameT.getText().toString().matches(""))){
        progress = ProgressDialog.show(this, "Please wait",
                "UPDATING", true);
        name = nameT.getText().toString();
        username= usernameT.getText().toString();
if(!checkusername) {
    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild(username)) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Username in use", Toast.LENGTH_SHORT).show();

            } else {
                if (username.contains("#") || username.contains(" ") || username.contains(".") || username.contains("$") || username.contains("[") || username.contains("]")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Username must not have space, '.', '#', '$', '[', or ']'", Toast.LENGTH_SHORT).show();
                } else {
                    ref.child("users").child(username).child("UID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("users").child(username).child("name").setValue(name);
                    ref.child("users").child(username).child("status").setValue(true);
                    TabFragment1 cls = new TabFragment1();

                    ref.child("users").child(username).child("P_num").setValue(cls.formatNumbers(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
                    if (location != null) {
                        uploadFile(location,username);

                    } else {
                        ref.child("users").child(username).child("photourl").setValue("null");
                        Glide.get(getApplicationContext()).clearDiskCache();
                        startActivity(new Intent(myProfile.this, MMainActivity.class));

                        finish();
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    });

}else{
    ref.child("users").child(preusername).child("name").setValue(name);
    if (location != null) {
        uploadFile(location,preusername);

}else{
        startActivity(new Intent(myProfile.this, Tabs.class));
        finish();

    }

}

}else{
 Toast.makeText(myProfile.this,"Please fill in all details",Toast.LENGTH_SHORT).show();

    }}



    public void uploadPhoto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);

        startActivityForResult(intent,9);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 9&&resultCode==RESULT_OK&&data!=null) {
            final Bundle extras = data.getExtras();

            if (extras != null) {
                 photo = extras.getParcelable("data");


Glide.get(myProfile.this).clearMemory();



                imageView.setImageBitmap(photo);
                try {
                    location= saveToInternalStorage(photo)+"/profile.jpg";

                } catch (IOException e) {
                    e.printStackTrace();
                }
//            MainActivity mainActivity = new MainActivity();
  //         location = mainActivity.getRealPathFromURI(this, savebitmap());

            }
        }

        }




    public void uploadFile(String string, final String username1){


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");
        Uri file = Uri.fromFile(new File(string));
        StorageReference riversRef = storageRef.child(username1+"/"+"Dp"+file.getLastPathSegment());

        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progress.dismiss();
                Log.i("log","failsss");
                Toast.makeText(getApplicationContext(),"Profile picture upload failed. Check you connection.",Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(myProfile.this)
                        .setTitle("Problem setting picture")
                        .setMessage("Profile picture cannot be set")
                        //If he selects accept
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        //If he declines
                        .setNegativeButton("Never mind", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                location = null;
                                ref.child("users").child(username1).child("photourl").setValue("null");
                                startActivity(new Intent(myProfile.this, Tabs.class));
                                finish();
                            }
                        })
                        //Sets happy icon on Session request
                        .setIcon(R.drawable.sad)
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.


                Log.i("log","Success");
progress.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                ref.child("users").child(username1).child("photourl").setValue(downloadUrl.toString());
                Log.i("Log",downloadUrl.toString());
                Glide.get(myProfile.this).clearMemory();
                photo.recycle();
                startActivity(new Intent(myProfile.this, MMainActivity.class));
                finish();

            }
        });


    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"It seems there is a problem loading your current display photo",Toast.LENGTH_SHORT).show();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    protected void onPause() {
        Tabs home = new Tabs();
if(checkusername){
        home.updateUserStatus(false,preusername);}
        super.onPause();
    }



    @Override
    protected void onResume() {
        Tabs home = new Tabs();
        if(checkusername){

            home.updateUserStatus(true,preusername);}
        super.onResume();
    }


}

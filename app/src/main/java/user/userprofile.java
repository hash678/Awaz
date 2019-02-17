package user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abbasi.awaz.R;
import viewpager.Tabs;
import com.facebook.drawee.view.SimpleDraweeView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Anjum on 7/19/2016.
 */
public class userprofile extends AppCompatActivity {
    String username;
    TextView buddynamez;
    TextView buddyusernamez;
    TextView buddystatus;
    ProgressDialog progress;
   // CircleImageView buddyphotos;
   SimpleDraweeView draweeView;
String myusername;
    Button requestss;
    Button addbuddy;
    DatabaseReference ref;
    String requeststatus= "null";
    String sessionstatus="null";
String friendrequestnode;
    String sessionrequestnode;
    DatabaseReference refroot;
    String myphotourl;
    String photourl;
    Boolean friends = false;
ImageView ticku;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("usernameofsearched");
        myusername = bundle.getString("myusername");
        requestss = (Button)findViewById(R.id.requestse);
        addbuddy = (Button)findViewById(R.id.addbuddy);

        draweeView = (SimpleDraweeView) findViewById(R.id.buddypic);
        ticku=(ImageView) findViewById(R.id.ticku);
        requestss.setEnabled(false);
        requestss.setAlpha(0);
        Log.i("pending", FirebaseAuth.getInstance().getCurrentUser().getUid());
//addbuddy.setEnabled(false);

        refroot = FirebaseDatabase.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference();
        if(username.contains(myusername)){

           addbuddy.setText("Edit profile");
            addbuddy.setEnabled(true);
        }

        Log.i("log001",username);
      progress = ProgressDialog.show(this, "Loading",
        "Give me a minute", true);
        buddynamez = (TextView)findViewById(R.id.buddynamez);
         buddyusernamez =(TextView)findViewById(R.id.buddyusernamez);
        buddystatus = (TextView)findViewById(R.id.buddystatus);
    getUserinfo();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Log.i("HAA",user.getUid().toString());

    }
    public void getUserinfo(){
ref.child("users").child(myusername).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.child("photourl")!=null){
        myphotourl=dataSnapshot.child("photourl").getValue().toString();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        ref.child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buddynamez.setText(dataSnapshot.child("name").getValue().toString().toUpperCase());
                buddyusernamez.setText(username);
                getUsersfriendstatus();
                //getSessionstatus();
                if(dataSnapshot.child("status").getValue().toString().contains("true")){
                    buddystatus.setText("online");
                buddystatus.setTextColor(0X6B8E23);
                }else if(dataSnapshot.child("status").getValue().toString().contains("false")){
buddystatus.setText("offline");
                   }else{
                    buddystatus.setText("Last online: "+getDate((Long)dataSnapshot.child("status").getValue(), "dd/MM/yyyy hh:mm:ss"));
buddystatus.setTextColor(0xbf284b);
                }
                if(dataSnapshot.child("photourl").getValue()!=null){


                if(!dataSnapshot.child("photourl").getValue().toString().contains("null")){
                    photourl=dataSnapshot.child("photourl").getValue().toString();
                    Uri uri = Uri.parse(photourl);
                    draweeView.setImageURI(uri);



progress.dismiss();

                }else{



                    progress.dismiss();

                }
                }else{

                    progress.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




    }
    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
              //  Toast.makeText(getApplicationContext(),"It seems there is a problem loading the display photo",Toast.LENGTH_SHORT).show();

                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public void onButtonBuddyadd(View view){
        if(username.contains(myusername)){
            Intent intent = new Intent(this,myProfile.class);
            intent.putExtra("edit-profile",true);
            intent.putExtra("username",username);
            startActivity(intent);
        }else{
            addBuddy();}

    }
    public void addBuddy(){

if(requeststatus=="pending"){
    addToBuddy addbuddys = new addToBuddy(myphotourl,myusername);
    addToBuddy addbuddys2 = new addToBuddy(photourl,username);

    refroot.child("users").child(myusername).child("friends").push().setValue(addbuddys2);
    refroot.child("users").child(username).child("friends").push().setValue(addbuddys);
    refroot.child("friendrequests").child(friendrequestnode).removeValue();
    }else{
    Log.i("sender","true");
    GetFriendRequests sender = new GetFriendRequests(username,myusername,"sent",myphotourl);
    refroot.child("friendrequests").push().setValue(sender);


}
    }

public void getUsersfriendstatus(){
refroot.child("users").child(myusername).child("friends").orderByChild("username").equalTo(username).addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if(dataSnapshot.getValue()!=null){
friends=true;
                addbuddy.setEnabled(false);
                addbuddy.setText("Buddies");
            ticku.setImageResource(R.drawable.tick);
                requestss.setEnabled(true);
                requestss.setAlpha(1);


    }}

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
    refroot.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            refroot.child("friendrequests").orderByChild("status").equalTo("sent").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GetFriendRequests gf = dataSnapshot.getValue(GetFriendRequests.class);

                    if(gf.getSendusername().contains(myusername)&&gf.recusername.contains(username)){
                        requeststatus="sent";
                        Log.i("pending",requeststatus);
                        updateAddbuddyButton();
                    }else if(gf.getRecusername().contains(myusername)&&gf.getSendusername().contains(username)){

                        requeststatus="pending";

                        friendrequestnode = dataSnapshot.getKey();
                        Log.i("pending",requeststatus);
                        updateAddbuddyButton();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    GetFriendRequests gf = dataSnapshot.getValue(GetFriendRequests.class);
                    if(gf.getSendusername().contains(myusername)&&gf.recusername.contains(username)){

                        requeststatus="sent";
                        Log.i("pending",requeststatus);
                        updateAddbuddyButton();
                    }else if(gf.getRecusername().contains(myusername)&&gf.getSendusername().contains(username)){

                        requeststatus="pending";
                        Log.i("pending",requeststatus);
                        updateAddbuddyButton();
                    }

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

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }



    });



}
    public void updateAddbuddyButton(){
        switch (requeststatus){
            case "sent":
                addbuddy.setText("Buddy request sent!");
                addbuddy.setEnabled(false);
                ticku.setImageResource(R.drawable.sent);
                return;
            case "pending":
                addbuddy.setText("Accept friend request");
                addbuddy.setEnabled(true);
                ticku.setImageResource(R.drawable.rec);
                return;
            default:
                addbuddy.setText("Send buddy request");
                addbuddy.setEnabled(true);
                ticku.setImageResource(R.drawable.ic_username);
                return;
        }

    }
    public void updateSessionReqButton(){
       if(friends){
        switch (sessionstatus) {
            case "sent":
                requestss.setText("Session request sent!");
                requestss.setEnabled(false);
                return;
            case "pending":
                requestss.setText("Accept session request");
                requestss.setEnabled(true);
                return;
            case "null":
                requestss.setText("Send session request");
                requestss.setEnabled(true);
                return;
        }
        }




    }
    public void sendSessionRequest(View view){
      if(sessionstatus!="sent") {
         // sendSessionRequest sender = new sendSessionRequest(myusername, myphotourl, username, "sent");
          //refroot.child("sessionrequests").push().setValue(sender);

          Intent sessionC = new Intent(userprofile.this,com.abbasi.awaz.ceatgroup.class);
          sessionC.putExtra("usernamebuddy",username);
          sessionC.putExtra("username",myusername);
          startActivity(sessionC);


      }else if(sessionstatus=="pending"){
          refroot.child("sessionrequests").child(sessionrequestnode).child("status").setValue("accepted");
      }


    }


    @Override
    protected void onPause() {
       Tabs home = new Tabs();

        home.updateUserStatus(false,myusername);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Tabs home = new Tabs();

        home.updateUserStatus(true,myusername);
        super.onResume();
    }
}

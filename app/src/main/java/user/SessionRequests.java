package user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.abbasi.awaz.R;
import viewpager.Tabs;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SessionRequests extends AppCompatActivity
       {
    public TextView status;
ArrayList<String> requests;
ArrayList<String> photoreq;
ArrayList<String> groupidd;
ListView sessionRecyclerView;
           String[] sessionreq;
           String[] photo;
           String[] groupid;
    // Firebase instance variables
    DatabaseReference reference;
    private static String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessionrequests);
        sessionRecyclerView = (ListView)findViewById(R.id.sessionRecyclerView);
requests = new ArrayList<String>();
photoreq = new ArrayList<String>();
groupidd = new ArrayList<String>();
        reference =FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("usernamek");
         reference.child("users").child(username).child("session").orderByChild("status").equalTo("false").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    Log.i("test",dataSnapshot.getValue().toString());
                   sendSessionRequest sc = dataSnapshot.getValue(sendSessionRequest.class);
                requests.add(sc.getUsernameofsessionuser());
groupidd.add(dataSnapshot.getKey());
//  Log.i("test",sc.getUsernameofsessionuser());
                    photoreq.add("");
                    sessionreq = new String[requests.size()];
                    groupid = new String[groupidd.size()];
                    photo = new String[photoreq.size()];

                    sessionreq = requests.toArray(sessionreq);
                    groupid = groupidd.toArray(groupid);
                    photo = photoreq.toArray(photo);

                    sessionRecyclerView.setAdapter(new Sessionadapter(SessionRequests.this,sessionreq,photo,groupid));


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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                Tabs tab = new Tabs();
                tab.logOut();
                return true;
            case R.id.fresh_config_menu:
                Intent intent = new Intent(this,myProfile.class);
                intent.putExtra("edit-profile",true);
               // intent.putExtra("username",mUsername);
                startActivity(intent);
                return true;
            case R.id.friendrequest:
                Intent a = new Intent(this,FriendRequests.class);
                //a.putExtra("names",mUsername);
                //a.putExtra("usernamek",mUsername);
                startActivity(a);

                return true;


            default:
                return super.onOptionsItemSelected(item);


        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  mGoogleApiClient.stopAutoManage(SessionRequests.this);
        //mGoogleApiClient.disconnect();
    }
public void requestDeal( Boolean deal, String id){
 if(deal){
     FirebaseDatabase.getInstance().getReference().child("users").child(Tabs.username).child("session").child(id).child("status").setValue("true");
 }   else{
     FirebaseDatabase.getInstance().getReference().child("users").child(Tabs.username).child("session").child(id).child("status").setValue("reject");

 }


}

       }



package chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.abbasi.awaz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import viewpager.Tabs;

public class Members extends AppCompatActivity {
String groupid;
    ListView memberslist;
    ArrayList<String> memebers;
    ArrayList<String> photourl;
    FirebaseDatabase ref;
    ImageView adduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members2);

        Intent ij = getIntent();
        groupid = ij.getStringExtra("groupid");
        Log.i("yo",groupid);
        memebers = new ArrayList<String>();
        photourl = new ArrayList<String>();
        memberslist =(ListView)findViewById(R.id.listofmembers);
        adduser =(ImageView) findViewById(R.id.adduser);
        ref = FirebaseDatabase.getInstance();
init();
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jk = new Intent(Members.this,com.abbasi.awaz.ceatgroup.class);
                jk.putExtra("groupid",groupid);
                jk.putExtra("username", Tabs.username);
                Toast.makeText(Members.this,R.string.notavaill,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void init(){
        if(groupid!=null) {
            ref.getReference().child("session").child(groupid).child("members").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
if(dataSnapshot.getValue()!=null){
    if(!memebers.contains(dataSnapshot.getKey().toString())){
memebers.add(dataSnapshot.getKey());
        Log.i("yo","added");
ref.getReference().child("users").child(dataSnapshot.getKey()).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue()!=null){
            photourl.add(dataSnapshot.getValue().toString());
            memberslist.setAdapter(new Membersadapter(Members.this,memebers,photourl,groupid));
        }else{
            photourl.add("null");
            memberslist.setAdapter(new Membersadapter(Members.this,memebers,photourl,groupid));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
    }

}

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
if(dataSnapshot.getValue()!=null){
                    if(memebers.contains(dataSnapshot.getKey())){
                      photourl.remove(memebers.indexOf(dataSnapshot.getKey()));
                        memebers.remove(dataSnapshot.getKey());
                        memberslist.setAdapter(new Membersadapter(Members.this,memebers,photourl,groupid));
                    }


}}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

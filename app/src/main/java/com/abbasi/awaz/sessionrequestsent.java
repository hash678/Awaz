package com.abbasi.awaz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import user.sendSessionRequest;

public class sessionrequestsent extends AppCompatActivity {
String username;
    String sesssionname;
    int count;
    int number=0;
String photourl;
    String sessionname="default";
    String groupid=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionrequestsent);

        final ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("ListBuddy");
        username= getIntent().getStringExtra("username");
        sesssionname= getIntent().getStringExtra("sessionname");
        count= getIntent().getIntExtra("counter",0);
groupid = getIntent().getStringExtra("groupid");
        Log.i("yahoo",myList.toString());
        final DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        if(groupid==null){
            groupid = fb.child("session").push().getKey();
            fb.child("session").child(groupid).child("host").setValue(username);
            user.sendSessionRequest Sr = new user.sendSessionRequest(photourl,sessionname,username,"true");
            fb.child("users").child(username).child("session").child(groupid).setValue(Sr);
        }


        fb.child("users").child(username).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue()!=null){
            photourl = dataSnapshot.getValue().toString();
        }else{
            photourl=null;
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        fb.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(number<1) {
            for (int x = 0; x < myList.size(); x++) {

                user.sendSessionRequest Sr = new user.sendSessionRequest(photourl,sessionname,username,"false");
                fb.child("users").child(myList.get(x)).child("session").child(groupid).setValue(Sr);
                sendNotificationz(dataSnapshot.child(myList.get(x)).child("FCMID").getValue().toString(), "Session request", username);
                number++;
            }
        }}

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    });

}







    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private void sendNotificationz(final String regtoken, final String message, final String title) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body",message);
                    dataJson.put("title",title);
                    dataJson.put("sound","default");
                    dataJson.put("tag","session");
                    json.put("notification",dataJson);
                    json.put("to",regtoken);

                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+ "AIzaSyD5rchgxwZWnohd7IEkSlnwkkky1Ytorn8")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d("SessionRequest",e+" eeeeeeh");
                    // Toast.makeText(ceatgroup.this,"Oops something went wrong! Are you connected to the interent?",Toast.LENGTH_SHORT).show();

                }
                return null;
            }
        }.execute();
    }
}

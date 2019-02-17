package com.abbasi.awaz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import user.addToBuddy;
import user.userprofile;

public class ceatgroup extends AppCompatActivity
implements GoogleApiClient.OnConnectionFailedListener {
        private DatabaseReference mFirebaseDatabaseReference;
    public static ArrayList<String> usernameofuser;
        private FirebaseRecyclerAdapter<addToBuddy, MessageViewHolder>
                mFirebaseAdapter;
        static DatabaseReference refroot;
        public CircleImageView buddyphoto;
        static String myusername;
    int counter=0;
    TextView buddynum;
String buddyname;
    EditText sessionname;
    ArrayList<String> list;


    public static class MessageViewHolder extends RecyclerView.ViewHolder implements  View.OnLongClickListener {
            public CircleImageView sessionImageView;
            public TextView sessionTextView;
            public ImageView statusicon;
            public LinearLayout clicker;
            SimpleDraweeView draweeView;
            Context context1;



            public MessageViewHolder(View v) {
                super(v);
                refroot= FirebaseDatabase.getInstance().getReference();
                sessionTextView = (TextView) itemView.findViewById(R.id.sessionTextView);
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.sessionImageView);
                statusicon = (ImageView) itemView.findViewById(R.id.cstatusicon);
                clicker= (LinearLayout)itemView.findViewById(R.id.sessionclicker);

             //   butoon.setOnClickListener(this);
              //  decline=(Button)itemView.findViewById(R.id.declinebut);
             //   decline.setOnClickListener(this);
                sessionTextView.setOnLongClickListener(this);
                draweeView.setOnLongClickListener(this);
                context1=v.getContext();
            }

            @Override
            public boolean onLongClick(View view) {
                switch (view.getId()){
                    case R.id.sessionTextView:
                        //ONCLICK
                        Log.i("onscene","CLickhogaya bacha");
                        return true;
                     case R.id.sessionImageView:
                    //onclick
                         Log.i("onscene","CLickhogaya bachaz");

                         return true;
                }
                Log.i("onscene","CLickhogaya bachaaaaaaa");

                return false;

            }

                   }

        private static final String TAG = "MainActivity";
        public static final String MESSAGES_CHILD = "messages";


        private GoogleApiClient mGoogleApiClient;
    private SharedPreferences mSharedPreferences;
        private Button mSendButton;
        private RecyclerView mMessageRecyclerView;
        private LinearLayoutManager kLinearLayoutManager;
        private ProgressBar mProgressBar;
        private EditText mMessageEditText;
        public String buddyname1;
        public String buddystatus1;
        public TextView status;
        private TextView nobuddy;
        private TextView sc;
        static String myphotourl;
        static String photourl;
String groupid=null;

        // Firebase instance variables
        DatabaseReference reference;
        private static String username;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ceatgroup);
            usernameofuser = new ArrayList<String>();
            reference =FirebaseDatabase.getInstance().getReference();
            Bundle bundle = getIntent().getExtras();
            username = bundle.getString("username");
            buddyname = bundle.getString("usernamebuddy");
            groupid = bundle.getString("groupid");

            buddynum= (TextView)findViewById(R.id.buddynum);
            list  = new ArrayList<String>();
            reference = FirebaseDatabase.getInstance().getReference();
            sessionname = (EditText)findViewById(R.id.sessionname);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();

            // Initialize ProgressBar and RecyclerView.
            mProgressBar = (ProgressBar) findViewById(R.id.cprogressBar);
            nobuddy = (TextView)findViewById(R.id.nobuddy);
            sc = (TextView)findViewById(R.id.sc);
            mMessageRecyclerView = (RecyclerView) findViewById(R.id.cRecyclerView);
            kLinearLayoutManager = new LinearLayoutManager(this);
            kLinearLayoutManager.setStackFromEnd(false);
            mMessageRecyclerView.setLayoutManager(kLinearLayoutManager);
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

            mFirebaseAdapter = new FirebaseRecyclerAdapter<addToBuddy,
                    ceatgroup.MessageViewHolder>(
                    addToBuddy.class,
                    R.layout.sessionrow,
                    ceatgroup.MessageViewHolder.class,
                    mFirebaseDatabaseReference.child("users").child(username).child("friends").orderByChild("username")) {

                @Override
                protected void populateViewHolder(final MessageViewHolder viewHolder,
                                                  final addToBuddy gf, final int position) {


                    usernameofuser.add(gf.getUsername());
                    ColorDrawable buttonColorrr = (ColorDrawable) viewHolder.clicker.getBackground();
                    if(checkBuddy(buddyname,gf.getUsername())&&buttonColorrr.getColor()!=getResources().getColor(R.color.image_border_end)){
                        counter++;
                        list.add(gf.getUsername());

                        buddynum.setText("Buddy# "+counter);

                        viewHolder.clicker.setBackgroundColor(getResources().getColor(R.color.image_border_end));

                    }
                    //  getMyPhotourl(username);
                    // mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    // nobuddy.setVisibility(TextView.INVISIBLE);
                    viewHolder.sessionTextView.setText(gf.getUsername());
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    //nbtext.setVisibility(ProgressBar.INVISIBLE);

                    reference.child("users").child(gf.getUsername()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("status").getValue().toString().contains("true")){
                                viewHolder.statusicon.setImageResource(R.drawable.ic_online);
                            }else{
                                viewHolder.statusicon.setImageResource(R.drawable.ic_offline);

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    reference.child("users").child(gf.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("photourl").getValue()!=null){
                                if(!dataSnapshot.child("photourl").getValue().toString().contains("null")){
                                    Uri uri = Uri.parse(dataSnapshot.child("photourl").getValue().toString());
                                    viewHolder.draweeView.setImageURI(uri);


                                }else {


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    viewHolder.sessionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ColorDrawable buttonColor = (ColorDrawable) viewHolder.clicker.getBackground();
                            if(buttonColor.getColor() ==getResources().getColor(R.color.image_border_end)) {

                                viewHolder.clicker.setBackgroundColor(0xFDFAF8f6);
                                if(counter!=0){
                                    counter--;
                                    list.remove(gf.getUsername());
                                    buddynum.setText("Buddy# "+counter);



                                }
                            }else{
                                counter++;
                                list.add(gf.getUsername());
                                buddynum.setText("Buddy# "+counter);
                                viewHolder.clicker.setBackgroundColor(getResources().getColor(R.color.image_border_end));//Green
                            }

                        }
                    });

                    viewHolder.clicker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          /*
                            Intent chatUser1 = new Intent(ceatgroup.this, userprofile.class);
                            // Log.i("log002",search.getText().toString());
                            chatUser1.putExtra("usernameofsearched",usernameofuser.get(position));
                            chatUser1.putExtra("myusername",username);
                            startActivity(chatUser1);
*/
                            /////Where we select
                            ColorDrawable buttonColor = (ColorDrawable) viewHolder.clicker.getBackground();
if(buttonColor.getColor() ==getResources().getColor(R.color.image_border_end)) {

    viewHolder.clicker.setBackgroundColor(0xFDFAF8f6);
if(counter!=0){
    counter--;
    list.remove(gf.getUsername());
    buddynum.setText("Buddy# "+counter);



}
}else{
    counter++;
    list.add(gf.getUsername());
    buddynum.setText("Buddy# "+counter);
    viewHolder.clicker.setBackgroundColor(getResources().getColor(R.color.image_border_end));//Green
}


                        }
                    });
                }

            };

            mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    kLinearLayoutManager.scrollToPositionWithOffset(2, 20);
                    super.onItemRangeInserted(positionStart, itemCount);
                    int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                    int lastVisiblePosition =
                            kLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.
                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        kLinearLayoutManager.scrollToPositionWithOffset(2, 20);
                    }
                }
            });

            mMessageRecyclerView.setLayoutManager(kLinearLayoutManager);
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);



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

                default:
                    return super.onOptionsItemSelected(item);


            }
        }


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
            Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
        }
    public void load(){

    }
    /*butoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refroot.child("users").child(myusername).child("friends").child(username).setValue(true);
                        refroot.child("users").child(username).child("friends").child(myusername).setValue(true);
                       // refroot.child("friendrequests").child(friendrequestnode).removeValue();
                    }
                });*/


    public void getMyPhotourl(String username3){
        refroot.child("users").child(username3).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    if(!dataSnapshot.getValue().toString().contains("null")){
                        myphotourl=dataSnapshot.getValue().toString();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(ceatgroup.this);
        mGoogleApiClient.disconnect();
    }


    public  void onTestnot(View view){
       /*
        Firebase refroot = new Firebase("https://awaz-56a71.firebaseio.com/").child("users").child("Amma");
        refroot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            sendNotificationz(dataSnapshot.child("FCMID").getValue().toString(),"HELLO","Session request!");


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

*/
if(counter!=0){
    Intent i = new Intent(ceatgroup.this,sessionrequestsent.class);
    i.putExtra("ListBuddy",list);
    i.putExtra("username",username);
    i.putExtra("counter",counter);
    if(sessionname.getText()!=null){
    i.putExtra("sessionname","default");}else {
        if(groupid!=null){
i.putExtra("groupid",groupid);

        }
        i.putExtra("sessionname",sessionname.getText().toString());

    }
    startActivity(i);
    finish();


}

    }


    public boolean checkBuddy(String buddy,String compare){
        if(buddy!=null){
            if (compare.toLowerCase().contains(buddy.toLowerCase())){
                return true;

            }else return false;

        }
        return false;
    }
public void saveBuddyNames(int count,String name){
SharedPreferences sp = this.getSharedPreferences("com.abbasi.awaz.sharedpreferences",MODE_PRIVATE);
sp.edit().putInt("number of buddies",count);



}


}
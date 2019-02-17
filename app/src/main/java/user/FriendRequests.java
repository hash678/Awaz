package user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.abbasi.awaz.R;
import viewpager.Tabs;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by Anjum on 7/20/2016.
 */
public class FriendRequests extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<GetFriendRequests, MessageViewHolder>
            mFirebaseAdapter;
    static Button butoon;
    static Button decline;
    static DatabaseReference refroot;
    public CircleImageView buddyphoto;
    static String myusername;
    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView requestImageView;
        public TextView requestTextView;
Context context1;



        public MessageViewHolder(View v) {
            super(v);
            refroot=FirebaseDatabase.getInstance().getReference();
            requestTextView = (TextView) itemView.findViewById(R.id.requestTextView);
            requestImageView = (CircleImageView) itemView.findViewById(R.id.requestImageView);
            butoon = (Button)itemView.findViewById(R.id.acceptbut);
            butoon.setOnClickListener(this);
            decline=(Button)itemView.findViewById(R.id.declinebut);
            decline.setOnClickListener(this);
            requestTextView.setOnClickListener(this);
            requestImageView.setOnClickListener(this);
context1=v.getContext();
        }

        @Override
        public void onClick(View view) {
            FriendRequests fr = new FriendRequests();
            switch (view.getId()){
                case R.id.requestTextView:
                    Intent chatUser = new Intent(context1, userprofile.class);
                    // Log.i("log002",search.getText().toString());
                    chatUser.putExtra("usernameofsearched",myusername);
                    chatUser.putExtra("myusername",username);
                    context1.startActivity(chatUser);

                    return;
                case R.id.acceptbut:
                    fr.acceptReq(myusername,username,myphotourl,photourl);
                    return;
                case R.id.requestImageView:
                    Intent chatUser1 = new Intent(context1, userprofile.class);
                    // Log.i("log002",search.getText().toString());
                    chatUser1.putExtra("usernameofsearched",myusername);
                    chatUser1.putExtra("myusername",username);
                    context1.startActivity(chatUser1);

                    return;
                case R.id.declinebut:
                    fr.declineReq(myusername,username);
                    return;
            }


        }
    }

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager kLinearLayoutManager;
    //private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    public String buddyname1;
    public String buddystatus1;
    public TextView status;
private TextView nobuddy;
    private TextView sc;
    static String myphotourl;
    static String photourl;


    // Firebase instance variables
    DatabaseReference reference;
    private static String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessionrequests);

        reference = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("usernamek");

        reference = FirebaseDatabase.getInstance().getReference();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.
       // mProgressBar = (ProgressBar) findViewById(R.id.requestprogressBar);
        nobuddy = (TextView)findViewById(R.id.nobuddy);
        sc = (TextView)findViewById(R.id.sc);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.requestRecyclerView);
        kLinearLayoutManager = new LinearLayoutManager(this);
        kLinearLayoutManager.setStackFromEnd(false);
        mMessageRecyclerView.setLayoutManager(kLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<GetFriendRequests,
                MessageViewHolder>(
                GetFriendRequests.class,
                R.layout.requestrow,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("friendrequests").orderByChild("recusername").equalTo(username)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              GetFriendRequests gf, int position) {
getMyPhotourl(username);
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                nobuddy.setVisibility(TextView.INVISIBLE);
//                sc.setVisibility(TextView.INVISIBLE);
                viewHolder.requestTextView.setText(gf.getSendusername());
                myusername =gf.getSendusername();


                if (gf.getSendersphotourl() == null||gf.getSendersphotourl().contains("null")) {
                    viewHolder.requestImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(FriendRequests.this,
                                            R.drawable.ic_account_circle_black_36dp));

                } else {
                    photourl = gf.getSendersphotourl();
                    Glide.with(FriendRequests.this)
                            .load(photourl)
                            .placeholder( R.drawable.userprofileicon)
                            .into(viewHolder.requestImageView);


                }

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
            case R.id.sign_out_menu:
                Tabs tab = new Tabs();
                tab.logOut();

                return true;
            case R.id.fresh_config_menu:
                Intent intent = new Intent(this,myProfile.class);
                intent.putExtra("edit-profile",true);
                intent.putExtra("username",mUsername);
                startActivity(intent);
                return true;
            case R.id.friendrequest:
                Intent a = new Intent(this,FriendRequests.class);
                a.putExtra("names",mUsername);
                a.putExtra("usernamek",mUsername);
                startActivity(a);

                return true;


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
public void acceptReq(final String myusername1, final String username1,final String myphotourl1,final String photourl1){
    refroot.child("friendrequests").orderByChild("recusername").equalTo(username1).addChildEventListener(new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.child("sendname").equals(myusername1));
            String friendrequestnode = dataSnapshot.getKey();
            addToBuddy addbuddys = new addToBuddy(myphotourl1,myusername);
            addToBuddy addbuddys2 = new addToBuddy(photourl1,username);
            refroot.child("users").child(myusername).child("friends").push().setValue(addbuddys2);
            refroot.child("users").child(username).child("friends").push().setValue(addbuddys);
            refroot.child("friendrequests").child(friendrequestnode).removeValue();
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

    });}
    public void declineReq(final String myusername1, final String username1){
        refroot.child("friendrequests").orderByChild("recusername").equalTo(username1).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("sendname").equals(myusername1));
                String friendrequestnode = dataSnapshot.getKey();
                refroot.child("friendrequests").child(friendrequestnode).removeValue();
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
    //refroot.child("users").child(myusername1).child("friends").child(username1).setValue(true);
    //refroot.child("users").child(username1).child("friends").child(myusername1).setValue(true);

}
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
        mGoogleApiClient.stopAutoManage(FriendRequests.this);
        mGoogleApiClient.disconnect();
    }

}



package user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;
import viewpager.Tabs;
import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anjum on 7/22/2016.
 */
public class MyBuddies extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<addToBuddy, MessageViewHolder>
            mFirebaseAdapter;

    public CircleImageView buddyphoto;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView requestImageView;
        public TextView requestTextView;





        public MessageViewHolder(View v) {
            super(v);
            requestTextView = (TextView) itemView.findViewById(R.id.mbTextView);
            requestImageView = (CircleImageView) itemView.findViewById(R.id.mbImageView);

        }
    }

    private static final String TAG = "MainActivity";

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager kLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    public String buddyname1;
    public String buddystatus1;
    public TextView status;
    String username;
    // Firebase instance variables

    protected void onCreate(Bundle savedInstanceState, final Context context) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_3);


        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("usernamek");


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.mbprogressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.mbRecyclerView);
        kLinearLayoutManager = new LinearLayoutManager(context);
        kLinearLayoutManager.setStackFromEnd(false);
        mMessageRecyclerView.setLayoutManager(kLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<addToBuddy,
                MessageViewHolder>(
                addToBuddy.class,
                R.layout.requestrow,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("users").child(username).child("friends").orderByChild("username")) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              addToBuddy ab, int position) {
                // nobuddy.setVisibility(TextView.INVISIBLE);
                viewHolder.requestTextView.setText(ab.getUsername());


                if (ab.getPhotourl() == null||ab.getPhotourl().contains("null")) {
                    viewHolder.requestImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(context,
                                            R.drawable.ic_account_circle_black_36dp));

                } else {

                    Glide.with(context)
                            .load(ab.getPhotourl())
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

    public  void clickmezz(final Context context){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}

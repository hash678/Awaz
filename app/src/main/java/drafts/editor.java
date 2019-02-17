package drafts;

/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import user.FriendRequests;
import com.abbasi.awaz.R;
import viewpager.Tabs;
import user.myProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;

import chat.FriendlyMessage;
import de.hdodenhof.circleimageview.CircleImageView;


public class editor extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
            mFirebaseAdapter;

    public CircleImageView buddyphoto;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView requestImageView;
        public TextView requestTextView;





        public MessageViewHolder(View v) {
            super(v);
            requestTextView = (TextView) itemView.findViewById(R.id.requestTextView);
            requestImageView = (CircleImageView) itemView.findViewById(R.id.requestImageView);

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
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    public String buddyname1;
    public String buddystatus1;
    public TextView status;
    String username;
    // Firebase instance variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequets);


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
    }

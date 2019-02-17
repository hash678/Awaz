package viewpager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;
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

import java.util.ArrayList;

import user.addToBuddy;
import user.userprofile;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class TabFragment3 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity().getApplication(),connectionResult.toString(),Toast.LENGTH_SHORT).show();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       // public CircleImageView requestImageView;
        public TextView requestTextView;
        public ImageView statusicon;
        public LinearLayout clicker;
        SimpleDraweeView draweeView;
        Context context1;
        View mView;



        public MessageViewHolder(View v) {

            super(v);
            mView = v;
            requestTextView = (TextView) itemView.findViewById(R.id.mbTextView);
           // requestImageView = (CircleImageView) itemView.findViewById(R.id.mbImageView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.mbImageView);
            statusicon = (ImageView) itemView.findViewById(R.id.statusicon);
            clicker= (LinearLayout)itemView.findViewById(R.id.clicker);
            clicker.setOnClickListener(this);
            context1=v.getContext();
        }

        @Override
        public void onClick(View view) {

        }
    }
    private DatabaseReference mFirebaseDatabaseReference;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    public static ArrayList<String> usernameofuser;
    public String photourlofuser;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<addToBuddy, MessageViewHolder>
            mFirebaseAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    public static String username;
    DatabaseReference reference;
ProgressBar mProgressBar;

    TextView nbtext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernameofuser = new ArrayList<String>();


        reference = FirebaseDatabase.getInstance().getReference();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_3, container, false);
        rootView.setTag(TAG);
        nbtext=(TextView)rootView.findViewById(R.id.nbtext);
username = ((Tabs)getActivity()).username;

        checkNoFriends();
        Log.i("username",username);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.mbprogressBar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mbRecyclerView);
        final LinearLayoutManager kLinearLayoutManager = new LinearLayoutManager(getActivity());
        kLinearLayoutManager.setStackFromEnd(false);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

       mRecyclerView.setLayoutManager(kLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<addToBuddy,
                MessageViewHolder>(
                addToBuddy.class,
                R.layout.buddyrow,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("users").child(username).child("friends").orderByChild("username")) {

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              addToBuddy gf, final int position) {


                usernameofuser.add(gf.getUsername());
              //  getMyPhotourl(username);
               // mProgressBar.setVisibility(ProgressBar.INVISIBLE);
               // nobuddy.setVisibility(TextView.INVISIBLE);
                viewHolder.requestTextView.setText(gf.getUsername());
     viewHolder.clicker.setTag(gf.getUsername());
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                nbtext.setVisibility(ProgressBar.INVISIBLE);

reference.child("users").child(gf.getUsername()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            if (dataSnapshot.child("status").getValue().toString().contains("true")) {
                viewHolder.statusicon.setImageResource(R.drawable.ic_online);
            } else {
                viewHolder.statusicon.setImageResource(R.drawable.ic_offline);

            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


});
                reference.child("users").child(gf.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        try{
                                                                                                        if(dataSnapshot.child("photourl").getValue()!=null){
                                                                                                            if(!dataSnapshot.child("photourl").getValue().toString().contains("null")){
                                                                                                                Uri uri = Uri.parse(dataSnapshot.child("photourl").getValue().toString());
                                                                                                                viewHolder.draweeView.setImageURI(uri);


                                                                                                            }else {
viewHolder.draweeView.setImageResource(R.drawable.userprofileicon);

                                                                                                            }
                                                                                                        }
                                                                                                    }catch (Exception e){

                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                                                                        });

                        viewHolder.clicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent chatUser1 = new Intent(getActivity(), userprofile.class);
        // Log.i("log002",search.getText().toString());
        if(view.getTag()!=null) {
            chatUser1.putExtra("usernameofsearched", view.getTag().toString());
            chatUser1.putExtra("myusername", username);
            getActivity().startActivity(chatUser1);
        }
    }
});
            }

        };
        //lol
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

        mRecyclerView.setLayoutManager(kLinearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);





        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    @Override
    public void onDestroy() {

        super.onDestroy();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
public void checkNoFriends(){

Log.i("HAA","1");
    reference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                if (!dataSnapshot.hasChild("friends")) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    nbtext.setVisibility(View.VISIBLE);
                    Log.i("HAA", "2");

                }
            }catch (Exception e){

                e.printStackTrace();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }


    });
}

   }
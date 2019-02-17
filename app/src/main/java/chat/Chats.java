package chat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.abbasi.awaz.R;
import com.abbasi.awaz.StarterApplication;
import com.bumptech.glide.Glide;
import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.SpeechRecognizerDbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.EglBase;
import org.webrtc.VideoRenderer;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import auth.phonelogin;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import pl.droidsonroids.gif.GifImageView;
import user.FriendRequests;
import user.myProfile;
import viewpager.Tabs;

import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
public class Chats extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, QBRTCSessionStateCallback, QBRTCClientVideoTracksCallbacks, QBRTCSessionEventsCallback, QBVideoChatSignalingManagerListener, View.OnTouchListener {
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
            mFirebaseAdapter;
    String messagelenght = "100";
    String mGroupId;
    String location;
    public static MediaPlayer mPlayer;
    Button playbutton;
    Button stopbuttom;
    Button nextbutton;
    Boolean host=true;
  AppRTCAudioManager audioManager;
    Button prevbutton;
    String[] songnameszz;
    String[] buddynamezzzz;
 ArrayList<String> members;
    ArrayList<String> downloading;
    String groupid;
    View rootView;
    Boolean playing = false;
    ArrayList<String> currently;
    String nextsong;
    String previoussong;
Boolean sessionkanj =false;
    QBRTCSession sessionaas;
    TextView myvideo;
    TextView buddyvideo;
    Button videobutton;
    GifImageView incoming;

    ArrayList<QBRTCVideoTrack> remote;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public EmojiconTextView messageTextView;
        public TextView messengerTextView;
        public SimpleDraweeView messengerImageView;

        LinearLayout ll;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (EmojiconTextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            ll = (LinearLayout) itemView.findViewById(R.id.messagz);
            messengerImageView = (SimpleDraweeView) itemView.findViewById(R.id.messengerImageView);

        }
    }

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private ListView Musicplaylist;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EmojiconEditText mMessageEditText;

    ArrayList<String> songnames;
    ArrayList<String> buddynames_S;
    ArrayList<Integer> videoid;
    public String buddystatus1;
    public TextView status;
    static Boolean active = false;
    File filerr;
    int counter = 0;
    SharedPreferences pref;
    SharedPreferences pref2;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    Boolean[] download;
    TextView namesofpeople;
    TextView chattext;
    EmojIconActions emojIcon;
    TextView playlisttext;
    TextView duration;
    Boolean mymessage;
    ImageView emojiImageView;
    int counterrr = 0;
    ValueEventListener listener;
    private AudioVisualization audioVisualization;
    DataSnapshot dada;
    int REQUEST_AUDIO_PERMISSION_RESULT = 1;
    GridLayout opp;
    QBRTCSurfaceView local;
    LinearLayout opplayout;
    List<Integer> opponents;
    List<String> opponents_name;
    ImageView callplace;
int payin;
    Boolean sessionstatus;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Application will not have audio on record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        active = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_layout);
        opponents = new ArrayList<Integer>();
        opponents_name = new ArrayList<String>();
        rootView = findViewById(R.id.root_view);
        Intent aijadk = getIntent();
        payin = aijadk.getIntExtra("paying",1);
members= new ArrayList<String>();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        songnames = new ArrayList<String>();
        buddynames_S = new ArrayList<String>();
        videoid = new ArrayList<Integer>();
        remote = new ArrayList<QBRTCVideoTrack>();
        downloading = new ArrayList<String>();
        buddyvideo =(TextView)findViewById(R.id.buddyvideo);
        myvideo =(TextView)findViewById(R.id.myvideo);
        incoming=(GifImageView)findViewById(R.id.incoming);
  callplace=(ImageView)findViewById(R.id.placecall);
        AudioManager mobilemode = (AudioManager)Chats.this.getSystemService(Context.AUDIO_SERVICE);
        mobilemode.setMode(AudioManager.MODE_NORMAL);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this,
                            "App requires access to audio to display background Audio Visualization", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA
                }, REQUEST_AUDIO_PERMISSION_RESULT);
            }

        } else {
            // put your code for Version < Marshmallow
        }

        audioVisualization = (AudioVisualization) findViewById(R.id.visualizer_view);
        SpeechRecognizerDbmHandler speechRecHandler = DbmHandler.Factory.newSpeechRecognizerHandler(this);
        speechRecHandler.innerRecognitionListener();
        audioVisualization.linkTo(speechRecHandler);

        Bundle bundle = getIntent().getExtras();
        buddystatus1 = "offline";
        status = (TextView) findViewById(R.id.statusa);
        chattext = (TextView) findViewById(R.id.chattext);
        playlisttext = (TextView) findViewById(R.id.playlisttext);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/chalkdust.ttf");
        status.setTypeface(face);
        chattext.setTypeface(face);
        playlisttext.setTypeface(face);

        mUsername = bundle.getString("Gotusername");
        groupid = bundle.getString("groupid");
        Log.i("groupid", groupid);
        sessionstatus = bundle.getBoolean("SessionActive");
        status.setText(bundle.getString("sessionname"));
        updateUserStatus(true);
        Musicplaylist = (ListView) findViewById(R.id.musicplaylist);
        pref = getSharedPreferences("songs" + "01", MODE_PRIVATE);//SessionID
        pref2 = getSharedPreferences("songs" + "adapter" + "01", MODE_PRIVATE);//SessionID
        playbutton = (Button) findViewById(R.id.playbutton);
        stopbuttom = (Button) findViewById(R.id.stopbutton);
        nextbutton = (Button) findViewById(R.id.nextbutton);
        prevbutton = (Button) findViewById(R.id.prevbutton);
        buttonEffect(playbutton);
        buttonEffect(stopbuttom);
        buttonEffect(prevbutton);
        buttonEffect(nextbutton);
        editor2 = pref2.edit();
        editor = pref.edit();
        mymessage = false;
        mMessageEditText = (EmojiconEditText) findViewById(R.id.messageEditText);
        emojiImageView = (ImageView) findViewById(R.id.emojibutton);
        duration = (TextView) findViewById(R.id.duration);
        emojIcon = new EmojIconActions(this, rootView, mMessageEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.keyboard, R.drawable.emoji_1f471);
        currently = new ArrayList<String>();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
mFirebaseDatabaseReference.child("session").child(groupid).child("members").child(mUsername).setValue("true");
        checkifnodeexists();

        // mFirebaseDatabaseReference.child("users").child(mUsername).child("session").child(groupid).child("status").setValue(true);
        getData();
        namesofpeople = (TextView) findViewById(R.id.namesofpeople);
        //    namesofpeople.startAnimation((Animation) AnimationUtils.loadAnimation(Chats.this,R.anim.translatw));
        checkSong(false, groupid);
        //onJOin();
        videobutton=(Button)findViewById(R.id.videobutton);

        mFirebaseDatabaseReference.child("session").child(groupid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dada = dataSnapshot;
                if(dataSnapshot.child("host").getValue()!=null){

                     host=true;
                   if(sessionaas==null) {
                       //videobutton.setVisibility(View.VISIBLE);
                       callplace.setVisibility(View.VISIBLE);
                   }
                    }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        songList();
        Intializevc();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        Musicplaylist = (ListView) findViewById(R.id.musicplaylist);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);


        getTree();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage,
                MessageViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(groupid)) {


            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                if (friendlyMessage.getText().contains(" playthissonghaskjka")) {
                    String[] nameofsong = friendlyMessage.getText().split("-songer-");
                    viewHolder.messageTextView.setText("Played: " + nameofsong[0]);
                    viewHolder.messengerImageView.setImageResource(R.drawable.note);

                }else if(friendlyMessage.getText().contains(" jhbgjhbjtyping")){
                    viewHolder.messageTextView.setText(friendlyMessage.getName()+" :typing..");
                    viewHolder.messengerImageView.setImageResource(R.drawable.userprofileicon);
                }

                else {
                    viewHolder.messageTextView.setText(friendlyMessage.getText());

                }
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() != null) {
                    Glide.with(Chats.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                } else if (!friendlyMessage.getText().contains(" playthissonghaskjka")) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(Chats.this,
                                            R.drawable.userprofileicon));
                }

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1)) || 1 == 1) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                    //mMessageRecyclerView.smoothScrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);


        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(messagelenght, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    FriendlyMessage friendlyMessage = new
                            FriendlyMessage( mUsername + " jhbgjhbjtyping",
                            mUsername,
                            null);
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(mUsername).setValue(friendlyMessage);
                    mSendButton.setEnabled(true);
                } else {

                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(mUsername).removeValue();

                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new
                        FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(groupid)
                        .push().setValue(friendlyMessage, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                  if(databaseError!=null){
                      Toast.makeText(Chats.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                  }
                    }
                });
                mMessageEditText.setText("");

            }
        });
    }

    public void updateUserStatus(final Boolean online) {
        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("users").child(mUsername.replaceAll(" ", "")).child("status");
        ref4.setValue(online);
        ref4.onDisconnect().setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void onStart() {
        active = true;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent i = new Intent(Chats.this, phonelogin.class);
            startActivity(i);
            finish();

        }

        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                Tabs tab = new Tabs();
                tab.logOut();


                return true;
            case R.id.fresh_config_menu:
                Intent intent = new Intent(this, myProfile.class);
                intent.putExtra("edit-profile", true);
                intent.putExtra("username", mUsername);
                startActivity(intent);
                return true;
            case R.id.friendrequest:
                Intent a = new Intent(this, FriendRequests.class);
                a.putExtra("names", mUsername);
                a.putExtra("usernamek", mUsername);
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


    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");
        int friendlyMessageCount = mFirebaseAdapter.getItemCount();
        int lastVisiblePosition =
                mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (message != null && chatRoomId != null) {
            //Add to messages
            if (mFirebaseAdapter.getItemCount() > 1) {
                mMessageRecyclerView.getLayoutManager().smoothScrollToPosition(mMessageRecyclerView, null, friendlyMessageCount - 1);
            }
        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */


    public void Menu(View view) {
        Log.i("Log99", "Got here");
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(Chats.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.chat, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //  Toast.makeText(Chats.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                if (item.getItemId() == R.id.s_send) {
                    selectSong();
                } else if (item.getItemId() == R.id.reactionnnn) {
                   switchaa();

                }else if(item.getItemId()==R.id.openmembersintent){
                  if(groupid!=null) {
                      openMemebers(null);
                  }
                }
                else if (item.getItemId() == R.id.leave) {
                    new AlertDialog.Builder(Chats.this)
                            .setTitle("Leave session")
                            .setMessage("Are you sure you want to leave?")
                            //If he selects accept
                            .setPositiveButton("Do it, remove me!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                 onLeave();

                                }
                            })
                            //If he declines
                            .setNegativeButton("Nope, I take it back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            //Sets happy icon on Session request
                            .setIcon(R.drawable.awaz)
                            .show();

                }else if(item.getItemId()==R.id.switchcamera)
                {
                    if(sessionaas!=null) {
                        final QBRTCCameraVideoCapturer videoCapturer = (QBRTCCameraVideoCapturer) (sessionaas.getMediaStreamManager().getVideoCapturer());

                        videoCapturer.switchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
                            @Override
                            public void onCameraSwitchDone(boolean b) {
                                Toast.makeText(Chats.this,videoCapturer.getCameraName(),Toast.LENGTH_SHORT).show();
                          }

                            @Override
                            public void onCameraSwitchError(String s) {
Toast.makeText(Chats.this,s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void showReaction(int id) {

        //final ImageView im= (ImageView)findViewById(R.id.reaction);
        //final TextView tv= (TextView) findViewById(R.id.reactionname);
        //im.animate().alpha(1).setDuration(50);
        //tv.animate().alpha(1).setDuration(50);


        new CountDownTimer(1200, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //    im.animate().alpha(0).setDuration(300);
                //      tv.animate().alpha(0).setDuration(300);
            }
        }.start();

    }

    public void selectSong() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        if (i.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(i, 1);


        } else {
            Toast.makeText(this, "Cant find activity", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null) {
            Uri selectedAudio = data.getData();

            //mPlayer = MediaPlayer.create(this, selectedAudio);
            String location = getRealPathFromURI(this, selectedAudio);
            File file = new File(location);
            Chats.this.location = getRealPathFromURI(this, selectedAudio);
            Uri file1 = Uri.fromFile(new File(location));
            //    songnames = file1.getLastPathSegment();
            //   String path =location.replace(songnames,"");
            //  if(selectfile==0){

            //  editor.putString("PATH",path);
//            }
            //          songname1.setText(songnames);
            //        sbutt1.setEnabled(false);
            //      selectfile++;


            uploadFile(location);

        }
    }
public void songEveryonecheck(String songname, ArrayList<String> members){
    if(members!=null){
        for(int x=0;x<members.size();x++){
    FirebaseDatabase.getInstance().getReference().child("session").child(groupid).child("songready").child(songname).child(members.get(x)).setValue(false);
        }
    }
}

    public void uploadFile(final String string) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");
        final Uri file = Uri.fromFile(new File(string));
        StorageReference riversRef;
        if (file.getLastPathSegment().replace("mp3", "").contains(".")) {

            riversRef = storageRef.child(mUsername + "/" + groupid + "/" + file.getLastPathSegment().replace("mp3", "").replace(".", "") + ".mp3");
        } else {
            riversRef = storageRef.child(mUsername + "/" + groupid + "/" + file.getLastPathSegment());
        }

        UploadTask uploadTask = riversRef.putFile(file);
//Notification
        final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
        mBuilderr.setSmallIcon(R.drawable.awaz);
        Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
        mBuilderr.setLargeIcon(largeIconn);
        mBuilderr.setContentTitle("Upload Song");
        mBuilderr.setContentText("Uploading: " + file.getLastPathSegment());
        mBuilderr.setAutoCancel(false);
        mBuilderr.setProgress(100, 0, false);
        mBuilderr.setOngoing(true);

        NotificationManager mNotificationManagerr = (NotificationManager) getSystemService(Chats.this.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManagerr.notify(01, mBuilderr.build());
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                //  Toast.makeText(getApplicationContext(),":( Upload failed:"+file.getLastPathSegment(),Toast.LENGTH_SHORT).show();

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(01);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //    Toast.makeText(getApplicationContext(),"Song sent :"+file.getLastPathSegment(),Toast.LENGTH_SHORT).show();

                //  check("Awaz","UPLOAD COMPLETE");

                if (file.getLastPathSegment().replace("mp3", "").contains(".")) {
                    uploadTree(file.getLastPathSegment().replace("mp3", "").replace(".", "") + ".mp3", mUsername);
                    songEveryonecheck(file.getLastPathSegment().replace("mp3", "").replace(".", "") + ".mp3",members);

                } else {
                    uploadTree(file.getLastPathSegment(), mUsername);
                    songEveryonecheck(file.getLastPathSegment(),members);

                }

                getTree();
                Log.i("log", " Upload complete:" + file.getLastPathSegment());
                editor.putString(file.getLastPathSegment(), string);
                editor.apply();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(01);
                Uri downloadUrl = taskSnapshot.getDownloadUrl();


            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long total = taskSnapshot.getTotalByteCount();
                long progress = taskSnapshot.getBytesTransferred();
                Integer i = (int) (long) total;
                Integer j = (int) (long) progress;
                final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
                mBuilderr.setSmallIcon(R.drawable.awaz);
                Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
                mBuilderr.setLargeIcon(largeIconn);
                mBuilderr.setContentTitle("Upload Song");
                mBuilderr.setContentText("Uploading: " + file.getLastPathSegment());
                mBuilderr.setAutoCancel(false);
                mBuilderr.setProgress(i, j, false);
                mBuilderr.setOngoing(true);


                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(
                        01,
                        mBuilderr.build());


            }
        });


    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void test101(final String name, final String buddy) {
        final int id = 3312;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");

        StorageReference islandRef;
        islandRef = storageRef.child(buddy + "/" + groupid + "/" + name);
        final File file = new File(getDataFolder(Chats.this, groupid), name);
        Log.i("pending", file.toString());
        Log.i("pending", downloading.toString());
        Log.i("log101", "Storagesuccess");
        downloading.add(name);
        islandRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i("log101", file.toString());
                final File file = new File(getDataFolder(Chats.this, groupid), name);
                Log.i("pending", file.toString());
                Log.i("pending", downloading.toString());
                Log.i("log101", "Storagesuccess");
                final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
                mBuilderr.setSmallIcon(R.drawable.awaz);
                Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
                mBuilderr.setLargeIcon(largeIconn);
                mBuilderr.setContentTitle("Updating Songs");
                mBuilderr.setContentText("Downloaded");
                NotificationManager mNotificationManagerr = (NotificationManager) getSystemService(Chats.this.NOTIFICATION_SERVICE);
// notificationID allows you to update the notification later on.
                mNotificationManagerr.notify(id, mBuilderr.build());
FirebaseDatabase.getInstance().getReference().child("session").child(groupid).child("songready").child(name).child(mUsername).setValue(true);
                //  currently.remove(name);
                if (songnameszz != null) {
                    for (int x = 0; x < songnameszz.length; x++) {
                        if (songnameszz[x].contains(name)) {
                            download[x] = true;
                            new LazyAdapter(Chats.this, songnameszz, buddynamezzzz, download, groupid).notifyDataSetChanged();
                            Musicplaylist.setAdapter(new LazyAdapter(Chats.this, songnameszz, buddynamezzzz, download, groupid));
                        }

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("log101", "StoragesFAILURE" + file.toString());
                downloading.remove(name);
                final File file = new File(getDataFolder(Chats.this, groupid), name);
                Log.i("pending", file.toString());
                Log.i("log101", "Storagesuccess");
                final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
                mBuilderr.setSmallIcon(R.drawable.awaz);
                Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
                mBuilderr.setLargeIcon(largeIconn);
                mBuilderr.setContentTitle("Updating Songs");
                mBuilderr.setContentText("Failed to download : " + name);


                NotificationManager mNotificationManagerr = (NotificationManager) getSystemService(Chats.this.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManagerr.notify(id, mBuilderr.build());
                //currently.remove(name);
                Musicplaylist.setAdapter(new LazyAdapter(Chats.this, songnameszz, buddynamezzzz, download, groupid));
                //  Toast.makeText(Chats.this, "FAILED", Toast.LENGTH_SHORT).show();
                //  num--;

            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                long total = taskSnapshot.getTotalByteCount();
                long progress = taskSnapshot.getBytesTransferred();
                Integer i = (int) (long) total;
                Integer j = (int) (long) progress;
                final File file = new File(getDataFolder(Chats.this, groupid), name);
                Log.i("progress", file.toString());
                Log.i("downloading", "Storagesuccess");
                final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
                mBuilderr.setSmallIcon(R.drawable.awaz);
                Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
                mBuilderr.setLargeIcon(largeIconn);
                mBuilderr.setContentTitle("Updating Songs");
                mBuilderr.setContentText("Downloading");
                mBuilderr.setAutoCancel(false);
                mBuilderr.setProgress(i, j, false);
                mBuilderr.setOngoing(true);

                NotificationManager mNotificationManagerr = (NotificationManager) getSystemService(Chats.this.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManagerr.notify(id, mBuilderr.build());


            }
        });
    }


    public File getDataFolder(Context context, String group) {
        File dataDir = null;
        dataDir = context.getDir(group, Context.MODE_PRIVATE);  //Don't do
        if (!dataDir.exists())
            dataDir.mkdirs();
        return dataDir;

    }

    public void uploadTree(String songname, String buddynamerrr) {

        mGroupId = mFirebaseDatabaseReference.push().getKey();


        mFirebaseDatabaseReference.child("session").child(groupid).push().setValue("song:" + songname + "-name-" + buddynamerrr);

    }

    public void getTree() {
        mFirebaseDatabaseReference.child("session").child(groupid).orderByValue().startAt("song:").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null)
                    Log.i("log001", dataSnapshot.toString());
                Log.i("log001", dataSnapshot.getValue().toString());
                try {
                    String[] splited = dataSnapshot.getValue().toString().split("-name-");
                    if (!songnames.contains(splited[0].replace("song:", ""))) {
                        Log.i("log3", splited[0].replace("song:", ""));//songname
                        Log.i("log", splited[1]);//buddyname
                        if (splited.length == 2) {
                            songnames.add(splited[0].replace("song:", ""));
                            buddynames_S.add(splited[1]);
                            //   adaptera = new ArrayAdapter<String>(Chats.this, R.layout.contacts_each, R.id.product_name, songnames);

                            songnameszz = new String[songnames.size()];
                            buddynamezzzz = new String[buddynames_S.size()];
                            songnameszz = songnames.toArray(songnameszz);
                            buddynamezzzz = buddynames_S.toArray(buddynamezzzz);

                            mFirebaseDatabaseReference.child("session").child(groupid).child("list").setValue(songnames.toString());
                            download = new Boolean[songnameszz.length];
                            for (int y = 0; y < songnameszz.length; y++) {

                                if (!checkifsong(songnameszz[y], Chats.this, groupid)) {
                                    Log.i("testerr", "here is the call");
                                    // get(songnameszz[y], buddynamezzzz[y], Chats.this, y);
                                    test101(songnameszz[y], buddynamezzzz[y]);
                                } else {
                                    download[y] = true;
                                }
                            }


                            Musicplaylist.setAdapter(new LazyAdapter(Chats.this, songnameszz, buddynamezzzz, download, groupid));

                            // Musicplaylist.setAdapter(adaptera);

                        }
                    }
                } catch (Exception e) {
                }
                Log.i("log", songnames.toString());//buddyname
                Log.i("log", buddynames_S.toString());//buddyname

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

    private void showNotification(String songname, boolean autocancel, boolean ongoing) {
        final NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Chats.this);
        mBuilderr.setSmallIcon(R.drawable.awaz);
        Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
        mBuilderr.setLargeIcon(largeIconn);
        mBuilderr.setContentTitle("Playing song: " + songname);
        // mBuilderr.setContentText("Uploading: "+file.getLastPathSegment());
        mBuilderr.setAutoCancel(autocancel);
        //  mBuilderr.setProgress(i,j,false);
        mBuilderr.setOngoing(ongoing);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                998,
                mBuilderr.build());
        String LOG_TAG = "d";

    }

    public void playSavedsong(View view) {
        if (dada != null) {
            try {
                playing = mPlayer.isPlaying();
            } catch (Exception e) {
            }
            if (!playing && dada.child("duration").getValue() == null) {
                setStateValue("true", groupid);
            } else if (playing) {
                setStateValue("pause", groupid);
                Log.i("check", "lol");
                if (mPlayer != null) {
                    mFirebaseDatabaseReference.child("session").child(groupid).child("duration").setValue(mPlayer.getCurrentPosition());
                }
            } else if (!playing && dada.child("state").getValue().toString().contains("pause")) {
                setStateValue("resume", groupid);
            } else {


            }
        }


    }

    public void onStop(View view) {
        stopper(groupid);
    }

    public void stopper(String group) {
        setStateValue("false", group);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("session").child(group).child("duration").removeValue();

    }

    public void play(File file, int x) {
        try {
            if (file.exists()) {
                String[] nameofsong = file.getAbsoluteFile().toString().split("/");
                ((StarterApplication) getApplicationContext()).currentlyplaying = nameofsong[nameofsong.length - 1];
                if (mPlayer != null) {
if(payin>0) {
    mPlayer.reset();
}else{}
                }
                if (x != 0) {
                    if(payin>0){
                    mPlayer = MediaPlayer.create(getBaseContext(), Uri.fromFile(file));}else{

                    }
                    try {
                        VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(Chats.this, mPlayer);

                        audioVisualization.linkTo(vizualizerHandler);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    mPlayer.seekTo(x);
                    mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            Log.i("error", Integer.toString(i) + Integer.toString(i1));
                            return false;
                        }
                    });
                    playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_play);
                    if(payin>0){
                    mPlayer.start();
                        Log.i("stopped","khuihuih");
                    }

                    setDuration(true);
                    showNotification(((StarterApplication) getApplicationContext()).currentlyplaying, false, true);
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                            new CountDownTimer(5000, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    //    im.animate().alpha(0).setDuration(300);
                                    //      tv.animate().alpha(0).setDuration(300);
                                    setStateValue("false", groupid);
                                    mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
                                    getNextPrevSong(((StarterApplication) getApplicationContext()).currentlyplaying, "next");

                                }
                            }.start();

                        }
                    });
                } else {
                    if(payin>0){
                    mPlayer = MediaPlayer.create(getBaseContext(), Uri.fromFile(file));
                        Log.i("stopped",";iou9oj");
                    }else{

                    }
                    try {
                        VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(Chats.this, mPlayer);

                        audioVisualization.linkTo(vizualizerHandler);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            Log.i("error", Integer.toString(i) + Integer.toString(i1));
                            return false;
                        }
                    });
                  if  (payin>0){
                    mPlayer.start();
                      Log.i("stopped","auiadgiu");
                  }else{

                  }
                    setDuration(true);
                    showNotification(((StarterApplication) getApplicationContext()).currentlyplaying, false, true);
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            new CountDownTimer(5000, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    //    im.animate().alpha(0).setDuration(300);
                                    //      tv.animate().alpha(0).setDuration(300);
                                    getNextPrevSong(((StarterApplication) getApplicationContext()).currentlyplaying, "next");

                                }
                            }.start();

                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Boolean checkifsong(String name, Context context, String group) {

        File file = new File(getDataFolder(context, group), name);
        if (file.exists()) {
            Log.i("Song", name);

            return true;

        } else if (downloading != null) {
            if (downloading.contains(name)) {

                Log.i("Song", name);

                return true;
            }

        }
        return false;

    }

    public void seSongValue(String songname, String buddy, String group) {

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("session").child(group).child("currentsong").setValue(songname + "-buddy-" + buddy);
        addtoChat(songname, buddy, group);


    }

    public void setStateValue(String state, String group) {

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("session").child(group).child("state").setValue(Tabs.username + "-b-name-" + state);


    }
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    public void checkState(final Boolean onjoin) {
        mFirebaseDatabaseReference.child("session").child(groupid).child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                counter++;
                if (dataSnapshot.getValue() != null) {
                    Log.i("yummsu", "aasa");
                    final String[] splited = dataSnapshot.getValue().toString().split("-b-name-");
                    if (splited.length == 2) {
                        if (splited[1].contains("true") && !playing) {
                            Log.i("new", "lol");
                            if (counterrr == 0) {
                                mFirebaseDatabaseReference.child("session").child(groupid).child("currentduration").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            Long l = (Long) dataSnapshot.getValue();
                                            play(filerr, l.intValue());
                                            playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_play);
                                            //  Toast.makeText(Chats.this,"Now playing: "+currentlyplaying,Toast.LENGTH_SHORT).show();
                                            playing = true;
                                            counterrr++;
                                            payin++;

                                        } else {
                                            play(filerr, 0);
                                            playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_play);
                                            //  Toast.makeText(Chats.this,"Now playing: "+currentlyplaying,Toast.LENGTH_SHORT).show();
                                            playing = true;
                                            counterrr++;
                                            payin++;


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                play(filerr, 0);
                                playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_play);
                                //  Toast.makeText(Chats.this,"Now playing: "+currentlyplaying,Toast.LENGTH_SHORT).show();
                                playing = true;


                            }
                        } else if (splited[1].contains("false") && playing) {

                            try {
                                duration.setText("00:00");

                                mPlayer.stop();
                                Log.i("stopped","asdkdnk");
                                setDuration(false);
                                NotificationManager notificationManager = (NotificationManager)
                                        getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(998);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            playing = false;
                            playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_pause);
                            //Toast.makeText(Chats.this,"Paused",Toast.LENGTH_SHORT).show();

                        } else if (splited[1].contains("resume")) {
                            Log.i("resume", splited[1]);
                            mFirebaseDatabaseReference.child("session").child(groupid).child("duration").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("new", "lol4");
                                    if (!onjoin) {
                                        playing = true;
                                        Long l = (Long) dataSnapshot.getValue();
                                        play(filerr, l.intValue());

                                        //          Toast.makeText(Chats.this,"Now playing: "+currentlyplaying,Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        } else if (splited[1].contains("pause")) {
                            try {
                                mPlayer.stop();
                                Log.i("stopped","kjkhk");
                              duration.setText("");
                                setDuration(false);
                            } catch (Exception e) {
                            }
                            NotificationManager notificationManager = (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(998);
                            Log.i("yum", "check");
                            playing = false;
                            playbutton.setBackgroundResource(R.drawable.cast_ic_expanded_controller_pause);
                            //    Toast.makeText(Chats.this,"Paused",Toast.LENGTH_SHORT).show();
                        }


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
public void removeavluer(String groaiojd){
    mFirebaseDatabaseReference.child("session").child(groaiojd).child("duration").removeValue();
}
    public void checkSong(final Boolean onjoin, final String group) {
        mFirebaseDatabaseReference.child("session").child(groupid).child("currentsong").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Log.i("currentsong", dataSnapshot.getValue().toString());
                    String[] splited = dataSnapshot.getValue().toString().split("-buddy-");
                    Log.i("currentsong0", splited[0]);
                    Log.i("currentsong1", splited[1]);
                    if (1 == 1) {
                        File file = new File(getDataFolder(Chats.this, group), splited[0]);
                        filerr = file;
                        Log.i("currentsongr", file.getAbsolutePath().toString());
                        if (file.exists()) {
                            Log.i("currentsongr", "true");
                            if (counter == 0) {
                                if (((StarterApplication) getApplicationContext()).currentlyplaying != null) {
                                    if (!((StarterApplication) getApplicationContext()).currentlyplaying.contains(splited[0])) {
                                        //  setStateValue("false");
                                        //mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
                                    }
                                }
                                checkState(onjoin);

                                ((StarterApplication) getApplicationContext()).currentlyplaying = splited[0];
                            } else {

                            }
                        } else {
                            Log.i("currentsongr", "false");

                        }


                        //   mPlayer = MediaPlayer.create(Chats.this, Uri.fromFile(file));
                        // mPlayer.start();

                    } else {
                        if (pref.getString(splited[0], null) != null) {
                            File file = new File(pref.getString(splited[1], null));
                            if (file.exists()) {
                                if(payin>0){
                                mPlayer = MediaPlayer.create(getBaseContext(), Uri.fromFile(file));}else{

                                }
                                try {
                                    VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(Chats.this, mPlayer);

                                    audioVisualization.linkTo(vizualizerHandler);
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                                Log.i("currentsongp", file.getAbsolutePath().toString());
                                if (counter == 0) {
                                    checkState(onjoin);
                                    ((StarterApplication) getApplicationContext()).currentlyplaying = splited[0];
                                }
                            }
                        }

                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getData() {
mFirebaseDatabaseReference.child("session").child(groupid).child("members").addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if(dataSnapshot.getValue()!=null){
            if(!members.contains(dataSnapshot.getKey().toString())){
                members.add(dataSnapshot.getKey().toString());
                String namesofa ="";
                for(int x=0;x<members.size();x++){
                    namesofa = namesofa+" "+members.get(x);
                }
                namesofpeople.setText(namesofa);
            }

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

        mFirebaseDatabaseReference.child("users").child(mUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPhotoUrl = dataSnapshot.child("photourl").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public Boolean checkifSongready() {
        Log.i("Called", "called");
        return true;

    }

    public void songList() {

        mFirebaseDatabaseReference.child("session").child(groupid).child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    String[] songnames = dataSnapshot.getValue().toString().split(",");
                    Log.i("SongLists", dataSnapshot.getValue().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getNextPrevSong(final String current, final String one) {
        mFirebaseDatabaseReference.child("session").child(groupid).child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String[] songnames = dataSnapshot.getValue().toString().split(",");
                    Log.i("SongLists", dataSnapshot.getValue().toString());
                    for (int x = 0; x < songnames.length; x++) {
                        String sog;
                        if (songnames[x].charAt(0) == ' ') {
                            sog = songnames[x].substring(1, songnames[x].length());

                        }

                        else {
                            sog = songnames[x];
                        }
                        //tumblr_lj51k5LYTE1qzb20lo1
                        //tumblr_lj51k5LYTE1qzb20lo1

                        if (songnames[x].contains(current) && !current.isEmpty() && sog.length() == current.length()) {
                            Log.i("checking", songnames[x]);
                            Log.i("checking", current);
                            try {
                                if (one.contains("next") && songnames.length >= x + 1) {
                                    nextsong = songnames[x + 1].substring(1);
                                    Log.i("nextsong", nextsong);
                                    String songnamessa;
                                    if (songnames[x + 1].substring(1).endsWith("]")) {
                                        songnamessa = songnames[x + 1].substring(1).substring(0,(songnames[x + 1].substring(1).length())-1);
                                        //songnamessa = songnamessa.substring(0, songnamessa.length() - 1);

                                    } else {
                                        songnamessa = songnames[x + 1].substring(1);
                                    }
                                    seSongValue(songnamessa, mUsername, groupid);
                                    setStateValue("false", groupid);
                                    Log.i("checking", songnamessa);
                                    mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
                                    setStateValue("true", groupid);

                                } else if (one.contains("prev")) {
                                    try {
                                        previoussong = songnames[x - 1].substring(1);
                                        Log.i("Previoussong", previoussong);
                                        seSongValue(songnames[x - 1].substring(1), mUsername, groupid);
                                        setStateValue("false", groupid);
                                        mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
                                        setStateValue("true", groupid);
                                    } catch (IndexOutOfBoundsException e) {
                                        //seSongValue(songnames[x].substring(1), mUsername);
                                        //setStateValue("false");
                                        //mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
                                        //setStateValue("true");

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onNext(View view) {
        setStateValue("false", groupid);
        mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();
        getNextPrevSong(((StarterApplication) getApplicationContext()).currentlyplaying, "next");
    }

    public void onPrev(View view) {
        setStateValue("false", groupid);
        mFirebaseDatabaseReference.child("session").child(groupid).child("duration").removeValue();

        getNextPrevSong(((StarterApplication) getApplicationContext()).currentlyplaying, "prev");
    }

    public static void buttonEffect(View button) {


        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
       try{
        audioVisualization.onResume();
        if (sessionaas!=null) {
            QBRTCVideoTrack localVideoTrack = sessionaas.getMediaStreamManager().getLocalVideoTrack();
            localVideoTrack.setEnabled(true);
            QBMediaStreamManager mediaStreamManager = sessionaas.getMediaStreamManager();

            mediaStreamManager.startVideoSource();

        }}catch (Exception e){e.printStackTrace();}
        super.onResume();

    }

    @Override
    public void onPause() {
        audioVisualization.onPause();
        if (sessionaas!=null) {
try {
    QBRTCVideoTrack localVideoTrack = sessionaas.getMediaStreamManager().getLocalVideoTrack();
    localVideoTrack.setEnabled(false);
    QBMediaStreamManager mediaStreamManager = sessionaas.getMediaStreamManager();

    mediaStreamManager.stopVideoSource();
}catch (Exception e){
    e.printStackTrace();

}
        }
        super.onPause();
    }

    public void onLeave() {
        mFirebaseDatabaseReference.child("users").child(mUsername).child("session").child(groupid).child("status").child("done");
        try {
            File dirName = Chats.this.getDir(groupid, Context.MODE_PRIVATE);  //Don't do
            if (dirName.exists()) {
                dirName.delete();

            }


            if (mPlayer != null) {

                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    Log.i("stopped","oiu8u");
                    mPlayer.release();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addtoChat(String songnameboy, String username, String group) {
        FriendlyMessage friendlyMessage = new
                FriendlyMessage(songnameboy + "-songer-" + " playthissonghaskjka",
                username,
                null);
        mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(group)
                .push().setValue(friendlyMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });


    }

    public void setDuration(boolean what) {
        if (true) {
            duration.post(mUpdateTime);
        } else {
            duration.post(mUpdateTime);
        }


    }

    private Runnable mUpdateTime = new Runnable() {
        public void run() {
            int currentDuration = 0;
            if (mPlayer.isPlaying()) {
                currentDuration = mPlayer.getCurrentPosition();
                ;
                mFirebaseDatabaseReference.child("session").child(groupid).child("currentduration").setValue(currentDuration);
                updatePlayer(currentDuration);
                duration.postDelayed(this, 1000);
            } else {
                duration.setText("00:00");
                duration.removeCallbacks(this);
            }
        }
    };

    private void updatePlayer(int currentDuration) {
        duration.setText("" + milliSecondsToTimer((long) currentDuration));
    }

    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public void checkifnodeexists() {
        mFirebaseDatabaseReference.child("session").child(groupid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    FriendlyMessage friendlyMessage = new
                            FriendlyMessage("Welcome to this session, everyone!",
                            "-Team Awaz",
                            null);
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(groupid)
                            .push().setValue(friendlyMessage);
                    mMessageEditText.setText("");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    boolean clicked=false;
    public void Intializevc() {
             opp = (GridLayout) findViewById(R.id.opponentView1);
             local = (QBRTCSurfaceView) findViewById(R.id.oppviewer);
             opplayout = (LinearLayout) findViewById(R.id.opplayout);
              // opplayout = (LinearLayout) findViewById(R.id.opplayout);
              EglBase eglContext = QBRTCClient.getInstance(Chats.this).getEglContext();
              QBRTCClient.getInstance(Chats.this).addSessionCallbacksListener(new QBRTCClientSessionCallbacks() {
              @Override
              public void onReceiveNewSession(final QBRTCSession qbrtcSession) {
                incoming.setVisibility(View.VISIBLE);
                final Map<String, String> userInfo = new HashMap<>();
                userInfo.put("key", "value");
                if (sessionaas != null) {
                    sessionaas.removeVideoTrackCallbacksListener(Chats.this);
                    sessionaas.hangUp(userInfo);
                    acceptcall(qbrtcSession);

                } else {
                    incoming.setVisibility(View.VISIBLE);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(4000);
                    clicked=false;

                    // Vibrate for 400 milliseconds
callplace.setVisibility(View.INVISIBLE);
                    incoming.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clicked=true;
                            new AlertDialog.Builder(Chats.this)
                                    .setTitle("Host started video")
                                    .setMessage("Want to start video?")
                                    //If he selects accept
                                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                    acceptcall(qbrtcSession);
                                        }
                                    })
                                    //If he declines
                                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                     qbrtcSession.rejectCall(userInfo);
                                            incoming.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    //Sets happy icon on Session request
                                    .setIcon(R.drawable.awaz)
                                    .show();

                        }
                    });

                }
            }

            public void acceptcall(QBRTCSession qbrtcSession) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("key", "value");

                qbrtcSession.acceptCall(userInfo);
                qbrtcSession.addVideoTrackCallbacksListener(Chats.this);
                call=true;
                sessionkanj = true;
                videobutton.setVisibility(View.VISIBLE);
                callplace.setVisibility(View.INVISIBLE);
                videobutton.setVisibility(View.VISIBLE);
                incoming.setVisibility(View.INVISIBLE);
                sessionaas = qbrtcSession;
                AudioManager mobilemode = (AudioManager)Chats.this.getSystemService(Context.AUDIO_SERVICE);
                mobilemode.setMode(AudioManager.MODE_NORMAL);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                audioManager = AppRTCAudioManager.create(Chats.this, new AppRTCAudioManager.OnAudioManagerStateListener() {
                    @Override
                    public void onAudioChangedState(AppRTCAudioManager.AudioDevice audioDevice) {

                        if (audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.EARPIECE) {
                            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                            audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audio.setMode(AudioManager.MODE_NORMAL);
                        }
                    }

                });
                audioManager.setOnWiredHeadsetStateListener(new AppRTCAudioManager.OnWiredHeadsetStateListener() {
                    @Override
                    public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
                        if (plugged) {
                            Toast.makeText(Chats.this, "Ear phones plugged", Toast.LENGTH_SHORT).show();
                            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);

                        } else {
                            //   Toast.makeText(Chats.this,"Ear phones unplugged",Toast.LENGTH_SHORT).show();
                            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);

                        }


                    }
                });
                audioManager.init();

            }

            @Override
            public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {

            }

            @Override
            public void onSessionStartClose(QBRTCSession qbrtcSession) {

            }

            @Override
            public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
                if(opponents_name.get(opponents.indexOf(integer))!=null){
                    Toast.makeText(Chats.this,opponents_name.get(opponents.indexOf(integer))+" is not responding to video request",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
                if(opponents_name.get(opponents.indexOf(integer))!=null){
Toast.makeText(Chats.this,opponents_name.get(opponents.indexOf(integer))+" declined video request",Toast.LENGTH_SHORT).show();
            }}

            @Override
            public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
                if(opponents_name.get(opponents.indexOf(integer))!=null){
                    Toast.makeText(Chats.this,opponents_name.get(opponents.indexOf(integer))+" joined in",Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
                if(sessionaas.getState()!= QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE){
                   callplace.setVisibility(View.VISIBLE);
                local.setVisibility(View.INVISIBLE);
                    opplayout.setVisibility(View.INVISIBLE);

                opp.setVisibility(View.INVISIBLE);
                myvideo.setVisibility(View.INVISIBLE);
                buddyvideo.setVisibility(View.INVISIBLE);
                videobutton.setVisibility(View.INVISIBLE);
            //    try{opp.release(); local.release();}catch (Exception e){e.printStackTrace();}
                hangupcall(null);
                sessionaas=null;
            }else{
                    if(opponents_name.get(opponents.indexOf(integer))!=null){
                        Toast.makeText(Chats.this,opponents_name.get(opponents.indexOf(integer))+" switched off their video",Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onSessionClosed(QBRTCSession qbrtcSession) {
                callplace.setVisibility(View.VISIBLE);
                local.setVisibility(View.INVISIBLE);
                opplayout.setVisibility(View.INVISIBLE);
                opp.setVisibility(View.INVISIBLE);
                myvideo.setVisibility(View.INVISIBLE);
                buddyvideo.setVisibility(View.INVISIBLE);
                videobutton.setVisibility(View.INVISIBLE);
                //    try{opp.release(); local.release();}catch (Exception e){e.printStackTrace();}
                sessionaas=null;
            }
        });
        QBRTCClient.getInstance(this).prepareToProcessCalls();
       try{
        QBChatService.getInstance()
                .getVideoChatWebRTCSignalingManager().addSignalingManagerListener(this);}catch (Exception e){e.printStackTrace();}
StarterApplication sd = new StarterApplication();

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


  FirebaseDatabase.getInstance().getReference().child("session").child(groupid).child("vc").child(mUsername).setValue(pref.getInt("00028",0));
        final SharedPreferences prefz = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mFirebaseDatabaseReference.child("session").child(groupid).child("vc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    int iduser = Integer.parseInt(dataSnapshot.getValue().toString());


                    Log.i("users",dataSnapshot.getValue().toString());
                    if(!opponents.contains(iduser)&&iduser!=prefz.getInt("00028",0)){
                        opponents.add(iduser);
                        opponents_name.add(dataSnapshot.getKey().toString());
                        if(sessionaas!=null&&host){
                            call(null);

                        }

                    }
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


Boolean call=false;
    public void call(View view) {

        if(!call){
            Toast.makeText(Chats.this,"Called",Toast.LENGTH_SHORT).show();
            videobutton.setVisibility(View.VISIBLE);
        //Set conference type
//There are two types of calls:
// - QB_CONFERENCE_TYPE_VIDEO - for video call;
// - QB_CONFERENCE_TYPE_AUDIO - for audio call;
        QBRTCTypes.QBConferenceType qbConferenceType = QB_CONFERENCE_TYPE_VIDEO;
if(opponents.size()!=0) {
call=true;
//Initiate opponents list

//Set user information
// User can set any string key and value in user info
// Then retrieve this data from sessions which is returned in callbacks
// and parse them as he wish
    Map<String, String> userInfo = new HashMap<>();
    userInfo.put("key", "value");

//Init session
    QBRTCSession session =
            QBRTCClient.getInstance(this).createNewSessionWithOpponents(opponents, qbConferenceType);
    sessionaas=session;

//Start call
    session.startCall(userInfo);
    session.addVideoTrackCallbacksListener(this);
callplace.setVisibility(View.INVISIBLE);
    audioManager = AppRTCAudioManager.create(Chats.this, new AppRTCAudioManager.OnAudioManagerStateListener() {
        @Override
        public void onAudioChangedState(AppRTCAudioManager.AudioDevice audioDevice) {

            if (audioManager.getSelectedAudioDevice() == AppRTCAudioManager.AudioDevice.EARPIECE) {
                audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audio.setMode(AudioManager.MODE_NORMAL);
            }
        }

    });
    audioManager.setOnWiredHeadsetStateListener(new AppRTCAudioManager.OnWiredHeadsetStateListener() {
        @Override
        public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
            if (plugged) {
                Toast.makeText(Chats.this, "Ear phones plugged", Toast.LENGTH_SHORT).show();
                audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);

            } else {
                //   Toast.makeText(Chats.this,"Ear phones unplugged",Toast.LENGTH_SHORT).show();
                audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);

            }


        }
    });
    audioManager.init();
    AudioManager mobilemode = (AudioManager)Chats.this.getSystemService(Context.AUDIO_SERVICE);
    mobilemode.setMode(AudioManager.MODE_NORMAL);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

}    }}

        public void hangupcall(View view){
        if(call){
            Toast.makeText(Chats.this,"Switched off video",Toast.LENGTH_SHORT).show();
            if(sessionaas!=null) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("key", "value");
                sessionaas.hangUp(userInfo);
                call=false;
                callplace.setVisibility(View.VISIBLE);
                local.setVisibility(View.INVISIBLE);
                opplayout.setVisibility(View.INVISIBLE);
                opp.setVisibility(View.INVISIBLE);
                myvideo.setVisibility(View.INVISIBLE);
                buddyvideo.setVisibility(View.INVISIBLE);
                videobutton.setVisibility(View.INVISIBLE);
                call=false;
               // try{opp.release(); local.release();}catch (Exception e){e.printStackTrace();}
            }
    }

    }
    @Override
    public void onLocalVideoTrackReceive(QBRTCSession qbrtcSession, final QBRTCVideoTrack qbrtcVideoTrack) {
          qbrtcVideoTrack.addRenderer(new VideoRenderer(local));
          local.setVisibility(View.VISIBLE);
        opplayout.setVisibility(View.VISIBLE);
        myvideo.setVisibility(View.VISIBLE);
      //    local.setOnTouchListener(this);

    }
    float dX;
    float dY;
    int lastAction;
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)

                break;

            default:
                return false;
        }
        return true;
    }
      int countz =0;
      int c=0;
      int r=0;
      int total;
      int column;
      int row;
    @Override
    public void onRemoteVideoTrackReceive(QBRTCSession qbrtcSession, QBRTCVideoTrack qbrtcVideoTrack, Integer integer) {
        if(!videoid.contains(integer)) {
    QBRTCSurfaceView crap = new QBRTCSurfaceView(this);
    qbrtcVideoTrack.addRenderer(new VideoRenderer(crap));
            videoid.add(integer);
            crap.setId(videoid.indexOf(integer));
            opp.setVisibility(View.VISIBLE);
            buddyvideo.setVisibility(View.VISIBLE);
            if(countz==0){
                total = opponents.size();
                column=4;
                if((total-4)>0){
                    row=total-4;

                }else {
                    row=1;
                }


                gridLao(opp);
            }

                if (c == column) {
                    c = 0;
                    r++;
                }


            GridLayout.LayoutParams param =new GridLayout.LayoutParams();

            LinearLayout parent = new LinearLayout(this);

            parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            TextView name = new TextView(this);
            name.setText(opponents_name.get(opponents.indexOf(integer)));
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            name.setTextColor(getResources().getColor(R.color.white));
            name.setTextSize(15);
            LinearLayout.LayoutParams layoutParamstext = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,(LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParamstext.setMargins(0,0,0,5);
            layoutParams.setMargins(12, 12, 12, 12);
            ViewGroup.LayoutParams params = opp.getLayoutParams();
// Changes the height and width to the specified *pixels*
            parent.addView(name,layoutParamstext);
            parent.addView(crap,layoutParams);
            param.height = opp.getMeasuredHeight()/opponents.size();
            param.width =  opp.getMeasuredHeight()/opponents.size();
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            parent.setPadding(20,20,20,20);

                parent.setLayoutParams(param);
            opp.addView(parent);

            c++;
            countz++;


            /*
            TextView name = new TextView(this);
            name.setText(opponents_name.get(opponents.indexOf(integer)));
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            name.setTextColor(getResources().getColor(R.color.white));
            name.setTextSize(15);
            LinearLayout.LayoutParams layoutParamstext = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            int finalwidth=(opp.getMeasuredHeight()/opponents.size())-(myvideo.getMeasuredHeight()+20);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(finalwidth,
                    (opp.getMeasuredHeight()/opponents.size())-(myvideo.getMeasuredHeight()+20));
            layoutParamstext.setMargins(0,0,0,5);
            layoutParams.setMargins(12, 12, 12, 12);
            ViewGroup.LayoutParams params = opp.getLayoutParams();
// Changes the height and width to the specified *pixels*

            params.width = finalwidth+100;
            opp.setLayoutParams(params);
            opp.setGravity(Gravity.CENTER_HORIZONTAL);
            opp.addView(name,layoutParamstext);
            opp.addView(crap,layoutParams);

*/
        }}
public void gridLao(GridLayout gridLayout){
    gridLayout.removeAllViews();


    gridLayout.setColumnCount(column);
    gridLayout.setRowCount(row + 1);

}


    @Override
    public void signalingCreated(QBSignaling qbSignaling, boolean b) {
        if (!b) {
            QBRTCClient.getInstance(this).addSignaling((QBWebRTCSignaling) qbSignaling);
        }
    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
try {
    //local.release();
  //  opp.release();
}catch (Exception e){}
    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {

    }

    @Override
    public void onConnectedToUser(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onDisconnectedFromUser(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onConnectionClosedForUser(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    protected void onDestroy() {
       try{
        if (sessionaas!=null) {
           QBRTCVideoTrack localVideoTrack = sessionaas.getMediaStreamManager().getLocalVideoTrack();
           localVideoTrack.setEnabled(false);
           QBMediaStreamManager mediaStreamManager = sessionaas.getMediaStreamManager();

           mediaStreamManager.stopVideoSource();
           QBRTCClient.getInstance(Chats.this).removeSessionsCallbacksListener(this);

           if (local != null){
              // local.release();
           }
           if (opp != null) {
            //   opp.release();
           }

           Map<String,String> userInfo = new HashMap<String,String>();
           userInfo.put("key", "value");

           sessionaas.hangUp(userInfo);
            videoid.clear();

       }}catch (Exception e){e.printStackTrace();}
        super.onDestroy();
    }

public void switchaa(){
    if(sessionaas!=null) {
        QBMediaStreamManager mediaStreamManager = sessionaas.getMediaStreamManager();
        if (mediaStreamManager.getLocalAudioTrack().enabled()){
            mediaStreamManager.getLocalAudioTrack().setEnabled(false);
            Toast.makeText(Chats.this,"Mute",Toast.LENGTH_SHORT).show();
        }else{
            mediaStreamManager.getLocalAudioTrack().setEnabled(true);
            Toast.makeText(Chats.this,"Unmute",Toast.LENGTH_SHORT).show();

        }
    }else{

        Toast.makeText(Chats.this,"Video is not active :/",Toast.LENGTH_SHORT).show();

    }


}
public void openMemebers(View view){
    if(groupid!=null){
       Intent ij = new Intent(Chats.this, Members.class);
        ij.putExtra("groupid",groupid);
        startActivity(ij);
    }
}
}
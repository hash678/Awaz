package viewpager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;

import com.abbasi.awaz.StarterApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;


import auth.SmsReceiver;
import auth.phonelogin;
import chat.Chats;
import drafts.FindFriends;
import drafts.SongSend;
import drafts.player;
import user.FriendRequests;
import user.SessionRequests;
import user.myProfile;


public class Tabs extends AppCompatActivity {
EditText search;
    FindFriends findFriends;
    public static  String username;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Menu notifications;
Boolean active=false;
    static int x=1;
    static  int y=1;
    static int z=1;
    static int k=1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences prefs;
    static final String APP_ID = "61008";
    static final String AUTH_KEY = "NYpChvvmqnanV87";
    static final String AUTH_SECRET = "8rPaW7kL4aetjaE";
    static final String ACCOUNT_KEY = "P21WhLg5AFcYgArWaqxq";
    public String useridvc;
    ProgressBar activesessionloading;
    TextView nosessionsactive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
sp =getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        //EnableRuntimePermission();

        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Tabs.this, phonelogin.class));
                    finish();
                }else{
closeSms();

                }


            }


        };
        if(username==null){
        username = bundle.getString("Gotusername");}
        loginbutton(null);
        Log.d("FCM", "Instance ID: " + FirebaseInstanceId.getInstance().getToken());
        DatabaseReference aaa = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("FCMID");
        aaa.setValue(FirebaseInstanceId.getInstance().getToken());

      //  registerGCM();
          // fcm();
           notif();
          // sessionNotif();
           //checkSessionStatus();
          // check4songsrecieved();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
       // tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.abc_ic_search_api_material));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sessionsicon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.buddiesicon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
spin(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        notifications=menu;
        Log.i("pending",menu.toString());
        //FriendRequests requests = new FriendRequests();//requests.check4requests(username,names,Tabs.this);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
               logOut();


                return true;
            case R.id.fresh_config_menu:
                Intent intent = new Intent(this, myProfile.class);
                intent.putExtra("edit-profile",true);
                intent.putExtra("username",username);
                startActivity(intent);

                return true;
            case R.id.friendrequest:
                Intent a = new Intent(this, FriendRequests.class);
                a.putExtra("usernamek",username);
                startActivity(a);

                return true;
            case R.id.sessionrequests:
                Intent c = new Intent(this, SessionRequests.class);
                c.putExtra("usernamek",username);
                startActivity(c);

                return true;

            default:
                return super.onOptionsItemSelected(item);


        }


    }
public void closeSms(){

    ComponentName receiver = new ComponentName(Tabs.this, SmsReceiver.class);
    PackageManager pm = this.getPackageManager();

    pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP);
    Log.i("sms","Disabled broadcst receiver");
}
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("pending",FirebaseAuth.getInstance().getCurrentUser().toString());
        if(username!=null){
            updateUserStatus(true,username);
            //	chat1.checkSessionRequest(UserList.this,username);

        }


        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

    }

    @Override
    protected void onDestroy() {
        if(username!=null){updateUserStatus(false,username);}

        ComponentName receiver = new ComponentName(Tabs.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
        QBRTCClient.getInstance(this).destroy();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(01);
        notificationManager.cancel(998);
        super.onDestroy();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }


    public void logOut(){

        FirebaseAuth.getInstance().signOut();
        editor= prefs.edit();
editor.putString("username","null");
        editor.commit();
        Log.i("TEST101","1");


        startActivity(new Intent(Tabs.this, phonelogin.class));
        finish();

    }

    public void updateUserStatus(final Boolean online, String username1)
    {

    DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("users").child(username1.replaceAll(" ","")).child("status");
   if(online==false){
       ref4.setValue(ServerValue.TIMESTAMP);
   }else{
    ref4.setValue(online);}
    ref4.onDisconnect().setValue(ServerValue.TIMESTAMP);
}


public void notif(){







    final DatabaseReference   ref = FirebaseDatabase.getInstance().getReference().child("friendrequests");

            ref.orderByChild("recusername").equalTo(username).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

if(dataSnapshot.getValue()!=null) {
    if (x < 1) {
        if(!active) {
            usenotify("usernamek",username,"null","null","null","null","Pending buddy requests!"
                    ,"You have new/pending buddy requests. You can view them by going to Options->BuddyRequests",1,FriendRequests.class);

        }
            if (active) {
            new AlertDialog.Builder(Tabs.this)
                    .setTitle("Pending buddy requests!")
                    .setMessage("You have new/pending buddy requests. You can view them by going to Options->BuddyRequests")
                    //If he selects accept
                    .setPositiveButton("Open buddy requests", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(Tabs.this, FriendRequests.class);

                            a.putExtra("usernamek", username);
                            startActivity(a);

                        }
                    })
                    //If he declines
                    .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationManager notificationManager = (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(1);
                        }
                    })
                    //Sets happy icon on Session request
                    .setIcon(R.drawable.awaz)
                    .show();

        }
    }
    x++;
    Log.i("lo001", Long.toString(dataSnapshot.getChildrenCount()));
}
            if(dataSnapshot.getValue()!=null&&x>=2){
                if(!active) {
                    usenotify("usernamek",username,"null","null","null","null","New buddy requests!"
                            ,"You have a new buddy requests!",1,FriendRequests.class);

                } if(active){
                        new AlertDialog.Builder(Tabs.this)
                                .setTitle("New buddy requests!")
                                .setMessage(dataSnapshot.child("sendusername").getValue()+" has sent you a buddy request. You can view it by going to Options->BuddyRequests")
                                //If he selects accept
                                .setPositiveButton("Open buddy requests", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Tabs.this,FriendRequests.class);

                                        a.putExtra("usernamek",username);
                                        startActivity(a);

                                    }
                                })
                                //If he declines
                                .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                })
                                //Sets happy icon on Session request
                                .setIcon(R.drawable.awaz)
                                .show();
                   }
       }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getValue()!=null) {
                if (y < 1) {
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","Pending buddy requests!"
                                ,"You have new/pending buddy requests. You can view them by going to Options->BuddyRequests",1,FriendRequests.class);

                    }  if(active){
                    new AlertDialog.Builder(Tabs.this)
                        .setTitle("Pending buddy requests!")
                        .setMessage("You have new/pending buddy requests. You can view them by going to Options->BuddyRequests")
                        //If he selects accept
                        .setPositiveButton("Open buddy requests", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent a = new Intent(Tabs.this,FriendRequests.class);

                                a.putExtra("usernamek",username);
                                startActivity(a);

                            }
                        })
                        //If he declines
                        .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                NotificationManager notificationManager = (NotificationManager)
                                        getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(1);
                            }
                        })
                        //Sets happy icon on Session request

                        .setIcon(R.drawable.awaz)
                        .show();

                }
        }
                y++;
                Log.i("lo001", Long.toString(dataSnapshot.getChildrenCount()));
                if(dataSnapshot.getValue()!=null&&y>=2){
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","New buddy request!"
                                ,"You have a new buddy requests",1,FriendRequests.class);

                    } if(active){
                        new AlertDialog.Builder(Tabs.this)
                                .setTitle("New buddy request!")
                                .setMessage(dataSnapshot.child("sendusername").getValue()+" has sent you a buddy request. You can view it by going to Options->BuddyRequests")
                                //If he selects accept
                                .setPositiveButton("Open buddy requests", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Tabs.this,FriendRequests.class);

                                        a.putExtra("usernamek",username);
                                        startActivity(a);

                                    }
                                })
                                //If he declines
                                .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                })
                                //Sets happy icon on Session request
                                .setIcon(R.drawable.awaz)
                                .show();
                    }
    }}}

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
public void openchats(View view){
    Intent k = new Intent(Tabs.this, Chats.class);
    k.putExtra("Gotusername",username);
  startActivity(k);
    Toast.makeText(getApplicationContext(),Integer.toString(sp.getInt("Numberofnot",0)),Toast.LENGTH_SHORT).show();
}
public void sessionNotif(){
    final DatabaseReference   ref2 =FirebaseDatabase.getInstance().getReference().child("sessionrequests");

    ref2.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
Log.i("Naa masla",dataSnapshot.toString());
            if(dataSnapshot.toString().contains("0="+username)||
                    dataSnapshot.toString().contains("1="+username)||
                    dataSnapshot.toString().contains("2="+username)||
                    dataSnapshot.toString().contains("3="+username)||
                    dataSnapshot.toString().contains("4="+username)||
                    dataSnapshot.toString().contains("5="+username)||
                    dataSnapshot.toString().contains("6="+username)||
                    dataSnapshot.toString().contains("7="+username)||
                    dataSnapshot.toString().contains("8="+username)||
                    dataSnapshot.toString().contains("9="+username)||
                    dataSnapshot.toString().contains("10="+username)||
                    dataSnapshot.toString().contains("11="+username)||
                    dataSnapshot.toString().contains("12="+username)||
                    dataSnapshot.toString().contains("13="+username)||
                    dataSnapshot.toString().contains("14="+username)||
                    dataSnapshot.toString().contains("15="+username)||
                    dataSnapshot.toString().contains("16="+username)||
                    dataSnapshot.toString().contains("17="+username)||
                    dataSnapshot.toString().contains("18="+username)||
                    dataSnapshot.toString().contains("19="+username)
                    ){

                if (z < 1) {
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","Pending session requests!"
                                ,"You have new/pending session requests. You can view them by going to Options->SessionRequests",2,SessionRequests.class);


                    }   if (active) {

                    new AlertDialog.Builder(Tabs.this)
                            .setTitle("Pending session requests!")
                            .setMessage("You have new/pending session requests. You can view them by going to Options->SessionRequests")
                            //If he selects accept
                            .setPositiveButton("Open session requests", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent a = new Intent(Tabs.this, SessionRequests.class);

                                    a.putExtra("usernamek", username);
                                    startActivity(a);

                                }
                            })
                            //If he declines
                            .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NotificationManager notificationManager = (NotificationManager)
                                            getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(2);
                                }
                            })
                            //Sets happy icon on Session request
                            .setIcon(R.drawable.awaz)
                            .show();

                }
            }
            z++;
                if(dataSnapshot.getValue()!=null&&z>=2){
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","Pending session requests!"
                                ,"New session request!",2,SessionRequests.class);


                    } if(active){
                        new AlertDialog.Builder(Tabs.this)
                                .setTitle("New session request!")
                                .setMessage(dataSnapshot.child("usernameofsessionuser").getValue()+" has sent you a buddy request. You can view your session requests by going to Options->Session Requests")
                                //If he selects accept
                                .setPositiveButton("Open session requests", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Tabs.this,SessionRequests.class);

                                        a.putExtra("usernamek",username);
                                        startActivity(a);

                                    }
                                })
                                .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                })
                                .setIcon(R.drawable.awaz)
                                .show();
                    }}

            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getValue()!=null) {
                if (k < 1) {
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","Pending session requests!"
                                ,"You have new/pending session requests. You can view them by going to Options->SessionRequests",2,SessionRequests.class);



                    }  if (active) {

                        new AlertDialog.Builder(Tabs.this)
                                .setTitle("Pending session requests!")
                                .setMessage("You have new/pending session requests. You can view them by going to Options->SessionRequests")
                                //If he selects accept
                                .setPositiveButton("Open session requests", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Tabs.this, SessionRequests.class);

                                        a.putExtra("usernamek", username);
                                        startActivity(a);

                                    }
                                })
                                //If he declines
                                .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(2);
                                    }
                                })
                                //Sets happy icon on Session request
                                .setIcon(R.drawable.awaz)
                                .show();

                    }
                }
                k++;
                if(dataSnapshot.getValue()!=null&&k>=2){
                    if(!active) {
                        usenotify("usernamek",username,"null","null","null","null","Pending session requests!"
                                ,"You have new/pending session requests. You can view them by going to Options->SessionRequests",2,SessionRequests.class);



                    }  if(active){
                        new AlertDialog.Builder(Tabs.this)
                                .setTitle("New session request!")
                                .setMessage(dataSnapshot.child("usernameofsessionuser").getValue()+" has sent you a buddy request. You can view your session requests by going to Options->Session Requests")
                                //If he selects accept
                                .setPositiveButton("Open session requests", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Tabs.this,SessionRequests.class);

                                        a.putExtra("usernamek",username);
                                        startActivity(a);

                                    }
                                })
                                //If he declines
                                .setNegativeButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                })
                                //Sets happy icon on Session request
                                .setIcon(R.drawable.awaz)
                                .show();
                    }}

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
    public void checkSessionStatus(){
        final DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference();
        ref4.child("users").child(username).child("sessions").orderByChild("status").equalTo("accepted").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
if(dataSnapshot.getValue()!=null){
    final String acceptorsusername= dataSnapshot.child("username").getValue().toString();
    if(!active) {
        usenotify("usernamek", username,"theirusername", acceptorsusername,"null","null",acceptorsusername + " accepted your session request!"
                ,acceptorsusername + " has accepted your session request. Would you like to continue or cancel?",3, SongSend.class);



    } if (active) {

        new AlertDialog.Builder(Tabs.this)
                .setTitle("")
                .setMessage(acceptorsusername+" has accepted your session request. Would you like to continue or cancel?")
                //If he selects accept
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(3);
                        Intent a = new Intent(Tabs.this, SongSend.class);
a.putExtra("usernamek",username);
                        a.putExtra("theirusername", acceptorsusername);
                        startActivity(a);

                    }
                })
                //If he declines
                .setNegativeButton("Never mind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(3);
                    ///Cancel the session request. Infrom the other user

                    }
                })
                //Sets happy icon on Session request
                .setIcon(R.drawable.awaz)
                .show();

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


    @Override
    protected void onStart() {
        active=true;
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        active=false;
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
public void spin(View view){
    ImageView imageview =(ImageView)findViewById(R.id.logotop);
    imageview.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));

   // imageview.setRotation(-3600);
   // imageview.setRotationY(-360);
    //imageview.animate().rotation(3600).rotationY(360).setDuration(2000);


}
    public void check4songsrecieved() {
        DatabaseReference ref3 =FirebaseDatabase.getInstance().getReference();
        ref3.child("Sessions").orderByChild("recusername").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           if(dataSnapshot.getValue()!=null){
               final String key;
               key=dataSnapshot.getKey();
final String senderusernamez;
               senderusernamez=dataSnapshot.child("username").getValue().toString();
               if(!active) {
                   usenotify("myusername", username,"key", key,"senderusernamez", senderusernamez,"Songs received"
                           ,"Your session request has been accepted and songs have been received!",4, player.class);


               }if(active){
                   new AlertDialog.Builder(Tabs.this)
                           .setTitle("Songs received")
                           .setMessage(senderusernamez+" has sent you a buddy request. You can view your session requests by going to Options->Session Requests")
                           //If he selects accept
                           .setPositiveButton("Get songs", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent k = new Intent(Tabs.this, player.class);
                                   k.putExtra("myusername",username);
                                   k.putExtra("key",key);
                                   k.putExtra("senderusernamez",senderusernamez);
                                   startActivity(k);
                               }
                           })
                           //If he declines
                           .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   NotificationManager notificationManager = (NotificationManager)
                                           getSystemService(Context.NOTIFICATION_SERVICE);
                                   notificationManager.cancel(4);
                               }
                           })
                           //Sets happy icon on Session request
                           .setIcon(R.drawable.awaz)
                           .show();
               }}
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
    public void playorpause(View view){
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference();


    }


    // starting the service to register with GCM


    @Override
    protected void onPause() {
      //  LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
updateUserStatus(false,username);
        super.onPause();

    }
    public void usenotify(String extra1key,String extra1value, String extra2key,String extra2vaule,String extra3key, String extra3value,
                          String title, String content, int number,Class classe
                          ){

        NotificationCompat.Builder mBuilderr = new NotificationCompat.Builder(Tabs.this);
        mBuilderr.setSmallIcon(R.drawable.awaz);
        Bitmap largeIconn = BitmapFactory.decodeResource(getResources(), R.drawable.awazfas);
        mBuilderr.setLargeIcon(largeIconn);
        mBuilderr.setContentTitle(title);
        mBuilderr.setContentText(content);
        mBuilderr.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setLights(0xdb77ff, 300, 900);

        Intent resultIntentt = new Intent(Tabs.this, classe);
        resultIntentt.putExtra(extra1key, extra1value);
        resultIntentt.putExtra(extra2key, extra2vaule);
        resultIntentt.putExtra(extra3key, extra3value);
        TaskStackBuilder stackBuilderr = TaskStackBuilder.create(Tabs.this);
        stackBuilderr.addParentStack(classe);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilderr.addNextIntent(resultIntentt);
        PendingIntent resultPendingIntentt = stackBuilderr.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilderr.setContentIntent(resultPendingIntentt);
        NotificationManager mNotificationManagerr = (NotificationManager) getSystemService(Tabs.this.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManagerr.notify(number, mBuilderr.build());
    }

    public void onCreateSession(View view){

               Intent creategroup = new Intent(Tabs.this, com.abbasi.awaz.ceatgroup.class);
               creategroup.putExtra("username",username);
               creategroup.putExtra("SessionActive",false);
               startActivity(creategroup);


    }
    public void createuser(View view) {

        QBUser qbUser = new QBUser();
        qbUser.setLogin(username);
        qbUser.setPassword("password");
        QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
Log.i("done","ues");
            }

            @Override
            public void onError(QBResponseException error) {
                Log.i("done",error.getMessage());
            }
        });

    }






    public void loginbutton(View view) {

        final QBUser user = new QBUser(username, "password");
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
//createuser(null);

// CREATE SESSION WITH USER
// If you use create session with user data,
// then the user will be logged in automatically

        if (1==1) {
            QBAuth.createSession(user).performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession qbSession, Bundle bundle) {
                    QBChatService chatService = QBChatService.getInstance();
                    chatService = QBChatService.getInstance();
                    // LOG IN CHAT SERVICE
                    chatService.login(user, new QBEntityCallback<QBUser>() {


                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            Log.i("error", user.getId().toString());
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = pref.edit();

                            edit.putInt("00028", user.getId());
                            edit.commit();
//00028
                            activesessionloading = (ProgressBar)findViewById(R.id.activesessionloading);
                            nosessionsactive = (TextView)findViewById(R.id.nosessionsactive);
                            nosessionsactive.setVisibility(View.INVISIBLE);
                            activesessionloading.setVisibility(View.INVISIBLE);
                            ListView Sessionlistview = (ListView) findViewById(R.id.Sessionlistview);
                            Sessionlistview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(QBResponseException errors) {
                            try {
                                Log.i("error", errors.getMessage());
                                if(errors.getMessage().contains("You have already logged in chat")){
                                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.putInt("00028", user.getId());
                                    edit.commit();
//00028
                                    activesessionloading = (ProgressBar)findViewById(R.id.activesessionloading);
                                    nosessionsactive = (TextView)findViewById(R.id.nosessionsactive);
                                    nosessionsactive.setVisibility(View.INVISIBLE);
                                    activesessionloading.setVisibility(View.INVISIBLE);
                                    ListView Sessionlistview = (ListView) findViewById(R.id.Sessionlistview);
                                    Sessionlistview.setVisibility(View.VISIBLE);
                                }else if(errors.getMessage().contains("Connection failed. Please check your internet connection")){

                                    activesessionloading.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Tabs.this,"Connection failed. Please check your internet connection",Toast.LENGTH_SHORT).show();
                                    nosessionsactive.setVisibility(View.VISIBLE);
retryconnect();



                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    });

                }


                @Override
                public void onError(QBResponseException e) {
                    Log.i("Error", e.getMessage());
                }
            });
        }else {
            QBChatService chatService = QBChatService.getInstance();
            chatService = QBChatService.getInstance();
            Log.i("error", user.getId().toString());
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit = pref.edit();

            edit.putInt("00028", user.getId());
            edit.commit();
//00028
            activesessionloading = (ProgressBar)findViewById(R.id.activesessionloading);
            nosessionsactive = (TextView)findViewById(R.id.nosessionsactive);
            ListView Sessionlistview = (ListView) findViewById(R.id.Sessionlistview);
            Sessionlistview.setVisibility(View.VISIBLE);

        }
        }
        public void retryconnect(){
            new CountDownTimer(1200000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                  loginbutton(null);
                }
            }.start();

        }

        }








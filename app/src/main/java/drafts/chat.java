package drafts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.abbasi.awaz.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by anjum on 7/15/2016.
 */
public class chat extends Activity {
   String buddy;
    String songname;
    android.content.Context mcontext;
    MainActivity ul;
    String requestSendername;
    DatabaseReference ref2;
    android.content.Context context;
    int y;
    int k=0;
    DatabaseReference  ref;
    String pendingreq[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);


        //ul = new MainActivity();
      //  buddy = getIntent().getStringExtra("message");
        mcontext =this;

        }
    public void sendSessionRequest(String yourbuddy,String username){
        //Simply adds a pending value to chat/theuseryousentrequesto/yourusername/ sending him a request
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();

        ref1.child("chat").child(yourbuddy).child("Session").child(username).setValue("pending");

    }

    public void checkSessionRequest(final android.content.Context context, final String username){

        ref2 = FirebaseDatabase.getInstance().getReference();
       //Adds a listener to monitor our own chat nodes(/chat/yourusername)
        ref2.child("chat").child(username).child("Session").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //This basically gets the name of the user who changed the value, by either sending request, accepting, or declining
                    String string = dataSnapshot.getValue().toString();
                    String[] parts = string.split("=");
                    String part1 = parts[0]; // 004
                    String value = part1;
                    requestSendername = value.substring(1);
                    //Log username of sender
                    Log.i("log002", requestSendername);
                    //Adds a listener to the senders node on chat/yourusername/session. So basically monitoring /chat/yourusername/session/sender
                    ref2.child("chat").child(username).child("Session").child(requestSendername).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                //sets the value set by the sender to the variable status
                                String status = dataSnapshot.getValue().toString();
                                Log.i("log001", status);
                                //If the sender sent pending(Using contains instead of == because == doesnt register change for some reason)
                                if (status.contains("pending")) {
                                    //Standard logging
                                    Log.i("Log701", "Works");
                                    //MainActivity mainActivity = new MainActivity();
                                    //mainActivity.check("New Session Request!",requestSendername+" has sent you a music session request!", context,classs);
                                    //Gives an alert to the user about his pending request in other words, the request he received
                                    new AlertDialog.Builder(context)
                                            .setTitle("You have a music session request!")
                                            .setMessage(requestSendername + " has sent you a music session request!")
                                            //If he selects accept
                                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Next updates two things. 1) His own pending request to accept to inform session active.
                                                    ref2.child("chat").child(username).child("Session").child(requestSendername).setValue("accept");
//2) senders Session to hasaccepted to alert him
                                                    ref2.child("chat").child(requestSendername).child("Session").child(username).setValue("hasaccepted");
                                                }
                                            })
                                            //If he declines
                                            .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Again updates two things.1) his own pending request to null as it no longer exists
                                                    ref2.child("chat").child(username).child("Session").child(requestSendername).setValue("null");
//2)Senders Session to hasdeclined to alert him
                                                    ref2.child("chat").child(requestSendername).child("Session").child(username).setValue("hasdeclined");

                                                }
                                            })
                                            //Sets happy icon on Session request
                                            .setIcon(R.drawable.not)
                                            .show();
//This checks for already sent session request by user.
                                } else if (status.contains("hasaccepted")) {
                                    // MainActivity mainActivity = new MainActivity();
                                    // mainActivity.check(requestbuddy+" has accepted your request!","Music session request accepted!",context,classs);
                                    //If the user to whom the request has been sent, has accepted it
                                    new AlertDialog.Builder(context)
                                            //It further gives the user the choice to process his earlier request, or cancel it
                                            .setTitle("Music session request accepted!")
                                            .setMessage(requestSendername + " has accepted your music session request!")
                                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
//On processing, it takes him to select song activity
                                                    //Doesnt work currently + select song needs to be properly developed with added features
                                                    Intent intent = new Intent(context, MainActivity.class);
                                                    intent.putExtra("requestbuddy", requestSendername);
                                                    startActivity(intent);

                                                    //continue's request....
                                                }
                                            })
                                            //This cancels his earlier request
                                            .setNegativeButton("Never mind", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //This again sets his own request from pending to null on the other users session node
                                                    ref2.child("chat").child(requestSendername).child("Session").child(username).setValue("cancel");
                                                    //and sets his alert variable to null since he has been alerted
                                                    ref2.child("chat").child(username).child("Session").child(requestSendername).setValue("null");

                                                    //canceled....
                                                }
                                            })
                                            //Again happy icon on having request accepted
                                            .setIcon(R.drawable.not)
                                            .show();
                                    //If the user to whom request was sent, has declined
                                } else if (status.contains("hasdeclined")) {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Music session request declined!")
                                            .setMessage(requestSendername + " has declined your music session request!")
                                            .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //sets his alert variable to null as he has been alerted and does nothing further
                                                    ref2.child("chat").child(username).child("Session").child(requestSendername).setValue("null");
                                                    //do nothing
                                                }
                                            })
                                            .setNegativeButton("Resend request", new DialogInterface.OnClickListener() {
                                                //Sends another request to the user. Same process and also sets his alert variable to null as he has been alerted
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ref2.child("chat").child(requestSendername).child("Session").child(username).setValue("pending");
                                                    ref2.child("chat").child(username).child("Session").child(requestSendername).setValue("null");


                                                }
                                            })
                                            //Sad emoticon on having request rejected
                                            .setIcon(R.drawable.sad)
                                            .show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
   /* public void checkRequestStatus(){
        Firebase ref3 = new Firebase("https://awaz-56a71.firebaseio.com/").child("chat").child(buddy).child("Session").child(ParseUser.getCurrentUser().getUsername());
        ref3.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String status = dataSnapshot.getValue().toString();
        if(status=="accept"){
            //Request Accepted
        }else if(status=="declined"){
            //Request Declined
        }else{
            //Request pending
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
});
    }*/




    public void deleteSong(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
// Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://awaz-56a71.appspot.com");

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(buddy+"/"+songname);

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Log","File deleted");
            }
        });

    }
    /*public void monitorRequestStatus(final String requestbuddy, final android.content.Context context, final Class classs){
        Firebase ref = new Firebase("https://awaz-56a71.firebaseio.com/").child(requestbuddy).child("Session").child(ParseUser.getCurrentUser().getUsername());
ref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
if(dataSnapshot.getValue()!=null){
        String status =dataSnapshot.getValue().toString();
       }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
});
  }*/
public void checkSessionStatus(final String username){


  DatabaseReference  ref2 = FirebaseDatabase.getInstance().getReference();
    ref2.child("users").child(username).child("session").orderByChild("status").equalTo("accepted").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           String acceptorusername = dataSnapshot.child("username").getValue().toString();
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
    ref2.child("users").child(username).child("session").orderByChild("status").equalTo("declined").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String declinerusername = dataSnapshot.child("username").getValue().toString();
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
}

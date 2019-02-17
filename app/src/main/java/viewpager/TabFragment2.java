package viewpager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.abbasi.awaz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import user.SessionRequests;

public class TabFragment2 extends Fragment {
    ListView Sessionlistview;
    TextView nosessionsactive;
ArrayList<String> session;
ArrayList<String> groupid;
    String[] sessionarray;
    String[] groupidarray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);
        Sessionlistview = (ListView) rootView.findViewById(R.id.Sessionlistview);
      //  nosessionsactive = (TextView) rootView.findViewById(R.id.nosessionsactive);
       session = new ArrayList<String>();
       groupid = new ArrayList<String>();
        checkSessions();
     //   nosessionsactive.setText("No active sessions");
       // Sessionlistview.setEmptyView(nosessionsactive);
        return rootView;
    }
public void checkSessions(){
    FirebaseDatabase.getInstance().getReference().child("users").child(Tabs.username).child("session").orderByChild("status").equalTo("true").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            groupid.add(dataSnapshot.getKey());
            Log.i("wtf",dataSnapshot.getKey());
            user.sendSessionRequest sc = dataSnapshot.getValue(user.sendSessionRequest.class);
            if(sc.getSessionname()!=null) {
                session.add(sc.getSessionname());
            }else{
                session.add("default");
            }
            sessionarray = new String[session.size()];
            groupidarray = new String[groupid.size()];
            sessionarray = session.toArray(sessionarray);
            groupidarray = groupid.toArray(groupidarray);
            Sessionlistview.setAdapter(new tab2adapter(getActivity(),sessionarray,groupidarray));
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

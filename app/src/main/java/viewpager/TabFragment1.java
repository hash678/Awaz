package viewpager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import com.abbasi.awaz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import user.userprofile;


//Although, for retrieving contact on the third screen, I added some codes in TabFragment3.java.

public class TabFragment1  extends Fragment {
    // Cursor Adapter for storing contacts data
    SimpleCursorAdapter adapter;
    // List View Widget
    ListView lvContacts;
    DatabaseReference reference;
    DataSnapshot data;
    String[] awazine;
    String[] awazineF;
    String[] awazine_names;
    String[] awazineF_names;
    String[] awazusers;
    String[] awazusers_num;
    List<String> numbers_awaz;
    List<String> names_awaz;
    ProgressBar pb;
    EditText contactseach;
    ArrayAdapter<String> adaptera;
    int z;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);
        lvContacts = (ListView) rootView.findViewById(R.id.fragment_3);
        pb = (ProgressBar)rootView.findViewById(R.id.loading_contacts);
        contactseach = (EditText) rootView.findViewById(R.id.contactsearch);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // OnActivityCreated work after onCreateView
        reference = FirebaseDatabase.getInstance().getReference();
        // Initialize Content Resolver object to work with content Provider
        ContentResolver cr=getActivity().getContentResolver();
       // Better();

        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        Context context = getActivity();


        adapter = new SimpleCursorAdapter(context,
                R.layout.single_item, //the layout containing TextView
                c,   //the cursor

                new String[] { ContactsContract.Contacts.DISPLAY_NAME }, //content
                new int[] { R.id.myTextView },  //to where?
                0);       //flag 0=no requery loader
       // Log.i("Tester",Integer.toString(c.getCount()));

Better();
        getContactsFirebase();
     }

public void Better() {
    ContentResolver cr = getActivity().getContentResolver();


    final Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
if(phones.getCount()!=0){
awazine = new String[phones.getCount()];
awazineF = new String[phones.getCount()];
awazine_names = new String[phones.getCount()];
awazineF_names = new String[phones.getCount()];


}
int s=0;
int k=0;
   String previous="";
        while (phones.moveToNext()) {

            //get name and number from cursor using column index
            String name = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
            awazine[k]=phoneNumber;

if(phoneNumber.replaceAll("\\s+","").replaceAll("-","").equals(previous)){

}else{
    //Log.i("TESTER", name + " " + phoneNumber.replaceAll("\\s+","").replaceAll("-",""));
    awazine[k] = phoneNumber.replaceAll("\\s+","").replaceAll("-","");
    awazine_names[k]=name;
    k++;
}
previous=phoneNumber.replaceAll("\\s+","").replaceAll("-","");

    }

    phones.close();
    names_awaz = new ArrayList<String>(Arrays.asList(awazine_names));
    names_awaz.removeAll(Arrays.asList("", null));
   numbers_awaz = new ArrayList<String>(Arrays.asList(awazine));
    numbers_awaz.removeAll(Arrays.asList("", null));
    getContactsFirebase();
   // System.out.println(names_awaz);

}

public void getContactsFirebase(){
   z = 0;
    awazusers = new String[numbers_awaz.size()];
    awazusers_num = new String[numbers_awaz.size()];
    for (int i=0; i< numbers_awaz.size();i++){

        getUsername(formatNumbers(numbers_awaz.get(i)));
    }


}
    public void getUsername(final String number1){
//Log.i("Firebase8",number1);
        reference.child("users").orderByChild("P_num").keepSynced(true);
        reference.child("users").orderByChild("P_num").equalTo(number1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
try{
                    if(dataSnapshot.getValue()!=null){
  //                      Log.i("Firebase4",dataSnapshot.getKey());
                        if(!checkIfExists(awazusers,dataSnapshot.getKey())&&!dataSnapshot.getKey().contains(Tabs.username)){
                            awazusers[z] = dataSnapshot.getKey();
                            awazusers_num[z]= number1;

    //                        Log.i("Firebase6",awazusers_num[z]);
                            final List<String> names = new ArrayList<String>(Arrays.asList(awazusers));
                            names.removeAll(Arrays.asList("", null));
                            List<String>  Phonenumbers = new ArrayList<String>(Arrays.asList(awazusers_num));
                            Phonenumbers.removeAll(Arrays.asList("", null));
                            Collections.sort(names);

                            if(names!=null){
                            // adaptera = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, names);
                                adaptera = new ArrayAdapter<String>(getActivity(), R.layout.contacts_each, R.id.product_name, names);
                            lvContacts.setAdapter(adaptera);
                                Search();
                            }
                            lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
      //                              Log.i("Firebase101",names.get(i));
                                    String nameofuser = names.get(i);
                                    //username = bundle.getString("usernameofsearched");
                                    //myusername = bundle.getString("myusername");
                                    Intent opener = new Intent(getActivity(), userprofile.class);
                                    opener.putExtra("usernameofsearched",nameofuser);
                                    opener.putExtra("myusername",Tabs.username);
                                    startActivity(opener);

                                }
                            });
                            z++;
                            if (z == 1) {
                           pb.setVisibility(View.INVISIBLE);
                                contactseach.setVisibility(View.VISIBLE);
                                contactseach.setEnabled(true);

                            }
                        }

                    }
                    else {
                    //    Log.i("Firebase7", number1);

                    }
            }catch (Exception e){

    Log.i("Problem at TabFragment1",e.getMessage());

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

    public String formatNumbers(String numberss){

        if(numberss.length()=="+923332230503".length()){
            numberss= numberss.substring(3);
        }else if(numberss.length()=="03332230503".length()){
            numberss=   numberss.substring(1);
        }else if(numberss.length()=="+13332230503".length()){
            numberss=  numberss.substring(2);
        }else if(numberss.length()=="8803332230503".length()) {
            numberss = numberss.substring(3);

        }
        return numberss;
    }

    public Boolean checkIfExists(String[] args, String word){
        if(args!=null){
        for(int i=0;i<args.length;i++){
            if(args[i]!=null){
            if(args[i].contains(word)){
                return true;
            }}
        }}

        return false;
    }

public void Search(){   if(adaptera!=null){
    contactseach.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            adaptera.getFilter().filter(charSequence);


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });


}}
}



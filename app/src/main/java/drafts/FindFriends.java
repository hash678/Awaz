package drafts;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.net.URL;

/**
 * Created by Anjum on 7/17/2016.
 */
public class FindFriends extends AppCompatActivity {

    DatabaseReference ref;
    public String nameofuser;
    public String photourlofuser;
String usernameofsearched;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void findUser(final String searcheduser, final Context context, final TextView tv, final TextView lv, final ImageView iv, final ProgressDialog progress, final RelativeLayout relLayout){

        ref = FirebaseDatabase.getInstance().getReference().child("users");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(searcheduser)){
                usernameofsearched=searcheduser;
              Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
if(dataSnapshot.child(searcheduser).child("name").getValue()!=null) {
    nameofuser = dataSnapshot.child(searcheduser).child("name").getValue().toString();
}
               tv.setText("NAME: "+nameofuser);
                lv.setText("B-NAME: "+searcheduser);
                relLayout.setAlpha(1);

                if(!dataSnapshot.child(searcheduser).child("photourl").getValue().toString().contains("null")){

                    photourlofuser=dataSnapshot.child(searcheduser).child("photourl").getValue().toString();

                    Glide.with(context)
                            .load(photourlofuser)
                            .into(iv);

                    lv.setEnabled(true);
                    tv.setEnabled(true);
                    iv.setEnabled(true);
                    progress.dismiss();

                }else{

                    iv.setImageResource(R.drawable.userprofileicon);
                    lv.setEnabled(true);
                    tv.setEnabled(true);
                    iv.setEnabled(true);
                    progress.dismiss();
                }


            }else{
progress.dismiss();
                Toast.makeText(context,"Buddy does not exist",Toast.LENGTH_SHORT).show();


            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

  /*  private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),"It seems there is a problem loading your current display photo",Toast.LENGTH_SHORT).show();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
*/}

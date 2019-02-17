package viewpager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abbasi.awaz.R;
import com.bumptech.glide.Glide;

import chat.Chats;
import user.SessionRequests;

public class tab2adapter extends BaseAdapter{
    String [] name;
    String [] groupid;
    Context context;
    ProgressDialog progress;


    private static LayoutInflater inflater=null;
    public tab2adapter(Context mainActivity, String[] prgmNameList, String[] groupidd) {
        // TODO Auto-generated constructor stub
        name=prgmNameList;
        context=mainActivity;

        groupid =groupidd;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        Button accept;
        Button reject;
        LinearLayout thumbnail;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.sessionaaa, null);
        holder.tv = (TextView)rowView.findViewById(R.id.sessionTextView);
        holder.thumbnail=(LinearLayout)rowView.findViewById(R.id.thumbnail);
        holder.tv.setText(name[position]);

        holder.thumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(context, "Please wait",
                        "Give me a minute", true);
                progress.setCancelable(false);
                Intent intent = new Intent(context,chat.Chats.class);
                intent.putExtra("Gotusername",Tabs.username);
                intent.putExtra("groupid",groupid[position]);
                intent.putExtra("sessionname",name[position]);
                AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                if(manager.isMusicActive())

                {
                  intent.putExtra("paying",0);
                    Log.i("playing","yes");

                }else{
                    intent.putExtra("paying",1);
                    Log.i("playing","no");
                }
                context.startActivity(intent);
                progress.dismiss();
            }
        });
        return rowView;


    }
}
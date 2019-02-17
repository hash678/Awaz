package user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import chat.Chats;
import viewpager.Tabs;

import static android.content.Context.MODE_PRIVATE;
import static viewpager.Tabs.username;

public class Sessionadapter extends BaseAdapter{
    String [] name;
    String [] photourl;
    String [] groupid;
    Context context;


    private static LayoutInflater inflater=null;
    public Sessionadapter(SessionRequests mainActivity, String[] prgmNameList, String[] photo, String[] groupidd) {
        // TODO Auto-generated constructor stub
        name=prgmNameList;
        context=mainActivity;
        photourl=photo;
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
        rowView = inflater.inflate(R.layout.requestrow, null);
        holder.tv = (TextView)rowView.findViewById(R.id.requestTextView);
        holder.img = (ImageView) rowView.findViewById(R.id.requestImageView);
        holder.accept = (Button) rowView.findViewById(R.id.acceptbut);
        holder.reject = (Button) rowView.findViewById(R.id.declinebut);
        holder.tv.setText(name[position]);
        Glide.with(context).load(photourl[position]).into(holder.img);

        holder.accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionRequests sc = new SessionRequests();
                sc.requestDeal(true,groupid[position]);
            }
        });
        holder.reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionRequests sc = new SessionRequests();
                sc.requestDeal(false,groupid[position]);
                notifyDataSetChanged();
            }
        });
        return rowView;


    }
}
package chat;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasi.awaz.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import chat.Chats;
import viewpager.Tabs;

import static android.content.Context.MODE_PRIVATE;
import static viewpager.Tabs.username;

public class Membersadapter extends BaseAdapter {
    ArrayList<String> name;
    Context context;
    ArrayList<String>  photourl;

    String groupid;
    private static LayoutInflater inflater = null;

    public Membersadapter(Context contexts,  ArrayList<String>  prgmNameList, ArrayList<String> prgmImages, String group) {
        // TODO Auto-generated constructor stub
       name=prgmNameList;
        context = contexts;
        photourl = prgmImages;
        groupid = group;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.size();
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

    public class Holder {
        TextView tv;
        ImageView img;
        LinearLayout thumbnail;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.memberrows, null);
        holder.tv = (TextView)rowView.findViewById(R.id.membername);
        holder.img=(ImageView)rowView.findViewById(R.id.memberphoto);
        if(name.get(position)!=null){
           holder.tv.setText(name.get(position));
        }
        try {
            if (photourl.get(position) != null) {
                if (photourl.get(position) != "null") {

                    Glide.with(context).load(photourl.get(position)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.img);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return rowView;


    }


}
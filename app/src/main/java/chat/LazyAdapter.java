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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import viewpager.Tabs;

import static android.content.Context.MODE_PRIVATE;
import static viewpager.Tabs.username;

public class LazyAdapter extends BaseAdapter{
    String [] result;
    Context context;
    String [] imageId;
Boolean[] download;
    String groupid;
    private static LayoutInflater inflater=null;
    public LazyAdapter(Chats mainActivity, String[] prgmNameList, String[] prgmImages, Boolean[] downloa, String group) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        download=downloa;
        groupid=group;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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
        TextView td;
        ImageView img;
        LinearLayout thumbnail;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.musicrow, null);
        holder.thumbnail=(LinearLayout)rowView.findViewById(R.id.thumbnail);
        holder.tv=(TextView) rowView.findViewById(R.id.titlerowmusic);
        holder.td=(TextView) rowView.findViewById(R.id.buddyrowmusic);
       holder.img=(ImageView) rowView.findViewById(R.id.downloadstatus);
        holder.tv.setText(result[position]);

final Chats chat = new Chats();
        if(!chat.checkifSongready()){
            holder.thumbnail.setBackgroundColor(Color.GRAY);
        }
       if(download[position]!=null){
       if(download[position]){
           holder.img.setImageResource(R.drawable.ic_online);



       }else{
           holder.img.setImageResource(R.drawable.ic_offline);



       }}

      holder.td.setText(imageId[position]);
        SharedPreferences pref = context.getSharedPreferences("songs"+"adapter"+"01", MODE_PRIVATE);//SessionID
        SharedPreferences pref2 = context.getSharedPreferences("songs"+"01", MODE_PRIVATE);//SessionID

        final SharedPreferences.Editor editor = pref.edit();
        editor.putString(imageId[position], result[position]);
        editor.apply();

        if(imageId[position].contains(username)&&!pref2.contains(result[position])){



      //
        }
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("longclick","yus");
                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.download, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       if(item.getItemId()==R.id.savesong){
                           if(chat.checkifsong(result[position],context,groupid)&& !imageId[position].contains(Tabs.username)){
                           try{

                               File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AwazSongs");
                               File song = new File(chat.getDataFolder(context,groupid),result[position]);

                               if (!mediaStorageDir.exists()) {
                                   if (!mediaStorageDir.mkdirs()) {

                                   }
                               }
                               File songdest = new File(mediaStorageDir,result[position]);
                               copyFile(song,songdest);
                           }catch (Exception e){

                               e.printStackTrace();

                           }}else if(chat.checkifsong(result[position],context,groupid)&& imageId[position].contains(Tabs.username)){
                               new AlertDialog.Builder(context)
                                       .setTitle("Save song")
                                       .setMessage("You sent this song, are you sure you want to save it?")
                                       //If he selects accept
                                       .setPositiveButton("I know, save it", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               try{

                                                   File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AwazSongs");
                                                   File song = new File(chat.getDataFolder(context,groupid),result[position]);

                                                   if (!mediaStorageDir.exists()) {
                                                       if (!mediaStorageDir.mkdirs()) {

                                                       }
                                                   }
                                                   File songdest = new File(mediaStorageDir,result[position]);
                                                   copyFile(song,songdest);
                                               }catch (Exception e){

                                                   e.printStackTrace();

                                               }
                                           }
                                       })
                                       //If he declines
                                       .setNegativeButton("oops forgot!", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {

                                           }
                                       })
                                       //Sets happy icon on Session request
                                       .setIcon(R.drawable.awaz)
                                       .show();

                           }else{
                               Toast.makeText(context,"Please wait for song to load",Toast.LENGTH_SHORT).show();

                           }
                       }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
if(download[position]!=null){
    if(download[position]){
        Chats chat = new Chats();
chat.stopper(groupid);
        chat.seSongValue(result[position],Tabs.username,groupid);
       chat.removeavluer(groupid);
        chat.setStateValue("true",groupid);
     //   Toast.makeText(context, "Now playing: "+result[position], Toast.LENGTH_SHORT).show();

    }else{

       // Toast.makeText(context, "Please wait for "+result[position]+" to be ready", Toast.LENGTH_SHORT).show();
    }

}else{
    //Toast.makeText(context, "Please wait for "+result[position]+" to be ready", Toast.LENGTH_SHORT).show();

}

            }
        });
        return rowView;


    }





public String getIdnumber(String name){
    for(int x=0;x<result.length;x++){
        if(result[x].contains(name)){
            return result[x];
        }

    }
return null;
}
    public static void copyFile(File src, File dst) throws IOException
    {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);

        }
        finally
        {

            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

}
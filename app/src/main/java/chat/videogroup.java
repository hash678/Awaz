package chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abbasi.awaz.R;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.util.ArrayList;

public class videogroup extends RecyclerView.Adapter<videogroup.ViewHolder> {


    ArrayList<QBRTCVideoTrack> local = new ArrayList<QBRTCVideoTrack>();
    ArrayList<Integer> mData = new ArrayList<Integer>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    QBRTCSession sesso;
    Context contexto;

    // data is passed into the constructor
    public videogroup(Context context, ArrayList<Integer> data,ArrayList<QBRTCVideoTrack> videotrack, QBRTCSession session) {
        this.mInflater = LayoutInflater.from(context);
        contexto=context;
        this.mData = data;
        sesso=session;
        local = videotrack;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.videorow, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final videogroup.ViewHolder holder, int position) {
      //  String animal = mData[position];
    //    holder.myTextView.setText(animal);

        //local.get(position).addRenderer(new VideoRenderer(holder.myTextView))

        sesso.addVideoTrackCallbacksListener(new QBRTCClientVideoTracksCallbacks() {
            @Override
            public void onLocalVideoTrackReceive(QBRTCSession qbrtcSession, QBRTCVideoTrack qbrtcVideoTrack) {

            }

            @Override
            public void onRemoteVideoTrackReceive(QBRTCSession qbrtcSession, QBRTCVideoTrack qbrtcVideoTrack, Integer integer) {
                qbrtcVideoTrack.addRenderer(new VideoRenderer(holder.myTextView));
            }
        });



    }

    // binds the data to the textview in each cell

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public QBRTCSurfaceView myTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (QBRTCSurfaceView) itemView.findViewById(R.id.info_text);
          itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Integer getItem(int id) {
        return mData.get(id);
    }
public Integer getItemIduser(int userid){

    for(int y=0;y<mData.size();y++){
        if(mData.get(y)==userid){
            return mData.get(y);
        }
    else{
        return 0;
    }

}
    return null;
}
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
package com.hfu.inmusic;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context context, ArrayList<MusicFiles> mFiles) {
        this.context = context;
        this.mFiles = mFiles;
    }



    @NonNull
    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder holder, final int position) {
        holder.filename.setText(mFiles.get(position).getTitle());
        byte[] img = getAlbumArt(mFiles.get(position).getPath());
        if(img != null){
            Glide.with(context).asBitmap()
                    .load(img)
                    .into(holder.ir);
        }else{
            Glide.with(context).load(R.drawable.logo).into(holder.ir);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,PlayerActivity.class);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView filename;
        ImageView ir;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.name);
            ir = itemView.findViewById(R.id.music_img);
        }


    }

    private byte[] getAlbumArt(String xyz){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(xyz);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}

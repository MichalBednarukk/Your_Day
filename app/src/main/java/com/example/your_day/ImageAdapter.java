package com.example.your_day;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.your_day.models.MediaModel;

import java.util.ArrayList;
;import static android.view.View.GONE;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<MediaModel> mediaModels;
    Context context;
    ItemClicked activity;

    public interface ItemClicked{
        void OnItemClicked(int index,String event);

        }


    public ImageAdapter(Context context, ArrayList<MediaModel> imagesList){
        mediaModels = imagesList;
        this.context = context;
        this.activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView btnPlay;
        ImageButton btnDeleteImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnPlay = itemView.findViewById(R.id.btnPlay);

            btnDeleteImage = itemView.findViewById(R.id.btnDeleteImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.OnItemClicked(mediaModels.indexOf((MediaModel) v.getTag()) , "VIEW");
                }
            });
        }}

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(mediaModels.get(position));
        Activity activity = (Activity) context;
        ItemClicked itemClicked = (ItemClicked) context;

        if (mediaModels.get(position).getisImage() == true) {
            Glide.with(activity)
                    .load(mediaModels.get(position).getFileUri())
                    .into(viewHolder.imageView);
            viewHolder.btnPlay.setVisibility(GONE);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.OnItemClicked(position,"VIEW");
                }
            });
            viewHolder.btnDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.OnItemClicked(position,"DELETE");
                }
            });

        }
        else {
            Glide.with(activity).load("empty")
                    .thumbnail(Glide.with(activity).load(mediaModels.get(position).getFileUri()))
                    .into(viewHolder.imageView);
            viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.OnItemClicked(position,"VIEW");
                }
            });
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.OnItemClicked(position,"VIEW");
                }
            });
            viewHolder.btnDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.OnItemClicked(position,"DELETE");
                }
            });

        }
        }

    @Override
    public int getItemCount() {
        return mediaModels.size();
    }

}

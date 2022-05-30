package com.example.proect;

import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Upload> images;

    public ImageAdapter(Context context, List<Upload> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate( R.layout.image_item, parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload upload_current = images.get(position);
        holder.tv_type.setText(upload_current.getType());
        Picasso.get().
                load(upload_current.getImageUrl()).
                fit().
                centerCrop().
                into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_type;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tv_type = itemView.findViewById(R.id.tv_type);
            imageView = itemView.findViewById(R.id.iv_upload);
        }
    }
}

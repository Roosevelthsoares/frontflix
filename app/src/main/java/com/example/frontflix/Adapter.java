package com.example.frontflix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter {

    Context context;
    ArrayList movieItems;

    public Adapter(Context context, ArrayList userItems) {
        this.context = context;
        this.movieItems = userItems;
    }

    @NonNull
//    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Utiliza o list_item.xml definido
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sugestoes, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

//    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        MovieItem item = (MovieItem) movieItems.get(position);

//        holder.image.setImageResource(item.getImgUrl()).into(holder.image);
        Picasso.get().load(item.getImgUrl()).into(holder.image);
        holder.name.setText(item.getName());
    }

//    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.imgUrl);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

}

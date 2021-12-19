package com.example.furniture.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Avatar;

import java.util.ArrayList;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHD> {

    private Context context;
    private ArrayList<Avatar> avatars;
    private OnAvatarClick onAvatarClick;

    public AvatarAdapter(Context context, ArrayList<Avatar> avatars, OnAvatarClick onAvatarClick) {
        this.context = context;
        this.avatars = avatars;
        this.onAvatarClick = onAvatarClick;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.avatar_item,parent,false);
        ViewHD viewHD=new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, int position) {
        Avatar avatar=avatars.get(position);
        holder.imageView.setImageResource(avatar.getUrl());
        holder.itemView.setOnClickListener(v -> {
            onAvatarClick.setOnClickAvatar(position);
        });
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    class ViewHD extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.itemAvatar);
        }
    }

    public interface OnAvatarClick{
        void setOnClickAvatar(int pos);
    }
}

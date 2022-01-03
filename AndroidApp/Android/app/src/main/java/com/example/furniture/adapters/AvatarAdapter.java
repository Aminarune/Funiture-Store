package com.example.furniture.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture.R;
import com.example.furniture.models.Avatar;

import java.util.List;

public class AvatarAdapter extends ArrayAdapter<Avatar> {

    public AvatarAdapter(@NonNull Context context, @NonNull List<Avatar> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Avatar avatar=getItem(position);
        if(convertView==null)
            convertView=LayoutInflater.from(getContext()).inflate(
                    R.layout.item_avatar,parent,false);
        ImageView ivCon = convertView.findViewById(R.id.icon_avatar);

        ivCon.setImageResource(avatar.getImg());

        return convertView;

    }

}

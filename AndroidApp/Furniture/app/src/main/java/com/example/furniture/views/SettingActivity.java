package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.furniture.R;
import com.example.furniture.adapters.AvatarAdapter;
import com.example.furniture.models.Avatar;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {


    private ImageView btnChangeAvatar;
    private ImageView ivAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ivAvatar = findViewById(R.id.ivAvatar);

        ArrayList<Avatar> avatars = new ArrayList<>();
        avatars.add(new Avatar(R.drawable.ic_avatar1));
        avatars.add(new Avatar(R.drawable.ic_avatar2));
        avatars.add(new Avatar(R.drawable.ic_avatar3));
        avatars.add(new Avatar(R.drawable.ic_avatar4));
        avatars.add(new Avatar(R.drawable.ic_avatar5));
        avatars.add(new Avatar(R.drawable.ic_avatar6));
        avatars.add(new Avatar(R.drawable.ic_avatar7));
        avatars.add(new Avatar(R.drawable.ic_avatar8));

        btnChangeAvatar = findViewById(R.id.ivChangeAvatarButton);
        btnChangeAvatar.setOnClickListener(v -> {
            openDialog(avatars);
        });
    }

    private void openDialog(ArrayList<Avatar> avatars) {
        Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.list_avatar_dialog);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewAvatar);
        AvatarAdapter arrayAdapter = new AvatarAdapter(SettingActivity.this, avatars, pos -> {
            ivAvatar.setImageResource(avatars.get(pos).getUrl());
            dialog.dismiss();
        });
        GridLayoutManager layoutManager = new GridLayoutManager(SettingActivity.this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(arrayAdapter);
        dialog.show();
    }

}
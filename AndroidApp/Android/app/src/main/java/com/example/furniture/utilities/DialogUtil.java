package com.example.furniture.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.furniture.R;

import pl.droidsonroids.gif.GifImageView;

public class DialogUtil {

    private Context context;

    public DialogUtil(Context context) {
        this.context = context;
    }


    public static Dialog showDialog(Context context, int sourceGif, String mess) {

        // Create an dialog builder
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_layout_alert_gif);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = dialog.findViewById(R.id.textMessAlbert);

        textView.setText(mess);
        GifImageView imageView = dialog.findViewById(R.id.ivAlbert);
        imageView.setImageResource(sourceGif);

        return dialog;

    }

}

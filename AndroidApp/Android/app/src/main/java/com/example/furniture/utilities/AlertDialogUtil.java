package com.example.furniture.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.furniture.R;

import pl.droidsonroids.gif.GifImageView;

public class AlertDialogUtil {

    private Context context;

    public AlertDialogUtil(Context context) {
        this.context = context;
    }


    public static AlertDialog showAlertDialog(Context context,int sourceGif, String mess) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(context);


        // set the custom layout
        final View customLayout
                = LayoutInflater.from(context)
                .inflate(
                        R.layout.custom_layout_alert_gif,
                        null);
        builder.setView(customLayout);

        TextView textView = customLayout.findViewById(R.id.textMessAlbert);

        textView.setText(mess);
        GifImageView imageView=customLayout.findViewById(R.id.ivAlbert);
        imageView.setImageResource(sourceGif);

        // create and show
        // the alert dialog

        AlertDialog dialog
                = builder.create();

        return dialog;

    }

}

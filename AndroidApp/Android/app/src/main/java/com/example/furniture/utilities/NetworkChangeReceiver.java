package com.example.furniture.utilities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.furniture.R;

import pl.droidsonroids.gif.GifImageView;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private Dialog dialog;
    private int sourceGif;

    public NetworkChangeReceiver(Dialog dialog, int sourceGif) {
        this.dialog = dialog;
        this.sourceGif = sourceGif;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int status = NetworkUtil.getConnectivityStatus(context);

        if (status == NetworkUtil.TYPE_NOT_CONNECTED) {

            String mess ="Please check your connection and try again";

            TextView textView = dialog.findViewById(R.id.textMessDialogExit);
            textView.setText(mess);
            GifImageView imageView = dialog.findViewById(R.id.ivDialogExit);
            imageView.setImageResource(sourceGif);

            TextView tvNo = dialog.findViewById(R.id.tvNo);
            tvNo.setText("Open setting");
            tvNo.setWidth(200);
            tvNo.setTextColor(context.getResources().getColor(R.color.black));
            tvNo.setBackgroundColor(context.getResources().getColor(R.color.white));
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dialogIntent);
                    dialog.dismiss();
                }
            });

            TextView tvYes = dialog.findViewById(R.id.tvYes);
            tvYes.setVisibility(View.GONE);


            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}

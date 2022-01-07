package com.example.furniture.utilities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private AlertDialog alertDialog;

    public NetworkChangeReceiver(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int status = NetworkUtil.getConnectivityStatus(context);

        if (status == NetworkUtil.TYPE_NOT_CONNECTED) {
            Log.d("TAG","onStop");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);

            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Open setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dialogIntent);
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            alertDialog.dismiss();
        }
    }
}

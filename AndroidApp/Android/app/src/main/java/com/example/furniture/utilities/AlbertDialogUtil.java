package com.example.furniture.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class AlbertDialogUtil {

    private Context context;

    public AlbertDialogUtil(Context context) {
        this.context = context;
    }

    public static AlertDialog showAlertDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please check your connection and try again");
        AlertDialog alertDialog = builder.create();

        return alertDialog;

    }
}

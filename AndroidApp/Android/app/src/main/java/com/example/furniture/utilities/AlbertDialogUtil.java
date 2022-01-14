package com.example.furniture.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.furniture.R;

import pl.droidsonroids.gif.GifImageView;

public class AlbertDialogUtil {

    private Context context;

    public AlbertDialogUtil(Context context) {
        this.context = context;
    }

    public static Dialog showAlertDialog(Context context) {


        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_layout_exit_dialog_gif);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;

    }

}

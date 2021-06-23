package com.miljanpeles.lib.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.miljanpeles.lib.dialog.listener.DialogYesNoButtonsClickListener;

public class DialogBuilder {

    private Context context;

    public DialogBuilder(Context context) {
        this.context = context;
    }

    public void showYesNoDialog(String title, String message, String positiveButtonTitle, String negativeButtonTitle, final DialogYesNoButtonsClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveButtonClick();
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onNegativeButtonClick();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //todo https://developer.android.com/guide/topics/ui/dialogs

}

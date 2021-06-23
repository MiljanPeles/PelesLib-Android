package com.miljanpeles.lib.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.miljanpeles.lib.R;

public class PProgressDialog {

    private final Context context;
    private final AlertDialog alertDialog;
    private final TextView ttitle;
    private final TextView tmessage;
    private ProgressBar progress;
    private final LinearLayout root;

    private boolean isHorizontal = false;

    /**
     * Kreira progress dialog sa sledecim parametrima
     * @param context - context
     * @param title - Naslov progress dialoga ili null, ako je null onda title ne postoji
     * @param text - Text progress dialoga ili null, ako je null onda text ne postoji
     */
    public PProgressDialog(Context context, String title, String text) {
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_progress_bar, null);

        ttitle = v.findViewById(R.id.title);
        tmessage = v.findViewById(R.id.message);
        root = v.findViewById(R.id.root);
        setProgressBar(false); // default nije horizontal
        //progress = v.findViewById(R.id.progress);

        if(title != null) ttitle.setText(title);
        else ttitle.setVisibility(View.GONE);

        if(text != null) tmessage.setText(text);
        else tmessage.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(false); // ne moze da se zatvori ovaj tip dialoga

        alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }

    /**
     * Promeni progressbar da bude horizontalan, koristi se uz metode setProgress, increaseProgress
     * @param maxProgress - maksimalna vrednost progressa
     */
    public void setHorizontalProgress(int maxProgress) {
        setProgressBar(true);
        isHorizontal = true;
        progress.setMax(maxProgress);
    }

    /**
     * Setuje progress na horizontalni progressbar
     * @param progressValue - vrednost
     */
    public void setProgress(int progressValue) {
        if(isHorizontal) return;
        progress.setProgress(progressValue);
        if(progress.getProgress() >= progress.getMax()) progress.setProgress(0);
    }

    /**
     * Setuje progress na horizontalni progressbar
     * @param progressValue - vrednost
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setProgress(int progressValue, boolean animate) {
        if(isHorizontal) return;
        progress.setProgress(progressValue, animate);
    }

    /**
     * Povecava progress na horizontalnom progressbaru
     * @param progressValue - vrednost
     */
    public void increaseProgress(int progressValue) {
        if(isHorizontal) return;
        progress.setProgress(progress.getProgress() + progressValue);
        if(progress.getProgress() >= progress.getMax()) progress.setProgress(0);
    }

    /**
     * Povecava progress na horizontalnom progressbaru
     * @param progressValue - vrednost
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void increaseProgress(int progressValue, boolean animate) {
        if(isHorizontal) return;
        progress.setProgress(progress.getProgress() + progressValue, animate);
        if(progress.getProgress() >= progress.getMax()) progress.setProgress(0);
    }

    /**
     * Pozadina naslova
     * @param color Color.RED, getResource().getColor(R.color.xx) ,...
     */
    public void setTitleBackgroundColor(int color) {
        ttitle.setBackgroundColor(color);
    }

    /**
     * Pozadina naslova
     * @param resource R.drawable.x
     */
    public void setTitleBackgroundResource(int resource) {
        ttitle.setBackgroundResource(resource);
    }

    /**
     * Pozadina naslova
     * @param drawable - drawable
     */
    public void setTitleBackgroundDrawable(Drawable drawable) {
        ttitle.setBackground(drawable);
    }

    /**
     * Boja teksta naslova
     * @param color - Color.RED
     */
    public void setTitleTextColor(int color) {
        ttitle.setTextColor(color);
    }

    /**
     * Boja teksta glavne poruke
     * @param color Color.RED
     */
    public void setMessageTextColor(int color) {
        tmessage.setTextColor(color);
    }

    /**
     * Prikazuje progress dialog
     */
    public void show() {
        if(((Activity)context).isFinishing()) return; // ako context vise ne vazi tj acitivty je zatvoren...

        alertDialog.show();
    }

    /**
     * Gasi progress dialog
     */
    public void dismiss() {
        if(((Activity)context).isFinishing()) return; // ako context vise ne vazi tj acitivty je zatvoren...

        alertDialog.dismiss();
    }

    private void setProgressBar(boolean isHorizontal) {
        if(progress != null) root.removeView(progress);
        progress = new ProgressBar(context, null, isHorizontal ? android.R.attr.progressBarStyleHorizontal : android.R.attr.progressBarStyleInverse);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        progress.setLayoutParams(params);
        progress.setPadding(30,25,30,35);
        root.addView(progress);
    }
}

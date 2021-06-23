package com.miljanpeles.lib.thread;

import android.app.Activity;

public abstract class PBackgroundTask {

    private final Activity activity;
    public PBackgroundTask(Activity activity) {
        this.activity = activity;
    }

    private void startBackground() {
        new Thread(new Runnable() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() { onPreExecute(); }
                });

                doInBackground();

                activity.runOnUiThread(new Runnable() {
                    public void run() { onPostExecute(); }
                });
            }
        }).start();
    }
    public void execute(){
        startBackground();
    }

    public abstract void onPreExecute();
    public abstract void doInBackground();
    public abstract void onPostExecute();
}
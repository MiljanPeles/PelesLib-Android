package com.miljanpeles.lib.file;

import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

public enum ExternalDirectory {

    ALARMS(Environment.DIRECTORY_ALARMS),
    @RequiresApi(api = Build.VERSION_CODES.Q)
    AUDIOBOOKS(Environment.DIRECTORY_AUDIOBOOKS),
    DCIM(Environment.DIRECTORY_DCIM),
    DOCUMENTS(Environment.DIRECTORY_DOCUMENTS),
    DOWNLOADS(Environment.DIRECTORY_DOWNLOADS),
    MOVIES(Environment.DIRECTORY_MOVIES),
    MUSIC(Environment.DIRECTORY_MUSIC),
    NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS),
    PICTURES(Environment.DIRECTORY_PICTURES),
    PODCASTS(Environment.DIRECTORY_PODCASTS),
    RINGTONES(Environment.DIRECTORY_RINGTONES),
    @RequiresApi(api = Build.VERSION_CODES.Q)
    SCREENSHOTS(Environment.DIRECTORY_SCREENSHOTS);

    String directory;
    ExternalDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

}

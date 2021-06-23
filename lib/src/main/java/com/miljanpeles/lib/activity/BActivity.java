package com.miljanpeles.lib.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.miljanpeles.lib.lang.LangManager;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LangManager.setAndLoadLanguage(BActivity.this, "en");
    }

}

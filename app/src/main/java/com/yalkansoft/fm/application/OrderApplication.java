package com.yalkansoft.fm.application;

import android.app.Application;
import android.content.Intent;

import com.yalkansoft.fm.MainActivity;

public class OrderApplication extends Application {

    public static OrderApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static OrderApplication getInstance(){
        return instance;
    }

    public void restartApplication() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        System.exit(0);
    }
}

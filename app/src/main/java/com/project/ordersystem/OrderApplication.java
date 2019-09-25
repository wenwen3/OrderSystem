package com.project.ordersystem;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
        startActivity(intent);

        System.exit(0);
    }
}

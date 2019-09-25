package com.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Locale;

public class ConfigurationWrapper {
    public static ConfigurationWrapper instance;

    public static ConfigurationWrapper getInstance(){
        if(instance == null){
            instance = new ConfigurationWrapper();
        }

        return instance;
    }

    public String language = "zh";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public static Context wrapConfiguration(@NonNull final Context context, @NonNull final Configuration config) {
        return context.createConfigurationContext(config);
    }


    public static Context wrapLocale(@NonNull final Context context,@NonNull final Locale locale) {
        final Resources res = context.getResources();
        final Configuration config = res.getConfiguration();
        config.setLocale(locale);
        return wrapConfiguration(context, config);
    }

    public void putLanguage(Context context,String language){
        SharedPreferences.Editor edit = context.getSharedPreferences("language_order", Activity.MODE_PRIVATE).edit();
        edit.putString("language",language);
        edit.apply();
    }

    public String getLanguage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("language_order", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "zh");
        return language;
    }
}

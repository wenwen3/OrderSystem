package com.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.project.ordersystem.OrderApplication;

import java.util.Locale;

public class LanguageUtils {
    public static LanguageUtils instance;

    public static LanguageUtils getInstance(){
        if(instance == null){
            instance = new LanguageUtils();
        }

        return instance;
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

    public void changeLanguage(Context context){
        if(LanguageUtils.getInstance().getLanguage(context).equals("zh")){
            LanguageUtils.getInstance().putLanguage(context,"ug");
        }else{
            LanguageUtils.getInstance().putLanguage(context,"zh");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OrderApplication.getInstance().restartApplication();
            }
        },500);
    }
}

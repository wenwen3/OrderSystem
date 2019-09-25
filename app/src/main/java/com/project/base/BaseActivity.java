package com.project.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.project.utils.ConfigurationWrapper;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ConfigurationWrapper.wrapLocale(newBase, getLocale(newBase)));
    }

    public Locale getLocale(Context context){
        String language = ConfigurationWrapper.getInstance().getLanguage(context);
        return new Locale(language, Locale.CHINA.getCountry());
    }
}

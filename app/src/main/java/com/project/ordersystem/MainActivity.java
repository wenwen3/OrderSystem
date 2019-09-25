package com.project.ordersystem;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.base.BaseActivity;
import com.project.utils.ConfigurationWrapper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConfigurationWrapper.getInstance().getLanguage(MainActivity.this).equals("zh")){
                    ConfigurationWrapper.getInstance().putLanguage(MainActivity.this,"ug");
                }else{
                    ConfigurationWrapper.getInstance().putLanguage(MainActivity.this,"zh");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OrderApplication.getInstance().restartApplication();
                    }
                },500);
            }
        });
    }
}

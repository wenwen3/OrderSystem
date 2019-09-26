package com.project.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.ordersystem.R;
import com.project.utils.LanguageUtils;
import com.project.utils.UiUtils;
import com.project.widget.MyLinearLayout;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseRxDataActivity extends AppCompatActivity {

    private FrameLayout contentView;
    private LinearLayout actionBar;
    private TextView titleTextView;
    private View backLayout;
    private View rightLayout;
    private ImageView rightImage;
    private View statusBar;

    private Unbinder unbinder;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.base_content_layout);
        contentView = (FrameLayout) findViewById(R.id.contentView);
        MyLinearLayout allLayout = (MyLinearLayout) findViewById(R.id.allLayout);
        if(hasEdit()) {
            allLayout.setFitsSystemWindows(true);
        }
        backLayout =  findViewById(R.id.backLayout);
        statusBar = findViewById(R.id.statusBar);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = UiUtils.getInstance().getStatusBarHeight(this);
        statusBar.setLayoutParams(layoutParams);
        statusBar.setBackgroundResource(onCreateStatusBarBg());
        rightLayout =  findViewById(R.id.rightLayout);
        rightImage = (ImageView) findViewById(R.id.rightImage);
        actionBar = (LinearLayout) findViewById(R.id.actionBar);
        titleTextView = (TextView) findViewById(R.id.title);

        if(hasToolbar()){
            if(getBarTitle() != null && !TextUtils.isEmpty(getBarTitle())){
                titleTextView.setText(getBarTitle());
            }
            actionBar.setVisibility(View.VISIBLE);
            backLayout.setVisibility(View.VISIBLE);
            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if(hasRightLogo()){
                rightLayout.setVisibility(View.VISIBLE);
                rightLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRightLogo(rightLayout);
                    }
                });
            }else{
                rightLayout.setVisibility(View.INVISIBLE);
            }
            actionBar.setBackgroundResource(onCreateStatusBarBg());
        }else{
            actionBar.setVisibility(View.GONE);
        }
        if(onCreateRightLogo() != 0){
            rightImage.setImageResource(onCreateRightLogo());
        }

        if( onCreateContentView() != null ){
            View inflate = LayoutInflater.from(this).inflate(onCreateContentView(), null);
            contentView.addView(inflate);
            unbinder = ButterKnife.bind(this);
            onActivityPrepared(inflate);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected Integer onCreateStatusBarBg(){
        return R.color.colorPrimary;
    }

    protected boolean hasEdit(){
        return true;
    }

    protected void showBackLogo(){
        if(hasToolbar() && backLayout != null ) {
            backLayout.setVisibility(View.VISIBLE);
        }
    }

    protected void hideBackLogo(){
        if(hasToolbar() && backLayout != null ) {
            backLayout.setVisibility(View.INVISIBLE);
        }
    }

    public View getAppBar(){
        return actionBar;
    }

    public void setActionBarBackgroundRes(int resId){
        actionBar.setBackgroundResource(resId);
        statusBar.setBackgroundResource(resId);
    }

    protected int onCreateRightLogo(){
        return 0;
    }

    protected abstract void onActivityPrepared(View view);

    protected abstract Integer onCreateContentView();

    protected void onClickRightLogo(View view){

    }

    protected abstract String getBarTitle();

    protected abstract boolean hasToolbar();

    protected boolean hasRightLogo(){
        return true;
    }

    protected void setTitle(String title){
        if(hasToolbar()){
            titleTextView.setText(title);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtils.wrapLocale(newBase, getLocale(newBase)));
    }

    public Locale getLocale(Context context){
        String language = LanguageUtils.getInstance().getLanguage(context);
        return new Locale(language, Locale.CHINA.getCountry());
    }

}

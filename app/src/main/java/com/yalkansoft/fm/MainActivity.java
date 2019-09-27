package com.yalkansoft.fm;

import android.app.AlertDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;
import com.yalkansoft.utils.LanguageUtils;
import com.yalkansoft.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseRxDataActivity {

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.nameImage)
    ImageView nameImage;

    @BindView(R.id.login)
    ImageView login;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.editPassword)
    EditText editPassword;

    private View rootView;
    @Override
    protected void onActivityPrepared(View view) {
        rootView = view;
    }

    @Override
    protected Integer onCreateContentView() {
        return R.layout.activity_main;
    }

    @OnClick
    public void login(View view){
        HallActivity.showActivity(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick
    public void setting(View view){
        showSettingDialog();
    }

    @Override
    public void onBackPressed() {
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }else{
            super.onBackPressed();
        }
    }

    private AlertDialog dialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showSettingDialog(){
        if(isDestroyed()){
            return;
        }
        dialog = new AlertDialog.Builder(this).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.setting_popup_layout, null);
        dialog.setView(dialogView);
        TextView currentLanguage = dialogView.findViewById(R.id.currentLanguage);
        if(LanguageUtils.getInstance().getLanguage(MainActivity.this).equals("zh")){
            currentLanguage.setText(getResources().getString(R.string.Current_Language_Chinese));
        }else{
            currentLanguage.setText(getResources().getString(R.string.Current_Language_Uyghur));
        }
        TextView changeLanguage = dialogView.findViewById(R.id.changeLanguage);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageUtils.getInstance().changeLanguage(MainActivity.this);
            }
        });
        dialogView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = UiUtils.getInstance().getScreenWidth(this)*10/11;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected String getBarTitle() {
        return null;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }
}

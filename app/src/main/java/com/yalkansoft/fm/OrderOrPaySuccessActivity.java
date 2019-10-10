package com.yalkansoft.fm;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 点餐、结账成功后的页面
 * */
public class OrderOrPaySuccessActivity extends BaseRxDataActivity {

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.message)
    TextView message;

    @Override
    protected void onActivityPrepared(View view) {
        getData();
    }

    @OnClick(R.id.backToHome)
    public void backToHome(View view){
        finish();
    }

    private void getData() {
        String title = getIntent().getStringExtra("title");
        String payType = getIntent().getStringExtra("payType");
        boolean isOrder = getIntent().getBooleanExtra("isOrder", false);

        if(isOrder){
            String string = getResources().getString(R.string.order_success_message);
            String msg = "<font color='#FF6565'>"+title+"</font>";
            final String format = String.format(string,msg);
            message.setText(Html.fromHtml(format));
            titleText.setText(getResources().getString(R.string.order_success_success));
        }else{
            titleText.setText(getResources().getString(R.string.pay_success_success));
            String string = getResources().getString(R.string.pay_success_message);
            String msg = "<font color='#FF6565'>"+title+"</font>";
            String payTypeString = "<font color='#FF6565'>"+payType+"</font>";
            final String format = String.format(string,msg,payTypeString);
            message.setText(Html.fromHtml(format));
        }

    }

    public static void showActivity(Context context,String title,boolean isOrder,String payType){
        Intent intent = new Intent(context, OrderOrPaySuccessActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("isOrder",isOrder);
        intent.putExtra("payType",payType);
        context.startActivity(intent);
    }

    @Override
    protected Integer onCreateContentView() {
        return R.layout.order_or_pay_success_layout;
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

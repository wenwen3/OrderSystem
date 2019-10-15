package com.yalkansoft.fm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;
import com.yalkansoft.bean.OrderFood;
import com.yalkansoft.utils.LanguageUtils;
import com.yalkansoft.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 弹出的结账清单DialogActivity
 * */
public class PayListActivity extends  Activity {

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.totalCount)
    TextView totalCount;

    @BindView(R.id.giveEdit)
    TextView giveEdit;

    @BindView(R.id.payEdit)
    EditText payEdit;

    @BindView(R.id.allLayout)
    LinearLayout allLayout;

    @BindView(R.id.rootView)
    FrameLayout rootView;
    private PayListAdapter mAdapter;
    private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_list_layout);
        unbinder = ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        ViewGroup.LayoutParams layoutParams = allLayout.getLayoutParams();
        layoutParams.height = UiUtils.getInstance().getScreenHeight(this)*10/13;
        allLayout.setLayoutParams(layoutParams);

        initListAdapter();

        getIntentData();

        getData();

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });
        payEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = payEdit.getText().toString().trim();
                if(TextUtils.isEmpty(trim)){
                    giveEdit.setText("");
                    return;
                }
                long pay = Long.parseLong(trim);
                if(pay < total){
                    giveEdit.setText("");
                    return;
                }
                giveEdit.setText("¥"+(pay - total));
            }
        });
    }

    @OnClick(R.id.backDialogActivity)
    public void backDialogActivity(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static void showActivity(Context context,String title){
        Intent intent = new Intent(context,PayListActivity.class);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtils.wrapLocale(newBase, getLocale(newBase)));
    }

    public Locale getLocale(Context context){
        String language = LanguageUtils.getInstance().getLanguage(context);
        return new Locale(language, Locale.CHINA.getCountry());
    }

    private String title;
    private void getIntentData() {
        title = getIntent().getStringExtra("title");
        titleText.setText(title);
    }

    private void initListAdapter() {
        mAdapter = new PayListAdapter();
        listView.setAdapter(mAdapter);
    }

    /** 0 默认（现金）
     *  1 支付宝
     *  2 微信*/
    private int pay_type = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.pay_ali)
    public void pay_ali(View view){
        pay_type = 1;
        showCodeDialog();
    }
    @OnClick(R.id.pay)
    public void pay(View view){
        if(pay_type == 0) {
            if(TextUtils.isEmpty(payEdit.getText().toString().trim())){
                Toast.makeText(this, getResources().getString(R.string.please_write_pay_money), Toast.LENGTH_SHORT).show();
                return;
            }
            OrderOrPaySuccessActivity.showActivity(this, title, false, getResources().getString(R.string.cash_payment));
        }else if(pay_type == 1) {
            OrderOrPaySuccessActivity.showActivity(this, title, false, getResources().getString(R.string.alipay_payment));
        }else if(pay_type == 2) {
            OrderOrPaySuccessActivity.showActivity(this, title, false, getResources().getString(R.string.weChat_payment));
        }

        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.pay_wechat)
    public void pay_wechat(View view){
        pay_type = 2;
        showCodeDialog();
    }
    private void getData() {
        mDatas.clear();
        for (int i = 0; i < 20; i++) {
            OrderFood orderFood = new OrderFood();
            orderFood.setId(i+"");
            orderFood.setPrice(new Random().nextInt(100));
            orderFood.setNumber(new Random().nextInt(10));
            orderFood.setName("第"+(i+1)+"道菜");
            orderFood.setType(OrderFood.TYPE_KAOROU);
            mDatas.add(orderFood);
        }
        mAdapter.notifyDataSetChanged();

        setDataTotal();
    }


    private int total;
    /**
     * 算出合计多少钱
     * */
    public void setDataTotal(){
        total = 0;
        if(mDatas != null && mDatas.size() != 0){
            for (int i = 0; i < mDatas.size(); i++) {
                total+=mDatas.get(i).getNumber()*mDatas.get(i).getPrice();
            }
            String string = getResources().getString(R.string.total);
            final String format = String.format(string, total+"");
            totalCount.setText(format);
        }
    }

    private AlertDialog dialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showCodeDialog(){
        if(isDestroyed()){
            return;
        }
        dialog = new AlertDialog.Builder(this).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.erweima_layout, null);
        dialog.setView(dialogView);
        dialogView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null ){
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = UiUtils.getInstance().getScreenWidth(this)*10/20;
        params.height = UiUtils.getInstance().getScreenWidth(this)*10/20;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private List<OrderFood> mDatas = new ArrayList<>();

    /***
     * 界面gridView适配器
     * */
    public class PayListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public OrderFood getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null ){
                convertView = LayoutInflater.from(PayListActivity.this).inflate(R.layout.order_confirm_list_item_layout,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.name);
                holder.price = convertView.findViewById(R.id.price);
                holder.number = convertView.findViewById(R.id.number);
                holder.addOrder = convertView.findViewById(R.id.addOrder);
                holder.cutOrder = convertView.findViewById(R.id.cutOrder);
                holder.edit = convertView.findViewById(R.id.edit);
                holder.lineView = convertView.findViewById(R.id.lineView);
                holder.remarks = convertView.findViewById(R.id.remarks);
                convertView.setTag(holder);
            }
            final OrderFood item = getItem(position);
            if(item != null ) {
                final ViewHolder holder = (ViewHolder) convertView.getTag();
                if(item.getType() == OrderFood.TYPE_HUOGUO){
                    holder.name.setText(getResources().getString(R.string.hot_pot)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_KAOROU){
                    holder.name.setText(getResources().getString(R.string.barbecued_meat)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_LIANGCAI){
                    holder.name.setText(getResources().getString(R.string.herbs)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_RECAI){
                    holder.name.setText(getResources().getString(R.string.hot_dishes)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_YINGLIAO){
                    holder.name.setText(getResources().getString(R.string.beverages)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_ZHUAFAN){
                    holder.name.setText(getResources().getString(R.string.catching_meals)+item.getName());
                }else if(item.getType() == OrderFood.TYPE_ZHUSHI){
                    holder.name.setText(getResources().getString(R.string.staple_food)+item.getName());
                }
                holder.remarks.setVisibility(View.GONE);
                holder.lineView.setVisibility(View.GONE);
                holder.addOrder.setVisibility(View.GONE);
                holder.cutOrder.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.number.setVisibility(View.GONE);
                holder.price.setText(item.getNumber()+"x"+item.getPrice()+"="+(item.getNumber() * item.getPrice()));

            }
            return convertView;
        }

        private class ViewHolder {
            private TextView name;
            private TextView price;
            private TextView number;
            private TextView remarks;
            private View lineView;
            private ImageView addOrder;
            private ImageView cutOrder;
            private ImageView edit;
        }
    }


}

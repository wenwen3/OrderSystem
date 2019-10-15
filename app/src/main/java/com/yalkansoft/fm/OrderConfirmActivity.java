package com.yalkansoft.fm;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;
import com.yalkansoft.bean.OrderFood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 点餐确认界面，类似购物车、
 * */
public class OrderConfirmActivity extends BaseRxDataActivity {

    private String title;

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.totalCount)
    TextView totalCount;

    @BindView(R.id.sure)
    TextView sure;

    @BindView(R.id.toBack)
    TextView toBack;
    private OrderConfirmListAdapter mAdapter;

    @Override
    protected void onActivityPrepared(View view) {
        getData();

        initListAdapter();

        toBack.setSelected(true);
        sure.setSelected(true);
    }

    private void initListAdapter() {
        mAdapter = new OrderConfirmListAdapter();
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.back)
    public void backLayout(View view){
        finish();
    }

    /**下单*/
    @OnClick(R.id.sureLayout)
    public void sureLayout(View view){
        ArrayList<OrderFood> removeData = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if(mDatas.get(i).getNumber() == 0){
                removeData.add(mDatas.get(i));
            }
        }
        mDatas.removeAll(removeData);

        OrderOrPaySuccessActivity.showActivity(this,title,true,"");
        Intent intent = new Intent();
        intent.putExtra("isFinish",true);
        setResult(12,intent);
        finish();
    }
    long total = 0;
    private void getData() {
        title = getIntent().getStringExtra("title");
        mDatas = (ArrayList<OrderFood>) getIntent().getSerializableExtra("datas");

        titleText.setText(title);

        setDataTotal();
    }

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

    private ArrayList<OrderFood> mDatas = new ArrayList<>();

    public static void showActivity(Activity activity, ArrayList<OrderFood> datas,String title){
        Intent intent = new Intent(activity, OrderConfirmActivity.class);
        intent.putExtra("datas", datas);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent,11);
    }

    @Override
    protected Integer onCreateContentView() {
        return R.layout.order_confirm_layout;
    }

    @Override
    protected String getBarTitle() {
        return "确认下单";
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }


    /***
     * 界面gridView适配器
     * */
    public class OrderConfirmListAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(OrderConfirmActivity.this).inflate(R.layout.order_confirm_list_item_layout,parent,false);
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
                if(item.getNumber() == 0){
                    holder.remarks.setVisibility(View.GONE);
                    holder.lineView.setVisibility(View.GONE);
                    holder.remarks.setText("");
                }else{
                    if(item.getRemarks() != null && !TextUtils.isEmpty(item.getRemarks())){
                        String string = getResources().getString(R.string.remarks);
                        final String remark = String.format(string, item.getRemarks());
                        holder.remarks.setText(remark);
                        holder.remarks.setVisibility(View.VISIBLE);
                        holder.lineView.setVisibility(View.VISIBLE);
                    }else{
                        holder.remarks.setVisibility(View.GONE);
                        holder.lineView.setVisibility(View.GONE);
                        holder.remarks.setText("");
                    }
                }

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getNumber() == 0){
                            Toast.makeText(OrderConfirmActivity.this, "菜品至少数量为1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(OrderConfirmActivity.this).builder()
                                .setTitle("请输入备注")
                                .setEditText("");
                        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String remarks = myAlertInputDialog.getResult();
                                item.setRemarks(remarks);
                                myAlertInputDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myAlertInputDialog.dismiss();
                            }
                        });
                        myAlertInputDialog.show();
                    }
                });
                holder.number.setText(""+item.getNumber());
                holder.price.setText(item.getNumber()+"x"+item.getPrice()+"="+(item.getNumber() * item.getPrice()));
                holder.cutOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getNumber() == 0){
                            return;
                        }
                        item.setNumber(item.getNumber()-1);
                        notifyDataSetChanged();
                        setDataTotal();
                    }
                });
                holder.addOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setNumber(item.getNumber() + 1);
                        notifyDataSetChanged();
                        setDataTotal();
                    }
                });
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

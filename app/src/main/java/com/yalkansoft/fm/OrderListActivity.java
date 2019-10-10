package com.yalkansoft.fm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;
import com.yalkansoft.bean.HallResultBean;
import com.yalkansoft.bean.OrderFood;
import com.yalkansoft.widget.SpinerPopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 点餐的菜单列表
 * */
public class OrderListActivity extends BaseRxDataActivity {

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.dishes)
    TextView dishes;

    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.hallImage)
    ImageView hallImage;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.shopCart)
    ImageView shopCart;

    @BindView(R.id.goneLayout)
    View goneLayout;

    private Map<String,OrderFood> mCheckMaps = new LinkedHashMap<>();

    private List<OrderFood> mDatas = new ArrayList<>();

    private OrderListAdapter orderListAdapter;
    private String customer;

    @Override
    protected void onActivityPrepared(View view) {

        initDriver();

        initAdapter();

        getData();
        customer = getIntent().getStringExtra("customer");
        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        price.setSelected(true);
        number.setSelected(true);
    }

    public static void showActivity(Context context, String customer){
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("customer",customer);
        context.startActivity(intent);
    }

    private SpinerPopWindow stringSpinerPopWindow;
    @OnClick(R.id.dishesLayout)
    public void dishesLayout(View view){
        hallImage.setImageResource(R.drawable.icon_bottom_cut);
        stringSpinerPopWindow = new SpinerPopWindow<>(this, mTypeLists, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dishes.setText(mTypeLists.get(position));
                stringSpinerPopWindow.dismiss();
                hallImage.setImageResource(R.drawable.icon_top_cut);
                getData();
            }
        });
        stringSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                goneLayout.setVisibility(View.GONE);
                hallImage.setImageResource(R.drawable.icon_top_cut);
            }
        });
        goneLayout.setVisibility(View.VISIBLE);
        stringSpinerPopWindow.showUp(view);
    }

    private HashMap<String,Integer> mTypeMaps = new HashMap<>();
    private List<String> mTypeLists =new ArrayList<>();
    /**初始化下方选择菜类别的数据*/
    private void initDriver() {
        mTypeLists.add(getResources().getString(R.string.hot_dishes));
        mTypeMaps.put(getResources().getString(R.string.hot_dishes),OrderFood.TYPE_RECAI);
        mTypeLists.add(getResources().getString(R.string.herbs));
        mTypeMaps.put(getResources().getString(R.string.herbs),OrderFood.TYPE_LIANGCAI);
        mTypeLists.add(getResources().getString(R.string.barbecued_meat));
        mTypeMaps.put(getResources().getString(R.string.barbecued_meat),OrderFood.TYPE_KAOROU);
        mTypeLists.add(getResources().getString(R.string.staple_food));
        mTypeMaps.put(getResources().getString(R.string.staple_food),OrderFood.TYPE_ZHUSHI);
        mTypeLists.add(getResources().getString(R.string.beverages));
        mTypeMaps.put(getResources().getString(R.string.beverages),OrderFood.TYPE_YINGLIAO);
        mTypeLists.add(getResources().getString(R.string.catching_meals));
        mTypeMaps.put(getResources().getString(R.string.catching_meals),OrderFood.TYPE_ZHUAFAN);
        mTypeLists.add(getResources().getString(R.string.hot_pot));
        mTypeMaps.put(getResources().getString(R.string.hot_pot),OrderFood.TYPE_HUOGUO);
    }

    private void getData() {
        mDatas.clear();
        for (int i = 0; i < 40; i++) {
            OrderFood orderFood = new OrderFood();
            orderFood.setId(mTypeMaps.get(dishes.getText().toString().trim())+""+(i+1));
            orderFood.setPrice(new Random().nextInt(100));
            orderFood.setName("第"+(i+1)+"道菜");
            orderFood.setType(mTypeMaps.get(dishes.getText().toString().trim()));
            mDatas.add(orderFood);
        }
        orderListAdapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11){
            if(resultCode == 12){
                if(data != null && data.getBooleanExtra("isFinish",false)){
                    onBackPressed();
                }
            }
        }
    }

    @OnClick(R.id.shopCart)
    public void shopCart(View view){
        OrderConfirmActivity.showActivity(this,new ArrayList<>(mCheckMaps.values()),customer);
    }

    private void initAdapter() {
        orderListAdapter = new OrderListAdapter();
        listView.setAdapter(orderListAdapter);
    }

    @Override
    protected Integer onCreateContentView() {
        return R.layout.order_list_layout;
    }

    @Override
    protected String getBarTitle() {
        return null;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    /***
     * 界面gridView适配器
     * */
    public class OrderListAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(OrderListActivity.this).inflate(R.layout.order_list_item_layout,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.name);
                holder.price = convertView.findViewById(R.id.price);
                holder.number = convertView.findViewById(R.id.number);
                holder.addOrder = convertView.findViewById(R.id.addOrder);
                holder.cutOrder = convertView.findViewById(R.id.cutOrder);
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
                if(mCheckMaps.containsKey(item.getId())) {
                    if(mCheckMaps.get(item.getId()).getNumber() != 0) {
                        holder.number.setText("" + mCheckMaps.get(item.getId()).getNumber());
                        holder.cutOrder.setVisibility(View.VISIBLE);
                    }else{
                        holder.number.setText("0");
                        holder.cutOrder.setVisibility(View.INVISIBLE);
                    }
                }else{
                    holder.number.setText("0");
                    holder.cutOrder.setVisibility(View.INVISIBLE);
                }
                holder.price.setText(""+item.getPrice());
                    holder.cutOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!mCheckMaps.containsKey(item.getId())){
                                return;
                            }
                            OrderFood orderFood = mCheckMaps.get(item.getId());
                            if(orderFood == null ){
                                return;
                            }
                            if(orderFood.getNumber() == 0){
                                holder.cutOrder.setVisibility(View.INVISIBLE);
                                return;
                            }
                            orderFood.setNumber(orderFood.getNumber()-1);
                            mCheckMaps.remove(item.getId());
                            if(orderFood.getNumber() != 0){
                                mCheckMaps.put(item.getId(),orderFood);
                            }

                            notifyDataSetChanged();
                        }
                    });
                holder.addOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mCheckMaps.containsKey(item.getId())) {
                            OrderFood orderFood = mCheckMaps.get(item.getId());
                            if (orderFood == null) {
                                return;
                            }
                            orderFood.setNumber(orderFood.getNumber() + 1);
                            mCheckMaps.remove(item.getId());
                            mCheckMaps.put(item.getId(), orderFood);
                        }else{
                            item.setNumber(1);
                            mCheckMaps.put(item.getId(), item);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView name;
            private TextView price;
            private TextView number;
            private ImageView addOrder;
            private ImageView cutOrder;
        }
    }

}

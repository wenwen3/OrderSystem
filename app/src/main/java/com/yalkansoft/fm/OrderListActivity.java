package com.yalkansoft.fm;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderListActivity extends BaseRxDataActivity {

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.dishes)
    TextView dishes;

    @BindView(R.id.hallImage)
    ImageView hallImage;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.shopCart)
    ImageView shopCart;

    @BindView(R.id.goneLayout)
    View goneLayout;

    private List<OrderFood> mDatas = new ArrayList<>();

    private HashMap<Integer,OrderFood> mCheckMaps = new HashMap<>();
    private OrderListAdapter orderListAdapter;

    @Override
    protected void onActivityPrepared(View view) {

        initDriver();

        initAdapter();

        getData();

        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public static void showActivity(Context context){
        Intent intent = new Intent(context, OrderListActivity.class);
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
            }
        });
        goneLayout.setVisibility(View.VISIBLE);
        stringSpinerPopWindow.showUp(view);
    }

    private HashMap<String,Integer> mTypeMaps = new HashMap<>();
    private List<String> mTypeLists =new ArrayList<>();
    /**初始化下方选择菜类别的数据*/
    private void initDriver() {
        mTypeLists.add("热菜类");
        mTypeMaps.put("热菜类",OrderFood.TYPE_RECAI);
        mTypeLists.add("凉菜类");
        mTypeMaps.put("凉菜类",OrderFood.TYPE_LIANGCAI);
        mTypeLists.add("烤肉类");
        mTypeMaps.put("烤肉类",OrderFood.TYPE_KAOROU);
        mTypeLists.add("主食类");
        mTypeMaps.put("主食类",OrderFood.TYPE_ZHUSHI);
        mTypeLists.add("饮料类");
        mTypeMaps.put("饮料类",OrderFood.TYPE_YINGLIAO);
        mTypeLists.add("抓饭类");
        mTypeMaps.put("抓饭类",OrderFood.TYPE_ZHUAFAN);
        mTypeLists.add("火锅类");
        mTypeMaps.put("火锅类",OrderFood.TYPE_HUOGUO);
    }

    private void getData() {
        mDatas.clear();
        for (int i = 0; i < 40; i++) {
            OrderFood orderFood = new OrderFood();
            orderFood.setId(i+1);
            orderFood.setPrice(new Random().nextInt(100));
            orderFood.setName("第"+(i+1)+"道菜");
            orderFood.setType(mTypeMaps.get(dishes.getText().toString().trim()));
            mDatas.add(orderFood);
        }
        orderListAdapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);
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
                    holder.name.setText("火锅:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_KAOROU){
                    holder.name.setText("烤肉:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_LIANGCAI){
                    holder.name.setText("凉菜:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_RECAI){
                    holder.name.setText("热菜:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_YINGLIAO){
                    holder.name.setText("饮料:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_ZHUAFAN){
                    holder.name.setText("抓饭:"+item.getName());
                }else if(item.getType() == OrderFood.TYPE_ZHUSHI){
                    holder.name.setText("主食:"+item.getName());
                }
                holder.number.setText(""+item.getNumber());
                holder.price.setText(""+item.getPrice());
                if(item.getNumber() > 0){
                    holder.cutOrder.setVisibility(View.VISIBLE);
                    holder.cutOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.getNumber() == 0){
                                holder.cutOrder.setVisibility(View.INVISIBLE);
                                return;
                            }
                            item.setNumber(item.getNumber()-1);
                            notifyDataSetChanged();
                        }
                    });
                }else{
                    holder.cutOrder.setVisibility(View.INVISIBLE);
                }
                holder.addOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setNumber(item.getNumber()+1);
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

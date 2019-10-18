package com.yalkansoft.fm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.badgeview.BadgeFactory;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.ordersystem.R;
import com.yalkansoft.base.BaseRxDataActivity;
import com.yalkansoft.bean.HallResultBean;
import com.yalkansoft.utils.RequestUtils;
import com.yalkansoft.utils.UiUtils;
import com.yalkansoft.widget.SelectPersonToOrderLayout;
import com.yalkansoft.widget.SpinerPopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**大厅页面*/
public class HallActivity extends BaseRxDataActivity {

    @BindView(R.id.gridView)
    GridView gridView;

    @BindView(R.id.changeHall)
    View changeHall;

    @BindView(R.id.hall)
    TextView hall;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.goneLayout)
    FrameLayout goneLayout;

    @BindView(R.id.allLayout)
    LinearLayout allLayout;

    @BindView(R.id.selectLayout)
    SelectPersonToOrderLayout selectLayout;

    @BindView(R.id.hallImage)
    ImageView hallImage;
    private SpinerPopWindow<String> stringSpinerPopWindow;

    private List<HallResultBean> mDatas = new ArrayList<>();
    private HallListAdapter mAdapter;

    @Override
    protected void onActivityPrepared(View view) {

        initAdapter();

        initDriver();

        getData();

        ui();
    }

    public static void showActivity(Activity context){
        Intent intent = new Intent(context, HallActivity.class);
        context.startActivity(intent);
    }

    private void ui() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private int currentTable = 0;

    /**初始化适配器*/
    private void initAdapter() {
        mAdapter = new HallListAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mAdapter.getItem(position).getPersonNumber() == 0){
                    Toast.makeText(HallActivity.this, "该位置没有人哦~", Toast.LENGTH_SHORT).show();
                    return;
                }
                gridView.setEnabled(false);
                refreshLayout.setEnabled(false);
                changeHall.setEnabled(false);
                selectLayout.setVisibility(View.VISIBLE);
                goneLayout.setVisibility(View.VISIBLE);
                currentTable = position+1;
                selectLayout.setNumber(HallActivity.this,mAdapter.getItem(position).getPersonNumber(), new SelectPersonToOrderLayout.OnLayoutClickListener() {
                    @Override
                    public void onClickCancel() {
                        clickPersonCancel();
                    }

                    @Override
                    public void onClickPersonLayout(int position) {
                        showOrderOrPay(position);
                    }
                });
            }
        });
    }

    /**选择顾客的界面的back事件*/
    public void clickPersonCancel(){
        gridView.setEnabled(true);
        changeHall.setEnabled(true);
        refreshLayout.setEnabled(true);
        selectLayout.clearData();
        selectLayout.removeAllViews();
        goneLayout.setVisibility(View.GONE);
        selectLayout.setVisibility(View.GONE);
    }

    AlertDialog alertDialog;
    /**弹出选择   开台点餐or买单结账*/
    private void showOrderOrPay(int position) {
        alertDialog = new AlertDialog.Builder(this).create();
        View rootView = LayoutInflater.from(this).inflate(R.layout.popup_hall_select_order_pay_layout, null);
        TextView title = rootView.findViewById(R.id.title);
        String string = getResources().getString(R.string.order_or_pay);
        final String format = String.format(string, currentTable, position);
        title.setText(format);
        rootView.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**点餐*/
                alertDialog.dismiss();
                clickPersonCancel();
//                showOrder(format);
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                RequestUtils.getInstance().request(stringStringHashMap, "http://101.200.54.249:8733/kazgu/?singleWsdl", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.d("xgw","response:"+responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.d("xgw","error:"+e.getMessage());
                    }
                });
            }
        });
        rootView.findViewById(R.id.payLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**买单*/
                alertDialog.dismiss();
                clickPersonCancel();
                PayListActivity.showActivity(HallActivity.this,format);
            }
        });
        alertDialog.setView(rootView);
        alertDialog.show();
        final WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = UiUtils.getInstance().getScreenWidth(this)*10/17;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    AlertDialog orderDialog;
    /**弹出选择   开台点餐or买单结账
     * @param format*/
    private void showOrder(final String format) {
        orderDialog = new AlertDialog.Builder(this).create();
        View rootView = LayoutInflater.from(this).inflate(R.layout.popup_order_person_layout, null);
        final EditText personNumber = rootView.findViewById(R.id.personNumber);
        View cutPerson = rootView.findViewById(R.id.cutPerson);
        cutPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personNumberString = personNumber.getText().toString().trim();
                if(Integer.parseInt(personNumberString) <= 1){
                    Toast.makeText(HallActivity.this, "至少一个人就餐", Toast.LENGTH_SHORT).show();
                    return;
                }
                int parseInt = Integer.parseInt(personNumberString);
                personNumber.setText((parseInt-1)+"");
            }
        });
        View addPerson = rootView.findViewById(R.id.addPerson);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personNumberString = personNumber.getText().toString().trim();
                int parseInt = Integer.parseInt(personNumberString);
                personNumber.setText((parseInt+1)+"");
            }
        });
        rootView.findViewById(R.id.startOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personNumberString = personNumber.getText().toString().trim();
                orderDialog.dismiss();
                OrderListActivity.showActivity(HallActivity.this,format);
            }
        });
        orderDialog.setView(rootView);
        orderDialog.show();
        final WindowManager.LayoutParams params = orderDialog.getWindow().getAttributes();
        params.width = UiUtils.getInstance().getScreenWidth(this)*10/17;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        orderDialog.getWindow().setAttributes(params);
        orderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        if(selectLayout.getVisibility() == View.VISIBLE){
            clickPersonCancel();
        }else{
            super.onBackPressed();
        }
    }

    /**初始化界面底部选择弹出的数据*/
    private void initDriver() {
        mHallList.add(getResources().getString(R.string.hall));
        mHallMaps.put(getResources().getString(R.string.hall),HallResultBean.TYPE_HALL);
        mHallList.add(getResources().getString(R.string.yard));
        mHallMaps.put(getResources().getString(R.string.yard),HallResultBean.TYPE_YARD);
        mHallList.add(getResources().getString(R.string.balcony));
        mHallMaps.put(getResources().getString(R.string.balcony),HallResultBean.TYPE_BALCONY);
    }

    private List<String> mHallList = new ArrayList<>();
    private Map<String,Integer> mHallMaps = new HashMap<>();
    private void getData() {
        mDatas.clear();
        for (int i = 0; i < 40; i++) {
            HallResultBean hallResultBean = new HallResultBean();
            hallResultBean.setType(mHallMaps.get(hall.getText().toString().trim()));
            if(i%4 == 0) {
                hallResultBean.setStatus(HallResultBean.STATUS_ENOUGH);
            }
            if(i%4 == 1) {
                hallResultBean.setStatus(HallResultBean.STATUS_NOTHING);
            }
            if(i%4 == 2) {
                hallResultBean.setStatus(HallResultBean.STATUS_NOT_ENOUGH);
            }
            hallResultBean.setPersonNumber(5);
            mDatas.add(hallResultBean);
        }
        mAdapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);

    }

    /**
     * 点击更换大厅、院子、包厢
     * */
    @OnClick
    public void changeHall(View view){
        hallImage.setImageResource(R.drawable.icon_bottom_cut);
        stringSpinerPopWindow = new SpinerPopWindow<>(this, mHallList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hall.setText(mHallList.get(position));
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

    @Override
    protected Integer onCreateContentView() {
        return R.layout.activity_hall_layout;
    }

    @Override
    protected String getBarTitle() {
        return "点餐中心";
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }

    /***
     * 界面gridView适配器
     * */
    public class HallListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public HallResultBean getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null ){
                convertView = LayoutInflater.from(HallActivity.this).inflate(R.layout.hall_list_item_layout,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.title = convertView.findViewById(R.id.title);
                holder.badgeView = convertView.findViewById(R.id.badgeView);
                holder.image = convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            }
            HallResultBean item = getItem(position);
            if(item != null ) {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                if(item.getPersonNumber() != 0) {
                    holder.image.setVisibility(View.VISIBLE);
                }else{
                    holder.image.setVisibility(View.GONE);
                }
                if(item.getType() == HallResultBean.TYPE_HALL) {
                    holder.title.setText(getResources().getString(R.string.hall)+" :"+(position+1) + "号桌");
                }else if(item.getType() == HallResultBean.TYPE_YARD){
                    holder.title.setText(getResources().getString(R.string.yard)+" :"+(position+1) + "号桌");
                }else if(item.getType() == HallResultBean.TYPE_BALCONY){
                    holder.title.setText(getResources().getString(R.string.balcony)+" :"+(position+1) + "号桌");
                }
                if(item.getPersonNumber() != 0) {
                    BadgeFactory.createCircle(HallActivity.this).setBadgeCount(item.getPersonNumber()).bind(holder.badgeView);
                }else{
                    BadgeFactory.createCircle(HallActivity.this).setBadgeCount(0).bind(holder.badgeView);
                }

            }
            return convertView;
        }

        private class ViewHolder {
            private TextView title;
            private TextView badgeView;
            private ImageView image;
        }
    }


}

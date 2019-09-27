package com.yalkansoft.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import com.project.ordersystem.R;

import java.util.ArrayList;
import java.util.List;

public class SelectPersonToOrderLayout extends FrameLayout {
    public SelectPersonToOrderLayout( Context context) {
        super(context);
    }

    public SelectPersonToOrderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectPersonToOrderLayout( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNumber(int personNumber, final OnLayoutClickListener onLayoutClickListener){
        if(personNumber == 1){
            View rootView = LayoutInflater.from(getContext()).inflate(R.layout.select_person_order_item_layout, this, false);
            rootView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    if(onLayoutClickListener != null ){
                        onLayoutClickListener.onClickCancel();
                    }
                }
            });
            TextView number = rootView.findViewById(R.id.number);
            rootView.findViewById(R.id.personLayout).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onLayoutClickListener != null ){
                        onLayoutClickListener.onClickPersonLayout(1);
                    }
                }
            });
            number.setText(personNumber+"");
            addView(rootView);
        }else if(personNumber > 1){
            View rootView = LayoutInflater.from(getContext()).inflate(R.layout.select_person_order_item_layout, this, false);
            rootView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    if(onLayoutClickListener != null ){
                        onLayoutClickListener.onClickCancel();
                    }
                }
            });
            TextView number = rootView.findViewById(R.id.number);
            GridView gridView = rootView.findViewById(R.id.gridView);
            gridView.setVisibility(VISIBLE);
            listAdapter listAdapter = new listAdapter();
            gridView.setAdapter(listAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(onLayoutClickListener != null ){
                        onLayoutClickListener.onClickPersonLayout(position+2);
                    }
                }
            });
            rootView.findViewById(R.id.personLayout).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onLayoutClickListener != null ){
                        onLayoutClickListener.onClickPersonLayout(1);
                    }
                }
            });
            for (int i = 1; i < personNumber; i++) {
                mDatas.add(i+"");
            }
            listAdapter.notifyDataSetChanged();
            number.setText(1+"");
            addView(rootView);
        }
    }

    public interface OnLayoutClickListener{
        void onClickCancel();
        void onClickPersonLayout(int position);
    }

    private List<String> mDatas = new ArrayList<>();
    /***
     * 界面gridView适配器
     * */
    public class listAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public String getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null ){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item_layout,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.number = convertView.findViewById(R.id.number);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.number.setText(position+2+"");
            return convertView;
        }

        private class ViewHolder {
            private TextView number;
        }
    }

    public void clearData(){
        mDatas.clear();
    }

}

package edu.hzuapps.salary2;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LvDayRecordAdapter extends BaseAdapter {

    private Context context;
    private List<Record> dayrecordList;

    public LvDayRecordAdapter() {}

    public LvDayRecordAdapter(Context context, List<Record> dayrecordList) {
        this.context = context;
        this.dayrecordList = dayrecordList;
    }

    public void setDayrecordList(List<Record> dayrecordList) {
        this.dayrecordList = dayrecordList;
    }

    @Override
    public int getCount() {
        return dayrecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayrecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dayrecord_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ly_all = convertView.findViewById(R.id.dayrecord_item_ly_all);
            viewHolder.tv_stylename = convertView.findViewById(R.id.dayrecord_item_tv_stylename);
            viewHolder.tv_number = convertView.findViewById(R.id.dayrecord_item_tv_number);
            viewHolder.tv_money = convertView.findViewById(R.id.dayrecord_item_tv_money);
            viewHolder.tv_remark = convertView.findViewById(R.id.dayrecord_item_tv_remark);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position%2 == 1) {
            viewHolder.ly_all.setBackgroundColor(Color.parseColor("#DADADA"));
        }else {
            viewHolder.ly_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        Record item = dayrecordList.get(position);
        viewHolder.tv_stylename.setText(item.getStylename());
        viewHolder.tv_stylename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getStylename(), Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.tv_stylename.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 交由父控件处理长点击事件
                return false;
            }
        });
        viewHolder.tv_number.setText(String.valueOf(item.getNumber()));
        viewHolder.tv_money.setText(item.getMoney().stripTrailingZeros().toPlainString());
        if(item.getRemark() == null || item.getRemark().trim().isEmpty()) viewHolder.tv_remark.setVisibility(View.GONE);
        else {
            viewHolder.tv_remark.setText(item.getRemark());
            viewHolder.tv_remark.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout ly_all;
        private TextView tv_stylename, tv_number, tv_money, tv_remark;
    }

}

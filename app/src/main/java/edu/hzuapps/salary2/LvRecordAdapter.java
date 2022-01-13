package edu.hzuapps.salary2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LvRecordAdapter extends BaseAdapter {

    private Context context;
    private List<Record> recordList;

    public LvRecordAdapter() {
    }

    public LvRecordAdapter(Context context, List<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ly_all = convertView.findViewById(R.id.record_item_ly_all);
            viewHolder.tv_time = convertView.findViewById(R.id.record_item_tv_time);
            viewHolder.tv_number = convertView.findViewById(R.id.record_item_tv_number);
            viewHolder.tv_money = convertView.findViewById(R.id.record_item_tv_money);
            viewHolder.tv_remark = convertView.findViewById(R.id.record_item_tv_remark);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position%2 == 1) {
            viewHolder.ly_all.setBackgroundColor(Color.parseColor("#DADADA"));
        }else {
            viewHolder.ly_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        Record item = recordList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.tv_time.setText(sdf.format(item.getTime()));
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
        private TextView tv_time, tv_number, tv_money, tv_remark;
    }

}

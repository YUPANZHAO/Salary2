package edu.hzuapps.salary2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LvEveryDayRecordAdapter extends BaseAdapter {

    private Context context;
    private List<List<Record>> recordList;

    public LvEveryDayRecordAdapter() {}

    public LvEveryDayRecordAdapter(Context context, List<List<Record>> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    public void setRecordList(List<List<Record>> recordList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.everydayrecord_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_time = convertView.findViewById(R.id.everydayrecord_item_tv_time);
            viewHolder.tv_totalmoney = convertView.findViewById(R.id.everydayrecord_item_tv_totalmoney);
            viewHolder.lv_dayrecord = convertView.findViewById(R.id.everydayrecord_item_lv_dayrecord);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Date time = recordList.get(position).get(0).getTime();
        viewHolder.tv_time.setText((new SimpleDateFormat("yyyy-MM-dd")).format(time));

        List<Record> data = recordList.get(position);
        LvDayRecordAdapter lvDayRecordAdapter = new LvDayRecordAdapter(context, data);
        viewHolder.lv_dayrecord.setAdapter(lvDayRecordAdapter);
        setListViewHeight(viewHolder.lv_dayrecord);
        lvDayRecordAdapter.notifyDataSetChanged();
        final int parentpositon = position;
        viewHolder.lv_dayrecord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String recordid = String.valueOf(data.get(position).getRecordid());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(data.get(position).getTime());
                String stylename = data.get(position).getStylename();
                String number = String.valueOf(data.get(position).getNumber());
                DelRecordDialog delRecordDialog = new DelRecordDialog((Activity) context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.del_record_dialog_btn_del:
                                delRecordDialog.dismiss();
                                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context, "salary2.db", null, 1);
                                SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("isdel", 1);
                                int res = db.update("record", values, "recordid=?", new String[]{recordid});
                                if(res == 1) {
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
                                    data.remove(position);
                                    if(data == null || data.size() == 0) {
                                        recordList.remove(parentpositon);
                                    }
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "该记录已删除", Toast.LENGTH_LONG).show();
                                }
                                break;
                        }
                    }
                };
                delRecordDialog.show();
                delRecordDialog.setClickListener(clickListener);
                delRecordDialog.setContext(time, stylename, number);
                return true;
            }
        });

        BigDecimal sum = new BigDecimal(0.0);
        for(Record item : data) sum = sum.add(item.getMoney());
        viewHolder.tv_totalmoney.setText(sum.stripTrailingZeros().toPlainString());

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time, tv_totalmoney;
        private ListView lv_dayrecord;
    }

    public void setListViewHeight(ListView listview){
        ListAdapter adapter = listview.getAdapter();
        if(adapter == null){
            return;
        }

        int totalHeight = 0;
        // 计算ListView的宽度
        int listViewWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth()-3*70;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);

        for(int i=0;i<adapter.getCount();i++){
            View view = adapter.getView(i, null, listview);
            // 这里的第一个参数必须使用widthSpec，
            // 如果使用0的话，无法计算出随内容变化而变化的Item的真正高度值
            view.measure(widthSpec, 0);
            totalHeight += view.getMeasuredHeight();
        }

        int dividerHeight = listview.getDividerHeight() * (adapter.getCount() - 1);
        totalHeight += dividerHeight;
        Log.i("ListViewHeight", "ListView DividerHeight : " + dividerHeight);

        int paddingHeight = listview.getPaddingTop() + listview.getPaddingBottom();
        totalHeight += paddingHeight;
        Log.i("ListViewHeight", "ListView PaddingHeight : " + paddingHeight);

        Log.i("ListViewHeight", "ListView TotalHeight : " + totalHeight);
        ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
        layoutParams.height = totalHeight;
        listview.setLayoutParams(layoutParams);
    }

}

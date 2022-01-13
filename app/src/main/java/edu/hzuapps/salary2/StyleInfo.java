package edu.hzuapps.salary2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StyleInfo extends AppCompatActivity {

    private Intent intent;
    private String styleid, stylename, price;
    private TextView tv_stylename, tv_price, tv_statistics;
    private Button btn_set, btn_del, btn_add;
    private ListView lv_record;
    private List<Record> recordList;
    private LvRecordAdapter lvRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_info);

        intent = getIntent();
        styleid = intent.getStringExtra("styleid");
        stylename = intent.getStringExtra("stylename");
        price = intent.getStringExtra("price");

        init_list();
        init_view();
    }

    private void init_list() {
        lv_record = findViewById(R.id.style_info_lv_record);
        lv_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String recordid = String.valueOf(recordList.get(position).getRecordid());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(recordList.get(position).getTime());
                String number = String.valueOf(recordList.get(position).getNumber());
                DelRecordDialog delRecordDialog = new DelRecordDialog(StyleInfo.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.del_record_dialog_btn_del:
                                delRecordDialog.dismiss();
                                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(StyleInfo.this, "salary2.db", null, 1);
                                SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("isdel", 1);
                                int res = db.update("record", values, "recordid=?", new String[]{recordid});
                                if(res == 1) {
                                    Toast.makeText(StyleInfo.this, "删除成功", Toast.LENGTH_LONG).show();
                                    flash_list();
                                }else {
                                    Toast.makeText(StyleInfo.this, "该记录已删除", Toast.LENGTH_LONG).show();
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
        flash_list();
    }

    private void flash_list() {
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this, "salary2.db", null, 1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                    "record.recordid," +
                    "record.number," +
                    "record.time," +
                    "record.remark," +
                    "style.price AS money " +
                    "FROM record " +
                    "LEFT JOIN style " +
                    "ON record.styleid = style.styleid " +
                    "WHERE record.isdel = 0 and record.styleid = " + styleid + " " +
                    "ORDER BY record.time",
                null);
        recordList = new ArrayList<>();
        while(cursor.moveToNext()) {
            Record item = new Record();
            item.setRecordid(cursor.getInt(0));
            item.setNumber(cursor.getInt(1));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                sdf.setLenient(false);
                date = sdf.parse(cursor.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            item.setTime(date);
            item.setRemark(cursor.getString(3));
            item.setMoney(BigDecimal.valueOf(cursor.getDouble(4)));
            recordList.add(item);
        }
        if(lvRecordAdapter == null) {
            lvRecordAdapter = new LvRecordAdapter(this, recordList);
            lv_record.setAdapter(lvRecordAdapter);
        }else {
            lvRecordAdapter.setRecordList(recordList);
            lvRecordAdapter.notifyDataSetChanged();
        }

        tv_statistics = findViewById(R.id.style_info_tv_statistics);
        BigDecimal sum = new BigDecimal(0.0);
        int cnt = 0;
        for(Record item : recordList) item.setMoney(item.getMoney().multiply(BigDecimal.valueOf(Double.valueOf(item.getNumber()))));
        for(Record item : recordList) {
            sum = sum.add(item.getMoney());
            cnt += item.getNumber();
        }
        tv_statistics.setText("累计：" + sum.stripTrailingZeros().toPlainString() + "元(" + String.valueOf(cnt) + "件)");
    }

    private void init_view() {
        tv_stylename = findViewById(R.id.style_info_tv_stylename);
        tv_stylename.setText("款式：" + stylename);
        tv_stylename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StyleInfo.this, stylename, Toast.LENGTH_LONG).show();
            }
        });
        tv_price = findViewById(R.id.style_info_tv_price);
        tv_price.setText("单价：" + price);

        AddRecordDialog addRecordDialog = new AddRecordDialog(StyleInfo.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        SetStyleDialog setStyleDialog = new SetStyleDialog(StyleInfo.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        DelStyleDialog delStyleDialog = new DelStyleDialog(StyleInfo.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.set_style_btn_set: {
                        String stylename1 = setStyleDialog.etv_stylename.getText().toString().trim();
                        String price1 = setStyleDialog.etv_price.getText().toString().trim();
                        if(stylename1.isEmpty()) {
                            Toast.makeText(StyleInfo.this, "款式不能为空!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        if(price1.isEmpty()) {
                            Toast.makeText(StyleInfo.this, "单价不能为空!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(StyleInfo.this, "salary2.db", null, 1);
                        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                        Cursor cursor = db.query("style", null, "stylename=? and styleid!=?", new String[]{stylename1, styleid}, null, null, null);
                        int cnt = cursor.getCount();
                        cursor.close();
                        if(cnt != 0) {
                            Toast.makeText(StyleInfo.this, "该款式已存在!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        ContentValues values = new ContentValues();
                        values.put("stylename", stylename1);
                        values.put("price", price1);
                        db.update("style", values, "styleid=?", new String[]{styleid});
                        Toast.makeText(StyleInfo.this, "修改成功!", Toast.LENGTH_LONG).show();
                        setStyleDialog.dismiss();
                        stylename = stylename1;
                        price = BigDecimal.valueOf(Double.valueOf(price1)).stripTrailingZeros().toPlainString();
                        tv_stylename.setText("款式：" + stylename);
                        tv_price.setText("单价：" + price);
                        flash_list();
                        break;}
                    case R.id.del_style_dialog_btn_del: {
                        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(StyleInfo.this, "salary2.db", null, 1);
                        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("isdel", 1);
                        db.update("record", values, "styleid=? and isdel=?", new String[]{styleid, "0"});
                        db.delete("style", "styleid=?", new String[]{styleid});
                        finish();
                        break;}
                    case R.id.add_record_dialog_btn_add: {
                        String number = addRecordDialog.etv_number.getText().toString().trim();
                        String year = addRecordDialog.etv_year.getText().toString().trim();
                        String month = addRecordDialog.etv_month.getText().toString().trim();
                        String day = addRecordDialog.etv_day.getText().toString().trim();
                        String remark = addRecordDialog.etv_remark.getText().toString().trim();
                        if(number.isEmpty()) {
                            Toast.makeText(StyleInfo.this, "数量不能为空!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        if(year.isEmpty() || month.isEmpty() || day.isEmpty()) {
                            Toast.makeText(StyleInfo.this, "日期不能为空!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(StyleInfo.this, "salary2.db", null, 1);
                        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                        Cursor cursor = db.query("style", null, "stylename=?", new String[]{stylename}, null, null, null);
                        int cnt = cursor.getCount();
                        if(cnt == 0) {
                            Toast.makeText(StyleInfo.this, "该款式不存在!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        cursor.moveToNext();
                        String styleid = cursor.getString(cursor.getColumnIndex("styleid"));
                        cursor.close();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            sdf.setLenient(false);
                            date = sdf.parse(year + "-" + month + "-" + day);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.i("mylog", "onClick: 日期有错");
                            Toast.makeText(StyleInfo.this, "日期出现错误!", Toast.LENGTH_LONG).show();
                            return ;
                        }
                        ContentValues values = new ContentValues();
                        values.put("styleid", styleid);
                        values.put("number", number);
                        values.put("time", sdf.format(date));
                        values.put("isdel", "0");
                        if(!remark.isEmpty()) values.put("remark", remark);
                        long res = db.insert("record", null, values);
                        if(res == -1) {
                            Toast.makeText(StyleInfo.this, "添加失败!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(StyleInfo.this, "添加成功!", Toast.LENGTH_LONG).show();
                            addRecordDialog.dismiss();
                            flash_list();
                        }
                        break;}
                }
            }
        };

        btn_set = findViewById(R.id.style_info_btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyleDialog.show();
                setStyleDialog.setContext(stylename, price);
                setStyleDialog.setClickListener(clickListener);
            }
        });

        btn_del = findViewById(R.id.style_info_btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StyleInfo.this, "长按删除按钮删除整张表", Toast.LENGTH_LONG).show();
            }
        });
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                delStyleDialog.show();
                delStyleDialog.setClickListener(clickListener);
                return true;
            }
        });

        btn_add = findViewById(R.id.style_info_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordDialog.show();
                addRecordDialog.setClickListener(clickListener);
            }
        });
    }
}
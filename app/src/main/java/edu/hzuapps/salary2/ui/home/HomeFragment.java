package edu.hzuapps.salary2.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.hzuapps.salary2.LvDayRecordAdapter;
import edu.hzuapps.salary2.LvEveryDayRecordAdapter;
import edu.hzuapps.salary2.LvStyleAdapter;
import edu.hzuapps.salary2.MyDBOpenHelper;
import edu.hzuapps.salary2.MyFloatBtn;
import edu.hzuapps.salary2.R;
import edu.hzuapps.salary2.Record;
import edu.hzuapps.salary2.Style;

public class HomeFragment extends Fragment {

    private View root;
    private ListView lv_everydayrecord;
    private List<List<Record>> everydayrecordList;
    private LvEveryDayRecordAdapter lvEveryDayRecordAdapter;
    private MyFloatBtn fbtn_select;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        init_everydayrecord_list();
        init_fbtn_select();

        return root;
    }

    private void init_fbtn_select() {
        fbtn_select = root.findViewById(R.id.home_fbtn_select);
        fbtn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
                                SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                                Cursor cursor = db.rawQuery(
                                        "SELECT " +
                                            "record.recordid," +
                                            "record.styleid," +
                                            "style.stylename," +
                                            "record.number," +
                                            "record.time," +
                                            "record.remark," +
                                            "style.price AS money " +
                                            "FROM record " +
                                            "LEFT JOIN style " +
                                            "ON record.styleid = style.styleid " +
                                            "WHERE record.isdel = 0 " +
                                            "and record.time = ?",
                                        new String[] {String.format("%04d",year)+"-"+String.format("%02d", month+1)+"-"+String.format("%02d", dayOfMonth)});
                                everydayrecordList.clear();
                                List<Record> data = null;
                                Record item = null;
                                while(cursor.moveToNext()) {
                                    item = new Record();
                                    item.setRecordid(cursor.getInt(0));
                                    item.setStyleid(cursor.getInt(1));
                                    item.setStylename(cursor.getString(2));
                                    item.setNumber(cursor.getInt(3));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        sdf.setLenient(false);
                                        date = sdf.parse(cursor.getString(4));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    item.setTime(date);
                                    item.setRemark(cursor.getString(5));
                                    item.setMoney(BigDecimal.valueOf(cursor.getDouble(6)));
                                    item.setMoney(item.getMoney().multiply(BigDecimal.valueOf(Double.valueOf(item.getNumber()))));
                                    if(data == null) data = new ArrayList<>();
                                    data.add(item);
                                }
                                if(data != null) everydayrecordList.add(data);
                                Log.i("mylog", "onDateSet: "+String.format("%04d",year)+"-"+String.format("%02d", month+1)+"-"+String.format("%02d", dayOfMonth));
                                Log.i("mylog", "onDateSet: " + data);
                                lvEveryDayRecordAdapter.setRecordList(everydayrecordList);
                                lvEveryDayRecordAdapter.notifyDataSetChanged();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void init_everydayrecord_list() {
        lv_everydayrecord = root.findViewById(R.id.home_lv_everydayrecord);
        flash_everydayrecord_list();
    }

    private void flash_everydayrecord_list() {
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                "record.recordid," +
                "record.styleid," +
                "style.stylename," +
                "record.number," +
                "record.time," +
                "record.remark," +
                "style.price AS money " +
                "FROM record " +
                "LEFT JOIN style " +
                "ON record.styleid = style.styleid " +
                "WHERE record.isdel = 0 " +
                "and julianday('now') - julianday(record.time) < 7 " +
                "ORDER BY record.time",
                null);
        everydayrecordList = new ArrayList<>();
        List<Record> data = null;
        Record pre = null, item = null;
        while(cursor.moveToNext()) {

            item = new Record();
            item.setRecordid(cursor.getInt(0));
            item.setStyleid(cursor.getInt(1));
            item.setStylename(cursor.getString(2));
            item.setNumber(cursor.getInt(3));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                sdf.setLenient(false);
                date = sdf.parse(cursor.getString(4));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            item.setTime(date);
            item.setRemark(cursor.getString(5));
            item.setMoney(BigDecimal.valueOf(cursor.getDouble(6)));
            item.setMoney(item.getMoney().multiply(BigDecimal.valueOf(Double.valueOf(item.getNumber()))));
            if(pre == null) {
                data = new ArrayList<>();
                data.add(item);
            }else if(pre.getTime().equals(item.getTime())) {
                data.add(item);
            }else {
                everydayrecordList.add(data);
                data = new ArrayList<>();
                data.add(item);
            }
            pre = item;

        }
        if(data != null) everydayrecordList.add(data);
        cursor.close();
        if(lvEveryDayRecordAdapter == null) {
            lvEveryDayRecordAdapter = new LvEveryDayRecordAdapter(root.getContext(), everydayrecordList);
            lv_everydayrecord.setAdapter(lvEveryDayRecordAdapter);
        }else {
            lvEveryDayRecordAdapter.setRecordList(everydayrecordList);
            lvEveryDayRecordAdapter.notifyDataSetChanged();
        }
    }

}
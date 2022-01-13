package edu.hzuapps.salary2.ui.notifications;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hzuapps.salary2.AddStyleDialog;
import edu.hzuapps.salary2.LvRecordAdapter;
import edu.hzuapps.salary2.LvStyleAdapter;
import edu.hzuapps.salary2.MyDBOpenHelper;
import edu.hzuapps.salary2.R;
import edu.hzuapps.salary2.Record;
import edu.hzuapps.salary2.Style;
import edu.hzuapps.salary2.StyleInfo;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {

    private View root;
    private TextView tv_totalmoney;
    private Button btn_addstyle;
    private ListView lv_style;
    private List<Style> styleList;
    private LvStyleAdapter lvStyleAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        init_btn_addstyle();
        init_styleList();
        flash_totalmoney();

        return root;
    }

    private void init_btn_addstyle() {
        btn_addstyle = root.findViewById(R.id.notification_btn_addstyle);
        btn_addstyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStyleDialog addStyleDialog = new AddStyleDialog((Activity) root.getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.add_style_btn_add:
                                String stylename = addStyleDialog.etv_stylename.getText().toString().trim();
                                String price = addStyleDialog.etv_price.getText().toString().trim();
                                if(stylename.isEmpty()) {
                                    Toast.makeText(root.getContext(), "款式不能为空!", Toast.LENGTH_LONG).show();
                                    return ;
                                }
                                if(price.isEmpty()) {
                                    Toast.makeText(root.getContext(), "单价不能为空!", Toast.LENGTH_LONG).show();
                                    return ;
                                }
                                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
                                SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                                Cursor cursor = db.query("style", null, "stylename=?", new String[]{stylename}, null, null, null);
                                int cnt = cursor.getCount();
                                cursor.close();
                                if(cnt != 0) {
                                    Toast.makeText(root.getContext(), "该款式已存在!", Toast.LENGTH_LONG).show();
                                    return ;
                                }
                                ContentValues values = new ContentValues();
                                values.put("stylename", stylename);
                                values.put("price", price);
                                long res = db.insert("style", null, values);
                                if(res == -1) {
                                    Toast.makeText(root.getContext(), "添加失败!", Toast.LENGTH_LONG).show();
                                    addStyleDialog.dismiss();
                                }else {
                                    Toast.makeText(root.getContext(), "添加成功!", Toast.LENGTH_LONG).show();
                                    flash_styleList();
                                    addStyleDialog.dismiss();
                                }
                                break;
                        }
                    }
                };
                addStyleDialog.show();
                addStyleDialog.setClickListener(clickListener);
            }
        });
    }

    private void init_styleList() {
        lv_style = root.findViewById(R.id.notification_lv_style);
        lv_style.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(root.getContext(), StyleInfo.class);
                intent.putExtra("styleid", styleList.get(position).getStyleid().toString());
                intent.putExtra("stylename", styleList.get(position).getStylename());
                intent.putExtra("price", styleList.get(position).getPrice().toString());
                startActivityForResult(intent, 0);
            }
        });
        flash_styleList();
    }

    private void flash_styleList() {
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("style", null, null, null, null, null, "stylename");
        styleList = new ArrayList<>();
        while(cursor.moveToNext()) {
            Style item = new Style();
            item.setStyleid(cursor.getInt(cursor.getColumnIndex("styleid")));
            item.setStylename(cursor.getString(cursor.getColumnIndex("stylename")));
            item.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("price"))));
            styleList.add(item);
        }
        cursor.close();
        if(lvStyleAdapter == null) {
            lvStyleAdapter = new LvStyleAdapter(root.getContext(), styleList);
            lv_style.setAdapter(lvStyleAdapter);
        }else {
            lvStyleAdapter.setStyleList(styleList);
            lvStyleAdapter.notifyDataSetChanged();
        }
    }

    private void flash_totalmoney() {
        tv_totalmoney = root.findViewById(R.id.notification_totalmoney);
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                    "record.number," +
                    "style.price AS money " +
                    "FROM record " +
                    "LEFT JOIN style " +
                    "ON record.styleid = style.styleid " +
                    "WHERE record.isdel = 0",
                null);
        BigDecimal sum = new BigDecimal(0.0);
        while(cursor.moveToNext()) {
            Record item = new Record();
            item.setNumber(cursor.getInt(0));
            item.setMoney(BigDecimal.valueOf(cursor.getDouble(1)));
            item.setMoney(item.getMoney().multiply(BigDecimal.valueOf(Double.valueOf(item.getNumber()))));
            sum = sum.add(item.getMoney());
        }
        tv_totalmoney.setText("总累计：" + sum.stripTrailingZeros().toPlainString() + "元");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flash_styleList();
        flash_totalmoney();
    }

}
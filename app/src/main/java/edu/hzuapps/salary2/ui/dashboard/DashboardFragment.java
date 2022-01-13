package edu.hzuapps.salary2.ui.dashboard;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hzuapps.salary2.MyDBOpenHelper;
import edu.hzuapps.salary2.R;

public class DashboardFragment extends Fragment {

    private View root;
    private EditText etv_stylename, etv_number, etv_year, etv_month, etv_day, etv_remark;
    private Button btn_submit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init_view();

        return root;
    }

    private void init_view() {
        etv_stylename = root.findViewById(R.id.dashboard_etv_stylename);
        etv_number = root.findViewById(R.id.dashboard_etv_number);
        etv_year = root.findViewById(R.id.dashboard_etv_year);
        etv_month = root.findViewById(R.id.dashboard_etv_month);
        etv_day = root.findViewById(R.id.dashboard_etv_day);
        etv_remark = root.findViewById(R.id.dashboard_etv_remark);
        btn_submit = root.findViewById(R.id.dashboard_btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stylename = etv_stylename.getText().toString().trim();
                String number = etv_number.getText().toString().trim();
                String year = etv_year.getText().toString().trim();
                String month = etv_month.getText().toString().trim();
                String day = etv_day.getText().toString().trim();
                String remark = etv_remark.getText().toString().trim();
                if(stylename.isEmpty()) {
                    Toast.makeText(root.getContext(), "款式不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }
                if(number.isEmpty()) {
                    Toast.makeText(root.getContext(), "数量不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }
                if(year.isEmpty() || month.isEmpty() || day.isEmpty()) {
                    Toast.makeText(root.getContext(), "日期不能为空!", Toast.LENGTH_LONG).show();
                    return ;
                }
                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(root.getContext(), "salary2.db", null, 1);
                SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
                Cursor cursor = db.query("style", null, "stylename=?", new String[]{stylename}, null, null, null);
                int cnt = cursor.getCount();
                if(cnt == 0) {
                    Toast.makeText(root.getContext(), "该款式不存在!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(root.getContext(), "日期出现错误!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(root.getContext(), "提交失败!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(root.getContext(), "提交成功!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
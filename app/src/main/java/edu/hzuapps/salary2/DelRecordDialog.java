package edu.hzuapps.salary2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DelRecordDialog extends Dialog {

    Activity context;
    public TextView tv_time, tv_stylename, tv_number;
    public Button btn_del;

    private View.OnClickListener clickListener;

    public DelRecordDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public DelRecordDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        btn_del.setOnClickListener(clickListener);
    }

    public void setContext(String time, String stylename, String number) {
        tv_time.setText("日期：" + time);
        tv_stylename.setText("款式：" + stylename);
        tv_number.setText("数量：" + number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_record_dialog);
        tv_time = findViewById(R.id.del_record_dialog_tv_time);
        tv_stylename = findViewById(R.id.del_record_dialog_tv_stylename);
        tv_number = findViewById(R.id.del_record_dialog_tv_number);
        btn_del = findViewById(R.id.del_record_dialog_btn_del);

        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        this.setCancelable(true);
    }

}

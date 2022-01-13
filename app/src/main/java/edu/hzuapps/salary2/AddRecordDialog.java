package edu.hzuapps.salary2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddRecordDialog extends Dialog {

    Activity context;
    public EditText etv_number, etv_year, etv_month, etv_day, etv_remark;
    public Button btn_add;

    private View.OnClickListener clickListener;

    public AddRecordDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public AddRecordDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        btn_add.setOnClickListener(clickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.add_record_dialog);
        etv_number = findViewById(R.id.add_record_dialog_etv_number);
        etv_year = findViewById(R.id.add_record_dialog_etv_year);
        etv_month = findViewById(R.id.add_record_dialog_etv_month);
        etv_day = findViewById(R.id.add_record_dialog_etv_day);
        etv_remark = findViewById(R.id.add_record_dialog_etv_remark);
        btn_add = findViewById(R.id.add_record_dialog_btn_add);

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

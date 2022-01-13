package edu.hzuapps.salary2;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStyleDialog extends Dialog {

    Activity context;
    public EditText etv_stylename, etv_price;
    public Button btn_add;

    private View.OnClickListener clickListener;

    public AddStyleDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;
        this.setContentView(R.layout.add_style_dialog);
        etv_stylename = findViewById(R.id.add_style_etv_stylename);
        etv_price = findViewById(R.id.add_style_etv_price);
        btn_add = findViewById(R.id.add_style_btn_add);
    }

    public AddStyleDialog(Activity context, int theme, View.OnClickListener clickListener) {
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


package edu.hzuapps.salary2;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * /**
 * 创 建 者：下一页5（轻飞扬）
 * 创建时间：2018/4/18.11:25
 * 关于键盘方法的所有工具类
 */
public class KeyboardUtils {
    /**
     * 隐藏键盘的方法
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
}
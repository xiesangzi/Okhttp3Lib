package com.yhms.okhttp3.utils;

import android.widget.Toast;

import com.yhms.okhttp3.RxHttpUtils;

/**
 * Created by Allen on 2017/10/31.
 *
 * @author Allen
 *         Toast工具类
 */

public class AlertUtils {

    private static Toast mToast;

    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(RxHttpUtils.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}

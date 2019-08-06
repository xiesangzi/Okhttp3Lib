package com.yhms.okhttp3.observer;


import android.text.TextUtils;

import com.yhms.okhttp3.base.BaseObserver;
import com.yhms.okhttp3.exception.ApiException;
import com.yhms.okhttp3.utils.AlertUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Allen on 2017/5/3.
 *
 * @author Allen
 * 通用的Observer
 * 用户可以根据自己需求自定义自己的类继承BaseObserver<T>即可
 */

public abstract class CommonObserver<T> extends BaseObserver<T> {


    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(ApiException errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void doOnSubscribe(Disposable d) {
    }

    @Override
    public void doOnError(ApiException errorMsg) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg.getMessage())) {
            AlertUtils.showToast(errorMsg.getMessage());
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
    }

}

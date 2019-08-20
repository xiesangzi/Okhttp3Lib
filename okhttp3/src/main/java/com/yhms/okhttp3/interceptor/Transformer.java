package com.yhms.okhttp3.interceptor;


import com.yhms.okhttp3.interfaces.ILoadingView;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Allen on 2016/12/20.
 * <p>
 *
 * @author Allen
 * 控制操作线程的辅助类
 */

public class Transformer {

    /**
     * 无参数
     *
     * @param <T> 泛型
     * @return 返回Observable
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return switchSchedulers(null);
    }

    /**
     * 带参数  显示loading对话框
     *
     * @param loadingView loading
     * @param <T>         泛型
     * @return 返回Observable
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers(final ILoadingView loadingView) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (loadingView != null) {
                        loadingView.showLoadingView();
                    }
                })
                .subscribeOn(Schedulers.io())     // 尽量避免使用Schedulers.newThread()每次都去创建一个线程，而是去使用Schedulers.io()可以去复用已有的线程，订阅处理在子线程中
                .observeOn(AndroidSchedulers.mainThread()) // 转回主线程，订阅处理都在ui线程中
                .doFinally(() -> {
                    if (loadingView != null) {
                        loadingView.hideLoadingView();
                    }
                });
    }

}

package com.yhms.okhttp3.download;

import android.annotation.SuppressLint;

import com.yhms.okhttp3.base.BaseObserver;
import com.yhms.okhttp3.exception.ApiException;
import com.yhms.okhttp3.manage.RxHttpManager;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by allen on 2017/6/13.
 * <p>
 *
 * @author Allen
 * 文件下载
 */

public abstract class DownloadObserver extends BaseObserver<ResponseBody> {

    /**文件名+后缀**/
    private String destFileName;
    /**文件下载路径**/
    private String destFileDir;

    public DownloadObserver(String destFileName) {
        this.destFileName = destFileName;
    }

    public DownloadObserver(String destFileName, String destFileDir) {
        this.destFileName = destFileName;
        this.destFileDir = destFileDir;
    }


    /**
     * 失败回调
     *
     * @param e errorMsg
     */
    protected abstract void onDownloadError(ApiException e);


    /**
     * 成功回调
     *
     * @param filePath filePath
     */
    /**
     * 成功回调
     *
     * @param bytesRead     已经下载文件的大小
     * @param contentLength 文件的大小
     * @param progress      当前进度
     * @param done          是否下载完成
     * @param filePath      文件路径
     */
    protected abstract void onDownloadSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath);


    @Override
    public void doOnError(ApiException e) {
        DownloadObserver.this.onDownloadError(e);
    }

    @Override
    public void doOnSubscribe(Disposable d) {
        RxHttpManager.get().add(setTag(), d);
    }

    @SuppressLint("CheckResult")
    @Override
    public void doOnNext(ResponseBody responseBody) {
        Observable
                .just(responseBody)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        DownloadObserver.this.doOnSubscribe(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            new DownloadManager().saveFile(responseBody, destFileName, destFileDir, new ProgressListener() {
                                @Override
                                public void onResponseProgress(final long bytesRead, final long contentLength, final int progress, final boolean done, final String filePath) {
                                    Observable
                                            .just(progress)
                                            .distinctUntilChanged()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Integer>() {
                                                @Override
                                                public void accept(@NonNull Integer integer) throws Exception {
                                                    onDownloadSuccess(bytesRead, contentLength, progress, done, filePath);
                                                }
                                            });
                                }

                            });

                        } catch (IOException e) {
                            Observable
                                    .just(e)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<IOException>() {
                                        @Override
                                        public void accept(IOException s) throws Exception {
                                            ApiException exception = new ApiException(s, ApiException.ERROR.PARSE_ERROR);
                                            DownloadObserver.this.doOnError(exception);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ApiException exception = new ApiException(e, ApiException.ERROR.PARSE_ERROR);
                        DownloadObserver.this.doOnError(exception);
                    }

                    @Override
                    public void onComplete() {
                        DownloadObserver.this.doOnCompleted();
                    }
                });

    }

    @Override
    public void doOnCompleted() {

    }
}

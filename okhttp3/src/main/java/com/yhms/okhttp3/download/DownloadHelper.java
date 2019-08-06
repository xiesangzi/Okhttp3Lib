package com.yhms.okhttp3.download;


import com.yhms.okhttp3.factory.ApiFactory;
import com.yhms.okhttp3.interceptor.Transformer;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Created by allen on 2017/6/14.
 * <p>
 *
 * @author Allen
 * 为下载单独建一个retrofit
 */

public class DownloadHelper {
    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        String DEFAULT_DOWNLOAD_KEY = "defaultDownloadUrlKey";
        String DEFAULT_BASE_URL = "https://api.github.com/";
        return ApiFactory.getInstance()
                .setOkClient(new OkHttpClient.Builder().addInterceptor(new DownloadInterceptor()).build())
                .createApi(DEFAULT_DOWNLOAD_KEY, DEFAULT_BASE_URL, DownloadApi.class)
                .downloadFile(fileUrl)
                .compose(Transformer.<ResponseBody>switchSchedulers());
    }
}

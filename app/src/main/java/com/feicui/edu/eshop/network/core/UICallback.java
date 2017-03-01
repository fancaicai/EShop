package com.feicui.edu.eshop.network.core;

import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/25 0025.
 */
// 为了统一处理OkHttp的Callback不能更新UI的问题
public abstract class UICallback implements Callback {
    // 创建一个运行在主线程的Handler
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // 添加到消息队列里，和handler运行在同一个线程
                onFailureInUI(call,e);
            }
        });
    }


    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponseInUI(call,response);
            }
        });
    }


    // 对外提供两个必须实现的方法：将onFailure、onResponse的数据传递出去。
    protected abstract void onFailureInUI(Call call, IOException e);
    protected abstract void onResponseInUI(Call call, Response response);
}

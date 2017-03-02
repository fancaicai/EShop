package com.feicui.edu.eshop.network.core;

import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.LogUtils;
import com.feicui.edu.eshop.base.utils.wrapper.ToastWrapper;
import com.feicui.edu.eshop.network.EShopClient;

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
    private Class<? extends ResponseEntity> mResponseType;
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

//这里call和response的值是怎么得到的
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    onResponseInUI(call,response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    对于请求失败的处理
    public   void onFailureInUI(Call call, IOException e){
        ToastWrapper.show(R.string.error_network);
        LogUtils.error("onFailureInUi",e);
        onBusinessResponse(false,null);

    }
    //    对于请求成功的处理
    public void onResponseInUI(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            // 要转换成真正的实体类
            ResponseEntity responseEntity = EShopClient.getInstance().getResponseEntity(response,mResponseType);

            // 判断类为null
            if (responseEntity==null || responseEntity.getStatus()==null){
                throw new RuntimeException("Fatal Api Error");
            }
            // 判断是不是真正的拿到数据了
            // 判断是不是真正的拿到数据了
            if (responseEntity.getStatus().isSucceed()){
                // 成功，数据也有
                onBusinessResponse(true,responseEntity);
        }else {
                ToastWrapper.show(responseEntity.getStatus().getErrorDesc());
                onBusinessResponse(false,responseEntity);
            }
        }
    }
    // 告诉我们要转换的实际的实体类型
    public void setResponseType(Class<? extends ResponseEntity> responseType){
        mResponseType = responseType;
    }
    protected abstract void onBusinessResponse(boolean isSucces,ResponseEntity responseEntity);


}

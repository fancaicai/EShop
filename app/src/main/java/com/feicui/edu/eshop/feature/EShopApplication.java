package com.feicui.edu.eshop.feature;

import android.app.Application;

import com.feicui.edu.eshop.base.utils.wrapper.ToastWrapper;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by fcc on 2017/2/23 0023.
 */

public class EShopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {

            // 这个是用于分析内存的线程，我们不能在这里面初始化我们项目
            return;
        }
        // 正常的app初始化
        LeakCanary.install(this);

        // Toast的包装类的初始化
        ToastWrapper.init(this);

    }
}

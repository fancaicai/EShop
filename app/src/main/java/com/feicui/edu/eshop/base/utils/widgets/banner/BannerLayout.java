package com.feicui.edu.eshop.base.utils.widgets.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.feicui.edu.eshop.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 自定义的轮播图控件
 * 1. 自动轮播
 * 2. 数据可随意设置(适配器的问题)
 * 3. 自动和手动的冲突
 */

public class BannerLayout extends RelativeLayout {
    private static final long duration = 4000;
    @BindView(R.id.pager_banner)
    ViewPager pagerBanner;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    private long mResumecycleTime;
    private CyclingHandler mCyclingHandler;
    private Timer timer;
    private TimerTask timertask;

    // 代码中使用控件
    public BannerLayout(Context context) {
        super(context);
        init(context);
    }

    // 布局中使用，但是未设置style
    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // 设置了style
    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 布局的填充和初始化相关
    private void init(Context context) {
// Merge标签一定要设置ViewGroup和attachToRoot为true
        LayoutInflater.from(context).inflate(R.layout.widget_banner_layout, this, true);
        ButterKnife.bind(this);
        mCyclingHandler = new CyclingHandler(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 在视图上显示出来的时候
        // 计时器
        timer = new Timer();

// 定时的发送一些事件：使用Handler来发送，并且处理
        timertask = new TimerTask() {
            @Override
            public void run() {
             // 定时的发送一些事件：使用Handler来发送，并且处理
                mCyclingHandler.sendEmptyMessage(0);
            }
        };
// 任务（事件）、延时事件、循环时间
        timer.schedule(timertask, duration, duration);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 取消我们开启的计时任务
        timer.cancel();
        timertask.cancel();
        timer=null;
        timertask=null;
    }
    // 首先获取到我们触摸的时间
    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        mResumecycleTime = System.currentTimeMillis() + duration;
        return super.dispatchGenericFocusedEvent(event);

    }

    // 为了防止内部类持有外部类的引用而造成内存泄漏，所以静态内部类+弱引用的方式
    private static class CyclingHandler extends Handler {
        WeakReference<BannerLayout> mBannerReference;
        public CyclingHandler(BannerLayout banner) {
            mBannerReference = new WeakReference<BannerLayout>(banner);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 接收到消息，处理：轮播图切换到下一页
            if (mBannerReference == null) return;
            BannerLayout bannerLayout = mBannerReference.get();
            if (bannerLayout == null) return;
            // 触摸之后时间还没过四秒，不去轮播
            if (System.currentTimeMillis() < bannerLayout.mResumecycleTime) {
                return;
            }
            // 切换到下一页
            bannerLayout.moveToNextPosition();


        }
    }


    // 切换到下一页的方法
    private void moveToNextPosition() {
        // 看有没有设置适配器
        if (pagerBanner.getAdapter() == null) {
            throw new IllegalStateException("you need set a banner adapter");
        }
        // 看适配器里面是不是有数据
        int count = pagerBanner.getAdapter().getCount();
        if (count == 0) return;
        // 看是不是展示的最后一条
        if (pagerBanner.getCurrentItem() == count - 1) {
            // 如果展示的是最后一条，切换到0,不设置平滑滚动
            pagerBanner.setCurrentItem(0, false);
        } else {
            pagerBanner.setCurrentItem(pagerBanner.getCurrentItem() + 1, true);
        }

    }
    // 设置适配器的方法
public void setAdapter(BannerAdapter adapter){
    pagerBanner.setAdapter(adapter);
    indicator.setViewPager(pagerBanner);
    adapter.registerDataSetObserver(indicator.getDataSetObserver());
}
}

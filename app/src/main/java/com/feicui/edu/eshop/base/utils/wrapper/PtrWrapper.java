package com.feicui.edu.eshop.base.utils.wrapper;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.feicui.edu.eshop.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/2/28 0028.
 */
// 下拉刷新和加载的包装类
public abstract class PtrWrapper {
    private PtrFrameLayout mRefreshLayout;

    /**
     * 1. 找到刷新控件：构造方法
     * 2. 设置刷新的头布局样式、尾部加载的布局
     * 3. 刷新的处理，获取数据
     * 4. 自动刷新、延时自动刷新
     * 5. 停止刷新
     * 6. 判断是不是在刷新
     */

    public PtrWrapper(Activity activity,boolean isNeedLoad) {
        mRefreshLayout = ButterKnife.findById(activity, R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }


    public PtrWrapper(Fragment fragment,boolean isNeedLoad) {
        mRefreshLayout = ButterKnife.findById(fragment.getView(), R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    // 初始化刷新加载
    private void initPtr(boolean isNeedLoad) {
        if (mRefreshLayout!=null){
            mRefreshLayout.disableWhenHorizontalMove(true);
        }

        // 设置刷新的布局：头布局
        initPtrHeader();

        // 需要加载，设置尾部布局
        if (isNeedLoad){
            initPtrFooter();
        }

        mRefreshLayout.setPtrHandler(mPtrHandler);
    }

    // 处理刷新和加载
    private PtrDefaultHandler2 mPtrHandler = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            // 加载
            onLoadMore();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            // 刷新
            onRefresh();
        }
    };

    // 设置加载的布局
    private void initPtrFooter() {
        PtrClassicDefaultFooter footer = new PtrClassicDefaultFooter(mRefreshLayout.getContext());
        mRefreshLayout.setFooterView(footer);
        mRefreshLayout.addPtrUIHandler(footer);
    }

    // 设置刷新布局
    private void initPtrHeader() {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mRefreshLayout.getContext());
        mRefreshLayout.setHeaderView(header);
        mRefreshLayout.addPtrUIHandler(header);
    }

    // 自动刷新
    public void autoRefresh(){
        mRefreshLayout.autoRefresh();
    }

    // 延时自动刷新
    public void postRefreshDelayed(long delay){
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        },delay);
    }

    // 停止刷新
    public void stopRefresh(){
        if (isRefreshing()){
            mRefreshLayout.refreshComplete();
        }
    }

    // 是不是正在刷新
    public boolean isRefreshing(){
        return mRefreshLayout.isRefreshing();
    }

    // 刷新数据
    protected abstract void onRefresh();

    // 加载更多数据
    protected abstract void onLoadMore();

}


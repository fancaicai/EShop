package com.feicui.edu.eshop.base.utils;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/25 0025.
 */
// 通用的Fragment的基类
public abstract class BaseFragment extends Fragment {
    private Unbinder mUnbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentViewLayout(),container,false);
        mUnbinder= ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mUnbinder=null;
    }
    protected abstract void initView();
   /**定义一个抽象的方法用于将Fragmenty所需要的布局加载回来，由于用到的重复的方法太多，所以街封装一下这个方法
  这样皆可以让子类根据自己的实际情况灵活的趋势线这个方法*/
    @LayoutRes protected abstract int getContentViewLayout();
}

package com.feicui.edu.eshop.base.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.feicui.edu.eshop.network.entity.CategoryBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 适配器的基类
 *
 * 1. 数据不一样
 * 2. item的布局不一样，ViewHolder
 * 不确定的内容可以用泛型来表示
 * <T>数据源
 * <V> ViewHolder
 */

public abstract class BaseListAdapter<T,V extends BaseListAdapter.ViewHolder > extends BaseAdapter {
    List<T> mDataSet = new ArrayList<>();

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public T getItem(int i) {
        return mDataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(getItemViewLayout(), viewGroup, false);
            view.setTag(getItemViewHolder(view));
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.bind(i);
        return view;
    }

    protected abstract V getItemViewHolder(View view);

    @LayoutRes
    protected abstract int getItemViewLayout();


    public void reset(List<T> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
            notifyDataSetChanged();
        }
    }
    // 数据添加
    public void addAll(List<T> dataSet){
        if (dataSet!=null) mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public abstract class ViewHolder {
        View mItemView;

        public ViewHolder(View itemView) {

            ButterKnife.bind(this, itemView);
            mItemView = itemView;
        }

        // 让数据和视图绑定
        public abstract void bind(int i);

        // 提供一个可以直接拿到上下文的方法
        protected final Context getContext() {
            return mItemView.getContext();
        }
    }
}
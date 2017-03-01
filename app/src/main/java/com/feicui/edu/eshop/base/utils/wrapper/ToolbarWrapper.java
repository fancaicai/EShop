package com.feicui.edu.eshop.base.utils.wrapper;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseActivity;
import com.feicui.edu.eshop.base.utils.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class ToolbarWrapper {
    private BaseActivity mBaseActivity;
    private TextView mTvTitle;

    // 在Activity里面使用
    public ToolbarWrapper(BaseActivity activity) {
        mBaseActivity = activity;
        Toolbar toolbar = ButterKnife.findById(activity, R.id.standard_toolbar);
        init(toolbar);
        // 标题不设置(TextView展示)、返回箭头有的
        setShowBack(true);// 显示返回箭头
        setShowTitle(false);// 不显示默认标题
    }
    // 在Fragment里面使用
    public ToolbarWrapper(BaseFragment fragment){
        mBaseActivity= (BaseActivity) fragment.getActivity();
        Toolbar toolbar=ButterKnife.findById(fragment.getView(),R.id.standard_toolbar);
        init(toolbar);
        // Fragment显示选项菜单
        fragment.setHasOptionsMenu(true);

        // 标题不设置(TextView展示)、返回箭头没有
        setShowBack(false);
        setShowTitle(false);
    }
    // 为了方便链式调用，所以返回值为本身
    private ToolbarWrapper setShowTitle(boolean isShowTitle) {
        getSupportActionBar().setDisplayShowTitleEnabled(isShowTitle);
        return this;

    }

    // 为了方便链式调用，所以返回值为本身
    public ToolbarWrapper setShowBack(boolean isShowback) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShowback);
        return this;
    }

    private void init(Toolbar toolbar) {
        // 找到标题的textView
        mTvTitle = ButterKnife.findById(toolbar, R.id.standard_toolbar_title);
        // 设置Toolbar作为Actionbar展示
        mBaseActivity.setSupportActionBar(toolbar);
    }
    // 设置自定义标题
    public ToolbarWrapper setCustomTitle(int resId){

        if (mTvTitle==null){
            throw new UnsupportedOperationException("No title TextView in Toolbar");
        }
        mTvTitle.setText(resId);
        return this;
    }
    private ActionBar getSupportActionBar() {
        return mBaseActivity.getSupportActionBar();
    }

}

package com.feicui.edu.eshop.feature.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseActivity;
import com.feicui.edu.eshop.network.entity.Filter;
import com.google.gson.Gson;

public class SearchGoodActivity extends BaseActivity {
    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";
    // 因为需要传递数据，为了规范我们传递的数据内容，所以我们在此页面对外提供一个跳转的方法
    public static Intent getStartIntent(Context context, Filter filter){
        Intent intent = new Intent(context,SearchGoodActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER,new Gson().toJson(filter));
        return intent;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_search;
    }
}

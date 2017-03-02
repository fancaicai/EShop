package com.feicui.edu.eshop.feature.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseActivity;
import com.feicui.edu.eshop.base.utils.widgets.banner.SimpleSearchView;
import com.feicui.edu.eshop.base.utils.wrapper.PtrWrapper;
import com.feicui.edu.eshop.base.utils.wrapper.ToolbarWrapper;
import com.feicui.edu.eshop.network.entity.Filter;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class SearchGoodActivity extends BaseActivity {
    @BindViews({R.id.text_is_hot,R.id.text_most_expensive,R.id.text_cheapest})
    List<TextView> mTvOrderList;
    @BindView(R.id.list_goods)
    ListView mGoodsListView;
    @BindView(R.id.search_view)
    SimpleSearchView mSearchView;
    private PtrWrapper mPtrWrapper;
    private Filter mFilter;
    private SearchGoodsAdapter mGoodsAdapter;
    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";
    // 因为需要传递数据，为了规范我们传递的数据内容，所以我们在此页面对外提供一个跳转的方法
    public static Intent getStartIntent(Context context, Filter filter){
        Intent intent = new Intent(context,SearchGoodActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER,new Gson().toJson(filter));
        return intent;
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_search;
    }
    @Override
    protected void initView() {
        // toolbar的展示
        new ToolbarWrapper(this);
        // 一进入，默认选择热销
        mTvOrderList.get(0).setActivated(true);
        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter=new Gson().fromJson(filterStr ,Filter.class);
        // 刷新加载
        mPtrWrapper=new PtrWrapper(this,true) {
            // 刷新
            @Override
            protected void onRefresh() {
                // 要进行网络请求获取数据
                searchGoods(true);
            }
            // 加载
            @Override
            protected void onLoadMore() {
                // 也是要进行请求，和刷新是一个接口，只是分页的参数不一样
                searchGoods(false);
            }
        };
        mSearchView.setOnSearchListener(new SimpleSearchView.OnSearchListener() {
            // 当去搜索会触发
            @Override
            public void search(String query) {
                mFilter.setKeywords(query);
                mPtrWrapper.autoRefresh();

            }
        });
        // 处理ListView:设置适配器
        mGoodsAdapter = new SearchGoodsAdapter();
        mGoodsListView.setAdapter(mGoodsAdapter);

        // 自动刷新
        mPtrWrapper.postRefreshDelayed(50);
    }

    private void searchGoods(boolean isRefresh) {

    }

}

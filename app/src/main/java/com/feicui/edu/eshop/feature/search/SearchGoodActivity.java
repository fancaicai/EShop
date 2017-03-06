package com.feicui.edu.eshop.feature.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseActivity;
import com.feicui.edu.eshop.base.utils.LogUtils;
import com.feicui.edu.eshop.base.utils.widgets.banner.SimpleSearchView;
import com.feicui.edu.eshop.base.utils.wrapper.PtrWrapper;
import com.feicui.edu.eshop.base.utils.wrapper.ToolbarWrapper;
import com.feicui.edu.eshop.network.EShopClient;
import com.feicui.edu.eshop.network.core.ApiPath;
import com.feicui.edu.eshop.network.core.ResponseEntity;
import com.feicui.edu.eshop.network.core.UICallback;
import com.feicui.edu.eshop.network.entity.Filter;
import com.feicui.edu.eshop.network.entity.Paginated;
import com.feicui.edu.eshop.network.entity.Pagination;
import com.feicui.edu.eshop.network.entity.SearchReq;
import com.feicui.edu.eshop.network.entity.SearchRsp;
import com.feicui.edu.eshop.network.entity.SimpleGoods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;

public class SearchGoodActivity extends BaseActivity {
    @BindViews({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    List<TextView> mTvOrderList;
    @BindView(R.id.list_goods)
    ListView mGoodsLV;
    @BindView(R.id.search_view)
    SimpleSearchView mSearchView;
    private PtrWrapper mPtrWrapper;
    private Filter mFilter;
    private SearchGoodsAdapter mGoodsAdapter;
    private Call mSearchCall;
    private long mLastRefreshTime;
    private Pagination mPagination = new Pagination();
    private Paginated mPaginated;
    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";

    // 因为需要传递数据，为了规范我们传递的数据内容，所以我们在此页面对外提供一个跳转的方法
    public static Intent getStartIntent(Context context, Filter filter) {
        Intent intent = new Intent(context, SearchGoodActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER, new Gson().toJson(filter));
        return intent;
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initView() {
        // toolbar的展示
        new ToolbarWrapper(this);
        // 一进入，默认选择热销
        mTvOrderList.get(0).setActivated(true);
        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter = new Gson().fromJson(filterStr, Filter.class);
        // 刷新加载
        mPtrWrapper = new PtrWrapper(this, true) {
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
        mGoodsLV.setAdapter(mGoodsAdapter);

        // 自动刷新
        mPtrWrapper.postRefreshDelayed(50);
    }

    @OnClick({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    public void chooseGoodsOrder(View view) {

        // 如果当前已经是此项，就不触发
        if (view.isActivated()) return;
        // 如果在刷新，不去执行
        if (mPtrWrapper.isRefreshing()) return;
        // 三个都不是活动状态
        for (TextView sortView :mTvOrderList) {
            sortView.setActivated(false);
        }
        // 选择的某项设置为Activated
        view.setActivated(true);

        // 排序字段
        String sortBy;
        switch (view.getId()) {
            case R.id.text_is_hot:
                sortBy = Filter.SORT_IS_HOT;
                break;
            case R.id.text_most_expensive:
                sortBy = Filter. SORT_PRICE_DESC;
                break;
            case R.id.text_cheapest:
                sortBy = Filter.SORT_PRICE_ASC;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        mFilter.setSortBy(sortBy);
        // 简单解决切换过快的问题
        long time = 2000+mLastRefreshTime-System.currentTimeMillis();
        time = time<0?0:time;
        mPtrWrapper.postRefreshDelayed(time);
    }

    // 网络请求获取数据
    private void searchGoods(boolean isRefresh) {
        if (mSearchCall != null) {
            mSearchCall.cancel();
        }
        if (isRefresh) {
            mLastRefreshTime = System.currentTimeMillis();
            // 刷新：页数从1开始
            mPagination.reset();
            // 将ListView定位到第一条
            mGoodsLV.setSelection(0);
        } else {
            // 加载：页数+1
            mPagination.next();
            LogUtils.debug("Load More page = " + mPagination.getPage());
        }
        // 请求体
        SearchReq searchReq = new SearchReq();
//        这为什么传这个参数就不行啊
        searchReq.setFilter(mFilter);
        searchReq.setPagination(mPagination);
        mSearchCall= EShopClient.getInstance().enqueue(ApiPath.SEARCH,searchReq, SearchRsp.class,mUICallback);

    }
    private UICallback mUICallback = new UICallback() {
        // 数据的处理
        @Override
        protected void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
            mPtrWrapper.stopRefresh();
            mSearchCall = null;
            if (isSucces) {
                SearchRsp searchRsp = (SearchRsp) responseEntity;
                // 将分页结果拿出来，便于判断下次加载需要需要进行
                mPaginated = searchRsp.getPaginated();

                // 设置数据给适配器
                List<SimpleGoods> goodsList = searchRsp.getData();
                if (mPagination.isFirst()) {
                    // 刷新
                    mGoodsAdapter.reset(goodsList);
                } else {
                    // 加载
                    mGoodsAdapter.addAll(goodsList);
                }
            }
        }
    };
}

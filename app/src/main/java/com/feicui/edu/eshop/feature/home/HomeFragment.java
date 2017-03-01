package com.feicui.edu.eshop.feature.home;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseFragment;
import com.feicui.edu.eshop.base.utils.widgets.banner.BannerAdapter;
import com.feicui.edu.eshop.base.utils.widgets.banner.BannerLayout;
import com.feicui.edu.eshop.base.utils.wrapper.PtrWrapper;
import com.feicui.edu.eshop.base.utils.wrapper.ToastWrapper;
import com.feicui.edu.eshop.base.utils.wrapper.ToolbarWrapper;
import com.feicui.edu.eshop.network.EShopClient;
import com.feicui.edu.eshop.network.core.UICallback;
import com.feicui.edu.eshop.network.entity.Banner;
import com.feicui.edu.eshop.network.entity.HomeBannerRsp;
import com.feicui.edu.eshop.network.entity.HomeCategoryRsp;
import com.feicui.edu.eshop.network.entity.Picture;
import com.feicui.edu.eshop.network.entity.SimpleGoods;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_home_goods)
    ListView mListHomeGoods;
    @BindView(R.id.standard_refresh_layout)
    PtrFrameLayout mRefreshLayout;
    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mMTvPromoteGoods;
    private BannerAdapter<Banner> mBannerAdapter;
    private HomeGoodsAdapter mGoodsAdapter;
    private PtrWrapper mPtrWrapper;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }
    @Override
    protected void initView() {
        // 利用Toolbar的包装类
        new ToolbarWrapper(this).setCustomTitle(R.string.home_title);
        // 利用刷新的包装类
        mPtrWrapper=new PtrWrapper(this,false) {
            @Override
            protected void onRefresh() {
                // 请求数据刷新页面
                getHomeData();
            }

            @Override
            protected void onLoadMore() {

            }
        };
        mPtrWrapper.postRefreshDelayed(50);
        // ListView的头布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.partial_home_header, mListHomeGoods, false);
// 找到头布局里面的控件
        BannerLayout bannerLayout = ButterKnife.findById(view, R.id.layout_banner);
        // 数据和视图的绑定
// TODO: 2017/2/28 图片展示待实现
        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override
            protected void bind(ViewHolder viewHolder, Banner data) {
                // 数据和视图的绑定
                viewHolder.imageBannerItem.setImageResource(R.drawable.image_holder_banner);
                Picasso.with(getContext()).load(data.getPicture().getLarge()).into(viewHolder.imageBannerItem);
            }

        };
        bannerLayout.setAdapter(mBannerAdapter);
        // 促销商品
        mIvPromotes[0] = ButterKnife.findById(view, R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view, R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view, R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view, R.id.image_promote_four);
        // 促销单品的TextView
        mMTvPromoteGoods = ButterKnife.findById(view, R.id.text_promote_goods);
        mListHomeGoods.addHeaderView(view);
        // 设置适配器
        mGoodsAdapter = new HomeGoodsAdapter();
        mListHomeGoods.setAdapter(mGoodsAdapter);

    }


    // 去请求数据
    private void getHomeData() {
        // 轮播图和促销单品的数据
        Call bannerCall = EShopClient.getInstance().getHomeBanner();
        bannerCall.equals(new UICallback() {
            @Override
            protected void onFailureInUI(Call call, IOException e) {
                ToastWrapper.show("请求失败"+e.getMessage());
            }

            @Override
            protected void onResponseInUI(Call call, Response response) {
                // 返回成功
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        // 解析拿到数据
                        HomeBannerRsp bannerRsp = new Gson().fromJson(json, HomeBannerRsp.class);
                        if (bannerRsp.getStatus().isSucceed()) {
                            // 数据拿到了，首先给bannerAdapter,另外是给促销单品
                            mBannerAdapter.reset(bannerRsp.getData().getBanners());
                            setPromoteGoods(bannerRsp.getData().getGoodsList());
                        } else {
                            ToastWrapper.show(bannerRsp.getStatus().getErrorDesc());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
// 推荐的分类商品
        Call categoryCall = EShopClient.getInstance().getHomeCategory();
        categoryCall.enqueue(new UICallback() {
            @Override
            protected void onFailureInUI(Call call, IOException e) {
                ToastWrapper.show("请求失败"+e.getMessage());
            }

            @Override
            protected void onResponseInUI(Call call, Response response) {
                if (response.isSuccessful()){
                    try {
                        String json = response.body().string();
                        HomeCategoryRsp categoryRsp = new Gson().fromJson(json, HomeCategoryRsp.class);
                        if (categoryRsp.getStatus().isSucceed()){
                     // 拿到了推荐分类商品的数据
                            mGoodsAdapter.reset(categoryRsp.getData());
                        }else {
                            ToastWrapper.show(categoryRsp.getStatus().getErrorDesc());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        // 拿到数据之后，停止刷新
        mPtrWrapper.stopRefresh();
    }
    // 设置促销单品的展示
    private void setPromoteGoods(List<SimpleGoods> goodsList) {
        mMTvPromoteGoods.setVisibility(View.VISIBLE);
        for (int i = 0; i <mIvPromotes.length ; i++) {
            mIvPromotes[i].setVisibility(View.VISIBLE);
            final SimpleGoods simpleGoods = goodsList.get(i);
            Picture picture = simpleGoods.getImg();
//            mIvPromotes[i].setImageResource(R.drawable.image_holder_goods);
            // 圆形、灰度
            Picasso.with(getContext()).load(picture.getLarge())
                    .transform(new CropCircleTransformation())// 圆形
                    .transform(new GrayscaleTransformation())// 灰度
                    .into(mIvPromotes[i]);
            mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastWrapper.show(simpleGoods.getName());
                }
            });

        }

    }



}

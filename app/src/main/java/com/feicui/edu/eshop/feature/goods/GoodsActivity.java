package com.feicui.edu.eshop.feature.goods;

import android.content.Context;
import android.content.Intent;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseActivity;


/**
 * Created by gqq on 2017/3/2.
 */
// 商品页面
public class GoodsActivity extends BaseActivity{

    private static final String EXTRA_GOODS_ID = "EXTRA_GOOD_ID";

    // 提供一个跳转的方法
    public static Intent getStartIntent(Context context, int goodsId){
        Intent intent = new Intent(context,GoodsActivity.class);
        intent.putExtra(EXTRA_GOODS_ID,goodsId);
        return intent;
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_goods;
    }

    @Override
    protected void initView() {

    }
}

package com.feicui.edu.eshop.feature.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseListAdapter;
import com.feicui.edu.eshop.base.utils.wrapper.ToastWrapper;
import com.feicui.edu.eshop.network.entity.CategoryHome;
import com.feicui.edu.eshop.network.entity.Picture;
import com.feicui.edu.eshop.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class HomeGoodsAdapter extends BaseListAdapter<CategoryHome, HomeGoodsAdapter.ViewHolder> {


    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_home_goods;
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView mtextCategory;
        @BindViews(
                {R.id.image_goods_01,
                R.id.image_goods_02,
                R.id.image_goods_03,
                R.id.image_goods_04}
        )
        ImageView[] mImageViews;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            CategoryHome categoryHome=getItem(position);
            mtextCategory.setText(categoryHome.getName());
            final List<SimpleGoods> goodsList=categoryHome.getHotGoodsList();
            for (int i = 0; i <mImageViews.length ; i++) {
        // 取出商品List里面的商品图片
                Picture picture = goodsList.get(i).getImg();
//                // TODO: 2017/2/28 图片加载待实现
//                mImageViews[i].setImageResource(R.drawable.image_holder_goods);
                // Picasso加载图片
                Picasso.with(getContext()).load(picture.getLarge()).into(mImageViews[i]);
                // 设置点击事件
                final int index = i;
                mImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SimpleGoods simpleGoods = goodsList.get(index);
                        ToastWrapper.show(simpleGoods.getName());
                    }
                });

            }

        }

    }
}

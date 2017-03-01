package com.feicui.edu.eshop.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseListAdapter;
import com.feicui.edu.eshop.network.entity.CategoryPrimary;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/2/25 0025.
 */

public class CategoryAdapter extends BaseListAdapter<CategoryPrimary, CategoryAdapter.ViewHolder> {



    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_primary_category;
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView textCategory;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            textCategory.setText(getItem(position).getName());
        }
    }
}

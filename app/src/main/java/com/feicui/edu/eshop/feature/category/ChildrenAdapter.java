package com.feicui.edu.eshop.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseListAdapter;
import com.feicui.edu.eshop.network.entity.CategoryBase;

import butterknife.BindView;

/**
 * 子分类的适配器
 */

public class ChildrenAdapter extends BaseListAdapter<CategoryBase,ChildrenAdapter.ViewHolder>  {


    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_children_category;
    }
    class ViewHolder extends BaseListAdapter.ViewHolder{
@BindView( R.id.text_category)
        TextView mTextCategory;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}

package com.feicui.edu.eshop.feature.category;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.edu.eshop.R;
import com.feicui.edu.eshop.base.utils.BaseFragment;
import com.feicui.edu.eshop.base.utils.wrapper.ToastWrapper;
import com.feicui.edu.eshop.base.utils.wrapper.ToolbarWrapper;
import com.feicui.edu.eshop.feature.search.SearchGoodActivity;
import com.feicui.edu.eshop.network.EShopClient;
import com.feicui.edu.eshop.network.core.ApiPath;
import com.feicui.edu.eshop.network.core.ResponseEntity;
import com.feicui.edu.eshop.network.core.UICallback;
import com.feicui.edu.eshop.network.entity.CategoryPrimary;
import com.feicui.edu.eshop.network.entity.CategoryRsp;
import com.feicui.edu.eshop.network.entity.Filter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.standard_toolbar_title)
    TextView standardToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar standardToolbar;
    @BindView(R.id.list_category)
    ListView listCategory;
    @BindView(R.id.list_children)
    ListView listChildren;
    private List<CategoryPrimary> mData;
    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {
        initToolbar();
        mCategoryAdapter = new CategoryAdapter();
        listCategory.setAdapter(mCategoryAdapter);

        mChildrenAdapter = new ChildrenAdapter();
        listChildren.setAdapter(mChildrenAdapter);
        // 拿到数据
        if (mData != null) {
      // 可以直接更新UI
            updateCategory();
        } else {
        // 去进行网络请求拿到数据
            UICallback uiCallback =new UICallback() {
                //请求成功的处理
                @Override
                protected void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
                    if (isSucces) {
                        CategoryRsp categoryRsp = (CategoryRsp) responseEntity;
                        mData=categoryRsp.getData();
                        // 数据有了之后，数据给一级分类，默认选择第一条，二级分类才能展示
                        updateCategory();
                    }
                }
            };
            EShopClient.getInstance().enqueue(ApiPath.CATEGORY,null,CategoryRsp.class,uiCallback);
        }
    }

    // 更新分类数据
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }

    // 用于根据一级分类的选项展示二级分类的内容
    private void chooseCategory(int position) {
        listCategory.setItemChecked(position, true);
        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类：展示相应二级分类
    @OnItemClick(R.id.list_category)
    public void ItemClick(int postion) {
        chooseCategory(postion);
    }

    // 点击二级分类
    @OnItemClick(R.id.list_children)
    public void onChildrenClick(int postion) {
        int categoryId = mChildrenAdapter.getItem(postion).getId();
        navigateToSearch(categoryId);
    }

    private void initToolbar() {
        // 利用包装好的Toolbar
        new ToolbarWrapper(this).setCustomTitle(R.string.category_title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
//        返回箭头的处理
        if (itemId == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        if (itemId == R.id.menu_search) {
            int position = listCategory.getCheckedItemPosition();
            int id = mCategoryAdapter.getItem(position).getId();
            navigateToSearch(id);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void navigateToSearch(int categoryId) {
        // 根据id构建Filter，然后跳转页面
        Filter filter = new Filter();
        filter.setCategoryId(categoryId);
        Intent intent = SearchGoodActivity.getStartIntent(getContext(), filter);
        getActivity().startActivity(intent);
    }


}

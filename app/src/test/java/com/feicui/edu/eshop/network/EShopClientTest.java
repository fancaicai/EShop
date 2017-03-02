package com.feicui.edu.eshop.network;

import com.feicui.edu.eshop.network.entity.CategoryRsp;
import com.feicui.edu.eshop.network.entity.HomeBannerRsp;
import com.feicui.edu.eshop.network.entity.HomeCategoryRsp;
import com.feicui.edu.eshop.network.entity.SearchReq;
import com.feicui.edu.eshop.network.entity.SearchRsp;
import com.google.gson.Gson;

import org.junit.Test;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/2/23 0023.
 */
//单元测试：测试接口
public class EShopClientTest {
    @Test
    public void getCategory() throws Exception {
        CategoryRsp categoryRsp = EShopClient.getInstance().execute("/category", null, CategoryRsp.class);
// 断言方法：为我们做一个判断
        assertTrue(categoryRsp.getStatus().isSucceed());
//        单元测试不知道怎么运行来着
    }
    @Test
    public void getHomeBanner() throws Exception{
        HomeBannerRsp bannerRsp = EShopClient.getInstance().execute("/home/data", null, HomeBannerRsp.class);
        assertTrue(bannerRsp.getStatus().isSucceed());

    }
    @Test
    public void getHomeCategory() throws Exception{
        CategoryRsp categoryRsp = EShopClient.getInstance().execute("/home/category", null, CategoryRsp.class);
        assertTrue(categoryRsp.getStatus().isSucceed());
    }
@Test
    public void getSearch() throws Exception{
    SearchReq searchReq = new SearchReq();
    SearchRsp searchRsp = EShopClient.getInstance().execute("/search",searchReq,SearchRsp.class);

    assertTrue(searchRsp.getStatus().isSucceed());
}
}
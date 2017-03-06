package com.feicui.edu.eshop.base.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.feicui.edu.eshop.R;


/**
 * Created by gqq on 2017/2/24.
 */

// 有转场动画的基类
public class TransitionActivity extends AppCompatActivity {

    // 处理返回箭头的事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // 设置转场动画
        setTransitionAnimation(true);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        setTransitionAnimation(true);
    }

    @Override
    public void finish() {
        super.finish();
        setTransitionAnimation(false);
    }

    public void finishWithDefault(){
        super.finish();
    }

    private void setTransitionAnimation(boolean isNewActivity) {
        if (isNewActivity){
            // 新页面从右边进入
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        }else {
            // 回到上个页面
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }
}

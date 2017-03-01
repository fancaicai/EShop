package com.feicui.edu.eshop.base.utils;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicui.edu.eshop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class TestFragment extends android.support.v4.app.Fragment {
    private static final String FRAGMENT_TEXT = "fragment_text";
    @BindView(R.id.text)
    TextView text;

    // 对外提供一个创建方法：传递数据
    public static TestFragment newInstance(String text) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TEXT, text);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        text.setText(getArgumentText());
        return view;
    }

    public String getArgumentText() {
        return getArguments().getString(FRAGMENT_TEXT);
    }

}

package com.edu.zwu.hydrops.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.SearchFragment;
import com.edu.zwu.hydrops.util.AppUtil;

/**
 * Created by shengwei.yi on 2016/4/8.
 */
public class SearchActivity extends BaseActivity {
    private SearchFragment mSearchFragment;
    private EditText mSearchInput;
    private InputMethodManager mInputMethodManager;
    private String mKeywords;
    private long mLastClicked = 0;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initSearchActionBar();
    }

    @Override
    protected BaseFragment getFragment() {
        mSearchFragment = new SearchFragment();
        return mSearchFragment;
    }

    private void initSearchActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setIcon(null);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.search_actionbar);
        actionBarLeftBtn = (ImageView) findViewById(R.id.actionbar_left_btn);
        actionBarLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popView();
            }
        });
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ImageView searchCancelImageView = (ImageView) findViewById(R.id.search_cancel);
        if (searchCancelImageView != null) {
            searchCancelImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchInput.setText("");
                    mSearchInput.requestFocus();
                }
            });
        }
        ImageView searchBtnImageView = (ImageView) findViewById(R.id.search_btn);
        if (searchBtnImageView != null) {
            searchBtnImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mKeywords = mSearchInput.getText().toString().trim();

                    // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                    mSearchFragment.mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(mKeywords)
                            .city("杭州"));
                }
            });
        }
        mSearchInput.requestFocus();
        mSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    handled = true;
                    mKeywords = mSearchInput.getText().toString().trim();
                    // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                    mSearchFragment.mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(mKeywords)
                            .city("杭州"));
                    if (TextUtils.isEmpty(mKeywords)) {
                        AppUtil.showShortMessage(SearchActivity.this, "请输入搜索内容");
                        //noinspection ConstantConditions
                        return handled;
                    }

                    if (System.currentTimeMillis() - mLastClicked > 100) {
                        mLastClicked = System.currentTimeMillis();
                        mInputMethodManager.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
                    }

                }
                return handled;
            }
        });

        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mKeywords = s.toString().trim();
                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSearchFragment.mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(mKeywords)
                        .city("杭州"));
            }
        });
    }

}

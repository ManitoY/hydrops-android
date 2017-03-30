package com.edu.zwu.hydrops.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.interfaces.WebViewActionListener;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.util.WebViewUtil;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class WebPagerFragment extends BaseFragment  implements WebViewActionListener {

    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.webview_progress)
    ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web_pager;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        String webUrl = thisActivity.getIntent().getStringExtra("webUrl");
        WebViewUtil webViewUtil = new WebViewUtil(this);
        webViewUtil.initWebView(mWebView, webUrl);
    }

    @Override
    public void changeProgress(int progress) {
        if (progress == 100) {
            mProgressBar.setVisibility(View.GONE);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } else {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = AppUtil.showProgress(thisActivity);
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress);
        }
    }
}

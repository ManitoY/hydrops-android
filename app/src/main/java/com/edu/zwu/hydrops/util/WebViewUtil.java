package com.edu.zwu.hydrops.util;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import com.edu.zwu.hydrops.interfaces.WebViewActionListener;

import java.util.Map;

/**
 * Created by shengwei.yi on 2016/3/19.
 */
public class WebViewUtil {
    private static final String SCALE_PARAM = "1";  // 这个表示支持缩放

    private WebViewActionListener webViewActionListener;

    public WebViewUtil(WebViewActionListener listener) {
        this.webViewActionListener = listener;
    }

    public void initWebView(final WebView webView, String url) {
        if (TextUtils.isEmpty(url)) {
            url = "http://www.baidu.com";
        }

        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        Map<String, String> map = AppUtil.getRequestParamsMap(url, "&");
        if (SCALE_PARAM.equals(map.get("scale"))) {
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webView.getSettings().setDisplayZoomControls(false);
            } else {
                try {
                    final ZoomButtonsController zoomButtonsController = (ZoomButtonsController) webView.getClass().getMethod("getZoomButtonsController").invoke(webView, (Object[]) null);
                    zoomButtonsController.getContainer().setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.requestFocus();

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (webViewActionListener != null) {
                    webViewActionListener.changeProgress(newProgress);
                }
            }
        });

    }
}

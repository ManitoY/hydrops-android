package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class SearchPresenter extends BasePresenter<SearchPresenter.SearchView> {

    public SearchPresenter(SearchView view, Context context) {
        super(view, context);
    }

    public OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                return;
                //未找到相关结果
            }
            //获取在线建议检索结果
            mView.refreshViewByReplaceData(res);
        }
    };

    public void setBundle(double latitude, double longitude){
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        i.putExtras(bundle);
        mView.setActivityResult(i);
    }

    public interface SearchView extends BaseView {
        void refreshViewByReplaceData(SuggestionResult res);

        void setActivityResult(Intent i);
    }
}

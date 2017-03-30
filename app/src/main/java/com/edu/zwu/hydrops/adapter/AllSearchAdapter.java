package com.edu.zwu.hydrops.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.view.FontTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shengwei.yi on 2016/4/9.
 */
public class AllSearchAdapter extends BaseRecyclerListAdapter<SuggestionResult.SuggestionInfo, AllSearchAdapter.ViewHolder>{
    @Override
    protected AllSearchAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_search_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder viewHolder, int position) {
        SuggestionResult.SuggestionInfo si = mDataList.get(position);
        if(TextUtils.isEmpty(si.city)){
            si.city = "";
        }
        if(TextUtils.isEmpty(si.district)){
            si.district = "";
        }
        viewHolder.mSearchDistrict.setText(si.city + si.district);
        viewHolder.mSearchKey.setText(si.key);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.search_key)
        FontTextView mSearchKey;

        @Bind(R.id.search_district)
        FontTextView mSearchDistrict;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}

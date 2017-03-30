package com.edu.zwu.hydrops.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.bmob.News;
import com.edu.zwu.hydrops.view.FontTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shengwei.yi on 2016/3/19.
 */
public class AllNewsAdapter extends BaseRecyclerListAdapter<News, AllNewsAdapter.ViewHolder>{

    private Activity thisActivity;

    public AllNewsAdapter(Activity activity){thisActivity = activity;}

    @Override
    protected AllNewsAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_new_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(AllNewsAdapter.ViewHolder viewHolder, int position) {
        News item = mDataList.get(position);
        Glide.with(thisActivity).load(item.imgStr).error(R.drawable.head).placeholder(R.drawable.head).into(viewHolder.mImgStr);
        viewHolder.mTitle.setText(item.title);
        viewHolder.mContent.setText(item.content);
        viewHolder.mDate.setText(item.getCreatedAt());
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.new_list_img)
        ImageView mImgStr;
        @Bind(R.id.new_list_title)
        FontTextView mTitle;
        @Bind(R.id.new_list_content)
        FontTextView mContent;
        @Bind(R.id.new_list_date)
        FontTextView mDate;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}

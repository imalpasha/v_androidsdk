package com.vav.cn.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.listener.ViewClickListener;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/20/2016.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder> {
    private Activity mActivity;
    private List<String> mSearchHistoryItemList;
    private ViewClickListener viewClickListener;

    public SearchListAdapter(Activity activity, List<String> searchHistoryItemList, ViewClickListener viewClickListener) {
        mActivity = activity;
        mSearchHistoryItemList = searchHistoryItemList;
        this.viewClickListener = viewClickListener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_list_item, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        String currentItem = mSearchHistoryItemList.get(position);

        holder.getLblItemSearch().setText(currentItem);
    }

    @Override
    public int getItemCount() {
        if (mSearchHistoryItemList != null) {
            return mSearchHistoryItemList.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblItemSearch;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setFocusable(true);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            lblItemSearch = (TextView) itemView.findViewById(R.id.lblSearchHistoryItem);
        }

        public TextView getLblItemSearch() {
            return lblItemSearch;
        }

        public void setLblItemSearch(TextView lblItemSearch) {
            this.lblItemSearch = lblItemSearch;
        }

        @Override
        public void onClick(View v) {
            if (viewClickListener != null) {
                viewClickListener.onItemClick(mSearchHistoryItemList.get(this.getAdapterPosition()));
            }
        }
    }
}

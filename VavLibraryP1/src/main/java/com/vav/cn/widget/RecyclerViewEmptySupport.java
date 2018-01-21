package com.vav.cn.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Handrata Samsul on 10/19/2015.
 */
public class RecyclerViewEmptySupport extends RecyclerView {
    private View mEmptyView;
    private AdapterDataObserver mEmptyDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.INVISIBLE);
                } else {
                    mEmptyView.setVisibility(View.INVISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.INVISIBLE);
                } else {
                    mEmptyView.setVisibility(View.INVISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public RecyclerViewEmptySupport(Context context, View emptyView) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
        }

        mEmptyDataObserver.onChanged();
    }

    public void setmEmptyDataObserver() {
        if (this.getAdapter() != null) {
            this.getAdapter().unregisterAdapterDataObserver(mEmptyDataObserver);
        }
    }

    public void onDataChange() {
        mEmptyDataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void removeEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
            this.setVisibility(VISIBLE);
            mEmptyView = null;
        }
    }
}

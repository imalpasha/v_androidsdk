package com.vav.cn.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Handrata Samsul on 1/14/2016.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;


    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        //last item no space
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}
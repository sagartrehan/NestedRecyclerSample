package com.ecommerce.ui.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ProductItemDecorator extends RecyclerView.ItemDecoration {

    private final int mRightEndSpacing;

    public ProductItemDecorator(int rightEndSpacing) {
        this.mRightEndSpacing = rightEndSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = mRightEndSpacing;
        }
    }
}

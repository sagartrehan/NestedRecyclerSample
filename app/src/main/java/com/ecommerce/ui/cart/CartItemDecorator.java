package com.ecommerce.ui.cart;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CartItemDecorator extends RecyclerView.ItemDecoration {

    private final int mBottomEndSpacing;

    public CartItemDecorator(int bottomEndSpacing) {
        this.mBottomEndSpacing = bottomEndSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mBottomEndSpacing;
        }
    }
}

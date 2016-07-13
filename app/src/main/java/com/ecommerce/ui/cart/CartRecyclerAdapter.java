package com.ecommerce.ui.cart;

import android.app.Activity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.ecommerce.R;
import com.ecommerce.product.Product;
import com.ecommerce.ui.home.ProductRecyclerAdapter;

import java.util.List;

public class CartRecyclerAdapter
    extends ProductRecyclerAdapter
    implements View.OnCreateContextMenuListener {

    public interface OnActionItemSelected {
        void onRemoveFromCartSelected(int position);
    }

    private OnActionItemSelected mActivityCallbacks;

    public CartRecyclerAdapter(Activity context, List<Product> productList, OnActionItemSelected callbacks) {
        super(context, productList);
        mActivityCallbacks = callbacks;
    }

    @Override
    protected void handleMenuActions(final ProductViewHolder holder) {
        holder.mActionButton.setVisibility(View.VISIBLE);
        holder.mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }

    private void showPopupMenu(final View mActionButton, final int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, mActionButton) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove_from_cart:
                        mActivityCallbacks.onRemoveFromCartSelected(position);
                        return true;
                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };
        popupMenu.inflate(R.menu.cart_item_menu);
        popupMenu.show();
    }

}

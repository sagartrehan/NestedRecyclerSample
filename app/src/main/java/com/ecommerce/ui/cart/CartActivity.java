package com.ecommerce.ui.cart;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.product.Product;
import com.ecommerce.util.AppUtil;
import com.ecommerce.util.common.GenericActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity
    extends GenericActivity<CartOps.View, CartOps>
    implements CartOps.View {

    private CartRecyclerAdapter mAdapter;
    private List<Product> mProducts;
    private TextView mTotalAmountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, CartOps.class, this);
        setContentView(R.layout.activity_cart);
        initViews();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getOps().cancel();
    }

    @Override
    protected void initViews() {
        super.initViews();
        setUpToolbar("Cart Details", true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new CartItemDecorator(getResources().getDimensionPixelOffset(R.dimen.product_spacing)));
        // Set Layout Manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        // Initialize and set Adapter
        mProducts = new ArrayList<>();
        mAdapter = new CartRecyclerAdapter(this, mProducts, mActionItemSelected);
        recyclerView.setAdapter(mAdapter);

        mTotalAmountView = (TextView) findViewById(R.id.total_amount);
    }

    // region Presenter Callbacks
    @Override
    public void onCartDetailsLoadSuccess(List<Product> cartProducts) {
        mProducts.clear();
        mProducts.addAll(cartProducts);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartDetailsLoadError(Throwable throwable) {
        showMessage(getString(R.string.empty_cart), true);
    }

    @Override
    public void onRemoveFromCartResult(boolean isSuccess, Product product) {
        if (isSuccess) {
            int index = mProducts.indexOf(product);
            if (index != -1) {
                mProducts.remove(product);
                mAdapter.notifyItemRemoved(index);

                if (AppUtil.isEmpty(mProducts)) {
                    showMessage(getString(R.string.empty_cart), true);
                }
            }
        }
    }

    @Override
    public void updateTotalAmount(long totalAmount) {
        if (totalAmount != 0) {
            mTotalAmountView.setText(getString(R.string.cart_total_amount, totalAmount));
            mTotalAmountView.setVisibility(View.VISIBLE);
        } else {
            mTotalAmountView.setVisibility(View.GONE);
        }
    }
    // endregion

    private void init() {
        getOps().getAllCartItems();
    }

    // region Action Listener
    CartRecyclerAdapter.OnActionItemSelected mActionItemSelected = new CartRecyclerAdapter.OnActionItemSelected() {
        @Override
        public void onRemoveFromCartSelected(int position) {
            getOps().removeFromCart(mProducts.get(position));
        }
    };
    // endregion
}
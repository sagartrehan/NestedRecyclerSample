package com.ecommerce.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.product.Product;
import com.ecommerce.util.AppConstants;
import com.ecommerce.util.UIUtils;
import com.ecommerce.util.common.GenericActivity;
import com.metova.cappuccino.Cappuccino;

public class ProductDetailsActivity
    extends GenericActivity<ProductDetailOps.View, ProductDetailOps>
    implements ProductDetailOps.View {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private TextView mName;
    private TextView mPrice;
    private ImageView mThumbnail;
    private Button mCartActionButton;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, ProductDetailOps.class, this);
        setContentView(R.layout.activity_product_details);
        Cappuccino.newIdlingResourceWatcher(TAG);
        initViews();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
        setUpToolbar("Product Details", true);

        mHasDefaultMenuItems = true;

        mName = (TextView) findViewById(R.id.name);
        mPrice = (TextView) findViewById(R.id.price);
        mThumbnail = (ImageView) findViewById(R.id.thumbnail);
        mCartActionButton = (Button) findViewById(R.id.cart_action);

        mCartActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cappuccino.getResourceWatcher(TAG).busy();
                if (mProduct.isInCart()) {
                    getOps().removeFromCart();
                } else {
                    getOps().addToCart();
                }
            }
        });
    }

    @Override
    public void onProductDetailsLoadSuccess(Product product) {
        mProduct = product;
        mName.setText(product.getName());
        mPrice.setText(getString(R.string.price, product.getPrice()));
        UIUtils.setImage(this, mThumbnail, product);
        updateCartActionButton(product.isInCart());
        Cappuccino.getResourceWatcher(TAG).idle();
    }

    @Override
    public void onProductDetailsLoadError(Throwable throwable) {
        showMessage(throwable.getMessage(), false);
        Cappuccino.getResourceWatcher(TAG).idle();
    }

    @Override
    public void onAddToCartResult(boolean isSuccess) {
        if (!isSuccess) {
            showMessage("Error occurred in adding product to cart. Please try again!", false);
            Cappuccino.getResourceWatcher(TAG).idle();
        }
    }

    @Override
    public void onRemoveFromCartResult(boolean isSuccess) {
        if (!isSuccess) {
            showMessage("Error occurred in removing product from cart. Please try again!", false);
            Cappuccino.getResourceWatcher(TAG).idle();
        }
    }

    @Override
    protected void refreshProducts() {
        if (mProduct != null) {
            Cappuccino.getResourceWatcher(TAG).busy();
            getOps().getUpdatedProductDetails(mProduct.getCatId(), mProduct.getProdId());
        }
    }

    private void updateCartActionButton(boolean isInCart) {
        mCartActionButton.setVisibility(View.VISIBLE);
        mCartActionButton.setText(getString(isInCart ? R.string.remove_from_Cart : R.string.add_to_cart));
    }

    private void init() {
        Intent intent = getIntent();
        int catId = intent.getIntExtra(AppConstants.INTENT_CATEGORY_ID, 0);
        int prodId = intent.getIntExtra(AppConstants.INTENT_PRODUCT_ID, 0);
        if (catId != 0 && prodId != 0) {
            Cappuccino.getResourceWatcher(TAG).busy();
            getOps().loadProductDetails(catId, prodId);
        } else {
            Log.e(TAG, "Category and Product id are not valid");
        }
    }
}
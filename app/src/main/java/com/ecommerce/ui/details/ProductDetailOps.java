package com.ecommerce.ui.details;

import android.os.Bundle;

import com.ecommerce.AppManager;
import com.ecommerce.AppModels;
import com.ecommerce.cart.CartItem;
import com.ecommerce.category.Category;
import com.ecommerce.product.Product;
import com.ecommerce.util.common.ConfigurableOps;
import com.ecommerce.util.common.ContextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ProductDetailOps implements ConfigurableOps<ProductDetailOps.View> {

    public interface View extends ContextView {
        void onProductDetailsLoadSuccess(Product product);
        void onProductDetailsLoadError(Throwable throwable);
        void onAddToCartResult(boolean isSuccess);
        void onRemoveFromCartResult(boolean isSuccess);
    }

    private ProductDetailOps.View mView;
    private AppModels mAppModels;
    private Subscription mSubscription;
    private Product mProduct;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mAppModels = AppManager.getModels();
        mView = view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public void loadProductDetails(final int catId, final int prodId) {
        // Return details immediately if available in cache
        if (mProduct != null && mProduct.getCatId() == catId && mProduct.getProdId() == prodId) {
            mView.onProductDetailsLoadSuccess(mProduct);
            return;
        }

        getUpdatedProductDetails(catId, prodId);
    }

    public void getUpdatedProductDetails(int catId, final int prodId) {
        List<Integer> requestedCatIds = new ArrayList<>();
        requestedCatIds.add(catId);
        mSubscription = mAppModels.getCategoryManager().getCategoryDetailsByIdObservable(requestedCatIds)
            .flatMap(new Func1<Category, Observable<Product>>() {
                @Override
                public Observable<Product> call(Category category) {
                    List<Integer> requestedProdDetailsIds = new ArrayList<>();
                    requestedProdDetailsIds.add(prodId);
                    return mAppModels.getProductManager().getProductByIdAndCategory(category, requestedProdDetailsIds);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toSingle()
            .subscribe(new SingleSubscriber<Product>() {
                @Override
                public void onSuccess(Product product) {
                    mSubscription.unsubscribe();
                    if (product != null) {
                        mProduct = product;
                        mView.onProductDetailsLoadSuccess(product);
                    } else {
                        onError(new Throwable("Error occurred while loading products details"));
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.onProductDetailsLoadError(throwable);
                }
            });
    }

    public void addToCart() {
        CartItem cartItem = new CartItem(mProduct);
        mSubscription = mAppModels.getCartManager()
            .addToCartObservable(cartItem)
            .toSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<Boolean>() {
                @Override
                public void onSuccess(Boolean isSuccess) {
                    mProduct.setIsInCart(isSuccess);
                    mView.onAddToCartResult(isSuccess);
                }

                @Override
                public void onError(Throwable error) {
                    mView.onAddToCartResult(false);
                }
            });
    }

    public void removeFromCart() {
        CartItem cartItem = new CartItem(mProduct);
        mSubscription = mAppModels.getCartManager()
            .removeFromCartObservable(cartItem)
            .toSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<Boolean>() {
                @Override
                public void onSuccess(Boolean isSuccess) {
                    mProduct.setIsInCart(!isSuccess);
                    mView.onRemoveFromCartResult(isSuccess);
                }

                @Override
                public void onError(Throwable error) {
                    mView.onRemoveFromCartResult(false);
                }
            });
    }

    public void cancel() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }
}

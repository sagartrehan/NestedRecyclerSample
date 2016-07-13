package com.ecommerce.ui.cart;

import android.os.Bundle;

import com.ecommerce.AppManager;
import com.ecommerce.AppModels;
import com.ecommerce.cart.CartItem;
import com.ecommerce.category.Category;
import com.ecommerce.product.Product;
import com.ecommerce.util.AppUtil;
import com.ecommerce.util.common.ConfigurableOps;
import com.ecommerce.util.common.ContextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CartOps implements ConfigurableOps<CartOps.View> {

    public interface View extends ContextView {
        void onCartDetailsLoadSuccess(List<Product> cartProducts);
        void onCartDetailsLoadError(Throwable throwable);
        void onRemoveFromCartResult(boolean isSuccess, Product product);
        void updateTotalAmount(long totalAmount);
    }

    private CartOps.View mView;
    private AppModels mAppModels;
    private Subscription mSubscription;
    private List<Product> mCartProducts;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = view;
        mAppModels = AppManager.getModels();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public void getAllCartItems() {
        final Map<Integer, List<Integer>> mCatProdMap = new HashMap<>();
        mSubscription = mAppModels.getCartManager()
            .getProductsInCartObservable()
            .toList()
            .flatMap(new Func1<List<CartItem>, Observable<Category>>() {
                @Override
                public Observable<Category> call(List<CartItem> cartItems) {
                    if (AppUtil.isEmpty(cartItems)) {
                        return Observable.empty();
                    }
                    for (CartItem item : cartItems) {
                        List<Integer> catProducts = mCatProdMap.get(item.getCatId());
                        if (AppUtil.isEmpty(catProducts)) {
                            catProducts = new ArrayList<>();
                            catProducts.add(item.getProdId());
                            mCatProdMap.put(item.getCatId(), catProducts);
                        } else {
                            catProducts.add(item.getProdId());
                        }
                    }
                    return mAppModels.getCategoryManager().getCategoryDetailsByIdObservable(new ArrayList<>(mCatProdMap.keySet()));
                }
            })
            .flatMap(new Func1<Category, Observable<Product>>() {
                @Override
                public Observable<Product> call(Category category) {
                    return mAppModels.getProductManager().getProductByIdAndCategory(category, mCatProdMap.get(category.getId()));
                }
            })
            .toList()
            .toSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<List<Product>>() {
                @Override
                public void onSuccess(List<Product> cartProducts) {
                    if (AppUtil.isValid(cartProducts)) {
                        mCartProducts = cartProducts;
                        mView.onCartDetailsLoadSuccess(cartProducts);
                        getTotalAmount();
                    } else {
                        onError(new Throwable("Cart is empty!"));
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.onCartDetailsLoadError(throwable);
                }
            });
    }

    public void removeFromCart(final Product product) {
        CartItem cartItem = new CartItem(product);
        mSubscription = mAppModels.getCartManager()
            .removeFromCartObservable(cartItem)
            .toSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<Boolean>() {
                @Override
                public void onSuccess(Boolean isSuccess) {
                    mCartProducts.remove(product);
                    mView.onRemoveFromCartResult(isSuccess, product);
                    getTotalAmount();
                }

                @Override
                public void onError(Throwable error) {
                    mView.onRemoveFromCartResult(false, product);
                }
            });
    }

    public void cancel() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }

    private void getTotalAmount() {
        if (AppUtil.isEmpty(mCartProducts)) {
            mView.updateTotalAmount(0);
            return;
        }

        long totalAmount = 0;
        for (Product product : mCartProducts) {
            totalAmount += Long.valueOf(product.getPrice());
        }
        mView.updateTotalAmount(totalAmount);
    }
}
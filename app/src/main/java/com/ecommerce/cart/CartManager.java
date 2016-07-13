package com.ecommerce.cart;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ecommerce.AppManager;
import com.ecommerce.product.Product;
import com.ecommerce.util.AppConstants;
import com.ecommerce.util.AppUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class CartManager {

    public CartCache mCache;

    public CartManager() {
        mCache = new CartCacheImpl();
    }

    public Observable<Boolean> addToCartObservable(CartItem item) {
        return Observable.just(mCache.addToCart(item))
            .doOnNext(new Action1<Boolean>() {
                @Override
                public void call(Boolean isSuccess) {
                    if (isSuccess) {
                        sendCartUpdatedBroadcast();
                    }
                }
            });
    }

    public Observable<Boolean> removeFromCartObservable(CartItem item) {
        return Observable.just(mCache.removeFromCart(item))
            .doOnNext(new Action1<Boolean>() {
                @Override
                public void call(Boolean isSuccess) {
                    if (isSuccess) {
                        sendCartUpdatedBroadcast();
                    }
                }
            });
    }

    public Observable<CartItem> getProductsInCartObservable() {
        List<CartItem> cartItems = mCache.getProductsInCart();
        if (AppUtil.isEmpty(cartItems)) {
            return Observable.empty();
        }
        return Observable.from(cartItems);
    }

    public boolean isInCart(Product product) {
        return mCache.isInCart(new CartItem(product));
    }

    private void sendCartUpdatedBroadcast() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.INTENT_ACTION_CART_UPDATED);
        LocalBroadcastManager.getInstance(AppManager.getContext()).sendBroadcast(intent);
    }
}

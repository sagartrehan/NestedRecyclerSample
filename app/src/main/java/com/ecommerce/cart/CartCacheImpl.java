package com.ecommerce.cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ecommerce.AppManager;
import com.ecommerce.storage.AppDbProvider;
import com.ecommerce.storage.CartTable;

import java.util.ArrayList;
import java.util.List;

public class CartCacheImpl implements CartCache {

    private Context mAppContext;

    public CartCacheImpl() {
        mAppContext = AppManager.getContext();
    }

    @Override
    public boolean addToCart(CartItem cartItem) {
        Uri insertUri = mAppContext.getContentResolver().insert(AppDbProvider.CONTENT_URI_CART, toValues(cartItem));
        int rowId = -1;
        if (insertUri != null) {
            rowId = Integer.valueOf(insertUri.getLastPathSegment());
        }
        return rowId != -1;
    }

    @Override
    public boolean removeFromCart(CartItem cartItem) {
        String whereClause = CartTable.COL_ID + "=?";
        String[] whereArgs = new String[] {createCartItemPrimaryKey(cartItem)};
        int deletedRowCount = mAppContext.getContentResolver().delete(AppDbProvider.CONTENT_URI_CART, whereClause, whereArgs);
        return deletedRowCount > 0;
    }

    @Override
    public List<CartItem> getProductsInCart() {
        List<CartItem> cartItems = new ArrayList<>();
        Cursor cursor = mAppContext.getContentResolver().query(AppDbProvider.CONTENT_URI_CART, null, null, null, null);
        if (cursor == null) {
            return cartItems;
        }

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cartItems.add(toCartItem(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cartItems;
    }

    @Override
    public boolean isInCart(CartItem cartItem) {
        boolean isInCart;
        String whereClause = CartTable.COL_ID + "=?";
        String[] whereArgs = new String[] {createCartItemPrimaryKey(cartItem)};
        Cursor cursor = mAppContext.getContentResolver().query(AppDbProvider.CONTENT_URI_CART, null, whereClause, whereArgs, null);
        if (cursor == null) {
            isInCart = false;
        } else {
            isInCart = cursor.getCount() > 0;
            cursor.close();
        }
        return isInCart;
    }

    private ContentValues toValues(CartItem cartItem) {
        ContentValues values = new ContentValues();
        values.put(CartTable.COL_ID, createCartItemPrimaryKey(cartItem));
        values.put(CartTable.COL_CAT_ID, cartItem.getCatId());
        values.put(CartTable.COL_PROD_ID, cartItem.getProdId());
        return values;
    }

    private CartItem toCartItem(Cursor cursor) {
        return new CartItem(
            cursor.getInt(cursor.getColumnIndex(CartTable.COL_CAT_ID)),
            cursor.getInt(cursor.getColumnIndex(CartTable.COL_PROD_ID))
        );
    }

    private String createCartItemPrimaryKey(CartItem cartItem) {
        return cartItem.getCatId() + "_" + cartItem.getProdId();
    }
}
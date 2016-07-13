package com.ecommerce.cart;

import com.ecommerce.product.Product;

public class CartItem {

    private int catId;
    private int prodId;

    public CartItem(Product product) {
        this(product.getCatId(), product.getProdId());
    }

    public CartItem(int catId, int prodId) {
        this.catId = catId;
        this.prodId = prodId;
    }

    public int getCatId() {
        return catId;
    }

    public int getProdId() {
        return prodId;
    }
}

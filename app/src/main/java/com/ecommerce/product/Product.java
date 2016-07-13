package com.ecommerce.product;

import com.ecommerce.category.Category;
import com.ecommerce.rest.response.ProductData;

public class Product {

    private int catId;
    private ProductData productData;
    private boolean isInCart;

    public static Product from(Category category, ProductData productData) {
        Product product = new Product();
        product.catId = category.getId();
        product.productData = productData;
        return product;
    }

    public int getProdId() {
        return productData.id;
    }

    public int getCatId() {
        return catId;
    }

    public boolean isInCart() {
        return isInCart;
    }

    public void setIsInCart(boolean isInCart) {
        this.isInCart = isInCart;
    }

    public String getName() {
        return productData.name;
    }

    public String getThumbnailId() {
        return productData.thumbnail_id;
    }

    public String getPrice() {
        return productData.price;
    }
}

package com.ecommerce;

import com.ecommerce.cart.CartManager;
import com.ecommerce.category.CategoryManager;
import com.ecommerce.product.ProductManager;

public class AppModels {

    private static final Object lock = new Object();

    private CategoryManager mCategoryManager;
    private ProductManager mProductManager;
    private CartManager mCartManager;

    public CategoryManager getCategoryManager() {
        if (mCategoryManager == null) {
            synchronized (lock) {
                if (mCategoryManager == null) {
                    mCategoryManager = new CategoryManager();
                }
            }
        }
        return mCategoryManager;
    }

    public ProductManager getProductManager() {
        if (mProductManager == null) {
            synchronized (lock) {
                if (mProductManager == null) {
                    mProductManager = new ProductManager();
                }
            }
        }
        return mProductManager;
    }

    public CartManager getCartManager() {
        if (mCartManager == null) {
            synchronized (lock) {
                if (mCartManager == null) {
                    mCartManager = new CartManager();
                }
            }
        }
        return mCartManager;
    }

}

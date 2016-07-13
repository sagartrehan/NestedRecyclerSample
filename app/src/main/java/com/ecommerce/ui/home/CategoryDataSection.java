package com.ecommerce.ui.home;

import android.app.Activity;

import com.ecommerce.category.Category;
import com.ecommerce.product.Product;

import java.util.List;

public class CategoryDataSection {

    private String mCategoryTitle;
    private ProductRecyclerAdapter mProductRecyclerAdapter;

    public CategoryDataSection(Activity activity, Category category, List<Product> productList) {
        mCategoryTitle = category.getName();
        mProductRecyclerAdapter = new ProductRecyclerAdapter(activity, productList);
    }

    public String getCategoryTitle() {
        return mCategoryTitle;
    }

    public ProductRecyclerAdapter getProductRecyclerAdapter() {
        return mProductRecyclerAdapter;
    }
}

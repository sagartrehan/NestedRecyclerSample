package com.ecommerce.category;

import com.ecommerce.rest.response.CategoryData;

public class Category {

    private CategoryData categoryData;

    public static Category from(CategoryData categoryData) {
        Category category = new Category();
        category.categoryData = categoryData;
        return category;
    }

    public int getId() {
        return categoryData.id;
    }

    public String getDetailsPath() {
        return categoryData.details_path;
    }

    public String getName() {
        return categoryData.name;
    }
}

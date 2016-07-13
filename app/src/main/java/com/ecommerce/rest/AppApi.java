package com.ecommerce.rest;

import com.ecommerce.rest.response.CategoryData;
import com.ecommerce.rest.response.ProductData;

import java.util.List;

public interface AppApi {

    List<CategoryData> getCategories();

    List<ProductData> getProducts(String detailsPath);
}

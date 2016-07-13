package com.ecommerce.category;

import com.ecommerce.rest.AppApi;
import com.ecommerce.rest.AppApiImpl;
import com.ecommerce.rest.response.CategoryData;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

public class CategoryManager {

    private AppApi mAppAPI;

    public CategoryManager() {
        mAppAPI = new AppApiImpl();
    }

    public Observable<Category> getAllCategoryObservable() {
        return Observable.from(mAppAPI.getCategories())
            .map(new Func1<CategoryData, Category>() {
                @Override
                public Category call(CategoryData categoryData) {
                    return Category.from(categoryData);
                }
            });
    }

    public Observable<Category> getCategoryDetailsByIdObservable(final List<Integer> requestCatIds) {
        return getAllCategoryObservable()
            .filter(new Func1<Category, Boolean>() {
                @Override
                public Boolean call(Category category) {
                    return requestCatIds.contains(category.getId());
                }
            });
    }
}
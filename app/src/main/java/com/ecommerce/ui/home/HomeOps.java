package com.ecommerce.ui.home;

import android.app.Activity;
import android.os.Bundle;

import com.ecommerce.AppManager;
import com.ecommerce.AppModels;
import com.ecommerce.category.Category;
import com.ecommerce.product.Product;
import com.ecommerce.util.AppUtil;
import com.ecommerce.util.common.ConfigurableOps;
import com.ecommerce.util.common.ContextView;

import java.util.List;

import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HomeOps implements ConfigurableOps<HomeOps.View> {

    public interface View extends ContextView {
        void onProductsDetailsLoadSuccess(List<CategoryDataSection> sections);
        void onProductDetailsLoadError(Throwable throwable);
    }

    private AppModels mAppModels;
    private HomeOps.View mView;
    private List<CategoryDataSection> mSections;
    private Subscription mSubscription;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mAppModels = AppManager.getModels();
        mView = view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public void loadProducts() {
        // Return if data available in local cache
        if (AppUtil.isValid(mSections)) {
            mView.onProductsDetailsLoadSuccess(mSections);
            return;
        }

        // Data is not available in local cache so load data
        mSubscription = mAppModels.getCategoryManager().getAllCategoryObservable()
            .flatMap(new Func1<Category, Observable<CategoryDataSection>>() {
                @Override
                public Observable<CategoryDataSection> call(Category category) {
                    return new CategoryDetailsFetcher(category).loadDataSection();
                }
            })
            .toList()
            .toSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<List<CategoryDataSection>>() {
                @Override
                public void onSuccess(List<CategoryDataSection> dataSections) {
                    if (AppUtil.isValid(dataSections)) {
                        mSections = dataSections;
                        mView.onProductsDetailsLoadSuccess(mSections);
                    } else {
                        onError(new Throwable("Product details not available!"));
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    mView.onProductDetailsLoadError(throwable);
                }
            });
    }

    private class CategoryDetailsFetcher {
        Category mCategory;

        CategoryDetailsFetcher(Category category) {
            mCategory = category;
        }

        Observable<CategoryDataSection> loadDataSection() {
            return mAppModels.getProductManager().getAllProductByCategoryObservable(mCategory)
                .toList()
                .map(new Func1<List<Product>, CategoryDataSection>() {
                    @Override
                    public CategoryDataSection call(List<Product> products) {
                        return new CategoryDataSection((Activity) mView.getActivityContext(), mCategory, products);
                    }
                });
        }
    }

    public void cancel() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }
}

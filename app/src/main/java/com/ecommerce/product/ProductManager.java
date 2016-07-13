package com.ecommerce.product;

import com.ecommerce.AppManager;
import com.ecommerce.category.Category;
import com.ecommerce.rest.AppApi;
import com.ecommerce.rest.AppApiImpl;
import com.ecommerce.rest.response.ProductData;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

public class ProductManager {

    private AppApi mAppAPI;

    public ProductManager() {
        mAppAPI = new AppApiImpl();
    }

    public Observable<Product> getAllProductByCategoryObservable(final Category category) {
        return Observable.from(mAppAPI.getProducts(category.getDetailsPath()))
            .map(new Func1<ProductData, Product>() {
                @Override
                public Product call(ProductData productData) {
                    Product product = Product.from(category, productData);
                    product.setIsInCart(AppManager.getModels().getCartManager().isInCart(product));
                    return product;
                }
            });
    }

    public Observable<Product> getProductByIdAndCategory(final Category category, final List<Integer> requestedProdDetailsIds) {
        return getAllProductByCategoryObservable(category)
            .filter(new Func1<Product, Boolean>() {
                @Override
                public Boolean call(Product product) {
                    return requestedProdDetailsIds.contains(product.getProdId());
                }
            });
    }
}

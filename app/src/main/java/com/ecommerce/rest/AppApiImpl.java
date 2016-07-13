package com.ecommerce.rest;

import android.content.Context;
import android.util.Log;

import com.ecommerce.AppManager;
import com.ecommerce.rest.response.CategoryData;
import com.ecommerce.rest.response.ProductData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public class AppApiImpl implements AppApi {

    private static final String PATH_CATEGORIES = "categories";
    private static final String TAG = AppApiImpl.class.getSimpleName();

    private Context mAppContext;
    private Gson mGson;

    public AppApiImpl() {
        mAppContext = AppManager.getContext();
        mGson = new Gson();
    }

    @Override
    public List<CategoryData> getCategories() {
        int catResId = mAppContext.getResources().getIdentifier(PATH_CATEGORIES, "raw", mAppContext.getPackageName());
        if (catResId == 0) {
            throw new IllegalStateException("Expected raw resource " + PATH_CATEGORIES + " not found!");
        }

        String categoriesJson = readRawFileWithResId(catResId);
        return mGson.fromJson(categoriesJson, new TypeToken<List<CategoryData>>(){}.getType());
    }

    @Override
    public List<ProductData> getProducts(String detailsPath) {
        int prodListResId = mAppContext.getResources().getIdentifier(detailsPath, "raw", mAppContext.getPackageName());
        if (prodListResId == 0) {
            throw new IllegalStateException("Expected raw resource " + detailsPath + " not found!");
        }

        String categoriesJson = readRawFileWithResId(prodListResId);
        return mGson.fromJson(categoriesJson, new TypeToken<List<ProductData>>(){}.getType());
    }

    // region File Util
    private String readRawFileWithResId(int resId) {
        InputStream is = mAppContext.getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return writer.toString();
    }
    // endregion
}

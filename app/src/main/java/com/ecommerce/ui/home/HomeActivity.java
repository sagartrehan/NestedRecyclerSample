package com.ecommerce.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ecommerce.R;
import com.ecommerce.util.common.GenericActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity
    extends GenericActivity<HomeOps.View, HomeOps>
    implements HomeOps.View {

    private CategoryRecyclerAdapter mAdapter;
    private List<CategoryDataSection> mSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, HomeOps.class, this);
        setContentView(R.layout.activity_home);
        initViews();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getOps().cancel();
    }

    @Override
    protected void initViews() {
        super.initViews();
        setUpToolbar("Home", false);
        mHasDefaultMenuItems = true;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // Set Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // Set Adapter
        mSections = new ArrayList<>();
        mAdapter = new CategoryRecyclerAdapter(this, mSections);
        recyclerView.setAdapter(mAdapter);
    }

    private void init() {
        getOps().loadProducts();
    }

    @Override
    public void onProductsDetailsLoadSuccess(List<CategoryDataSection> sections) {
        mSections.clear();
        mSections.addAll(sections);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductDetailsLoadError(Throwable throwable) {
        showMessage("Error occurred while loading product listing", false);
    }
}
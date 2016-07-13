package com.ecommerce.ui.home;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecommerce.R;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private Activity mContext;
    private List<CategoryDataSection> mCategoryDataSectionList;

    public CategoryRecyclerAdapter(Activity context, List<CategoryDataSection> categoryDataSectionList) {
        this.mContext = context;
        this.mCategoryDataSectionList = categoryDataSectionList;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final TextView mName;
        public final RecyclerView mProductRecyclerView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.category_name);
            mProductRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
            mProductRecyclerView.addItemDecoration(new ProductItemDecorator(mContext.getResources().getDimensionPixelSize(R.dimen.product_spacing)));

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mProductRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cell_category, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        CategoryDataSection sectionData = mCategoryDataSectionList.get(position);
        holder.mName.setText(sectionData.getCategoryTitle());
        holder.mProductRecyclerView.setAdapter(sectionData.getProductRecyclerAdapter());
    }

    @Override
    public int getItemCount() {
        return mCategoryDataSectionList == null ? 0 : mCategoryDataSectionList.size();
    }
}
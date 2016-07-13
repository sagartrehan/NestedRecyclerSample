package com.ecommerce.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.product.Product;
import com.ecommerce.ui.details.ProductDetailsActivity;
import com.ecommerce.util.AppConstants;
import com.ecommerce.util.UIUtils;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {

    protected Activity mContext;
    private List<Product> mProductList;

    public ProductRecyclerAdapter(Activity context, List<Product> productList) {
        this.mContext = context;
        this.mProductList = productList;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public final TextView mName;
        public final TextView mPrice;
        public final ImageView mThumbnail;
        public final ImageView mActionButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mActionButton = (ImageView) itemView.findViewById(R.id.menu);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cell_product, parent, false));
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        Product product = mProductList.get(position);
        holder.mName.setText(product.getName());
        holder.mPrice.setText(mContext.getString(R.string.price, product.getPrice()));

        UIUtils.setImage(mContext, holder.mThumbnail, product);
        handleMenuActions(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra(AppConstants.INTENT_CATEGORY_ID, mProductList.get(holder.getAdapterPosition()).getCatId());
                intent.putExtra(AppConstants.INTENT_PRODUCT_ID, mProductList.get(holder.getAdapterPosition()).getProdId());
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList == null ? 0 : mProductList.size();
    }

    protected void handleMenuActions(ProductViewHolder holder) {

    }
}

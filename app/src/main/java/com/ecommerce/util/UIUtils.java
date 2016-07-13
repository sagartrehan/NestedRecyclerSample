package com.ecommerce.util;

import android.content.Context;
import android.widget.ImageView;

import com.ecommerce.R;
import com.ecommerce.product.Product;

public class UIUtils {

    public static void setImage(Context context, ImageView thumbnail, Product product) {
        int imgResId = context.getResources().getIdentifier(product.getThumbnailId(), "raw", context.getPackageName());
        thumbnail.setBackgroundResource(imgResId != 0 ? imgResId : R.mipmap.ic_launcher);
    }

}

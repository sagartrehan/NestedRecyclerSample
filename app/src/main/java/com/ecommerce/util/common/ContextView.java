package com.ecommerce.util.common;

import android.content.Context;

/**
 * Defines methods for obtaining Contexts used by all views in the
 * "View" layer.
 */
public interface ContextView {

    /**
     * Get the Activity Context.
     */
    Context getActivityContext();

    /**
     * Get the Application Context.
     */
    Context getApplicationContext();

    /**
     * Hook Method invokes by Ops class o nce it has been initialized. Activity class can use this method as an entry point to
     * start interaction with Ops.
     */
    void onOpsInitialized();

}

package com.ecommerce.util.common;

import android.os.Bundle;

/**
 * The base interface that an operations ("Ops") class must implement
 * so that it can be notified automatically by the GenericActivity
 * framework when runtime configuration changes occur.
 */
public interface ConfigurableOps<View> {
    /**
     * Hook method dispatched by the GenericActivity framework to
     * initialize an operations ("Ops") object after it's been
     * created.
     *
     * @param view
     *        The currently active View.
     * @param firstTimeIn
     *        Set to "true" if this is the first time the Ops class is
     *        initialized, else set to "false" if called after a
     *        runtime configuration change.
     */
    void onConfiguration(View view, boolean firstTimeIn);

    /**
     * Hook method dispatched by the GenericActivity framework to
     * saveInstanceState of Ops when activity destroys in background due to
     * low memory
     * @param outState Bundle in which to place your saved state.
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * Hook method dispatched by the GenericActivity framework to
     * restoreInstanceState of Ops when activity recreated when comes in foreground
     * @param savedInstanceState Bundle from which Presenter restore its state
     */
    void onRestoreInstanceState(Bundle savedInstanceState);
}

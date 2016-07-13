package com.ecommerce.util.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.ui.cart.CartActivity;
import com.ecommerce.util.AppConstants;

public class GenericActivity<Interface, OpsType extends ConfigurableOps<Interface>> extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    private final RetainedFragmentManager mRetainedFragmentManager = new RetainedFragmentManager(this.getSupportFragmentManager(), TAG);

    private OpsType mOpsInstance;

    protected TextView mEmptyMsg;
    // Configurable Flags
    protected boolean mHasDefaultMenuItems;

    public void onCreate(Bundle savedInstanceState, Class<OpsType> opsType, Interface instance) {
        super.onCreate(savedInstanceState);

        try {
            handleConfiguration(opsType, instance);
        } catch (InstantiationException e) {
            Log.d(TAG, "handleConfiguration " + e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "handleConfiguration " + e);
            throw new RuntimeException(e);
        }
        registerCartUpdateReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterCartUpdateReceiver();
    }

    public void handleConfiguration(Class<OpsType> opsType, Interface instance)
        throws InstantiationException, IllegalAccessException {
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG, "First time onCreate() call");
            initialize(opsType, instance);
        } else {
            Log.d(TAG, "Second or subsequent onCreate() call");
            mOpsInstance = mRetainedFragmentManager.get(opsType.getSimpleName());
            if (mOpsInstance == null) {
                initialize(opsType, instance);
            } else {
                mOpsInstance.onConfiguration(instance, false);
            }
        }
    }

    private void initialize(Class<OpsType> opsType, Interface instance) throws InstantiationException, IllegalAccessException {
        mOpsInstance = opsType.newInstance();
        mRetainedFragmentManager.put(opsType.getSimpleName(), mOpsInstance);
        mOpsInstance.onConfiguration(instance, true);
    }

    public OpsType getOps() {
        return mOpsInstance;
    }

    public Context getActivityContext() {
        return this;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mOpsInstance != null) {
            mOpsInstance.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mOpsInstance != null) {
            mOpsInstance.onRestoreInstanceState(savedInstanceState);
        }
    }

    public void onOpsInitialized() {

    }

    protected void initViews() {
        mEmptyMsg = (TextView) findViewById(R.id.empty_text);
    }

    public void setUpToolbar(String title, boolean shouldShowHomeAsUpEnabled) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
                actionBar.setDisplayHomeAsUpEnabled(shouldShowHomeAsUpEnabled);
            }
        }
    }

    protected void goTo(Intent intent) {
        if (intent != null) {
            startActivity(intent);
        }
    }

    // region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mHasDefaultMenuItems) {
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(this, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // endregion

    // region Refresh Products
    private BroadcastReceiver mCartRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshProducts();
        }
    };

    private void registerCartUpdateReceiver() {
        IntentFilter intentFilter = new IntentFilter(AppConstants.INTENT_ACTION_CART_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mCartRefreshReceiver, intentFilter);
    }

    private void unregisterCartUpdateReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCartRefreshReceiver);
    }

    protected void refreshProducts() {
        // Child activity can override this method to refresh details
    }
    // endregion

    // region UI
    protected void showMessage(String message, boolean shouldUseEmptyView) {
        if (shouldUseEmptyView && mEmptyMsg != null) {
            mEmptyMsg.setText(message);
            setEmptyMessageVisibility(true);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    protected void setEmptyMessageVisibility(boolean isVisible) {
        if (mEmptyMsg != null) {
            mEmptyMsg.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
    // endregion
}
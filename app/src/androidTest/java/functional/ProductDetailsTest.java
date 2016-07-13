package functional;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ecommerce.R;
import com.ecommerce.ui.details.ProductDetailsActivity;
import com.ecommerce.util.AppConstants;
import com.metova.cappuccino.Cappuccino;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProductDetailsTest {

    @Rule
    public ActivityTestRule<ProductDetailsActivity> mActivityRule = new ActivityTestRule<>(ProductDetailsActivity.class);

    @Test
    public void testUIForAddRemoveCartAction() {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.INTENT_CATEGORY_ID, 1);
        intent.putExtra(AppConstants.INTENT_PRODUCT_ID, 1);
        mActivityRule.launchActivity(intent);

        Cappuccino.registerIdlingResource(ProductDetailsActivity.class.getSimpleName());
        onView(withId(R.id.cart_action)).check(matches(withText(R.string.add_to_cart)));
        onView(withId(R.id.cart_action)).perform(click());
        onView(withId(R.id.cart_action)).check(matches(withText(R.string.remove_from_Cart)));

        onView(withId(R.id.cart_action)).check(matches(withText(R.string.remove_from_Cart)));
        onView(withId(R.id.cart_action)).perform(click());
        onView(withId(R.id.cart_action)).check(matches(withText(R.string.add_to_cart)));
        Cappuccino.unregisterIdlingResource(ProductDetailsActivity.class.getSimpleName());
    }
}

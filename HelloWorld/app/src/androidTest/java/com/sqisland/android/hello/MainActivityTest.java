package com.sqisland.android.hello;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void bye() {
        onView(withId(R.id.greeting))
                .check(matches(withText(R.string.hello_world)));

        // Open the target menu.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        // Click on the item, say_bye. The withText() matcher can be used with string
        // verification or with locating the view.
        onView(withText(R.string.say_bye)).perform(click());
        // Verify the text view is changed to bye.
        onView(withId(R.id.greeting))
                .check(matches(withText(R.string.bye)));
    }
}
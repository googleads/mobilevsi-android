/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ads.interactivemedia.v3.samples.MobileVSI.androidTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.MainActivity;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    // This super() constructor was deprecated - but we want to support lower API levels.
    @SuppressWarnings("deprecation")
    public MainActivityTest() {
        super("com.google.ads.interactivemedia.v3.samples.MobileVSI", MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testClickSample() {
        onView(withText("Sample Tag")).perform(click());

        onView(withId(R.id.video_player)).check(matches(isDisplayed()));
        onView(withId(R.id.video_player_controller)).check(matches(isDisplayed()));
    }
}

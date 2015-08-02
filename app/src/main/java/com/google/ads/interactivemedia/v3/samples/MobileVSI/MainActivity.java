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
package com.google.ads.interactivemedia.v3.samples.MobileVSI;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.slidermenu.VideoListFragment;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ActivityStarterWithContext;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.AdTagUrlDisplayFragment;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVSILogger;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVsiUriSchemaUtil;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ShortLinkUtil;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.VideoFragment;

import java.io.IOException;
import java.util.HashMap;

/**
 * Main Activity.
 */
public class MainActivity extends AppCompatActivity
    implements VideoListFragment.OnTagSelectedListener,
        AdTagUrlDisplayFragment.OnVideoAdStartListener,
        ActivityStarterWithContext {

    private static final String FRAGMENT_VIDEO_PLAYLIST = "fragment_video_playlist";
    private static final String FRAGMENT_VIDEO_DISPLAY = "fragment_video_example";
    private static final String FRAGMENT_AD_TAG_URL_DISPLAY = "fragment_video_url_display";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private VideoListFragment videoListFragment;
    private AdTagUrlDisplayFragment adTagUrlFragment;
    private VideoFragment videoFragment;

    private MobileVSILogger logger;
    private HashMap<Integer, ActivityStarterCallback> activityStarterCallbacks;
    private VideoItemMetadata metadataToLaunch;
    private boolean isActivityActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag(FRAGMENT_VIDEO_PLAYLIST) == null) {
            videoListFragment = new VideoListFragment();
            transaction.add(R.id.slidermenu_list, videoListFragment, FRAGMENT_VIDEO_PLAYLIST);
        }
        if (fragmentManager.findFragmentByTag(FRAGMENT_VIDEO_DISPLAY) == null) {
            videoFragment = new VideoFragment();
            transaction.add(R.id.frame_container, videoFragment, FRAGMENT_VIDEO_DISPLAY);
            // TODO: Must start using add and remove instead of hide and show
            transaction.hide(videoFragment);
        }
        if (fragmentManager.findFragmentByTag(FRAGMENT_AD_TAG_URL_DISPLAY) == null) {
            adTagUrlFragment = new AdTagUrlDisplayFragment();
            adTagUrlFragment.addOnVideoAdStartListener(this);
            transaction.add(R.id.frame_container, adTagUrlFragment, FRAGMENT_AD_TAG_URL_DISPLAY);
        }
        transaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.container);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (logger == null) {
            logger = new MobileVSILogger(getClass());
        }
        if (activityStarterCallbacks == null) {
            activityStarterCallbacks = new HashMap<>();
        }
        metadataToLaunch = null;
        isActivityActive = false;

        onIntent(getIntent());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // the fragments have been created and it is safe to execute fragment transactions now
        isActivityActive = true;
        if (metadataToLaunch != null) {
            this.displayAdTagFragment(metadataToLaunch);
            metadataToLaunch = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isActivityActive = false;
    }

    public void onIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            // opened through a web launcher
            Uri launchUri = intent.getData();
            VideoItemMetadata metadata = new VideoItemMetadata(
                    // TODO: Create a settings object and fetch value from there
                    "http://rmcdn.2mdn.net/MotifFiles/html/1248596/android_1330378998288.mp4",
                    "Custom URL",
                    launchUri.toString());
            preProcessTag(metadata);
        } else {
            // regular app launch
            openDrawer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            // return true since the option item selected triggered the
            // drawer open/close action and was handled by the drawerToggle
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Note: When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
        orientAppUi();
    }

    private void orientAppUi() {
        int orientation = getResources().getConfiguration().orientation;
        boolean isLandscape = (orientation == Configuration.ORIENTATION_LANDSCAPE);
        videoFragment.switchToLandscape(isLandscape);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(findViewById(R.id.slidermenu_list));
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(findViewById(R.id.slidermenu_list));
    }

    /**
     * This function is the goto function for any sort of ad tag entry. Call this function to set
     * the ad tag url from any source, intents or internal calls. It processes the ad tag
     * before using it and safely calls displayAdTagFragment() or alternatively delegates the task
     * to onPostResume().
     */
    private void preProcessTag(final VideoItemMetadata videoItemMetadata) {
        String adTagUrl = videoItemMetadata.getAdTagUrl();

        // Check if the Uri is the custom schema one, and extract if needed.
        if (MobileVsiUriSchemaUtil.matchesCustomSchema(adTagUrl)) {
            adTagUrl = MobileVsiUriSchemaUtil.parseLaunchUri(adTagUrl);
        }

        // Check if the Url is a short link, expand if needed.
        if (ShortLinkUtil.canExpandShortLink(adTagUrl)) {
            ShortLinkUtil.tryExpandShortLink(new ShortLinkUtil.ShortLinkUtilCallback() {
                @Override
                public void onExpandLink(String fullLink) {
                    metadataToLaunch = videoItemMetadata.setAdTagUrl(fullLink);
                    if (isActivityActive) {
                        displayAdTagFragment(metadataToLaunch);
                        metadataToLaunch = null;
                    } // else the metadataToLauch will be launched onPostResume()
                }

                @Override
                public void onError(IOException exception) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Error expanding short url: " + exception.getMessage());
                }
            }, adTagUrl);
        } else {
            metadataToLaunch = videoItemMetadata.setAdTagUrl(adTagUrl);
            if (isActivityActive) {
                displayAdTagFragment(metadataToLaunch);
                metadataToLaunch = null;
            } // else the metadataToLauch will be launched onPostResume()
        }
    }

    @Override
    public void onTagSelected(VideoItemMetadata videoItemMetadata) {
        preProcessTag(videoItemMetadata);
    }

    @Override
    public void onVideoAdStartAction(VideoItemMetadata videoItemMetadata) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(adTagUrlFragment)
                .show(videoFragment)
                .commit();

        videoFragment.loadVideo(videoItemMetadata);

        orientAppUi();
    }

    /**
     * This function actually makes changes to the fragment and commits a transaction.
     * We must ensure that the function is called only when the activity is in active state,
     * so that the fragments may not have been garbage collected.
     * Thus this function is only called at the end of preProcessTag() which puts the safety-check
     * and when the fragments and activity have resumed.
     */
    private void displayAdTagFragment(VideoItemMetadata videoItemMetadata) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(videoFragment)
                .show(adTagUrlFragment)
                .commit();

        adTagUrlFragment.setVideoItemMetadata(videoItemMetadata);
        closeDrawer();
    }

    @Override
    public void startActivityWithCallback(Intent intent, ActivityStarterCallback callback) {
        // Create a hash with only the lower 16 bits set. This is a constraint set by
        // FragmentActivity.startActivityForResult()
        int hash = callback.hashCode() & 0xffff;
        activityStarterCallbacks.put(hash, callback);
        startActivityForResult(intent, hash);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (activityStarterCallbacks.containsKey(requestCode)) {
            ActivityStarterCallback callback = activityStarterCallbacks.get(requestCode);
            callback.onActivityResult(resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

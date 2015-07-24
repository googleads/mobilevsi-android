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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.AdTagUrlDisplayFragment;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.VideoFragment;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.slidermenu.VideoListFragment;

/**
 * Main Activity.
 */
public class MainActivity extends AppCompatActivity
    implements VideoListFragment.OnTagSelectedListener,
        AdTagUrlDisplayFragment.OnVideoAdStartListener{

    private static final String FRAGMENT_VIDEO_PLAYLIST = "fragment_video_playlist";
    private static final String FRAGMENT_VIDEO_DISPLAY = "fragment_video_example";
    private static final String FRAGMENT_AD_TAG_URL_DISPLAY = "fragment_video_url_display";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private VideoListFragment videoListFragment;
    private AdTagUrlDisplayFragment adTagUrlFragment;
    private VideoFragment videoFragment;

    private int displayContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayContainerId = R.id.frame_container;

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

        openDrawer();
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

    @Override
    public void onTagSelected(VideoItemMetadata videoItemMetadata) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(videoFragment)
                .show(adTagUrlFragment)
                .commit();

        adTagUrlFragment.setVideoItemMetadata(videoItemMetadata);

        closeDrawer();
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
}

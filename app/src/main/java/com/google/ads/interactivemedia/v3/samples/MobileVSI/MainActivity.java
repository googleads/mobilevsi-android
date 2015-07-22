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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.VideoFragment;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.slidermenu.VideoListFragment;

/**
 * Main Activity.
 */
public class MainActivity extends AppCompatActivity
    implements VideoListFragment.OnVideoSelectedListener {

    private static final String VIDEO_PLAYLIST_FRAGMENT_TAG = "video_playlist_fragment_tag";
    private static final String VIDEO_DISPLAY_FRAGMENT_TAG = "video_example_fragment_tag";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private VideoFragment videoFragment;
    private VideoListFragment videoListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The video list fragment won't exist for phone layouts, so add it dynamically so we can
        // .replace() it once the user selects a video.
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(VIDEO_PLAYLIST_FRAGMENT_TAG) == null) {
            videoListFragment = new VideoListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.slidermenu_list, videoListFragment, VIDEO_PLAYLIST_FRAGMENT_TAG)
                    .commit();
        }
        if (fragmentManager.findFragmentByTag(VIDEO_DISPLAY_FRAGMENT_TAG) == null) {
            videoFragment = new VideoFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_container, videoFragment, VIDEO_DISPLAY_FRAGMENT_TAG)
                    .commit();
        }

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
        // Hide the non-video content when in landscape so the video is as large as possible.
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        VideoFragment videoFragment = (VideoFragment) fragmentManager
//                .findFragmentByTag(VIDEO_DISPLAY_FRAGMENT_TAG);
//
//        Fragment videoListFragment = fragmentManager.findFragmentByTag(
//                VIDEO_PLAYLIST_FRAGMENT_TAG);

//        if (videoFragment != null) {
            // If the video playlist is onscreen (tablets) then hide that fragment.
//            if (videoListFragment != null) {
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                if (isLandscape) {
//                    fragmentTransaction.hide(videoListFragment);
//                } else {
//                    fragmentTransaction.show(videoListFragment);
//                }
//                fragmentTransaction.commit();
//            }
            videoFragment.makeFullscreen(isLandscape);
//            if (isLandscape) {
//                hideStatusBar();
//            } else {
//                showStatusBar();
//            }
//        } else {
            // If returning to the list from a fullscreen video, check if the video
            // list fragment exists and is hidden. If so, show it.
//            if (videoListFragment != null && videoListFragment.isHidden()) {
//                fragmentManager.beginTransaction().show(videoListFragment).commit();
//                showStatusBar();
//            }
//        }
    }

    // TODO: Remove these commented sections when the landscape mode design is ready

//    private void hideStatusBar() {
//        if (Build.VERSION.SDK_INT >= 16) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
//            getSupportActionBar().hide();
//        }
//    }
//
//    private void showStatusBar() {
//        if (Build.VERSION.SDK_INT >= 16) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            getSupportActionBar().show();
//        }
//    }

    public void openDrawer() {
        drawerLayout.openDrawer(findViewById(R.id.slidermenu_list));
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(findViewById(R.id.slidermenu_list));
    }

    @Override
    public void onVideoSelected(VideoItemMetadata videoItemMetadata) {
        VideoFragment videoFragment = (VideoFragment)
                getSupportFragmentManager().findFragmentByTag(VIDEO_DISPLAY_FRAGMENT_TAG);

        // Add the video fragment if it's missing (phone form factor), but only if the user
        // manually selected the video.
        if (videoFragment == null) {
            VideoListFragment videoListFragment = (VideoListFragment) getSupportFragmentManager()
                    .findFragmentByTag(VIDEO_PLAYLIST_FRAGMENT_TAG);
            int videoPlaylistFragmentId = videoListFragment.getId();

            videoFragment = new VideoFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(videoPlaylistFragmentId, videoFragment, VIDEO_DISPLAY_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
        videoFragment.loadVideo(videoItemMetadata);
        orientAppUi();
        closeDrawer();
    }
}

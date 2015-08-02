package com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp;

import android.content.Context;
import android.content.Intent;

/**
 * An interface that is used to start activities and add a callback at the same time
 */
public interface ActivityStarterWithContext {

    interface ActivityStarterCallback {
        void onActivityResult(int resultCode, Intent data);
    }

    void startActivityWithCallback(Intent intent, ActivityStarterCallback callback);

    Context getContext();
}

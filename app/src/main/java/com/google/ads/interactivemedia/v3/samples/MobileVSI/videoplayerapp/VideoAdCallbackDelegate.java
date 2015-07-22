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
package com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp;

import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer.VideoAdPlayerCallback;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.VideoPlayer;

import java.util.ArrayList;

/**
 * Used to delegate VideoPlayer.PlayerCallback calls to VideoAdPlayerCallback calls.
 * uses AdDisplayStateMonitor.isAdDisplayed() to decide whether a call was during
 * ad display state or content display state.
 */
 public class VideoAdCallbackDelegate implements VideoPlayer.PlayerCallback {

    /**
     * Interface for letting this delegate know if the ad is playing
     * and the events need to be forwarded.
     */
    interface AdDisplayStateMonitor {
        boolean isAdDisplayed();
    }

    /**
     * Interface for alerting caller of video completion.
     */
    interface OnContentCompleteListener {
        void onContentComplete();
    }

    private ArrayList<VideoAdPlayerCallback> adCallbacks;
    private AdDisplayStateMonitor adDisplayStateMonitor;
    private OnContentCompleteListener onContentCompleteListener;

    public VideoAdCallbackDelegate(AdDisplayStateMonitor adDisplayStateMonitor) {
        this.adDisplayStateMonitor = adDisplayStateMonitor;
        adCallbacks = new ArrayList<>(1);
    }

    public void addVideoAdPlayerCallback(VideoAdPlayerCallback callback) {
        adCallbacks.add(callback);
    }

    public void removeVideoAdPlayerCallback(VideoAdPlayerCallback callback) {
        adCallbacks.remove(callback);
    }

    /**
     * Set a listener to be triggered when the content (non-ad) video completes.
     */
    public void setOnContentCompleteListener(OnContentCompleteListener onContentCompleteListener) {
        this.onContentCompleteListener = onContentCompleteListener;
    }

    @Override
    public void onStarted() {
        if (adDisplayStateMonitor.isAdDisplayed()) {
            int n = adCallbacks.size();
            for (int i = 0; i < n; i++) {
                adCallbacks.get(i).onPlay();
            }
        }
    }

    @Override
    public void onPaused() {
        if (adDisplayStateMonitor.isAdDisplayed()) {
            int n = adCallbacks.size();
            for (int i = 0; i < n; i++) {
                adCallbacks.get(i).onPause();
            }
        }
    }

    @Override
    public void onResumed() {
        if (adDisplayStateMonitor.isAdDisplayed()) {
            int n = adCallbacks.size();
            for (int i = 0; i < n; i++) {
                adCallbacks.get(i).onResume();
            }
        }
    }

    @Override
    public void onError() {
        if (adDisplayStateMonitor.isAdDisplayed()) {
            int n = adCallbacks.size();
            for (int i = 0; i < n; i++) {
                adCallbacks.get(i).onError();
            }
        }
    }

    @Override
    public void onCompleted() {
        if (adDisplayStateMonitor.isAdDisplayed()) {
            int n = adCallbacks.size();
            for (int i = 0; i < n; i++) {
                adCallbacks.get(i).onEnded();
            }
        } else {
            // Alert an external listener that our content video is complete.
            if (onContentCompleteListener != null) {
                onContentCompleteListener.onContentComplete();
            }
        }
    }

    @Override
    public void onStopped() {
        // Ignore this message
    }
}


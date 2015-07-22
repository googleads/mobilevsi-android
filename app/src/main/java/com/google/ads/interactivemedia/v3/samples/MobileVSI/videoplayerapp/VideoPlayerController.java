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

import android.content.Context;
import android.opengl.Visibility;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdError;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent.AdErrorListener;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent.AdEventListener;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsLoader.AdsLoadedListener;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.VideoPlayer;

import java.util.ArrayList;

/**
 * Ads logic for handling the IMA SDK integration code and events.
 */
public class VideoPlayerController implements AdsLoadedListener, AdEventListener, AdErrorListener {

    /**
     * Log interface, so we can output the log commands to the UI or similar.
     */
    public interface Logger {
        void log(String logMessage);
    }

    // Container with references to video player and ad UI ViewGroup.
    private AdDisplayContainer mAdDisplayContainer;

    // The AdsLoader instance exposes the requestAds method.
    private AdsLoader mAdsLoader;

    // AdsManager exposes methods to control ad playback and listen to ad events.
    private AdsManager mAdsManager;

    // Ad-enabled video player.
    private VideoPlayerWithAdPlayback mVideoPlayerWithAdPlayback;

    // Button the user taps to begin video playback and ad request.
    private View playButton;

    private ViewGroup adUIContainer;

    // VAST ad tag URL to use when requesting ads during video playback.
    private String mCurrentAdTagUrl;

    // ViewGroup to render an associated companion ad into.
    private ViewGroup mCompanionViewGroup;

    // View that we can write log messages to, to display in the UI.
    private Logger mLog;

    public VideoPlayerController(Context context, final View playButton,
            VideoPlayerWithAdPlayback videoPlayerWithAdPlayback, String language,
            ViewGroup adUIContainer, ViewGroup companionViewGroup, Logger log) {
        mVideoPlayerWithAdPlayback = videoPlayerWithAdPlayback;
        this.playButton = playButton;
        this.adUIContainer = adUIContainer;
        mCompanionViewGroup = companionViewGroup;
        mLog = log;

        // Create an AdsLoader and optionally set the language.
        ImaSdkFactory mSdkFactory = ImaSdkFactory.getInstance();
        ImaSdkSettings imaSdkSettings = new ImaSdkSettings();
        imaSdkSettings.setLanguage(language);
        mAdsLoader = mSdkFactory.createAdsLoader(context, imaSdkSettings);

        mAdsLoader.addAdErrorListener(new AdErrorEvent.AdErrorListener() {
            /**
             * An event raised when there is an error loading or playing ads.
             */
            @Override
            public void onAdError(AdErrorEvent adErrorEvent) {
                log("Ad Error: " + adErrorEvent.getError().getMessage());
            }
        });

        mAdsLoader.addAdsLoadedListener(this);

        mVideoPlayerWithAdPlayback.setOnContentCompleteListener(
            new VideoAdCallbackDelegate.OnContentCompleteListener() {
                /**
                 * Event raised by VideoPlayerWithAdPlayback when content video is complete.
                 */
                @Override
                public void onContentComplete() {
                    mAdsLoader.contentComplete();
                    playButton.setVisibility(View.VISIBLE);
                }
            });

        // When Play is clicked, request ads and hide the button.
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAndPlayAds();
            }
        });
    }

    private void log(String message) {
        mLog.log(message + "\n");
    }

    /**
     * Set the ad tag URL the player should use to request ads when playing a content video.
     */
    public void setAdTagUrl(String adTagUrl) {
        mCurrentAdTagUrl = adTagUrl;
        log("AD Tag URL: " + adTagUrl);
    }

    /**
     * Request and subsequently play video ads from the ad server.
     */
    public void requestAndPlayAds() {
        if (mCurrentAdTagUrl == null || mCurrentAdTagUrl == "") {
            log("No VAST ad tag URL specified");
            mVideoPlayerWithAdPlayback.playContent();
            return;
        }

        // Since we're switching to a new video, tell the SDK the previous video is finished.
        if (mAdsManager != null) {
            mAdsManager.destroy();
        }
        mAdsLoader.contentComplete();

        ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
        playButton.setVisibility(View.GONE);
        mAdDisplayContainer = sdkFactory.createAdDisplayContainer();
        mAdDisplayContainer.setPlayer(mVideoPlayerWithAdPlayback);
        mAdDisplayContainer.setAdContainer(adUIContainer);

        // MOE:strip_line [START devsite_companion_include]
        // Set up spots for companions.
        CompanionAdSlot companionAdSlot = sdkFactory.createCompanionAdSlot();
        companionAdSlot.setContainer(mCompanionViewGroup);
        companionAdSlot.setSize(728, 90);
        ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<>();
        companionAdSlots.add(companionAdSlot);
        mAdDisplayContainer.setCompanionSlots(companionAdSlots);
        // [END devsite_companion_include] MOE:strip_line

        // Create the ads request.
        AdsRequest request = sdkFactory.createAdsRequest();
        request.setAdTagUrl(mCurrentAdTagUrl);
        request.setAdDisplayContainer(mAdDisplayContainer);
        request.setContentProgressProvider(mVideoPlayerWithAdPlayback);

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);
    }

    /**
     * Set metadata about the content video. In more complex implementations, this might
     * more than just a URL and could trigger additional decisions regarding ad tag selection.
     */
    public void setContentVideo(String videoPath) {
        mVideoPlayerWithAdPlayback.setContentVideoPath(videoPath);
    }

    /**
     * Save position of the video, whether content or ad. Can be called when the app is
     * paused, for example.
     */
    public void savePosition() {
        if (mVideoPlayerWithAdPlayback.isAdDisplayed()) {
            mAdsManager.pause();
        } else {
            mVideoPlayerWithAdPlayback.pauseContent();
        }
    }

    /**
     * Restore the previously saved progress location of the video. Can be called when
     * the app is resumed.
     */
    public void restorePosition() {
       // TODO: Implement this
    }

    /**
     * An event raised when ads are successfully loaded from the ad server via AdsLoader.
     */
    @Override
    public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
        // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
        // events for ad playback and errors.
        mAdsManager = adsManagerLoadedEvent.getAdsManager();

        // Attach event and error event listeners.
        mAdsManager.addAdErrorListener(this);
        mAdsManager.addAdEventListener(this);
        mAdsManager.init();
    }

    /**
     * Responds to AdEvents.
     */
    @Override
    public void onAdEvent(AdEvent adEvent) {
        log("Event: " + adEvent.getType());

        // These are the suggested event types to handle. For full list of all ad
        // event types, see the documentation for AdEvent.AdEventType.
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be
                // played. AdsManager.start() begins ad playback. This method is
                // ignored for VMAP or ad rules playlists, as the SDK will
                // automatically start executing the playlist.
                mAdsManager.start();
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before
                // a video ad is played.
                mVideoPlayerWithAdPlayback.switchToAdDisplay();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is
                // completed and you should start playing your content.
                mVideoPlayerWithAdPlayback.switchToContentDisplay();
                mVideoPlayerWithAdPlayback.playContent();
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }
                break;
            default:
                break;
        }
    }

    /**
     * An event raised when there is an error loading or playing ads.
     */
    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        AdError error = adErrorEvent.getError();
        log("Ad Error(" + error.getErrorCode() + "): "
                + error.getMessage());
        mVideoPlayerWithAdPlayback.switchToContentDisplay();
        mVideoPlayerWithAdPlayback.playContent();
    }
}

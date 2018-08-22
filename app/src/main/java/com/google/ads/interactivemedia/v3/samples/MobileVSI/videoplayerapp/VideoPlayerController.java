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
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.SampleVideoPlayer;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.VideoAdCallbackDelegate.OnContentCompleteListener;

import java.util.ArrayList;

/**
 * Ads logic for handling the IMA SDK integration code and events.
 */
public class VideoPlayerController extends LinearLayout
        implements AdsLoadedListener, AdEventListener, AdErrorListener {

    /**
     * Log interface, so we can output the log commands to the UI or similar.
     * Implementations are free to post the same message to logcat as well unless it
     * is specifically prohibited by calling logSkipLogcat(String)
     */
    public interface Logger {
        void log(String logMessage);
        void logSkipLogcat(String logMessage);
    }

    // ViewGroup to render an associated companion ad into.
    private ViewGroup companionView;

    // Ad-enabled video player.
    private VideoPlayerWithAdPlayback videoAdPlayer;

    // Ad UI container that is layered over the video player.
    private ViewGroup adUiContainer;

    // TextView to display the ad tag URL
    private TextView adTagURLView;

    // Button the user taps to begin video playback and ad request.
    private View playButton;

    private boolean adsComplete;

    private boolean contentComplete;

    // The AdsLoader instance exposes the requestAds method.
    private AdsLoader adsLoader;

    // AdsManager exposes methods to control ad playback and listen to ad events.
    private AdsManager adsManager;

    // VAST ad tag URL to use when requesting ads during video playback.
    private String currentAdTagUrl;

    // VAST ad tag URL to use when requesting ads during video playback.
    private String currentContentPath;

    // View that we can write log messages to, to display in the UI.
    private Logger logger;

    // The Android LogCat logger
    private MobileVSILogger sysLog;

    public VideoPlayerController(Context context) {
        super(context);
    }

    public VideoPlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        View rootView = getRootView();
        Resources resources = getResources();
        contentComplete = false;
        adsComplete = false;
        sysLog = new MobileVSILogger(getClass());

        VideoView videoView = (VideoView) rootView.findViewById(R.id.video_player);
        adUiContainer = (ViewGroup) rootView.findViewById(R.id.ad_ui_container);
        playButton = rootView.findViewById(R.id.play_button);
        companionView = (ViewGroup) rootView.findViewById(R.id.companion_ad_slot);
        adTagURLView = (TextView) rootView.findViewById(R.id.ad_tag_url_view);

        videoAdPlayer = new VideoPlayerWithAdPlayback(
            new SampleVideoPlayer(rootView.getContext(), videoView));

        // Create an AdsLoader and optionally set the language.
        ImaSdkFactory mSdkFactory = ImaSdkFactory.getInstance();
        ImaSdkSettings imaSdkSettings = mSdkFactory.createImaSdkSettings();
        imaSdkSettings.setLanguage(resources.getString(R.string.ad_ui_lang));
        adsLoader = mSdkFactory.createAdsLoader(getContext(), imaSdkSettings);
        adsLoader.addAdErrorListener(this);
        adsLoader.addAdsLoadedListener(this);

        videoAdPlayer.setOnContentCompleteListener(new OnContentCompleteListener() {
            @Override
            public void onContentComplete() {
                adsLoader.contentComplete();
                contentComplete = true;
                showPlayButton();
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

    private void resetState() {
        sysLog.d("Resetting state.");
        contentComplete = adsComplete = false;

        if (adsManager != null) {
            sysLog.d("destroying adsManager instance");
            adsManager.destroy();
        }

        if (videoAdPlayer.isAdDisplayed()) {
            sysLog.d("switching videoAdPlayer to content display");
            videoAdPlayer.switchToContentDisplay();
        }
        if (!videoAdPlayer.isStopped()) {
            sysLog.d("stopping videoAdPlayer content playback");
            videoAdPlayer.stopContent();
        }
        sysLog.d("state reset complete");
    }

    /**
     * Checks the current state and displays the play button if needed.
     */
    private void showPlayButton() {
        if (adsComplete && contentComplete) {
            playButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Request and subsequently play video ads from the ad server.
     */
    public void requestAndPlayAds() {
        // Reset the state and do some cleanup
        resetState();

        // Check the content video path
        if (currentContentPath == null || currentContentPath.isEmpty()) {
            logger.logSkipLogcat("No content video URL specified");
            sysLog.w("No content video URL specified");
            return;
        }
        videoAdPlayer.setContentVideoPath(currentContentPath);

        // Check the ad tag URL
        if (currentAdTagUrl == null || currentAdTagUrl.isEmpty()) {
            logger.logSkipLogcat("No VAST ad tag URL specified");
            sysLog.w("No VAST ad tag URL specified");
            videoAdPlayer.playContent();
            return;
        }

        ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
        AdDisplayContainer adDisplayContainer = sdkFactory.createAdDisplayContainer();
        adDisplayContainer.setPlayer(videoAdPlayer);
        adDisplayContainer.setAdContainer(adUiContainer);

        // Set up spots for companions.
        CompanionAdSlot companionAdSlot = sdkFactory.createCompanionAdSlot();
        companionAdSlot.setContainer(companionView);
        companionAdSlot.setSize(728, 90);
        ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<>();
        companionAdSlots.add(companionAdSlot);
        adDisplayContainer.setCompanionSlots(companionAdSlots);

        // Create the ads request.
        AdsRequest request = sdkFactory.createAdsRequest();
        request.setAdTagUrl(currentAdTagUrl);
        request.setAdDisplayContainer(adDisplayContainer);
        request.setContentProgressProvider(videoAdPlayer);

        // Request the ad.
        adsLoader.requestAds(request);
        sysLog.d("Ads request sent. URL: " + currentAdTagUrl);

        playButton.setVisibility(View.GONE);
        adTagURLView.setText(currentAdTagUrl);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Set the ad tag URL the player should use to request ads.
     */
    public void setAdTagUrl(String adTagUrl) {
        this.currentAdTagUrl = adTagUrl;
    }

    /**
     * Set metadata about the content video that the player must use.
     */
    public void setContentVideo(String videoPath) {
        this.currentContentPath = videoPath;
    }

    /**
     * Pause whatever is playing.
     */
    public void pauseAllPlayback() {
        if (videoAdPlayer.isPlaying()) {
            if (videoAdPlayer.isAdDisplayed()) {
                adsManager.pause();
            } else {
                videoAdPlayer.pauseContent();
            }
        }
    }

    /**
     * Resume whatever was being played.
     */
    public void resumeAllPlayback() {
       if (videoAdPlayer.isPaused()) {
           if (videoAdPlayer.isAdDisplayed()) {
               adsManager.resume();
           } else {
               videoAdPlayer.playContent();
           }
       }
    }

    /**
     * Stops ad or content and resets the state of the controller
     */
    public void stopAllPlayback() {
        sysLog.v("stopAllPlayback()");

        // Force show the play button
        contentComplete = true;
        adsComplete = true;
        showPlayButton();

        resetState();
    }

    /**
     * @return true if AdEventType.ALL_ADS_COMPLETED, false otherwise
     */
    public boolean isAdsComplete() {
        return adsComplete;
    }

    /**
     * @return true if onContentComplete() has been called by the VideoPlayerWithAdPlayback,
     * false otherwise
     */
    public boolean isContentComplete() {
        return contentComplete;
    }

    public String getCurrentAdTagUrl() {
        return currentAdTagUrl;
    }

    public String getCurrentContentPath() {
        return currentContentPath;
    }

    /**
     * An event raised when ads are successfully loaded from the ad server via AdsLoader.
     */
    @Override
    public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
        // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
        // events for ad playback and errors.
        adsManager = adsManagerLoadedEvent.getAdsManager();

        // Attach event and error event listeners.
        adsManager.addAdErrorListener(this);
        adsManager.addAdEventListener(this);
        adsManager.init();
    }

    /**
     * Responds to AdEvents.
     */
    @Override
    public void onAdEvent(AdEvent adEvent) {
        String logMessage = "Event: " + adEvent.getType();

        // These are the suggested event types to handle. For full list of all ad
        // event types, see the documentation for AdEvent.AdEventType.
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be
                // played. AdsManager.start() begins ad playback. This method is
                // ignored for VMAP or ad rules playlists, as the SDK will
                // automatically start executing the playlist.
                adsManager.start();
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before
                // a video ad is played.
                videoAdPlayer.switchToAdDisplay();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is
                // completed and you should start playing your content.
                if (!contentComplete) {
                    videoAdPlayer.switchToContentDisplay();
                    videoAdPlayer.playContent();
                }
                break;
            case ALL_ADS_COMPLETED:
                if (adsManager != null) {
                    adsManager.destroy();
                    adsManager = null;
                }
                adsComplete = true;
                showPlayButton();
                break;
            case LOG:
                logMessage += adEvent.getAdData().toString();
                break;
            default:
                break;
        }

        logger.log(logMessage);
    }

    /**
     * An event raised when there is an error loading or playing ads.
     */
    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        AdError error = adErrorEvent.getError();
        logger.log("Ad Error(" + error.getErrorType() + ", " + error.getErrorCode() + "): "
                + error.getMessage());

        if (error.getErrorType() == AdError.AdErrorType.LOAD) {
            // Mark ads complete to true, since ALL_ADS_COMPLETE will not be fired
            adsComplete = true;
        }
        videoAdPlayer.switchToContentDisplay();
        videoAdPlayer.playContent();
    }
}

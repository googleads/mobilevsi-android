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

import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.VideoPlayer;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.VideoAdCallbackDelegate.AdDisplayStateMonitor;

/**
 * Video player that can play content video and ads. This class provides the functions
 * play(), pause(), resume() and stop() in two variants: one each for ad and content playback.
 * The player performs the requested action only if the request variant is currently displayed,
 * which can be checked using isAdDisplayed(). Other state checks can be isPlaying(), isPaused()
 * or isStopped(). Video Player display can be switched using switchToContentDisplay() or
 * switchToAdDisplay()
 */
public class VideoPlayerWithAdPlayback
        implements VideoAdPlayer, ContentProgressProvider, AdDisplayStateMonitor {

    // Used to track if the underlying VideoPlayer is showing an ad (as opposed to a content video).
    private boolean isAdDisplayed;

    // Used to track the current content video URL to resume content playback.
    private String contentVideoUrl;

    // Used to track the current ad URL that is being shown or loaded.
    private String adVideoUrl;

    // The saved position in the content to resume to once we switch to content display.
    private int savedContentVideoPosition;

    // The wrapped video player. The same player is used for both ad and content playback.
    private VideoPlayer videoPlayer;

    // The delegate for firing VideoAdCallbacks when a VideoPlayer.PlayerCallback is fired.
    private VideoAdCallbackDelegate videoAdCallbackDelegate;

    private MobileVSILogger logger;

    public VideoPlayerWithAdPlayback(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
        isAdDisplayed = false;
        contentVideoUrl = adVideoUrl = null;
        savedContentVideoPosition = 0;
        videoAdCallbackDelegate = new VideoAdCallbackDelegate(this);
        videoPlayer.addPlayerCallback(videoAdCallbackDelegate);
        logger = new MobileVSILogger(this.getClass());
    }

    /**
     * Stops the currently playing content video and saves position in preparation for an ad to play.
     * Also disables the playback controls visible on screen.
     */
    public void switchToAdDisplay() {
        if (!isAdDisplayed) {
            savedContentVideoPosition = videoPlayer.getCurrentPosition();
            videoPlayer.stop();
            videoPlayer.disablePlaybackControls();
            isAdDisplayed = true;
            logger.d("Switching to ad display. Saving content position: " +
                    savedContentVideoPosition);
        } else {
            logger.w("switchToAdDisplay() called when ad is displayed. Ignoring call.");
        }
    }

    /**
     * Stops the ad if it is still playing. Sets the content video to resume from its previous
     * playback position. Re-enables the media controller.
     * playContent() must be called after this function to actually start playing content.
     */
    public void switchToContentDisplay() {
        if (contentVideoUrl == null || contentVideoUrl.isEmpty()) {
            logger.w("No content URL specified.");
            return;
        }
        if (isAdDisplayed) {
            if (!videoPlayer.isStopped()) {
                videoPlayer.stop();
            }
            videoPlayer.enablePlaybackControls();
            videoPlayer.setVideoPath(contentVideoUrl);
            videoPlayer.seekTo(savedContentVideoPosition);
            isAdDisplayed = false;
            logger.d("Switching to content display. Restoring content position: " +
                    savedContentVideoPosition);
        } else {
            logger.w("switchToContentDisplay() called when ad not is displayed. Ignoring call.");
        }
    }

    @Override
    public VideoProgressUpdate getContentProgress() {
        if (isAdDisplayed || videoPlayer.getDuration() <= 0) {
            return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
        }
        return new VideoProgressUpdate(videoPlayer.getCurrentPosition(),
                videoPlayer.getDuration());
    }

    @Override
    public VideoProgressUpdate getAdProgress() {
        if (!isAdDisplayed || videoPlayer.getDuration() <= 0) {
            return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
        }
        return new VideoProgressUpdate(videoPlayer.getCurrentPosition(),
                videoPlayer.getDuration());
    }

    public void setOnContentCompleteListener(VideoAdCallbackDelegate.OnContentCompleteListener listener) {
        videoAdCallbackDelegate.setOnContentCompleteListener(listener);
    }

    /**
     * Asks the videoPlayer to start/resume content video.
     * Ignored with a warning if ad is being displayed.
     */
    public void playContent() {
        if (!isAdDisplayed) {
            videoPlayer.play();
            logger.v("playContent()");
        } else {
            logger.w("playContent() called when ad is displayed. Ignoring call.");
        }
    }

    /**
     * Asks the videoPlayer to pause content video.
     * Ignored with a warning if ad is being displayed.
     */
    public void pauseContent() {
        if (!isAdDisplayed) {
            videoPlayer.pause();
            logger.v("pauseContent()");
        } else {
            logger.w("pauseContent() called when ad is displayed. Ignoring call.");
        }
    }

    /**
     * Asks the videoPlayer to stop content video.
     * Ignored with a warning if ad is being displayed.
     */
    public void stopContent() {
        if (!isAdDisplayed) {
            videoPlayer.stop();
            logger.v("stopContent()");
        } else {
            logger.w("stopContent() called when ad is displayed. Ignoring call.");
        }
    }

    public int getCurrentPosition() {
        return videoPlayer.getCurrentPosition();
    }

    /**
     * Asks the videoPlayer to seek content video to a particular position.
     * Saves the position if ad is being displayed, for resuming later.
     */
    public void seekContentTo(int videoPosition) {
        if (isAdDisplayed) {
            savedContentVideoPosition = videoPosition;
        } else {
            videoPlayer.seekTo(videoPosition);
        }
    }

    public int getDuration() {
        return videoPlayer.getDuration();
    }

    /**
     * @return true if the ad player is displayed currently, false if the content player is displayed.
     * NOTE: The ad may be playing, paused or stopped when it is displayed.
     */
    public boolean isAdDisplayed() {
        return isAdDisplayed;
    }

    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }

    public boolean isPaused() {
        return videoPlayer.isPaused();
    }

    public boolean isStopped() {
        return videoPlayer.isStopped();
    }

    /**
     * Set the path of the video to be played as content.
     */
    public void setContentVideoPath(String contentVideoUrl) {
        if (this.contentVideoUrl == null) {
            // If there is no value we need to tell videoPlayer to the value
            videoPlayer.setVideoPath(contentVideoUrl);
            logger.v("Setting content video path");
        }
        this.contentVideoUrl = contentVideoUrl;
    }

    @Override
    public void playAd() {
        // The player must be stopped
        if (isAdDisplayed && videoPlayer.isStopped()) {
            if (adVideoUrl == null || adVideoUrl.isEmpty()) {
                logger.w("No ad URL specified.");
                return;
            }
            videoPlayer.setVideoPath(adVideoUrl);
            videoPlayer.play();
            logger.v("playAd()");
        } else {
            if (!isAdDisplayed) {
                logger.w("playAd() called when ad is not displayed. Ignoring call.");
            }
            if (!videoPlayer.isStopped()) {
                logger.w("playAd() called when video player is not stopped. Ignoring call.");
            }
        }
    }

    @Override
    public void loadAd(String adVideoPath) {
        adVideoUrl = adVideoPath;
        logger.v("loadAd()");
    }

    @Override
    public void stopAd() {
        if (isAdDisplayed) {
            videoPlayer.stop();
            logger.v("stopAd()");
        } else {
            logger.w("stopAd() called when ad is not displayed. Ignoring call.");
        }
    }

    @Override
    public void pauseAd() {
        if (isAdDisplayed) {
            videoPlayer.pause();
            logger.v("pauseAd()");
        } else {
            logger.w("pauseAd() called when ad is not displayed. Ignoring call.");
        }
    }

    @Override
    public void resumeAd() {
        if (isAdDisplayed && videoPlayer.isPaused()) {
            videoPlayer.play();
            logger.v("resumeAd()");
        } else {
            if (!isAdDisplayed) {
                logger.w("resumeAd() called when ad is not displayed. Ignoring call.");
            }
            if (!videoPlayer.isPaused()) {
                logger.w("resumeAd() called when video player is not paused. Ignoring call.");
            }
        }
    }

    @Override
    public void addCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
        videoAdCallbackDelegate.addVideoAdPlayerCallback(videoAdPlayerCallback);
    }

    @Override
    public void removeCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
        videoAdCallbackDelegate.removeVideoAdPlayerCallback(videoAdPlayerCallback);
    }
}

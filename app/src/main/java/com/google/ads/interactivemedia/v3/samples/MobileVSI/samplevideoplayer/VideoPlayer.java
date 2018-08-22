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
package com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer;

/**
 * Interface definition for controlling video playback.
 */
public interface VideoPlayer {

    /**
     *  Interface for alerting caller of major video events.
     */
    interface PlayerCallback {

        /**
         * Called when the current video starts playing from the beginning.
         */
        void onStarted();

        /**
         * Called when the current video pauses playback.
         */
        void onPaused();

        /**
         * Called when the current video resumes playing from a paused state.
         */
        void onResumed();

        /**
         * Called when the current video is stopped.
         */
        void onStopped();

        /**
         * Called when the current video has completed playback to the end of the video.
         */
        void onCompleted();

        /**
         * Called when an error occurs during video playback.
         */
        void onError();
    }

    /**
     * Play the currently loaded video from its current position.
     */
    void play();

    /**
     * Pause the currently loaded video.
     */
    void pause();

    /**
     * Stop playing the currently loaded video.
     */
    void stop();

    /**
     * Get the playback progress state (milliseconds) of the current video.
     */
    int getCurrentPosition();

    /**
     * Progress the currently loaded video to the given position (milliseconds).
     */
    void seekTo(int videoPosition);

    /**
     * Get the total length of the currently loaded video in milliseconds.
     */
    int getDuration();

    /**
     * Get the volume of the video player from 0 (mute) to 100 (loudest).
     */
    int getVolume();

    /**
     * @return true if the video is currently playing
     */
    boolean isPlaying();

    /**
     * @return true if playback is currently paused. Calling play() would cause
     * it to resume playback from the position the video was paused at
     */
    boolean isPaused();

    /**
     * @return true if playback is currently stopped. Calling play() would cause
     * it to start playback from the beginning of the video
     */
    boolean isStopped();

    /**
     * Prevent the media controller (playback controls) from appearing.
     */
    void disablePlaybackControls();

    /**
     * Allow the media controller (playback controls) to appear when appropriate.
     */
    void enablePlaybackControls();

    /**
     * Set the URL or path of the video to play.
     */
    void setVideoPath(String videoPath);

    /**
     * Provide the player with a getAdTagUrl for major video events (pause, complete, resume, etc).
     */
    void addPlayerCallback(PlayerCallback callback);

    /**
     * Remove a player getAdTagUrl from getting notified on video events.
     */
    void removePlayerCallback(PlayerCallback callback);
}

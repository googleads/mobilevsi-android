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

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * A VideoPlayer that provides custom functionality like resumable playback.
 */
public class SampleVideoPlayer implements VideoPlayer {

    private enum PlaybackState {
        STOPPED, PAUSED, PLAYING
    }

    private VideoView videoView;
    private MediaController mediaController;
    private PlaybackState playbackState;
    private String videoPath;
    private int savedPosition;
    private final List<PlayerCallback> playerCallbacks = new ArrayList<>(2);
    private MediaPlayerListenerImpl listener;

    public SampleVideoPlayer(Context context, VideoView videoView) {
        this.videoView = videoView;
        playbackState = PlaybackState.STOPPED;
        savedPosition = 0;
        videoPath = null;
        mediaController = new MediaController(context);

        mediaController.setAnchorView(videoView);
        enablePlaybackControls();

        listener = new MediaPlayerListenerImpl();
        this.videoView.setOnCompletionListener(listener);
        this.videoView.setOnErrorListener(listener);
    }

    // Methods implementing the VideoPlayer interface.
    @Override
    public void play() {
        switch (playbackState) {
            case PAUSED:
                videoView.start();
                videoView.seekTo(savedPosition);
                playbackState = PlaybackState.PLAYING;
                fireCallbackOnResumed();
                break;
            case STOPPED:
                videoView.setVideoPath(videoPath);
                videoView.start();
                if (savedPosition != 0) {
                    videoView.seekTo(savedPosition);
                }
                playbackState = PlaybackState.PLAYING;
                fireCallbackOnStarted();
                break;
            default: // Already playing
                break;
        }
    }

    @Override
    public void pause() {
        if (playbackState == PlaybackState.PLAYING) {
            videoView.pause();
            playbackState = PlaybackState.PAUSED;
            savedPosition = videoView.getCurrentPosition();
            fireCallbackOnPaused();
        }
    }

    private void performActionsForStop() {
        playbackState = PlaybackState.STOPPED;
        savedPosition = 0;
    }

    @Override
    public void stop() {
        if (playbackState != PlaybackState.STOPPED) {
            videoView.stopPlayback();
            performActionsForStop();
            fireCallbackOnStopped();
        }
    }

    @Override
    public int getCurrentPosition() {
        return videoView.getCurrentPosition();
    }

    @Override
    public void seekTo(int videoPosition) {
        if (playbackState == PlaybackState.PLAYING) {
            videoView.seekTo(videoPosition);
        } else {
            savedPosition = videoPosition;
        }
    }

    @Override
    public int getDuration() {
        return playbackState == PlaybackState.STOPPED ? 0 : videoView.getDuration();
    }

    @Override
    public int getVolume() {
        // MediaPlayer is at full volume by default; never changed here.
        return 100;
    }

    @Override
    public boolean isPlaying() {
        return playbackState == PlaybackState.PLAYING;
    }

    @Override
    public boolean isPaused() {
        return playbackState == PlaybackState.PAUSED;
    }

    @Override
    public boolean isStopped() {
        return playbackState == PlaybackState.STOPPED;
    }

    @Override
    public void disablePlaybackControls() {
        videoView.setMediaController(null);
    }

    @Override
    public void enablePlaybackControls() {
        videoView.setMediaController(mediaController);
    }

    @Override
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    @Override
    public void addPlayerCallback(PlayerCallback callback) {
        playerCallbacks.add(callback);
    }

    @Override
    public void removePlayerCallback(PlayerCallback callback) {
        playerCallbacks.remove(callback);
    }

    private void fireCallbackOnStarted() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onStarted();
        }
    }

    private void fireCallbackOnPaused() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onPaused();
        }
    }

    private void fireCallbackOnResumed() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onResumed();
        }
    }

    private void fireCallbackOnStopped() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onStopped();
        }
    }

    private void fireCallbackOnError() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onError();
        }
    }

    private void fireCallbackOnCompleted() {
        for (PlayerCallback callback : playerCallbacks) {
            callback.onCompleted();
        }
    }

    private class MediaPlayerListenerImpl implements OnCompletionListener, OnErrorListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            disablePlaybackControls();
            // Reset the MediaPlayer.
            // This prevents a race condition which occasionally results in the media
            // player crashing when switching between videos.
            mediaPlayer.reset();
            mediaPlayer.setDisplay(videoView.getHolder());
            enablePlaybackControls();
            performActionsForStop();

            fireCallbackOnCompleted();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            performActionsForStop();
            fireCallbackOnError();

            // Returning true signals to MediaPlayer that we handled the error. This will
            // prevent the completion handler from being called.
            return true;
        }
    }
}

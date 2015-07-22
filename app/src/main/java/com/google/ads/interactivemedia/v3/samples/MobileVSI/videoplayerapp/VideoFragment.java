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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.SampleVideoPlayer;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.samplevideoplayer.VideoPlayer;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;

/**
 * The main fragment for displaying video content.
 */
public class VideoFragment extends Fragment {

    private VideoPlayerController mVideoPlayerController;
    private LinearLayout videoFragmentLayout;
    private TextView logText;
    private ScrollView logScroll;

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        VideoView videoView = (VideoView) rootView.findViewById(R.id.video_player);
        ViewGroup adUiContainer = (ViewGroup) rootView.findViewById(R.id.ad_ui_container);
        View playButton = rootView.findViewById(R.id.play_button);

        ViewGroup companionAdSlot = (ViewGroup) rootView.findViewById(R.id.companion_ad_slot);
        TextView videoAdTagURL = (TextView) rootView.findViewById(R.id.video_ad_tag_url);
        videoFragmentLayout = (LinearLayout) rootView.findViewById(R.id.video_fragment_layout);

        logText = (TextView) rootView.findViewById(R.id.log_text);
        logScroll = (ScrollView) rootView.findViewById(R.id.log_scroll);

        VideoPlayer videoPlayer = new SampleVideoPlayer(getActivity(), videoView);
        VideoPlayerWithAdPlayback videoPlayerWithAdPlayback =
                new VideoPlayerWithAdPlayback(videoPlayer);
        mVideoPlayerController = new VideoPlayerController(this.getActivity(),
                playButton, videoPlayerWithAdPlayback, getString(R.string.ad_ui_lang),
                adUiContainer, companionAdSlot, new LoggerImpl());

        return rootView;
    }

    public void loadVideo(VideoItemMetadata videoItemMetadata) {
        mVideoPlayerController.setContentVideo(videoItemMetadata.getVideoUrl());
        mVideoPlayerController.setAdTagUrl(videoItemMetadata.getAdTagUrl());
        mVideoPlayerController.requestAndPlayAds();
    }

    /**
     * Shows or hides all non-video UI elements to make the video as large as possible.
     */
    public void makeFullscreen(boolean isFullscreen) {
        for (int i = 0; i < videoFragmentLayout.getChildCount(); i++) {
            View view = videoFragmentLayout.getChildAt(i);
            // If it's not the video element, hide or show it, depending on fullscreen status.
            if (view.getId() != R.id.videoContainer) {
                if (isFullscreen) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        if (mVideoPlayerController != null) {
            mVideoPlayerController.savePosition();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mVideoPlayerController != null) {
            mVideoPlayerController.restorePosition();
        }
        super.onResume();
    }

    private class LoggerImpl implements VideoPlayerController.Logger {

        @Override
        public void log(String message) {
            MobileVSILogger.log(getClass(), Log.INFO, message);
            if (logText != null) {
                logText.append(message);
            }
            if (logScroll != null) {
                logScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        logScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }
    }
}

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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

/**
 * The main fragment for displaying video content.
 */
public class VideoFragment extends Fragment
        implements OnClickListener, DialogInterface.OnClickListener{

    private VideoPlayerController playerController;
    private ViewGroup videoFragmentLayout;
    private FloatingActionButton shareButton;
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

        videoFragmentLayout = (ViewGroup) rootView.findViewById(R.id.video_fragment_layout);
        playerController = (VideoPlayerController) rootView.findViewById(R.id.video_player_controller);
        logScroll = (ScrollView) rootView.findViewById(R.id.log_scroll);
        logText = (TextView) rootView.findViewById(R.id.log_text);
        shareButton = (FloatingActionButton) rootView.findViewById(R.id.share_floating_button);

        playerController.setLogger(new LoggerImpl());
        shareButton.setOnClickListener(this);
        return rootView;
    }

    public void loadVideo(VideoItemMetadata videoItemMetadata) {
        playerController.setContentVideo(videoItemMetadata.getVideoUrl());
        playerController.setAdTagUrl(videoItemMetadata.getAdTagUrl());
        logText.setText("");
        playerController.requestAndPlayAds();
    }

    /**
     * Shows or hides all non-video UI elements to make the video as large as possible.
     */
    public void switchToLandscape(boolean isFullscreen) {
        toggleVisibility(videoFragmentLayout, isFullscreen ? View.GONE : View.VISIBLE);
    }

    private void toggleVisibility(ViewGroup viewGroup, int visibility) {
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; i++) {
            View view = viewGroup.getChildAt(i);
            if (view == playerController.getParent()) {
                toggleVisibility((ViewGroup)view, visibility);
            } else if (view != playerController) {
                view.setVisibility(visibility);
            }
        }
    }

    @Override
    public void onPause() {
        if (playerController != null) {
            playerController.pauseAllPlayback();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (playerController != null) {
            playerController.resumeAllPlayback();
        }
        super.onResume();
    }

    // Implementing OnClickListener.onClick(View)
    @Override
    public void onClick(View v) {
        // Share button was clicked
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.share_dialog_title)
            .setItems(R.array.share_dialog_options, this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Implementing DialogInterface.OnClickListener.onClick(DialogInterface, int)
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // A Share dialog box option was clicked
        // TODO(radsaggi): Complete this implementation
        Snackbar.make(videoFragmentLayout, "This option was selected: " + which, Snackbar.LENGTH_LONG).show();
    }

    private void composeEmail(String subject, Uri attachment) {
        Activity activity = getActivity();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    private class LoggerImpl implements VideoPlayerController.Logger {

        private static final String TIME_FORMAT = "HH:mm:ss";

        private SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);

        @Override
        public void log(String logMessage) {
            MobileVSILogger.log(getClass(), Log.INFO, logMessage);
            logSkipLogcat(logMessage);
        }

        @Override
        public void logSkipLogcat(String message) {
            if (logText != null) {
                logText.append(prependTime(message));
                logText.append("\n");
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

        private String prependTime(String message) {
            String time = formatter.format(Calendar.getInstance());
            return "(" + time + ") " + message;
        }
    }
}

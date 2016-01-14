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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;

import java.util.ArrayList;
import java.util.List;


/**
 * The fragment for displaying the selected AdTagUrl and allowing it to be edited.
 */
public class AdTagUrlDisplayFragment extends Fragment implements OnClickListener {

    public interface OnVideoAdStartListener {
        void onVideoAdStartAction(VideoItemMetadata videoItemMetadata);
    }

    private EditText adTagUrlEdit;
    private Button startButton;

    private VideoItemMetadata videoItemMetadata;

    private List<OnVideoAdStartListener> onVideoAdStartListeners;

    public AdTagUrlDisplayFragment() {
        onVideoAdStartListeners = new ArrayList<>(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ad_tag_url_display, container, false);

        adTagUrlEdit = (EditText) rootView.findViewById(R.id.ad_tag_url_edit);
        startButton = (Button) rootView.findViewById(R.id.start_button);

        startButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // Start Button was clicked

        String adTagUrl = adTagUrlEdit.getText().toString().trim();
        videoItemMetadata = new VideoItemMetadata(
                videoItemMetadata.getVideoUrl(),
                videoItemMetadata.getTitle(),
                adTagUrl);

        fireOnVideoAdStartedAction(videoItemMetadata);
    }

    public void setVideoItemMetadata(VideoItemMetadata videoItemMetadata) {
        this.videoItemMetadata = videoItemMetadata;
        adTagUrlEdit.setText(videoItemMetadata.getAdTagUrl());
    }

    public void addOnVideoAdStartListener(OnVideoAdStartListener listener) {
        onVideoAdStartListeners.add(listener);
    }

    public void removeOnVideoAdStartListener(OnVideoAdStartListener listener) {
        onVideoAdStartListeners.remove(listener);
    }

    private void fireOnVideoAdStartedAction(VideoItemMetadata metadata) {
        int n = onVideoAdStartListeners.size();
        for (int i = 0; i < n; i++) {
            onVideoAdStartListeners.get(i).onVideoAdStartAction(videoItemMetadata);
        }
    }
}

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
                adTagUrl,
                videoItemMetadata.getImageResource());

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

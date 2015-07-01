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
package com.google.ads.interactivemedia.v3.samples.MobileVSI.slidermenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.DirectInputVideoListItem;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.SampleVideoListItem;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoItemMetadata;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoListItem;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoListItem.VideoListItemCallback;

/**
 * Fragment for displaying a playlist of video thumbnails that the user can select from to play.
 */
public class VideoListFragment extends Fragment
        implements VideoListItemCallback, OnItemClickListener {

    /**
     * Listener called when the user selects a video from the list.
     * Container activity must implement this interface.
     */
    public interface OnVideoSelectedListener {
        void onVideoSelected(VideoItemMetadata videoItemMetadata);
    }

    private OnVideoSelectedListener onVideoSelectedListener;

    private VideoItemAdapter videoItemAdapter;

    private ListView listView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onVideoSelectedListener = (OnVideoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + OnVideoSelectedListener.class.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_list, container, false);

        videoItemAdapter = new VideoItemAdapter(rootView.getContext(), getAllVideoItems());

        listView = (ListView) rootView.findViewById(R.id.videoListView);
        listView.setAdapter(videoItemAdapter);
        listView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onVideoSelectedListener != null) {
            VideoListItem selectedVideoListItem = (VideoListItem) listView.getItemAtPosition(position);
            selectedVideoListItem.fireCallback(getActivity(), this);
        }
    }

    @Override
    public void deliverVideoItemMetadata(VideoItemMetadata metadata) {
        onVideoSelectedListener.onVideoSelected(metadata);
    }

    public static List<VideoListItem> getAllVideoItems() {
        final List<VideoListItem> videoListItems = new ArrayList<>();

        int imageId = R.drawable.thumbnail1;
        videoListItems.add(new SampleVideoListItem(imageId));
        videoListItems.add(new DirectInputVideoListItem(imageId));

        return videoListItems;
    }
}
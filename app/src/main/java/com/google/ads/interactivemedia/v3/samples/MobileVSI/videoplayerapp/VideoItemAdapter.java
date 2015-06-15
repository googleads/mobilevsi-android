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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel.VideoListItem;

import java.util.List;

/**
 * Renders VideoItems into a GridView for displaying videos in a playlist format.
 */
public class VideoItemAdapter extends ArrayAdapter<VideoListItem> {

    private int mLayoutResourceId;

    public VideoItemAdapter(Context context, int layoutResourceId, List<VideoListItem> data) {
        super(context, layoutResourceId, data);
        this.mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoItemHolder videoItemHolder;
        View row = convertView;

        // Check if it's recycled.
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(mLayoutResourceId, parent, false);
            videoItemHolder = new VideoItemHolder();
            videoItemHolder.title = (TextView) row.findViewById(R.id.videoItemText);
            videoItemHolder.image = (ImageView) row.findViewById(R.id.videoItemImage);
            row.setTag(videoItemHolder);
        } else {
            videoItemHolder = (VideoItemHolder) row.getTag();
        }

        VideoListItem item = getItem(position);

        videoItemHolder.title.setText(item.getTitle());
        videoItemHolder.image.setImageResource(item.getThumbnailResourceId());

        return row;
    }

    /**
     * Holds the UI element equivalents of a VideoItemMetadata.
     */
    private class VideoItemHolder {

        TextView title;
        ImageView image;
    }

}
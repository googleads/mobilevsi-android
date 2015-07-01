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

    private static final int LAYOUT_RESOURCE_ID = R.layout.video_item;

    public VideoItemAdapter(Context context, List<VideoListItem> data) {
        super(context, LAYOUT_RESOURCE_ID, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // Check if it's recycled.
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
        }

        ImageView imageView = (ImageView) row.findViewById(R.id.slidermenu_videoitem_image);
        TextView textView = (TextView) row.findViewById(R.id.slidermenu_videoitem_text);

        VideoListItem item = getItem(position);
        imageView.setImageResource(item.getThumbnailResourceId());
        textView.setText(item.getTitle());

        return row;
    }
}
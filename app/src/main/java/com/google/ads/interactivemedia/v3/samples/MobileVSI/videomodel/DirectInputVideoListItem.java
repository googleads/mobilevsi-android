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
package com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ActivityStarterWithContext;

/**
 * For a direct input of the ad tag url, through a text field UI.
 */
public class DirectInputVideoListItem extends VideoListItem {

    public DirectInputVideoListItem(int mThumbnailResourceId) {
        super("Manual Tag Input", mThumbnailResourceId);
    }

    @Override
    public void fireCallback(ActivityStarterWithContext asc, final VideoListItemCallback callback) {
        Context c = asc.getContext();
        final EditText txtUrl = new EditText(c);
        txtUrl.setHint("VAST ad tag URL");

        new AlertDialog.Builder(c)
                .setTitle("Custom VAST Ad Tag URL")
                .setView(txtUrl)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String customAdTagUrl = txtUrl.getText().toString();
                        callback.deliverVideoItemMetadata(generateMetadata(customAdTagUrl));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

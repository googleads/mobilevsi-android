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

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ActivityStarterWithContext;

/**
 * For a sample ad tag url, a predefined value.
 */
public class SampleVideoListItem extends VideoListItem {

    public SampleVideoListItem(int mThumbnailResourceId) {
        super("Sample Tag", mThumbnailResourceId);
    }

    public void fireCallback(ActivityStarterWithContext c, VideoListItemCallback callback) {
        String adTagURL = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x360&"
                + "iu=/6062/iab_vast_samples/skippable&ciu_szs=300x250,728x90&impl=s&"
                + "gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&"
                + "url=[referrer_url]&correlator=[timestamp]";

        callback.deliverVideoItemMetadata(generateMetadata(adTagURL));
    }
}

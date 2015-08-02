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

import android.provider.MediaStore;

/**
 * Information about a video playlist item that the user will select in a playlist.
 */
public final class VideoItemMetadata {

    private final String mTitle;
    private final String mVideoUrl;
    private final String mAdTagUrl;

    public VideoItemMetadata(String videoUrl, String title, String adTagUrl) {
        mTitle = title;
        mAdTagUrl = adTagUrl;
        mVideoUrl = videoUrl;
    }

    /**
     * Returns the title of the video item.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the URL of the content video.
     */
    public String getVideoUrl() {
        return mVideoUrl;
    }

    /**
     * Returns the ad tag for the video.
     */
    public String getAdTagUrl() {
        return mAdTagUrl;
    }

    /**
     * Create a new VideoItemMetadata by setting a new ad tag URL
     */
    public VideoItemMetadata setAdTagUrl(String adTagUrl) {
        return new VideoItemMetadata(mVideoUrl, mTitle, adTagUrl);
    }
}

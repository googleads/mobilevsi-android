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

/**
 * This class encapsulates a single Item in the VideoList. It also provides capability to execute
 * custom actions to fetch an ad tag url when the item is selected by the user.
 */
public abstract class VideoListItem {

    /**
     * Callback used by the VideoListItem to return the obtained VideoItemMetadata info
     */
    public interface VideoListItemCallback {
        void deliverVideoItemMetadata(VideoItemMetadata metadata);
    }

    private final String mTitle;
    private final int mThumbnailResourceId;

    /**
     * The url of the Video that will be played by default when generateMetadata(String) is called.
     * Use generateMetadata(String, String) to pass a custom video url.
     */
    private static final String DEFAULT_VIDEO_URL =
            "http://rmcdn.2mdn.net/MotifFiles/html/1248596/android_1330378998288.mp4";

    public VideoListItem(String mTitle, int mThumbnailResourceId) {
        this.mTitle = mTitle;
        this.mThumbnailResourceId = mThumbnailResourceId;
    }

    public final String getTitle() {
        return mTitle;
    }

    public final int getThumbnailResourceId() {
        return mThumbnailResourceId;
    }

    /**
     * Subclasses are expected to call this method in the fireCallback implementation with the
     * adTagURL they obtained. The function generates a VideoItemMetadata using existing properties
     * in this class. This function uses the DEFAULT_VIDEO_URL for the content video.
     * @param adTagURL the adTagURL to be packed in the VideoItemMetadata object
     * @return the VideoItemMetadata using properties of this class, the passed adTagURL and
     *         DEFAULT_VIDEO_URL for the content video
     */
    protected final VideoItemMetadata generateMetadata(String adTagURL) {
        return generateMetadata(adTagURL, DEFAULT_VIDEO_URL);
    }

    /**
     * Subclasses are expected to call this method in the fireCallback implementation with the
     * adTagURL they obtained. The function generates a VideoItemMetadata using existing properties
     * in this class. This function uses a custom videoURL for the content video.
     * @param adTagURL the adTagURL to be packed in the VideoItemMetadata object
     * @param videoURL the content videoURL to be packed in the VideoMetadata object
     * @return the VideoItemMetadata using properties of this class, the passed adTagURL and videoURL
     */
    protected final VideoItemMetadata generateMetadata(String adTagURL, String videoURL) {
        return new VideoItemMetadata(videoURL, mTitle, adTagURL, mThumbnailResourceId);
    }

    /**
     * Subclasses must provide a way to obtain the ad tag url from the user. It may include input
     * methods like a text field or scanning a QR code or even a Google drive file etc. This
     * function is called by the app whenever the ListItem is selected in the app. The ad tag url
     * obtained by any means must be packed in a VideoItemMetadata object which must then be passed
     * to callback.deliverVideoItemMetadata(VideoItemMetadata). Though they are not bound to do so,
     * implementations may make use of the generateMetadata(String) function to get the
     * VideoItemMetadata instance, in which case the typical function call will be something like:
     *
     * String adTagURL = ...
     * callback.deliverVideoItemMetadata(generateMetadata(adTagURL));
     *
     * @param c The Android Context which the implementing function may use to fire up some UI
     *          elements like an alert box, or some other input box.
     * @param callback The callback used to deliver the ad tag url packaged in a VideoItemMetadata to
     *                 the app.
     */
    public abstract void fireCallback(android.content.Context c, VideoListItemCallback callback);
}

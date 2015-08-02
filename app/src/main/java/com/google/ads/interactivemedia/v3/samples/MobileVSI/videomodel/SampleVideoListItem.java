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
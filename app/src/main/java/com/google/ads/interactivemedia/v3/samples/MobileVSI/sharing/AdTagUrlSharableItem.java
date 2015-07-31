package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

/**
 * Used for sharing the ad tag URL of the session.
 */
public class AdTagUrlSharableItem implements SharableStringItem {

    private String adTagURL;

    public AdTagUrlSharableItem(String adTagURL) {
        this.adTagURL = adTagURL;
    }

    @Override
    public String getString() {
        return "Ad Tag URL: " + adTagURL;
    }
}

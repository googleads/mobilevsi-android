package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A SharablePackage that defines a tearsheet.
 * This normally comprises an ad tag URL and a screenshot of the video ad player.
 */
public class TearSheetPackage extends SharablePackage {

    private AdTagUrlSharableItem adTagURL;
    private ScreenshotSharableItem screenshot;

    public TearSheetPackage(AdTagUrlSharableItem adTagURL, ScreenshotSharableItem screenshot) {
        super(Arrays.asList(adTagURL, screenshot));
        this.adTagURL = adTagURL;
        this.screenshot = screenshot;
    }

    @Override
    public String getDataTitle() {
        return "Tearsheet for ad";
    }

    @Override
    public String getDataContent() {
        return adTagURL.toString();
    }

    @Override
    public List<SharableItem> getExtraItems() {
        return Collections.<SharableItem>singletonList(screenshot);
    }
}

package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A SharablePackage that defines a debug session.
 * This normally comprises an ad tag URL, a screenshot,
 * metadata of the system and the sdk logs of the session.
 */
public class DebugSessionPackage extends SharablePackage {
    private AdTagUrlSharableItem adTagUrl;
    private ScreenshotSharableItem screenshot;
    private MetadataSharableItem metadata;
    private SdkLogsSharableItem sdkLogs;

    public DebugSessionPackage(AdTagUrlSharableItem adTagUrl, ScreenshotSharableItem screenshot,
                               MetadataSharableItem metadata, SdkLogsSharableItem sdkLogs) {
        super(Arrays.asList(adTagUrl, screenshot, metadata, sdkLogs));
        this.adTagUrl = adTagUrl;
        this.screenshot = screenshot;
        this.metadata = metadata;
        this.sdkLogs = sdkLogs;
    }

    @Override
    public String getDataTitle() {
        return "Ad Debug session";
    }

    @Override
    public String getDataContent() {
        return metadata.getString() + "\n\n" + adTagUrl.getString();
    }

    @Override
    public List<SharableItem> getExtraItems() {
        ArrayList<SharableItem> attachments = new ArrayList<>(2);
        Collections.addAll(attachments, screenshot, sdkLogs);
        return attachments;
    }
}

package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import android.os.Build;

/**
 * Used for sharing the metadata of the system.
 */
public class MetadataSharableItem implements SharableStringItem {

    private String metadata;

    public MetadataSharableItem(String sdkVersion) {
        metadata = getMetadata(sdkVersion);
    }

    private String getMetadata(String sdkVersion) {
        return "Android Version: " + Build.VERSION.RELEASE
               + "\nSystem Architecture: " + System.getProperty("os.arch")
               + "\nIMA SDK for Android " + sdkVersion;
    }

    @Override
    public String getString() {
        return "Metadata:\n" + metadata;
    }
}

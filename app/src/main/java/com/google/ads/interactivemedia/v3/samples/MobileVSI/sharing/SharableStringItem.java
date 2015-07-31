package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

/**
 * An interface for declaring an item or property that may be shared as a String.
 * Use this instead of using SharableItem interface for items that can be
 * represented or converted to a String
 */
public interface SharableStringItem extends SharableItem {
    String getString();
}
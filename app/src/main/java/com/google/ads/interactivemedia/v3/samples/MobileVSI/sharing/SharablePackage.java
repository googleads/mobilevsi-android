package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import java.util.List;

/**
 * A logical collection of SharableItems that may be shared using a SharedAction
 */
public abstract class SharablePackage {
    private List<SharableItem> sharableItems;

    public SharablePackage(List<SharableItem> sharableItems) {
        this.sharableItems = sharableItems;
    }

    public abstract String getDataTitle();

    public abstract String getDataContent();

    public abstract List<SharableItem> getExtraItems();

    public List<SharableItem> getAllItems() {
        return sharableItems;
    }
}

package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import android.content.Intent;

/**
 * Defines ways to share a SharablePackage
 */
public abstract class ShareAction {
    public abstract Intent getShareIntent(SharablePackage sharablePackage);
}

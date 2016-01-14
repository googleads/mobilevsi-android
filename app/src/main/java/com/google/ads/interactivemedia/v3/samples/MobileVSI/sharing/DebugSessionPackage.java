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

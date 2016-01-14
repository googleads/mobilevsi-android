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

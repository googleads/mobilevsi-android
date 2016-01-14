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

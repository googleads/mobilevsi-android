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

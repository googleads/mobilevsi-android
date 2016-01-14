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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * A ShareAction that generates an Email Intent
 */
public class EmailShareAction extends ShareAction {

    private Activity activity;

    public EmailShareAction(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Intent getShareIntent(SharablePackage sharablePackage) {
        String subject = sharablePackage.getDataTitle();
        String content = sharablePackage.getDataContent();

        List<SharableItem> attachmentItems = sharablePackage.getExtraItems();
        ArrayList<Uri> attachments = new ArrayList<>();
        for (int i = 0; i < attachmentItems.size(); i++) {
            SharableItem item = attachmentItems.get(i);
            if (item instanceof SharableUriItem) {
                SharableUriItem uriItem = (SharableUriItem) item;
                attachments.add(uriItem.getUri());
            } else {
                SharableStringItem stringItem = (SharableStringItem) item;
                attachments.add(Uri.parse(stringItem.getString()));
            }
        }

        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity);
        builder.setType("message/rfc822");
        builder.setSubject(subject);
        builder.setText(content);
        for (Uri stream: attachments) {
            builder.addStream(stream);
        }

        return builder.getIntent();
    }
}

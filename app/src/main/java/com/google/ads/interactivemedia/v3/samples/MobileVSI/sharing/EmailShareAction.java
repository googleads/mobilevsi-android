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

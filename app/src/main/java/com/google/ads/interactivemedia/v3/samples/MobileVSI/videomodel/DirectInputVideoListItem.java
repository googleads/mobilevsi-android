package com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ActivityStarterWithContext;

/**
 * For a direct input of the ad tag url, through a text field UI.
 */
public class DirectInputVideoListItem extends VideoListItem {

    public DirectInputVideoListItem(int mThumbnailResourceId) {
        super("Manual Tag Input", mThumbnailResourceId);
    }

    @Override
    public void fireCallback(ActivityStarterWithContext asc, final VideoListItemCallback callback) {
        Context c = asc.getContext();
        final EditText txtUrl = new EditText(c);
        txtUrl.setHint("VAST ad tag URL");

        new AlertDialog.Builder(c)
                .setTitle("Custom VAST Ad Tag URL")
                .setView(txtUrl)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String customAdTagUrl = txtUrl.getText().toString();
                        callback.deliverVideoItemMetadata(generateMetadata(customAdTagUrl));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

package com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * For a direct input of the ad tag url, through a text field UI.
 */
public class DirectInputVideoListItem extends VideoListItem {

    public DirectInputVideoListItem(int mThumbnailResourceId) {
        super("Direct Tag Input", mThumbnailResourceId);
    }

    public void fireCallback(Context c, final VideoListItemCallback callback) {
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}

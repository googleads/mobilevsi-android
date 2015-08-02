package com.google.ads.interactivemedia.v3.samples.MobileVSI.videomodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.ActivityStarterWithContext;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVSILogger;
import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVsiUriSchemaUtil;

/**
 * For invoking an app for scanning QR Code
 */
public class QRCodeVideoListItem extends VideoListItem {

    private static final String QR_CODE_APP_INTENT_ACTION = "com.google.zxing.client.android.SCAN";
    private static final String QR_CODE_APP_RESULT_KEY = "SCAN_RESULT";
    private static final String QR_CODE_APP_EXTRA_KEY = "SCAN_MODE";
    private static final String QR_CODE_APP_EXTRA_VALUE = "QR_CODE_MODE";

    private MobileVSILogger logger;

    public QRCodeVideoListItem(int mThumbnailResourceId) {
        super("Scan QR Code", mThumbnailResourceId);
        logger = new MobileVSILogger(getClass());
    }

    @Override
    public void fireCallback(ActivityStarterWithContext asc, final VideoListItemCallback callback) {
        final Context c = asc.getContext();

        try {
            Intent intent = new Intent(QR_CODE_APP_INTENT_ACTION);
            intent.putExtra(QR_CODE_APP_EXTRA_KEY, QR_CODE_APP_EXTRA_VALUE);

            asc.startActivityWithCallback(intent, new ActivityStarterWithContext.ActivityStarterCallback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle extras = data.getExtras();
                        String result = extras.getString(QR_CODE_APP_RESULT_KEY);
                        logger.i("Received scan result: " + result);
                        if (MobileVsiUriSchemaUtil.matchesCustomSchema(result)) {
                            result = MobileVsiUriSchemaUtil.parseLaunchUri(result);
                            logger.i("Decoded to this: " + result);
                        }
                        callback.deliverVideoItemMetadata(generateMetadata(result));
                    }
                }
            });
        } catch (ActivityNotFoundException e) {
            logger.e("No app found to handle QR Code request");
            AlertDialog alert = new AlertDialog.Builder(c)
                    .setTitle("Error!")
                    .setMessage("No QR code scanner app found on the phone.")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            callMarketForQRCodeApp(c);
                        }
                    })
                    .create();
            alert.show();

        }
    }

    public void callMarketForQRCodeApp(Context c) {
        try {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            c.startActivity(marketIntent);
        } catch (Exception e) {
            logger.e("No app found to handle market request");
        }
    }
}

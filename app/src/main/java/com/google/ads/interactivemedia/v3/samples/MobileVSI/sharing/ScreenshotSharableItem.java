package com.google.ads.interactivemedia.v3.samples.MobileVSI.sharing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.ViewGroup;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVSILogger;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Used for sharing the screenshot of the video ad player.
 */
public class ScreenshotSharableItem implements SharableUriItem {
    private Uri screenshotUri;

    private static final String SCREENSHOTS_DIR_NAME = "Screenshots";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot (%s).png";
    private static final String TIME_FORMAT = "yyyyMMMdd HH:mm:ss";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(TIME_FORMAT);

    public ScreenshotSharableItem(ViewGroup view) {
        screenshotUri = createScreenShotUri(captureScreenshot(view));
    }

    private Bitmap captureScreenshot(ViewGroup view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
            Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    /**
     * Captures a screen shot, saves it under "Screenshots" directory
     * in the default Pictures directory and returns its uri
     */
    private Uri createScreenShotUri(Bitmap bitmap) {
        String time = FORMATTER.format(Calendar.getInstance());
        String fileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, time);
        File screenshotsDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), SCREENSHOTS_DIR_NAME);
        screenshotsDir.mkdirs();
        File imageFile = new File(screenshotsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            MobileVSILogger.e(getClass(), "Unable to save screenshot. " + e.getMessage());
            return null;
        }
    }

    @Override
    public Uri getUri() {
        return screenshotUri;
    }
}

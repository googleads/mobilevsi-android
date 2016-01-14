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

import android.net.Uri;
import android.os.Environment;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp.MobileVSILogger;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used for sharing the sdk logs of the session.
 */
public class SdkLogsSharableItem implements SharableStringItem, SharableUriItem {
    private static final String LOGS_FILE_NAME_TEMPLATE = "IMA SDK VideoSuiteInspector Log (%s).txt";
    private static final String DOCUMENTS_DIR_NAME = "ImaSdkVideoSuiteInspector";
    private static final String TIME_FORMAT = "yyyyMMMdd HH:mm:ss";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(TIME_FORMAT);

    private String sdkLogs;
    private Uri logUri;

    public SdkLogsSharableItem(String sdkLogs) {
        this.sdkLogs = sdkLogs;
        this.logUri = saveLogs(sdkLogs);
    }

    private Uri saveLogs(String sdkLogs) {
        String time = FORMATTER.format(Calendar.getInstance());
        String fileName = String.format(LOGS_FILE_NAME_TEMPLATE, time);
        File screenshotsDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS), DOCUMENTS_DIR_NAME);
        screenshotsDir.mkdirs();
        File logsFile = new File(screenshotsDir, fileName);

        try {
            PrintWriter pw = new PrintWriter(logsFile);
            pw.print(sdkLogs);
            pw.close();
            return Uri.fromFile(logsFile);
        } catch (IOException e) {
            MobileVSILogger.e(getClass(), "Unable to save sdk logs. " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getString() {
        return sdkLogs;
    }

    @Override
    public Uri getUri() {
        return logUri;
    }
}

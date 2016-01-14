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
package com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp;

import android.content.res.Resources;
import android.util.Log;

import com.google.ads.interactivemedia.v3.samples.MobileVSI.R;

/**
 * Central logging functionality for MobileVSI app. Prepends class name in the log message.
 */
public class MobileVSILogger {

    private static final String GLOBAL_TAG = "MobileVSI";
    private String clazz;

    private static void log(String clazz, int priority, String message) {
        Log.println(priority, GLOBAL_TAG, clazz + message);
    }

    private static String classToString(Class<?> clazz) {
        return clazz.getSimpleName() + ": ";
    }

    public static void log(Class<?> clazz, int priority, String message) {
        log(classToString(clazz), priority, message);
    }

    public static void d(Class<?> clazz, String message) {
        log(clazz, Log.DEBUG, message);
    }

    public static void e(Class<?> clazz, String message) {
        log(clazz, Log.ERROR, message);
    }

    public static void i(Class<?> clazz, String message) {
        log(clazz, Log.INFO, message);
    }

    public static void v(Class<?> clazz, String message) {
        log(clazz, Log.VERBOSE, message);
    }

    public static void w(Class<?> clazz, String message) {
        log(clazz, Log.WARN, message);
    }

    public MobileVSILogger(Class<?> clazz) {
        this.clazz = classToString(clazz);
    }

    public void log(int priority, String message) {
        log(clazz, priority, message);
    }

    public void d(String message) {
        log(clazz, Log.DEBUG, message);
    }

    public void e(String message) {
        log(clazz, Log.ERROR, message);
    }

    public void i(String message) {
        log(clazz, Log.INFO, message);
    }

    public void v(String message) {
        log(clazz, Log.VERBOSE, message);
    }

    public void w(String message) {
        log(clazz, Log.WARN, message);
    }
}

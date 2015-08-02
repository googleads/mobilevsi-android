package com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp;

import android.net.Uri;
import android.net.UrlQuerySanitizer;

/**
 * Class for declaring the custom schema and converting a uri in custom schema to a regular uri
 * Custom schema is as follows
 *      mobilevsi://?url=<ad-tag-url|shortened-url>
 */
public class MobileVsiUriSchemaUtil {

    private static final String CUSTOM_SCHEMA_SCHEME = "mobilevsi";
    private static final String CUSTOM_SCHEMA_PATH = "";
    private static final String CUSTOM_SCHEMA_QUERY = "url";

    public static String parseLaunchUri(String uriString) {
        Uri uri = Uri.parse(uriString);
        if (!matchesCustomSchema(uri)) {
            MobileVSILogger.e(MobileVsiUriSchemaUtil.class,
                "The parsed URL does not belong to the schema: " + CUSTOM_SCHEMA_SCHEME);
            return null;
        }
        if (!uri.getPath().matches(CUSTOM_SCHEMA_PATH)) {
            MobileVSILogger.e(MobileVsiUriSchemaUtil.class,
                "The parsed URL does not match the path: [" + CUSTOM_SCHEMA_SCHEME + "]");
            return null;
        }

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(uriString);
        String customUrl = sanitizer.getValue(CUSTOM_SCHEMA_QUERY);
        if (customUrl != null) {
            customUrl = Uri.decode(customUrl);
            return customUrl;
        } else {
            MobileVSILogger.e(MobileVsiUriSchemaUtil.class,
                "The parsed URL does not contain the query: " + CUSTOM_SCHEMA_QUERY);
            return null;
        }
    }

    public static boolean matchesCustomSchema(Uri uri) {
        String scheme = uri.getScheme();
        return scheme != null && scheme.equalsIgnoreCase(CUSTOM_SCHEMA_SCHEME);
    }

    public static boolean matchesCustomSchema(String uriString) {
        return matchesCustomSchema(Uri.parse(uriString));
    }
}

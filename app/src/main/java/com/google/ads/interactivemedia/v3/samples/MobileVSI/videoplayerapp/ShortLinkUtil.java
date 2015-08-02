package com.google.ads.interactivemedia.v3.samples.MobileVSI.videoplayerapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * For determining whether the ad tag fetched is a short link, and expanding if needed.
 * We are not using the volley library because it redirects by default
 * and trying to work around that is no where near clean.
 */
public class ShortLinkUtil {

    /**
     * Callback used by the ShortLinkUtil to return the obtained full link
     */
    public interface ShortLinkUtilCallback {
        void onExpandLink(String fullLink);
        void onError(IOException exception);
    }

    private static final String LOCATION_HEADER = "Location";
    private static final String SHORT_LINK_HOST = "goo.gl";

    private static MobileVSILogger logger = new MobileVSILogger(ShortLinkUtil.class);

    public static boolean canExpandShortLink(String shortUrl) {
        try {
            String host = new URL(shortUrl).getHost();
            return host.startsWith(SHORT_LINK_HOST);
        } catch (MalformedURLException e) {
            logger.e(e.getMessage());
            return false;
        }
    }

    public static void tryExpandShortLink(ShortLinkUtilCallback callback, String shortUrl) {
        logger.i("Tying to expand url: " + shortUrl);
        ExpandUrlAsyncTask asyncTask = new ExpandUrlAsyncTask(callback);
        asyncTask.execute(shortUrl);
    }

    // TODO: Move away from AsyncTask to something better. AsyncTasks are not good.
    private static class ExpandUrlAsyncTask extends AsyncTask<String, Void, String> {
        private IOException exception;
        private ShortLinkUtilCallback callback;

        public ExpandUrlAsyncTask(ShortLinkUtilCallback callback) {
            exception = null;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String url = urls[0];
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setInstanceFollowRedirects(false);
                int responseCode = connection.getResponseCode();
                if (responseCode == -1) {
                    // -1 is returned by getResponseCode() if the response code could not be retrieved.
                    // Signal to the caller that something was wrong with the connection.
                    throw new IOException("Could not retrieve response code from HttpUrlConnection.");
                }

                String location = connection.getHeaderField(LOCATION_HEADER);
                if (location == null || location.isEmpty()) {
                    throw new IOException(LOCATION_HEADER + " header could not be found in the response.");
                }

                return location;
            } catch (IOException e) {
                logger.e("IOException: " + e.getMessage());
                exception = e;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onExpandLink(result);
            } else {
                callback.onError(exception);
            }
        }
    }
}

package com.example.android.newsapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RajBaba on 22-08-2017.
 */

public class Utility {
    static final String RESPONSE = "response";
    static final String RESULTS = "results";
    static final String NEWS_TITLE = "webTitle";
    static final String SECTION = "sectionName";
    static final String DATE = "webPublicationDate";
    static final String NEWS_URL = "webUrl";
    private static final String LOG_TAG = Utility.class.getSimpleName();

    private Utility() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Education News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseNewsObject = baseJsonResponse.getJSONObject(RESPONSE);
            JSONArray resultsNewsArray = responseNewsObject.getJSONArray(RESULTS);
            for (int i = 0; i < resultsNewsArray.length(); i++) {
                JSONObject currentNews = resultsNewsArray.getJSONObject(i);
                String title = "N/A";
                if (currentNews.has(NEWS_TITLE)) {
                    title = currentNews.getString(NEWS_TITLE);
                }

                String section = "N/A";
                if (currentNews.has(SECTION)) {
                    section = currentNews.getString(SECTION);
                }

                String date = "N/A";
                if (currentNews.has(DATE)) {
                    date = currentNews.getString(DATE);
                }

                String newsUrl = "N/A";
                if (currentNews.has(NEWS_URL)) {
                    newsUrl = currentNews.getString(NEWS_URL);
                }

                News newNews = new News(title, section, date, newsUrl);
                news.add(newNews);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Education News JSON results", e);
        }
        return news;
    }
}







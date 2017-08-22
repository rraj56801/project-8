package com.example.android.newsapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by RajBaba on 22-08-2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<News> educationNews = Utility.fetchNewsData(mUrl);
        return educationNews;
    }
}

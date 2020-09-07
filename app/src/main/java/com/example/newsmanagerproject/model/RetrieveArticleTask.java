package com.example.newsmanagerproject.model;

import android.content.Context;
import android.os.AsyncTask;

import com.example.newsmanagerproject.network.ModelManager;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

public class RetrieveArticleTask extends AsyncTask<Void, Void, Void> {
    Context context;
    Article article;
    public RetrieveArticleTask(Context cont, Article article) {
        super();
        this.context = cont;
        this.article = article;

        // TODO Auto-generated constructor stub
    }
    @Override
    protected Void doInBackground(Void...voids) {
        try {
            ModelManager.deleteArticle(article.getId());
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }
        return null;
    }
}


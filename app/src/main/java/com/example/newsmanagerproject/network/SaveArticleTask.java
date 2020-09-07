package com.example.newsmanagerproject.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

public class SaveArticleTask extends AsyncTask<Void, Void, Void> {
 Context context;
 Article article;
    public SaveArticleTask(Context cont, Article article) {
        super();
        this.context = cont;
        this.article = article;

        // TODO Auto-generated constructor stub
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Void...voids) {

        try {
            int res=ModelManager.saveArticle(article);
            if(res!=0){
                Article articleAdd=ModelManager.getArticle(res);
                ArticleDB.saveNewMessage(articleAdd);
            }

        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }

        return null;
    }
}

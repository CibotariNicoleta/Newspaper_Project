package com.example.newsmanagerproject.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.newsmanagerproject.MyArticleModel;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

public class UpdateArticleTask extends AsyncTask<Void, Void, Void> {
    Context context;
   private Article article;
    public UpdateArticleTask(Context cont, Article article) {
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
                Article articleUpdated=ModelManager.getArticle(res);
                ArticleDB.updateArticle(articleUpdated);
                MyArticleModel.updateArticle(articleUpdated,res);
            }

        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }

        return null;
    }
}

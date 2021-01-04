package com.example.newsmanagerproject.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.model.CreatArticle;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.firebase.auth.FirebaseAuth;

public class SaveArticleTask extends AsyncTask<Void, Void, Void> {
 Context context;
 Article article;
 FirebaseAuth auth;
    public SaveArticleTask(Context cont, Article article) {
        super();
        this.context = cont;
        this.article = article;

        // TODO Auto-generated constructor stub
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Void...voids) {

        byte[] imageByte = article.getImageByte();

        article.setImageByte(null);
        try {
            int res=ModelManager.saveArticle(article);
       //    int a =  article.getIdUser();
            if(res!=0){ // modified here
                Article articleAdd=ModelManager.getArticle(res);
                auth = FirebaseAuth.getInstance();
                int userId = ArticleDB.getUserId(auth.getCurrentUser().getEmail());
               // articleAdd.setImageData(image.getImage());
                articleAdd.setImageByte(imageByte);
                articleAdd.setIdUser(userId);
                CreatArticle.articleId = articleAdd.getId();
                ArticleDB.saveNewMessage(articleAdd);
            }else{
                article.setImageByte(imageByte);
                ArticleDB.saveNewMessage(article);
            }

        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }

        return null;
    }


}

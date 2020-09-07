package com.example.newsmanagerproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.model.Article;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MyArticleModel {
    private static List<Article> articles;

    /**
     * getArticles(): This method initialize list articles data to
     * visualize in the view components
     */

    public static List<Article> getArticles() throws ParseException {
        if (articles == null) {
            articles = new ArrayList<Article>();
            loadArticles();
        }
        return articles;
    }

    /**
     * loadArticles(): This method calls getArticles of DB to
     * get articles in 2 ways. From the article server or from article DB.
     */
    private static void loadArticles() throws ParseException {

        List<Article> aux = new ArrayList<Article>(ArticleDB.getArticles());
        Iterator<Article> iterator = aux.iterator();
        while (iterator.hasNext()) {
            articles.add((Article) iterator.next());
        }
    }

    /**
     * getMoreArticles(): This method load more articles from DB or
     * article server and add those elements to articles "atribute"
     */
    public static List<Article> getMoreArticles() throws ParseException {
        List<Article> aux = new ArrayList<Article>(ArticleDB.getArticles());
        articles.addAll(aux);
        return aux;
    }

    /**
     * getListFilter(param1 , param2): This method filter the list of articles
     * stored in param1 by category provided in the param2.
     */
    public static List<Article> getListFilter(List<Article> pre, int type) {
        String selectType = new String();
        switch (type) {
            case 1:
                selectType = "National";
                break;
            case 2:
                selectType = "Technology";
                break;
            case 3:
                selectType = "Sports";
                break;
            case 4:
                selectType = "Economy";
                break;
        }

        List<Article> res = new ArrayList<Article>();
        for (int i = 0; i < pre.size(); i++) {
            Article addArticle = pre.get(i);
            if (addArticle.getCategory().equals(selectType)) {
                res.add(addArticle);
            }
        }
        return res;
    }

    public static void deleteArticle(Article articleRemove) {
        articles.remove(articleRemove);
    }

    public static void updateArticle(Article updatedArticle, int id) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == id) {
                articles.set(i, updatedArticle);
            }
        }

    }
}

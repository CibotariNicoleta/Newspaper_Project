package com.example.newsmanagerproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.example.newsmanagerproject.LoadArticlesTask;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.model.Logger;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID;

public class ArticleDB {

    private static ArticleDatabaseHelper helper;
    private static int offset = 0;

    public ArticleDB(Context c) {
        helper = new ArticleDatabaseHelper(c);
    }

    public static void saveNewMessage(Article m) {


        ContentValues values = new ContentValues();
        SQLiteDatabase dbd = helper.getReadableDatabase();
        Cursor cursor = dbd.query(DatabaseConstants.DB_TABLE_ARTICLE_NAME,
                null, null, null, null,
                null, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE);
        cursor.moveToFirst();

        int id_test = 0;
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            if ((id == m.getId()) && (cursor.getInt(1) == m.getIdUser()))
                id_test = 1;

            cursor.moveToNext();
        }
        if (id_test == 0) {
            Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved");
            SQLiteDatabase db = helper.getWritableDatabase();
            values.put(DB_TABLE_FIELD_ARTICLE_ID, m.getId());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IDUSER, m.getIdUser());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_TITLE, m.getTitleText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_CATEGORY, m.getCategory());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ABSTRACT, m.getAbstractText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_BODY, m.getBodyText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_SUBTITLE, m.getSubtitleText());
            try {
                if (m.getImage() != null) {
                    try {


                        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDESCRIPTION, m.getImage().getDescription());

                    } catch (ServerComnmunicationError serverComnmunicationError) {
                        serverComnmunicationError.printStackTrace();
                    }

                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_THUMBNAIL, m.getThumbnail());
                    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(m.getLastUpdate().getTime());
                    Log.i("Dato insertta DB","Tiempo-> "+currentDateTimeString);
                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE, m.getLastUpdate().getTime());
                    try {
                        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDATA, m.getImage().getIdArticle());
                    } catch (ServerComnmunicationError serverComnmunicationError) {
                        serverComnmunicationError.printStackTrace();
                    }
                }
            } catch (ServerComnmunicationError serverComnmunicationError) {
                serverComnmunicationError.printStackTrace();
            }
            long insertId = db.insert(DatabaseConstants.DB_TABLE_ARTICLE_NAME, null, values);

            Logger.log(Logger.INFO, "saveeee" + " --------- >>>>>>>(Article) retrieved");
        } else {
            // helper.update(m);
        }
        cursor.close();
    }


    public static List<Article> loadAllMessages() {
        ArrayList<Article> result = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.DB_TABLE_ARTICLE_NAME,
                null, null, null, null,
                null, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article article = new Article();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            int id = cursor.getInt(0);
            int idUser = cursor.getInt(1);
            String title = cursor.getString(2);
            String category = cursor.getString(3);
            String abst = cursor.getString(4);
            String body = cursor.getString(5);
            String subtitle = cursor.getString(6);
            String ImageDescription = cursor.getString(7);
            String thumbnail = cursor.getString(8);
            Double lasupdate = cursor.getDouble(9);
            String imageData = cursor.getString(10);


            long myLong = System.currentTimeMillis() + ((long) (lasupdate * 1000));
            Date itemDate = new Date(myLong);
            article.setLastUpdate(itemDate);
       /*     try {

              Date date = formatter.parse(lasupdate);
              article.setLastUpdate(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            article.setId(id);
            article.setIdUser(idUser);
            article.setTitleText(title);
            article.setCategory(category);
            article.setAbstractText(abst);
            article.setBodyText(body);
            article.setSubtitle(subtitle);
            article.setImageDescription(ImageDescription);
            article.setThumbnail(thumbnail);
            article.setImageData(imageData);
            result.add(article);
            cursor.moveToNext();
            Logger.log(Logger.INFO, id + " --------- >>>>>>>(Article) retrieved");

        }

        return result;
    }

    public static boolean deleteArticle(Article article) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.delete(DatabaseConstants.DB_TABLE_ARTICLE_NAME, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID + "=" + article.getId(), null) > 0;
    }

    public static void updateArticle(Article article) {
        ContentValues values = new ContentValues();
        SQLiteDatabase dbd = helper.getReadableDatabase();

        values.put(DB_TABLE_FIELD_ARTICLE_ID, article.getId());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IDUSER, article.getIdUser());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_TITLE, article.getTitleText());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_CATEGORY, article.getCategory());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ABSTRACT, article.getAbstractText());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_BODY, article.getBodyText());
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_SUBTITLE, article.getSubtitleText());
        try {
            if (article.getImage() != null) {
                try {


                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDESCRIPTION, article.getImage().getDescription());

                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }

                values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_THUMBNAIL, article.getThumbnail());
                values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE, article.getLastUpdate().getTime());
                try {
                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDATA, article.getImage().getIdArticle());
                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }
            }
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        db.update(DatabaseConstants.DB_TABLE_ARTICLE_NAME, values, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID + "=" + article.getId(), null);

    }

    public static int getLength() {
        int res;
        SQLiteDatabase dbd = helper.getReadableDatabase();
        res = (int) DatabaseUtils.queryNumEntries(dbd, "Article_DB");
        return res;
    }


    /**
     * loadArticles(): This method returns a maximum
     * of 10 items from the database starting
     * from the index indicated by "offset"
     **/
    public static List<Article> loadArticles() throws ParseException {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Article> resList = new ArrayList<Article>();
        Cursor cursor = db.rawQuery("SELECT * FROM Article_DB LIMIT 10 OFFSET " + offset + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article article = new Article();
            int id = cursor.getInt(0);
            int idUser = cursor.getInt(1);
            String title = cursor.getString(2);
            String category = cursor.getString(3);
            String abst = cursor.getString(4);
            String body = cursor.getString(5);
            String subtitle = cursor.getString(6);
            String ImageDescription = cursor.getString(7);
            String thumbnail = cursor.getString(8);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long date=cursor.getLong(9);
            String longToString=String.valueOf(date);
            Date lasupdate = new Date(date);
            String imageData = cursor.getString(10);


//            long myLong = System.currentTimeMillis() + ((long) (lasupdate * 1000));
//            Date itemDate = new Date(myLong);
            article.setLastUpdate(lasupdate);

            article.setId(id);
            article.setIdUser(idUser);
            article.setTitleText(title);
            article.setCategory(category);
            article.setAbstractText(abst);
            article.setBodyText(body);
            article.setSubtitle(subtitle);
            article.setImageDescription(ImageDescription);
            article.setThumbnail(thumbnail);
            article.setImageData(imageData);
            resList.add(article);
            cursor.moveToNext();
        }
        int moreLength = cursor.getCount();
        int changeOffset = LoadArticlesTask.getOffset();
        offset = offset + moreLength;
        LoadArticlesTask.setOffset(changeOffset + moreLength); //Prueba
        cursor.close();
        return resList;
    }

    /**
     * resetOffset():  This method reset the offset count
     **/
    public static void resetOffset() {
        offset = 0;
    }

    /**
     * getArticles(): This method call an AsynTask method to get
     * articles from a server and if there are not in
     * the DB, then it will save within SqliteDB.
     * In case that DB has enough articles to load
     * it only returns the articles from DB.
     */
    public static List<Article> getArticles() throws ParseException {
        List<Article> res = new ArrayList<>();
        if (LoadArticlesTask.getOffset() < getLength()) {
            res = loadArticles();
        } else {
            //Call loadArticleTask service to get articles from server
            LoadArticlesTask loadArticlesTask = new LoadArticlesTask();
            try {
                res = loadArticlesTask.execute().get();
                if (res.size() != 0) {
                    for (Article r : res)
                        saveNewMessage(r);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}


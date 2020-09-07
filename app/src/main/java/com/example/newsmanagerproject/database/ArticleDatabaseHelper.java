package com.example.newsmanagerproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_ARTICLE_NAME;

public class ArticleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Articles_DB";
    private static final int DATABASE_VERSION = 3;

    public ArticleDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // crear la estructura y datos initicales de la base de datos
        sqLiteDatabase.execSQL(DatabaseConstants.DB_CREATE_TABLE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // ejecutar lo scripts necesarios (SQL) para actualizar
        // de la versón vieja (oldVersion) a la nueva (newVersion)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ARTICLE_NAME);
        //added here
         onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        // sentencias necesarias para bajar de version la bbdd
    }

   /* public boolean update(Article article){
        SQLiteDatabase db = this.getReadableDatabase();
        //try {
            db.execSQL("UPDATE "+DB_TABLE_ARTICLE_NAME+" SET idUser ="+ article.getIdUser() +
                 //  ", title =" + article.getTitleText().toString()+
                    //", category ="+article.getCategory().toString()+
                    ", subtitle ="+article.getSubtitle().toString()+
                //    ", body ="+article.getBodyText().toString()+
                   // ", abstract ="+article.getAbstractText().toString()+
                    //", imageDescription ="+article.getImage().getDescription().toString()+
                    //", thumbnail ="+article.getThumbnail().toString()+
                    //", lastupdate ="+article.getLastUpdate().getTime()+
                    //", imageData ="+article.getImage().getIdArticle()+
                    "WHERE id = " + article.getId()+ ";");
       // } catch (ServerComnmunicationError serverComnmunicationError) {
         //   serverComnmunicationError.printStackTrace();
        //}
        return true;
    }*/
}


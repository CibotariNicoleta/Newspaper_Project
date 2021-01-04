package com.example.newsmanagerproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArticleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Articles_DB";
    private static final int DATABASE_VERSION = 11;

    public ArticleDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // crear la estructura y datos initicales de la base de datos
        sqLiteDatabase.execSQL(DatabaseConstants.DB_CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(DatabaseConstants.DB_CREATE_TABLE_ARTICLE);
        sqLiteDatabase.execSQL(DatabaseConstants.DB_CREATE_TABLE_LOCATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // ejecutar lo scripts necesarios (SQL) para actualizar
        // de la versón vieja (oldVersion) a la nueva (newVersion)
        if (newVersion > oldVersion) {
            //sqLiteDatabase.execSQL(DatabaseConstants.DB_CREATE_TABLE_LOCATION);
            sqLiteDatabase.execSQL("ALTER TABLE "+ DatabaseConstants.DB_TABLE_LOCATION + " ADD COLUMN latitudine DOUBLE ");
            sqLiteDatabase.execSQL("ALTER TABLE "+ DatabaseConstants.DB_TABLE_LOCATION + " ADD COLUMN articleId INTEGER ");
        }
      // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.DB_TABLE_LOCATION);
       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ARTICLE_NAME);

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


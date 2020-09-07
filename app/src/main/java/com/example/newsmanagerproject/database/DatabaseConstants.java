package com.example.newsmanagerproject.database;

public class DatabaseConstants {
    public static final String DB_TABLE_ARTICLE_NAME = "Article_DB";
    public static final String DB_TABLE_FIELD_ARTICLE_ID = "id";
    public static final String DB_TABLE_FIELD_ARTICLE_IDUSER = "idUser";
    public static final String DB_TABLE_FIELD_ARTICLE_TITLE = "title";
    public static final String DB_TABLE_FIELD_ARTICLE_CATEGORY = "category";
    public static final String DB_TABLE_FIELD_ARTICLE_ABSTRACT = "abstract";
    public static final String DB_TABLE_FIELD_ARTICLE_BODY = "body";
    public static final String DB_TABLE_FIELD_ARTICLE_SUBTITLE = "subtitle";
    public static final String DB_TABLE_FIELD_ARTICLE_IMAGEDESCRIPTION = "imageDescription";
    public static final String DB_TABLE_FIELD_ARTICLE_THUMBNAIL = "thumbnail";
    public static final String DB_TABLE_FIELD_ARTICLE_LASTUPDATE = "lastupdate";
    public static final String DB_TABLE_FIELD_ARTICLE_IMAGEDATA = "imageData";

    public static final String DB_CREATE_TABLE_ARTICLE =
            "CREATE TABLE IF NOT EXISTS "+DB_TABLE_ARTICLE_NAME+" ( " +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    idUser INTEGER, " +
                    "    title TEXT, " +
                    "    category TEXT, " +
                    "    abstract TEXT, " +
                    "    body INTEGER, " +
                    "    subtitle TEXT, " +
                    "    imageDescription TEXT, " +
                    "    thumbnail TEXT, " +
                    "    lastupdate INTEGER, " +
                    "    imageData TEXT " +
                    ");";

}

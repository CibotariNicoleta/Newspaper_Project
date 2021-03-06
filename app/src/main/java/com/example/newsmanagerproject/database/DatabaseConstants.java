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
    public static final String DB_TABLE_FIELD_ARTICLE_IMAGEBYTES = "imageBytes";
    public static final String DB_TABLE_FIELD_ARTICLE_LOCATION = "location";

    public static final String DB_TABLE_USER = "User_table";
    public static final String DB_TABLE_FIELD_USER_ID = "userId";
    public static final String DB_TABLE_FIELD_USER_NAME = "userName";
    public static final String DB_TABLE_FIELD_USER_EMAIL = "userEmail";
    public static final String DB_TABLE_FIELD_USER_PASSWORD = "userPassword";
    public static final String DB_TABLE_FIELD_USER_IMAGE = "userImage";

    public static final String DB_TABLE_LOCATION = "User_location";
    public static final String DB_TABLE_FIELD_LOCATION_ID = "locationId";
    public static final String DB_TABLE_FIELD_LONGITUDINE = "longitudine";
    public static final String DB_TABLE_FIELD_LATITUDINE = "latitudine";
    public static final String DB_TABLE_FIELD_ARTICLE_ID_LOCATION = "articleId";

    public static final String DB_CREATE_TABLE_LOCATION =
            "CREATE TABLE IF NOT EXISTS " + DB_TABLE_LOCATION+"( "+
                    "locationId INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "longitudine REAL"+
                    "latitudine REAL"+
                    "articleId Integer"+");";

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
                    "    imageBytes TEXT " +
                   // "    location INTEGER" +
                   // "    FOREIGN KEY (idUSer) REFERENCES User_table (userId)" +
                    ");";

    public static final String DB_CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS "+DB_TABLE_USER+" ( " +
                    "    userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    userName TEXT, " +
                    "    userEmail TEXT, " +
                    "    userPassword TEXT " +
                    "    userImage TEXT" +
                    ");";

}

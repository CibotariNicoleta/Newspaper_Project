package com.example.newsmanagerproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.newsmanagerproject.LoadArticlesTask;
import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.model.Logger;
import com.example.newsmanagerproject.model.Mapp;
import com.example.newsmanagerproject.model.User;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_ARTICLE_NAME;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IDUSER;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID_LOCATION;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_ARTICLE_TITLE;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_LATITUDINE;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_LONGITUDINE;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_USER_EMAIL;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_USER_ID;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_USER_IMAGE;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_USER_NAME;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_FIELD_USER_PASSWORD;
import static com.example.newsmanagerproject.database.DatabaseConstants.DB_TABLE_USER;

public class ArticleDB {

    private static ArticleDatabaseHelper helper;
    private static int offset = 0;

    public ArticleDB(Context c) {
        helper = new ArticleDatabaseHelper(c);
    }

    public static void add(){
        List<Article> articles = new ArrayList<Article>();
        String title ;
        String subtitle;
        String category;
        String abstracts;
        String body;
        Date date;
        date = GregorianCalendar.getInstance().getTime();
        byte[] imageData;
        String idUSer;
        int a = R.drawable.sr;
        BigInteger bigInt = BigInteger.valueOf(a);
        imageData = bigInt.toByteArray();
        title = "Sri Lanka calls for joint patrols to resolve fishing issues with India";
        category = "National";
        abstracts = "The Sri Lankan delegation underlined the need to address the matter of illegal fishing in its waters by Indian fishermen, which resulted in loss of livelihood and damage to fishing equipment of local fishermen";
        subtitle = "Sri Lanka has called for joint patrols to address the issue of illegal fishing in the northern waters of the country while India insisted on the early release of its fishermen arrested for allegedly poaching in the island nation's territorial waters, as the two " +
                "countries held a meeting of the joint working group on fisheries.";
        body = "During a virtual meeting of the Sri Lanka-India Joint Working Group (JWG) on Fisheries held last month for the fourth time, the two sides discussed the issues related to fishermen.\n" +
                "\n" +
                "During the meeting, the Sri Lankan delegation underlined the need to address the matter of illegal fishing in its waters by Indian fishermen, which resulted in loss of livelihood and damage to fishing equipment of local fishermen, the Sri Lankan Ministry of Fisheries said in a statement on Thursday.\n" +
                "\n" +
                "Sri Lanka mooted joint patrols to end poaching in the northern waters of the island while India insisted on the release of Indian fishermen recently arrested, it said."+
                "“The Indian side elaborated details in regard to the legal action taken against such practices and the initiatives to encourage South Indian fishermen to engage in deep sea fishing and alternative livelihood,\" it said.\n" +
                "\n" +
                "The Sri Lankan delegation conveyed the need to further enhance the joint patrolling and operationalisation of the hotline between the Law enforcement authorities of the two countries to ensure effective results.\n" +
                "\n" +
                "The Sri Lankan side alleged that South Indian fishermen were freely trespassing on Sri Lankan waters during the long drawn out war in the north and eastern provinces where Sri Lankan fishermen''s movement had been restricted due to the Liberation Tigers of Tamil Eelam (LTTE) using them for their military purposes."+
        "The Indian delegation called on the Sri Lankan Government to ensure the early release of recently arrested fishermen by Sri Lankan authorities, the statement said.\n" +
                "\n" +
                "The Sri Lankan side assured to facilitate the release of arrested fishermen early, once the legal procedures are completed.\n" +
                "\n" +
                "The relevant authorities have already granted necessary facilities, including consular access to the arrested Indian fishermen, it said. The Indian High Commission here on Thursday said the Indian fishermen, apprehended in Sri Lanka for allegedly poaching in its territorial waters, have been provided consular assistance by the Consulate General of India in Jaffna.\n" +
                "\n" +
                "The high commission said it was in touch with the Sri Lankan Government to facilitate the early release of the detained fishermen.\n" +
                "\n" +
                "Fishermen from both countries are arrested frequently for inadvertently trespassing into each other's waters.\n" +
                "\n" +
                "During his five-day visit to India in February, Sri Lankan Prime Minister Mahinda Rajapaksa and his Indian counterpart Narendra Modi agreed to resolve the long-festering fishermen issue with a \"humane approach\".";
        idUSer = "0";
     Article article = new Article(category, title, abstracts, body, subtitle, idUSer);
     article.setLastUpdate(date);
     article.setImageByte(imageData);
     saveNewMessage(article);

    }
    public static void saveNewUser(User user){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = helper.getWritableDatabase();

        values.put(DB_TABLE_FIELD_USER_NAME, user.getUserName());
        values.put(DB_TABLE_FIELD_USER_EMAIL, user.getEmail());
        values.put(DB_TABLE_FIELD_USER_PASSWORD, user.getPassword());
        if(user.getImage() != null){
            values.put(DB_TABLE_FIELD_USER_IMAGE, user.getImage());
        } else values.put(DB_TABLE_FIELD_USER_IMAGE, (byte[]) null);
        db.insert(DatabaseConstants.DB_TABLE_USER, null, values);
    }

    public static void saveUpdateUser(User user, int idUser){
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userName", user.getUserName());
        cv.put("userImage", user.getImage());
        db.update(DB_TABLE_USER, cv, "userId = "+idUser, null);
    }

    public static int getUserId(String email){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "+ DatabaseConstants.DB_TABLE_FIELD_USER_ID +  " from "+DatabaseConstants.DB_TABLE_USER+" where " + DB_TABLE_FIELD_USER_EMAIL + " = "+ "'"+email+"'", null);
        cursor.moveToFirst();
        return cursor.getInt(0) ;
    }

    public static User getUser(int idUser){
        User user = new User();
        SQLiteDatabase db = helper.getReadableDatabase();
        // only temporary added here
        //db.execSQL("update User_table set userName = 'Nicoleta Cibotari' where userId = " + idUser);

        Cursor cursor = db.rawQuery("Select * " +  " from "+DatabaseConstants.DB_TABLE_USER+" where " + DB_TABLE_FIELD_USER_ID + " = "+ "'"+idUser+"'", null);
        cursor.moveToFirst();
        int userId = cursor.getInt(0);
        String username = cursor.getString(1);
        String password = cursor.getString(2);
        String email = cursor.getString(3);
        byte[] image = cursor.getBlob(4);

        user.setEmail(email);
        user.setIdUser(userId);
        user.setImage(image);
        user.setPassword(password);
        user.setUserName(username);

        return user;
    }

    public static int getNumberOfArticles(int userId){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(id) as nr_art from Article_DB where idUser="+userId, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    public static int getArticleId(int userId, String title){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * "+ DB_TABLE_ARTICLE_NAME+" where " + DB_TABLE_FIELD_ARTICLE_IDUSER + " = "+userId+" AND "+ DB_TABLE_FIELD_ARTICLE_TITLE+ " = '" + title +"'", null);
        cursor.moveToFirst();
        return cursor.getInt(0) ;
    }

    //here we get all the user articles
    public static List<Article> getUserArticles(int userid){
        ArrayList<Article> temporary = (ArrayList<Article>) loadAllMessages();
        ArrayList<Article> result = new ArrayList<Article>();

        for(Article i:temporary){
            if(i.getIdUser() == userid)
                result.add(i);
        }

        return result;
    }

    public static void saveMap(Mapp mapp){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = helper.getWritableDatabase();

        values.put(DB_TABLE_FIELD_LONGITUDINE, mapp.getLongitudine());
        values.put(DB_TABLE_FIELD_LATITUDINE, mapp.getLatitudine());
        values.put(DB_TABLE_FIELD_ARTICLE_ID_LOCATION, mapp.getArticleId());
        db.insert(DatabaseConstants.DB_TABLE_LOCATION, null, values);
    }

    public static Mapp getArticleLocation(int idArticle){
        double lng = 0;
        double lat = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select *" +  " from "+DatabaseConstants.DB_TABLE_LOCATION+" where " + DB_TABLE_FIELD_ARTICLE_ID_LOCATION + " = "+idArticle, null);

        if(cursor == null)
            return null;

        cursor.moveToFirst();
        Mapp mapp;
      //  while (!cursor.isAfterLast()) {
            lng = cursor.getDouble(1);
            lat = cursor.getDouble(2);
       // }

        mapp = new Mapp(lng, lat);
        return mapp;
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
            if(m.getId()>-1) {
                values.put(DB_TABLE_FIELD_ARTICLE_ID, m.getId());
            }
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IDUSER, m.getIdUser());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_TITLE, m.getTitleText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_CATEGORY, m.getCategory());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ABSTRACT, m.getAbstractText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_BODY, m.getBodyText());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEBYTES, m.getImageByte());
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_SUBTITLE, m.getSubtitleText());
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(m.getLastUpdate().getTime());
            Log.i("Dato insertta DB","Tiempo-> "+currentDateTimeString);
            long i = m.getLastUpdate().getTime();
            values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE, m.getLastUpdate().getTime());

            try {
                if (m.getImage() != null) {
                    try {
                        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDESCRIPTION, m.getImage().getDescription());
                    } catch (ServerComnmunicationError serverComnmunicationError) {
                        serverComnmunicationError.printStackTrace();
                    }
                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_THUMBNAIL, m.getThumbnail());
                     try {
                        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDATA, m.getImage().getImage());
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
            byte[] imageBytes = cursor.getBlob(11);


            //long myLong = System.currentTimeMillis() + ((long) (lasupdate * 1000));
            //Date itemDate = new Date(myLong);
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
            article.setImageByte(imageBytes);
            result.add(article);
            cursor.moveToNext();
        }

        return result;
    }

    public static boolean deleteArticle(Article article) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.delete(DatabaseConstants.DB_TABLE_ARTICLE_NAME, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID + "=" + article.getId(), null) > 0;
    }

    public static void updateUser(){
        ContentValues values = new ContentValues();
        SQLiteDatabase dbd = helper.getReadableDatabase();
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IDUSER, 0);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.update(DatabaseConstants.DB_TABLE_ARTICLE_NAME, values, DatabaseConstants.DB_TABLE_FIELD_ARTICLE_ID + "=" + 1867, null);

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
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE, article.getLastUpdate().getTime());
        try {
            if (article.getImage() != null) {
                try {


                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDESCRIPTION, article.getImage().getDescription());

                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }

                values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_THUMBNAIL, article.getThumbnail());

                try {
                    values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEDATA, article.getImage().getIdArticle());
                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }
            }
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }
        values.put(DatabaseConstants.DB_TABLE_FIELD_ARTICLE_IMAGEBYTES, article.getImageByte());

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
        Cursor cursor = db.rawQuery("SELECT * FROM Article_DB order by  "+DatabaseConstants.DB_TABLE_FIELD_ARTICLE_LASTUPDATE+" desc LIMIT 10 OFFSET " + offset + ";", null);
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
            byte[] imageBytes = cursor.getBlob(11);


            //long myLong = System.currentTimeMillis() + ((long) (lasupdate * 1000));
            //Date itemDate = new Date(myLong);
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
            article.setImageByte(imageBytes);
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


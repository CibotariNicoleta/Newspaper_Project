package com.example.newsmanagerproject.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.model.Image;
import com.example.newsmanagerproject.network.errors.AuthenticationError;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.example.newsmanagerproject.model.Logger;

import org.json.simple.JSONObject;

import static com.example.newsmanagerproject.network.ServiceCallUtils.parseHttpStreamResult;

public class ModelManager {
    private static RESTConnection rc;

    public static boolean isConnected() {
        return rc.idUser != null;
    }

    public static String getLoggedIdUSer() {
        return rc.idUser;
    }

    public static String getLoggedApiKey() {
        return rc.apikey;
    }

    public static String getLoggedAuthType() {
        return rc.authType;
    }

    /**
     * @param ini Initializes entity manager urls and users
     * @throws AuthenticationError
     */
    public static void configureConnection(Properties ini) {
        rc = new RESTConnection(ini);
    }

    public static void stayloggedin(String idUser, String apikey, String authType) {
        rc.idUser = idUser;
        rc.authType = authType;
        rc.apikey = apikey;
    }

    /**
     * Login onto remote service
     *
     * @param username
     * @param password
     * @throws AuthenticationError
     */
    @SuppressWarnings("unchecked")
    public static void login(String username, String password) throws AuthenticationError {
        String res = "";
        try {
            String parameters = "";
            String request = rc.serviceUrl + "login";

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches(false);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("passwd", password);

            ServiceCallUtils.writeJSONParams(connection, jsonParam);

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                res = parseHttpStreamResult(connection);

                JSONObject userJsonObject = ServiceCallUtils.readRestResultFromSingle(res);
                rc.idUser = userJsonObject.get("user").toString();
                rc.authType = userJsonObject.get("Authorization").toString();
                rc.apikey = userJsonObject.get("apikey").toString();
                rc.isAdministrator = userJsonObject.containsKey("administrator");
            } else {
                Logger.log(Logger.ERROR, connection.getResponseMessage());

                throw new AuthenticationError(connection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            throw new AuthenticationError(e.getMessage());
        } catch (IOException e) {
            //e.printStackTrace();
            throw new AuthenticationError(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            throw new AuthenticationError(e.getMessage());
        }
    }

    /**
     * @return user id logged in
     */
    public static String getIdUser() {
        return rc.idUser;
    }

    /**
     * @return auth token header for user logged in
     */
    private static String getAuthTokenHeader() {
        String authHeader = rc.authType + " apikey=" + rc.apikey;
        return authHeader;
    }

    /**
     * @return the list of articles in remote service
     * @throws ServerComnmunicationError
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Article> getArticles() throws ServerComnmunicationError {
        return getArticles(-1, -1);
    }

    /**
     * @return the list of articles in remote service with pagination
     * @throws ServerComnmunicationError
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Article> getArticles(int buffer, int offset) throws ServerComnmunicationError {
        String limits = "";
        if (buffer > 0 && offset >= 0) {
            limits = "/" + buffer + "/" + offset;
        }

        List<Article> result = new ArrayList<Article>();
        try {
            String parameters = "";
            String request = rc.serviceUrl + "articles" + limits;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            //connection.setDoOutput(true);
            //connection.setDoInput(false);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches(false);

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                List<JSONObject> objects = ServiceCallUtils.readRestResultFromList(res);
                for (JSONObject jsonObject : objects) {
                    result.add(new Article(jsonObject));
                }
                Logger.log(Logger.INFO, objects.size() + " objects (Article) retrieved");
            } else {
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Listing articles :" + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return result;
    }

    /**
     * @return the list of articles in remote service with pagination
     * @throws ServerComnmunicationError
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Article> getArticlesFrom(int buffer, int offset) throws ServerComnmunicationError {
        String limits = "";
        if (buffer > 0 && offset >= 0) {
            limits = "/" + buffer + "/" + offset;
        }

        List<Article> result = new ArrayList<Article>();
        try {
            String parameters = "";
            String request = rc.serviceUrl + "articles" + limits;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            //connection.setDoOutput(true);
            //connection.setDoInput(false);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches(false);

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                List<JSONObject> objects = ServiceCallUtils.readRestResultFromList(res);
                for (JSONObject jsonObject : objects) {
                    result.add(new Article(jsonObject));
                }
                Logger.log(Logger.INFO, objects.size() + " objects (Article) retrieved");
            } else {
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Listing articles :" + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return result;
    }

    /**
     * @return the article in remote service with id idArticle
     * @throws ServerComnmunicationError
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Article getArticle(int idArticle) throws ServerComnmunicationError {

        Article result = null;
        try {
            String parameters = "";
            String request = rc.serviceUrl + "article/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            //connection.setDoOutput(true);
            //connection.setDoInput(false);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches(false);

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                JSONObject object = ServiceCallUtils.readRestResultFromGetObject(res);
                result = new Article(object);
                Logger.log(Logger.INFO, " object (Article) retrieved");
            } else {
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Getting article :" + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return result;
    }

    public static int saveArticle(Article a) throws ServerComnmunicationError {
        try {
            String parameters = "";
            String request = rc.serviceUrl + "article";

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8"); //aici
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setUseCaches(false); //aici
            connection.setRequestMethod("POST");
            connection.getOutputStream();
            ServiceCallUtils.writeJSONParams(connection, a.toJSON());
            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                // get id from status ok when saved
                int id = ServiceCallUtils.readRestResultFromInsert(res);
                Logger.log(Logger.INFO, "Object inserted, returned id:" + id);
                return id;
            } else {
                //throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Inserting article [" + a + "] : " + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return 0;
    }

    public static void deleteArticle(int idArticle) throws ServerComnmunicationError{
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "article/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches (false);

            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK  || HttpResult ==HttpURLConnection.HTTP_NO_CONTENT){
                Logger.log (Logger.INFO,"Article (id:"+idArticle+") deleted with status "+HttpResult+":"+parseHttpStreamResult(connection));
            }else{
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Deleting article (id:"+idArticle+") : " + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }
    }

    public static int updateArticle(Article a, int idArticle) throws ServerComnmunicationError{
        try {
            String parameters = "";
            String request = rc.serviceUrl + "article/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8"); //aici
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setUseCaches(false); //aici
            connection.setRequestMethod("PUT");
            connection.getOutputStream();
            ServiceCallUtils.writeJSONParams(connection, a.toJSON());
            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                // get id from status ok when saved
               int id = ServiceCallUtils.readRestResultFromInsert(res);
                Logger.log(Logger.INFO, "Object updated, returned id:" + id);
                return id;
            } else {
                //throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Inserting article [" + a + "] : " + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return 0;
    }

    private static int saveImage(Image i) throws ServerComnmunicationError{
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "article/image";

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json; utf-8");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            //connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches (false);


            ServiceCallUtils.writeJSONParams(connection, i.toJSON());

            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                String res = parseHttpStreamResult(connection);
                //Logger.log (Logger.INFO, res);
                // get id from status ok when saved
                int id = ServiceCallUtils.readRestResultFromInsert(res);
                Logger.log(Logger.INFO, "Object image saved with id:" + id);
                return id;
            }else{
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR,"Saving image ["+i+"] : " + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }
    }

    private static void deleteImage(int idArticle) throws ServerComnmunicationError{
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "image/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Authorization", getAuthTokenHeader());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches (false);

            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK || HttpResult ==HttpURLConnection.HTTP_NO_CONTENT){
                Logger.log (Logger.INFO,"Image of article (id:"+idArticle+") deleted with status "+HttpResult+":"+parseHttpStreamResult(connection));
            }else{
                throw new ServerComnmunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log (Logger.ERROR, "Deleting image of article (id:"+idArticle+") : " + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerComnmunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }
    }
}


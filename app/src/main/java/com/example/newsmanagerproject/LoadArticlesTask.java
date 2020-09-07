package com.example.newsmanagerproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.model.MainActivity;
import com.example.newsmanagerproject.network.ModelManager;
import com.example.newsmanagerproject.network.errors.AuthenticationError;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.example.newsmanagerproject.network.RESTConnection.ATTR_REQUIRE_SELF_CERT;
import static com.example.newsmanagerproject.network.RESTConnection.ATTR_SERVICE_URL;

public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {

    private static final String TAG = "LoadArticlesTask";
    private static int offset = 0;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Article> doInBackground(Void... voids) {
        List<Article> res = new ArrayList<Article>();
        Properties p = new Properties();
        p.put(ATTR_SERVICE_URL, "https://sanger.dia.fi.upm.es/pmd-task/");
        p.put(ATTR_REQUIRE_SELF_CERT, "TRUE");
        ModelManager.configureConnection(p);
        String strIdUser = ModelManager.getIdUser(), strApiKey = ModelManager.getLoggedApiKey(), strIdAuthUser = ModelManager.getLoggedAuthType();
        //ModelManager uses singleton pattern, connecting once per app execution in enough
        if (!ModelManager.isConnected()) {
            // if it is the first login
            if (ModelManager.getIdUser() == null || ModelManager.getIdUser().equals("")) {
                try {
                    ModelManager.login("DEV_TEAM_09", "65424");
                } catch (AuthenticationError e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            // if we have saved user credentials from previous connections
            else {
                ModelManager.stayloggedin(strIdUser, strApiKey, strIdAuthUser);
            }
        }
        //If connection has been successful
        if (ModelManager.isConnected()) {
            try {
                // obtain 6 articles from offset 0
                Log.d("El usuario es ->", ModelManager.getIdUser());
                Log.d("Con la clave de API->", ModelManager.getLoggedApiKey());
                res = ModelManager.getArticles(10, offset);
                int index = res.size();
                if (index != 0) {
                    offset = offset + index;
                }
            } catch (ServerComnmunicationError e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return res;
    }

    public void postExecute(List<Article> res) {

    }

    public static int getOffset() {
        return offset;
    }
    public static void setOffset(int newOffset) {
        offset=newOffset;
    }
}


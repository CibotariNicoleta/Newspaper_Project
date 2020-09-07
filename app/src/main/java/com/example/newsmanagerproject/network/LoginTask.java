package com.example.newsmanagerproject.network;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.newsmanagerproject.model.Article;
import com.example.newsmanagerproject.network.errors.AuthenticationError;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.example.newsmanagerproject.network.RESTConnection.ATTR_REQUIRE_SELF_CERT;
import static com.example.newsmanagerproject.network.RESTConnection.ATTR_SERVICE_URL;

public class LoginTask extends AsyncTask<Void,Void,Void> {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Void doInBackground(Void... voids) {
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
                    Log.e("Fallo Login", e.getMessage());
                }
            }
            // if we have saved user credentials from previous connections
            else {
                ModelManager.stayloggedin(strIdUser, strApiKey, strIdAuthUser);
            }
        }
        //If connection has been successful
        if (ModelManager.isConnected()) {
            Log.i("Login","Logueado");
        }
        return null;
    }
}

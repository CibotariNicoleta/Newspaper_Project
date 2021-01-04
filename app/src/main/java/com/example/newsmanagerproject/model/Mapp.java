package com.example.newsmanagerproject.model;

import java.io.Serializable;

public class Mapp implements Serializable {

    private double longitudine;
    private double latitudine;
    private int articleId;


    public Mapp(double longitudine, double latitudine) {
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }
}

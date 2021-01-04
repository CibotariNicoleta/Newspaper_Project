package com.example.newsmanagerproject.model;

import java.io.Serializable;

public class User implements Serializable {
    private int idUser;
    private String userName;
    private String email;
    private String password;
    private byte[] image;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password){
        this.email = email ;
        this.password = password;
    }

    public User(){};
    public int getIdUser() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image ) {
        this.image = image;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

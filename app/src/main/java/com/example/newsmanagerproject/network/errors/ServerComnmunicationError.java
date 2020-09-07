package com.example.newsmanagerproject.network.errors;

public class ServerComnmunicationError extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -2703194338913940270L;

    public ServerComnmunicationError(String message){
        super(message);
    }
}
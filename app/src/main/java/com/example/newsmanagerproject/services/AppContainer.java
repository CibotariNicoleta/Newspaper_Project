package com.example.newsmanagerproject.services;

import java.util.ArrayList;
import java.util.List;

public class AppContainer {
    public static List<String> activities = new ArrayList<>();

    public static void goForward(String activity){
        activities.add(activity);
    }

    public static String goBack(){
        String activity = activities.get(activities.size()-1);
        activities.remove(activity);
        return activity;
    }
}

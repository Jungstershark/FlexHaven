package com.example.flexhaven.helpers;
//Singleton Design Pattern
//1 class amongst all areas of the app!
public class CommonData {
    private static CommonData instance;
    private String username;
    private int userPoints;

    //CANNOT BE INSTANTIATED!
    private CommonData(){
    }
    public static synchronized CommonData getInstance(){
        if (instance==null){
            instance = new CommonData();
        }
        return instance;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUserPoints(int userPoints){
        this.userPoints = userPoints;
    }
    public int getUserPoints(){
        return this.userPoints;
    }
}

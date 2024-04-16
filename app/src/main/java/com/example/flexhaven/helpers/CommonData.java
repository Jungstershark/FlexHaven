package com.example.flexhaven.helpers;

import java.util.ArrayList;

//Singleton Design Pattern
//1 class amongst all areas of the app!
public class CommonData {
    private static CommonData instance;
    private int userPoints;
    private String[] categories = {"Woman", "Men", "Electronics", "Shorts", "T-Shirts", "Jeans"};
    private String searchBar = "";
    private ArrayList<String> categoriesToSearch;
    private ArrayList<Item> itemsToDisplay;
    private User currentUser;

    //CANNOT BE INSTANTIATED!
    private CommonData(){
    }
    public static synchronized CommonData getInstance(){
        if (instance==null){
            instance = new CommonData();
        }
        return instance;
    }
    public void setCurrentUser(User user){
        this.currentUser = user;
    }
    public User getCurrentUser(){
        return this.currentUser;
    }
    public void setUserPoints(int userPoints){
        this.userPoints = userPoints;
    }
    public int getUserPoints(){
        return this.userPoints;
    }
    public String[] getCategories(){
        return this.categories;
    }
    public void setSearchBar(String searchBar){
        this.searchBar = searchBar;
    }
    public String getSearchBar(){
        return this.searchBar;
    }
    public void CategoriesToSearch(ArrayList<String> categories){
        this.categoriesToSearch = categories;
    }
    public ArrayList<String> getCategoriesToSearch(){
        return this.categoriesToSearch;
    }
    public void itemsToDisplay(ArrayList<Item> items){
        this.itemsToDisplay = items;
    }
    public ArrayList<Item> getItemsToDisplay(){
        return this.itemsToDisplay;
    }

    // Method to reset the CommonData singleton instance
    public static void resetInstance() {
        instance = null;
    }
}

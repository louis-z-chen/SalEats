package models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RestaurantList {
	private int total;
    private ArrayList<Restaurant> businesses;

    public RestaurantList(ArrayList<Restaurant> restaurants, int total)  {
        this.businesses = restaurants;
        this.total = total;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return businesses;
    }
    public void setRestaurants(ArrayList<Restaurant> businesses) {
    	this.businesses = businesses;
    }
    
    public int getTotal() {
        return total;
    }
    public void setTotal(int total){
        this.total = total;
    }
}

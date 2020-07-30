package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.FieldNotFoundException;

import java.util.ArrayList;

public class Restaurant {
    private String rating;
    private String price;
    private String phone;
    private String id;
    private ArrayList<Categories> categories;
    private String review_count;
    private String name;
    private String url;
    private Coordinates coordinates;
    private String image_url;
    private Location location;
    private String distance;
    
    public Restaurant(String rating, String price, String phone, String id, ArrayList<Categories> categories, 
        String review_count, String name, String url, Coordinates coordinates,
        String image_url, Location location, String distance) {

        this.rating = rating;
        this.price = price;
        this.phone = phone;
        this.id=id;
        this.categories = categories;
        this.review_count = review_count;
        this.name = name;
        this.url = url;
    	this.coordinates = coordinates;
        this.image_url = image_url;
        this.location = location;
        this.distance = distance;
    }

    //rating
    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

    //price
    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    //phone
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    //id
    public String getID(){
        return id;
    }
    public void setID(String id){
        this.id = id;
    }

    //categories
    public ArrayList<Categories> getCategories(){
        return categories;
    }
    public void setCategories(ArrayList<Categories> Categories){
        this.categories = Categories;
    }

    //review_count
    public String getReviewCount() {
        return review_count;
    }
    public void setReviewCount(String review_count) {
        this.review_count = review_count;
    }

    //name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //url
    public String getURL() {
        return url;
    }
    public void setURL(String url) {
        this.url = url;
    }

    //coordinates
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public void setCoordinates(Coordinates Coordinates) {
        this.coordinates = Coordinates;
    }

    //image_url
    public String getImage_URL(){
        return image_url;
    }
    public void setImage_URL(String image_url){
        this.image_url = image_url;
    }

    //location
    public Location getLocation(){
        return location;
    }
    public void setLocation(Location Location){
        this.location = Location;
    }
    
    public String getAddress() {
    	Location temp = location;
    	String address = temp.getAddress1() + " " + temp.getCity() + " " + temp.getState() + " " + temp.getZipCode();
    	return address;
    }

    //distance
    public String getDistance(){
        return distance;
    }
    public void setDistance(String distance){
        this.distance = distance;
    }
}

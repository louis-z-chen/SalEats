package parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import models.Restaurant;
import models.RestaurantList;
import util.FieldNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class RestaurantParser {
    private static Gson gson = new Gson();

    //todo: Fill out the javadoc for the exceptions
    /**
     * Loads restaurants from JSON file and outputs a [RestaurantList] object
     * @param fileName Name of file to parse
     * @return [RestaurantList] object
     * @throws IOException
     * @throws FieldNotFoundException
     * @throws ClassCastException
     * @throws JsonSyntaxException
     */
    public static RestaurantList loadRestaurants(String fileName) throws IOException,
            FieldNotFoundException, ClassCastException, JsonSyntaxException {

        // Parse the JSON file
        RestaurantList restaurantList = gson.fromJson(new FileReader(fileName), RestaurantList.class);

        // Validate the fields
        validateFields(restaurantList.getRestaurants());

        // Return the validated object
        return restaurantList;
    }

    /**
     * @param restaurants [ArrayList] of [Restaurant] objects
     * @throws FieldNotFoundException Indicates that a field is missing, probably since it was parsed wrong
     */
    private static void validateFields(ArrayList<Restaurant> restaurants) throws FieldNotFoundException {
        for (Restaurant restaurant: restaurants) {
//            if(!restaurant.isComplete()) {
//                throw new FieldNotFoundException("Restaurant file missing data parameters!");
//            }
        }
    }
}

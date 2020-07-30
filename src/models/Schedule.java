package models;

import java.util.ArrayList;

public class Schedule {
    // Keep track of tasks
    private ArrayList<Task> taskList;

    public Schedule() {
        taskList = new ArrayList<>();
    }

    public void addTask(int time, String restaurant, String food) {
        taskList.add(new Task(time, restaurant, food));
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    // Inner class to store task object
    public static class Task {
        private int time;
        private String restaurant;
        private String food;

        public Task(int time, String restaurant, String food) {
            this.time = time;
            this.restaurant = restaurant;
            this.food = food;
        }

        public int getTime() {
            return time;
        }

        public String getRestaurant() {
            return restaurant;
        }

        public String getFood() {
            return food;
        }
        
        public void fixName() {
        	String temp = restaurant;
        	temp.replaceAll("\\s+", "-");
        	temp.replaceAll("’", "");
        	restaurant = temp;
        }
    }
}


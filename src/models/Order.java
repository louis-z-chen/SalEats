package models;

import java.util.concurrent.Semaphore;

import util.TimestampUtil;

public class Order extends Thread {
	
	Semaphore driverLock;
	double distance;
	String restaurantName;
	String item;
	
	public Order(Semaphore driverLock, double distance, String restaurantName, String item) throws InterruptedException {
		this.driverLock = driverLock;
		this.distance = distance;
		this.restaurantName = restaurantName;
		this.item = item;
	}
	
	public void run() {
		try {
			this.driverLock.acquire();
			System.out.println("[" + TimestampUtil.getTimestamp() + "] Starting delivery of " + this.item + " from " + this.restaurantName + "!");
			Thread.sleep((long)(distance * 2 * 1000));
			System.out.println("[" + TimestampUtil.getTimestamp() + "] Finished delivery of " + this.item + " from " + this.restaurantName + "!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.driverLock.release();
		}
	}
}

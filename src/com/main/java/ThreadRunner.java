package com.main.java;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Priyanka Kulkarni ThreadRunner class is to implement runners
 */

public class ThreadRunner extends Thread {
	/**
	 * Declaring variables
	 * name,restPercentage,runnersSpeed,newLocation,ran,restThread
	 */
	public static final int MARATHON_DISTANCE = 1000;
	private final String name;
	private final int restPercentage;
	private final int runnersSpeed;
	private int newLocation = 0;
	private final Random ran = new Random();
	private final ArrayList<MainRunApp> restThread = new ArrayList<MainRunApp>();

	/**
	 * Default constructor set default values to variables
	 */
	ThreadRunner() {
		name = "";
		restPercentage = 0;
		runnersSpeed = 0;
	}

	/**
	 * ThreadRunner class Constructor
	 * @param name for Runners Name
	 * @param restPercentage for Runners Rest Percentage
	 * @param runnersSpeed for Runners Speed
	 * @throws java.lang.Exception - exceptions related to rest percentage and speed
	 */
	public ThreadRunner(String name, int restPercentage, int runnersSpeed) throws Exception {
		this.name = name;
		this.restPercentage = restPercentage;
		this.runnersSpeed = runnersSpeed;

		if (!(0 <= restPercentage && restPercentage <= 100)) {
			throw new Exception("Rest Percentage must be between 0 and 100.");
		}
		if (runnersSpeed < 0) {
			throw new Exception("Runner Speed must be positive.");
		}
	}

	/**
	 * 
	 * @return name
	 */
	public String getRunnersName() {
		return name;
	}

	/**
	 * 
	 * @return restPercentage
	 */
	public int getRestPercentage() {
		return restPercentage;
	}

	/**
	 * 
	 * @return runnersSpeed
	 */
	public int getRunnersSpeed() {
		return runnersSpeed;
	}

	/**
	 * 
	 * @return ran - random number
	 */
	public Random getRan() {
		return ran;
	}

	/**
	 * 
	 * @return restThread - ArrayList of MainRunApp
	 */
	public ArrayList<MainRunApp> getRestThread() {
		return restThread;
	}

	/**
	 * 
	 * @param newLocation - set value
	 */
	public void setNewLocation(int newLocation) {
		this.newLocation = newLocation;
	}

	/**
	 * @return newLocation
	 */
	public int getNewLocation() {
		return newLocation;
	}

	/**
	 * This method overrides Thread class run method
	 */
	@Override
	public void run() {
		while (newLocation < MARATHON_DISTANCE && !isInterrupted()) {
			int r = ran.nextInt(100);
			if (r > restPercentage) {
				newLocation += runnersSpeed;
			}

			try {
				int sleep = 100;
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.print("");
				break;
			}
			notifyRestThreads();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.print("");
				break;
			}
		}
	}

	/**
	 * This method adds thread runner of MainRunApp to array of runners
	 * @param restT - to add object of MainRunApp to restThread
	 */
	public void addRestThread(MainRunApp restT) {
		restThread.add(restT);
	}

	/**
	 * This method updates location and notify the same
	 */
	void notifyRestThreads() {
		for (MainRunApp rf : restThread) {
			rf.update(this);
		}
	}

}

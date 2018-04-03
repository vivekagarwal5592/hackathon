package com.start.Hackathon.model.customModels;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class parkingsummary {

	Integer totalNumberOfCars;
	Integer TimeOverTwoHrs;
	String locationcoordinates;
	Map<String, Integer> numberOfCarsParked = new TreeMap<>();

	public parkingsummary() {
		super();
		TimeOverTwoHrs = 0;
	}

	public Integer getTotalNumberOfCars() {
		return totalNumberOfCars;
	}

	public void setTotalNumberOfCars(Integer totalNumberOfCars) {
		this.totalNumberOfCars = totalNumberOfCars;
	}

	public Integer getTimeOverTwoHrs() {
		return TimeOverTwoHrs;
	}

	public void setTimeOverTwoHrs(Integer timeOverTwoHrs) {
		TimeOverTwoHrs = timeOverTwoHrs;
	}

	public Map<String, Integer> getNumberOfCarsParked() {
		return numberOfCarsParked;
	}

	public void setNumberOfCarsParked(Map<String, Integer> numberOfCarsParked) {
		this.numberOfCarsParked = numberOfCarsParked;
	}

	public String getLocationcoordinates() {
		return locationcoordinates;
	}

	public void setLocationcoordinates(String locationcoordinates) {
		this.locationcoordinates = locationcoordinates;
	}

}

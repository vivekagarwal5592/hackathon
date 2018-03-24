package com.start.Hackathon.model.customModels;

import java.util.HashMap;
import java.util.Map;

public class parkingsummary {
	
	Integer totalNumberOfCars;
	Integer TimeOverTwoHrs;
	Map<String,Integer> numberOfCarsParked = new HashMap<>();
	
	
	
	public parkingsummary() {
		super();
		TimeOverTwoHrs =0;
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
	
	
	

	
	

}

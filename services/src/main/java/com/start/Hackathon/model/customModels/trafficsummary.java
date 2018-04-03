package com.start.Hackathon.model.customModels;

import java.util.Map;
import java.util.TreeMap;

public class trafficsummary {

	Integer noOfVehivles;
	String locationUid;
	String locationcoordinates;
	Map<String,Integer> numberOfCarsSpotted = new TreeMap<>();
	
	
	public trafficsummary() {
		super();
		this.noOfVehivles = 0;
	}

	public Integer getNoOfVehivles() {
		return noOfVehivles;
	}
	public void setNoOfVehivles(Integer noOfVehivles) {
		this.noOfVehivles = noOfVehivles;
	}
	public String getLocationUid() {
		return locationUid;
	}
	public void setLocationUid(String locationUid) {
		this.locationUid = locationUid;
	}
	public String getLocationcoordinates() {
		return locationcoordinates;
	}
	public void setLocationcoordinates(String locationcoordinates) {
		this.locationcoordinates = locationcoordinates;
	}
	public Map<String, Integer> getNumberOfCarsSpotted() {
		return numberOfCarsSpotted;
	}
	public void setNumberOfCarsSpotted(Map<String, Integer> numberOfCarsSpotted) {
		this.numberOfCarsSpotted = numberOfCarsSpotted;
	}
	
	
	
	
	
}

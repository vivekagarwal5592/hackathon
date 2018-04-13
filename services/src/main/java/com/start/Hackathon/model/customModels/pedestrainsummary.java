package com.start.Hackathon.model.customModels;

import java.util.Map;
import java.util.TreeMap;

public class pedestrainsummary {

	Integer noOfPeople;
	String locationUid;
	String locationcoordinates;
	Map<String, Integer> numberOfCarsSpotted = new TreeMap<>();

	public pedestrainsummary() {
		super();
		this.noOfPeople = 0;
	}

	public Integer getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(Integer noOfPeople) {
		this.noOfPeople = noOfPeople;
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

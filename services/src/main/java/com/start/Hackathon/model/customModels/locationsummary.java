package com.start.Hackathon.model.customModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class locationsummary {

	Integer total_vehicles;
	Integer total_cars_parked;
	String most_traffic_prone_location;
	String most_number_of_cars_parked;
	Map<String, Integer> traffic_chart = new HashMap<String, Integer>();
	Map<String, Integer> parking_chart = new HashMap<String, Integer>();
	List<trafficsummary> trafficsummary = new ArrayList<trafficsummary>();
	List<parkingsummary> parkingsummary = new ArrayList<parkingsummary>();
	List<pedestrainsummary> pedestrainsummary = new ArrayList<pedestrainsummary>();

	public List<pedestrainsummary> getPedestrainsummary() {
		return pedestrainsummary;
	}

	public void setPedestrainsummary(List<pedestrainsummary> pedestrainsummary) {
		this.pedestrainsummary = pedestrainsummary;
	}

	public Map<String, Integer> getParking_chart() {
		return parking_chart;
	}

	public void setParking_chart(Map<String, Integer> parking_chart) {
		this.parking_chart = parking_chart;
	}

	public locationsummary() {
		super();
		this.total_vehicles = 0;
		this.total_cars_parked = 0;

	}

	public Integer getTotal_vehicles() {
		return total_vehicles;
	}

	public void setTotal_vehicles(Integer total_vehicles) {
		this.total_vehicles = total_vehicles;
	}

	public Integer getTotal_cars_parked() {
		return total_cars_parked;
	}

	public void setTotal_cars_parked(Integer total_cars_parked) {
		this.total_cars_parked = total_cars_parked;
	}

	public String getMost_traffic_prone_location() {
		return most_traffic_prone_location;
	}

	public void setMost_traffic_prone_location(String most_traffic_prone_location) {
		this.most_traffic_prone_location = most_traffic_prone_location;
	}

	public String getMost_number_of_cars_parked() {
		return most_number_of_cars_parked;
	}

	public void setMost_number_of_cars_parked(String most_number_of_cars_parked) {
		this.most_number_of_cars_parked = most_number_of_cars_parked;
	}

	public Map<String, Integer> getTraffic_chart() {
		return traffic_chart;
	}

	public void setTraffic_chart(Map<String, Integer> traffic_chart) {
		this.traffic_chart = traffic_chart;
	}

	public List<trafficsummary> getTrafficsummary() {
		return trafficsummary;
	}

	public void setTrafficsummary(List<trafficsummary> trafficsummary) {
		this.trafficsummary = trafficsummary;
	}

	public List<parkingsummary> getParkingsummary() {
		return parkingsummary;
	}

	public void setParkingsummary(List<parkingsummary> parkingsummary) {
		this.parkingsummary = parkingsummary;
	}

}

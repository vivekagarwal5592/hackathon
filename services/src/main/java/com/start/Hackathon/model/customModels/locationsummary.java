package com.start.Hackathon.model.customModels;

public class locationsummary {
	
	Integer total_vehicles;
	Integer total_cars_parked;
	String most_traffic_prone_location;
	String most_number_of_cars_parked;
	
	
	
	public locationsummary() {
		super();
		this.total_vehicles = 0;
		this.total_cars_parked =0;
		
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
	
	

}

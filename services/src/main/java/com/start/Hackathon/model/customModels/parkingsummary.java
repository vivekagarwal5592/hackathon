package com.start.Hackathon.model.customModels;

public class parkingsummary {
	
	Integer totalNumberOfCars;
	Integer TimeOverTwoHrs;
	
	
	
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
	

	
	

}

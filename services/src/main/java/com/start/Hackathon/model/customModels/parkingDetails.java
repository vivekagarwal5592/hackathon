package com.start.Hackathon.model.customModels;

public class parkingDetails {
	
	String locationUid;
	String assetUid;
	String parkingintimestamp;
	String parkingouttimestamp;
	String objectUid;
	String totaltime;
	String coordinates;
	
	public parkingDetails(String locationUid, String assetUid, String parkingintimestamp, String objectUid,
			String coordinates) {
		super();
		this.locationUid = locationUid;
		this.assetUid = assetUid;
		this.parkingintimestamp = parkingintimestamp;
		this.objectUid = objectUid;
		this.coordinates = coordinates;
	}
	
	public String getLocationUid() {
		return locationUid;
	}
	public void setLocationUid(String locationUid) {
		this.locationUid = locationUid;
	}
	public String getAssetUid() {
		return assetUid;
	}
	public void setAssetUid(String assetUid) {
		this.assetUid = assetUid;
	}
	public String getParkingintimestamp() {
		return parkingintimestamp;
	}
	public void setParkingintimestamp(String parkingintimestamp) {
		this.parkingintimestamp = parkingintimestamp;
	}
	public String getParkingouttimestamp() {
		return parkingouttimestamp;
	}
	public void setParkingouttimestamp(String parkingouttimestamp) {
		this.parkingouttimestamp = parkingouttimestamp;
	}
	public String getObjectUid() {
		return objectUid;
	}
	public void setObjectUid(String objectUid) {
		this.objectUid = objectUid;
	}
	public String getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	
	
	

}

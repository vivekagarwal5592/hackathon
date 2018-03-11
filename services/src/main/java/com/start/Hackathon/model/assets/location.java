package com.start.Hackathon.model.assets;

public class location {

	String locationUid;
	String locationType;
	String parentLocationUid;
	String coordinatesType;
	String coordinates;
	String name;

	public String getLocationUid() {
		return locationUid;
	}

	public void setLocationUid(String locationUid) {
		this.locationUid = locationUid;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getParentLocationUid() {
		return parentLocationUid;
	}

	public void setParentLocationUid(String parentLocationUid) {
		this.parentLocationUid = parentLocationUid;
	}

	public String getCoordinatesType() {
		return coordinatesType;
	}

	public void setCoordinatesType(String coordinatesType) {
		this.coordinatesType = coordinatesType;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

package com.start.Hackathon.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.assets.AssetDetails;
import com.start.Hackathon.model.assets.asset;
import com.start.Hackathon.model.assets.contentdetails;
import com.start.Hackathon.model.assets.location;
import com.start.Hackathon.model.assets.locationDetail;
import com.start.Hackathon.model.customModels.parkingDetails;
import com.start.Hackathon.model.media.media;
import com.start.Hackathon.model.media.mediastep1;
import com.start.Hackathon.model.media.mediastep2;

@RestController
public class AssetController {

	@Autowired
	private RestOperations restoperations;

	private final String url;
	private final String authorization;
	RestTemplate restTemplate;

	@Autowired
	public AssetController(@Value("${predix.asset.metadata.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
		restTemplate = new RestTemplate();
	}

	// dummy method
	@RequestMapping(value = "/getallAssets", method = RequestMethod.GET)
	public ResponseEntity<contentdetails> getallassets() {

		return restTemplate.exchange(url
				+ "assets/search?bbox=33.077762:-117.663817,32.559574:-116.584410&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, getHeaders(), contentdetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getallAssetswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<contentdetails> getallassetswithinarea(@RequestParam String lattitude,
			@RequestParam String longitude) {

		return restTemplate.exchange(
				url + "assets/search?bbox=" + lattitude + "," + longitude + "&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, getHeaders(), contentdetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getAllLocationswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<locationDetail> getAllLocations(@RequestParam String bbox) {

		
		System.out.println("I am in getAllLocationswithinbbox");
		ResponseEntity<locationDetail> locationDetail = restTemplate.exchange(
				url + "locations/search?q=locationType:PARKING_ZONE&bbox=" + bbox + "&page=0&size=50", HttpMethod.GET,
				getHeaders(), locationDetail.class);

		locationDetail lDetails = locationDetail.getBody();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<locationDetail>(lDetails, responseHeaders, HttpStatus.CREATED);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getSingleAssetDetails", method = RequestMethod.POST)
	public ResponseEntity<AssetDetails> getallSingleAssetDetails(@RequestParam String assetuid) {

		return restTemplate.exchange(url + "assets/" + "522de83f-e524-4f76-80f0-463d3815b7a4", HttpMethod.GET,
				getHeaders(), AssetDetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getSingleLocationDetails", method = RequestMethod.POST)
	public ResponseEntity<location> getallSingleLocationDetails(@RequestParam String locationuid) {

		location l = restTemplate
				.exchange(url + "locations/" + locationuid, HttpMethod.GET, getHeaders(), location.class).getBody();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		l.setX_coordinate(getXcoordinate(l.getCoordinates()));
		l.setY_coordinate(getYcoordinate(l.getCoordinates()));
		return new ResponseEntity<location>(l, responseHeaders, HttpStatus.CREATED);

		// return restoperations.getForObject(url,companydetails.class);
	}

	public HttpEntity<String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}
	
	public float getXcoordinate(String s) {
		
		String[] tokens = s.split(",|[:]+");
		float x_coordinate = (Float.parseFloat(tokens[0]) + Float.parseFloat(tokens[2])
				+ Float.parseFloat(tokens[4]) + Float.parseFloat(tokens[6])) / 4;
		return x_coordinate;
		
	}
	
public float getYcoordinate(String s) {
		
		String[] tokens = s.split(",|[:]+");
		float y_coordinate = (Float.parseFloat(tokens[1]) + Float.parseFloat(tokens[3])
				+ Float.parseFloat(tokens[5]) + Float.parseFloat(tokens[7])) / 4;
		
		return y_coordinate;
	}

}

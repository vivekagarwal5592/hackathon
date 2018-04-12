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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
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
	
	//org.springframework.context.ApplicationContextInitializer

	// dummy method
	@RequestMapping(value = "/getallAssets", method = RequestMethod.GET)
	public ResponseEntity<contentdetails> getallassets() {

		return restTemplate.exchange(url
				+ "assets/search?bbox=33.077762:-117.663817,32.559574:-116.584410&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, getParkingHeaders(), contentdetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getallAssetswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<contentdetails> getallassetswithinarea(@RequestParam String lattitude,
			@RequestParam String longitude) {

		return restTemplate.exchange(
				url + "assets/search?bbox=" + lattitude + "," + longitude + "&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, getParkingHeaders(), contentdetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	@RequestMapping(value = "/getAllLocationswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<locationDetail> getAllParkingLocations(@RequestParam String bbox) {

		try {
			locationDetail locationDetail = restTemplate
					.exchange(url + "locations/search?q=locationType:PARKING_ZONE&bbox=" + bbox + "&page=0&size=50",
							HttpMethod.GET, getParkingHeaders(), locationDetail.class)
					.getBody();

			return new ResponseEntity<locationDetail>(locationDetail, getResponseHeaders(), HttpStatus.CREATED);
		} catch (HttpServerErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			return null;
		}

	}

	@RequestMapping(value = "/getAllTrafficLocationswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<locationDetail> getAllTrafficLocations(@RequestParam String bbox) {

		try {
			locationDetail locationDetail = restTemplate
					.exchange(url + "locations/search?q=locationType:TRAFFIC_LANE&bbox=" + bbox + "&page=0&size=50",
							HttpMethod.GET, getTrafficHeaders(), locationDetail.class)
					.getBody();
			return new ResponseEntity<locationDetail>(locationDetail, getResponseHeaders(), HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getSingleAssetDetails", method = RequestMethod.POST)
	public ResponseEntity<AssetDetails> getallSingleAssetDetails(@RequestParam String assetuid) {

		return restTemplate.exchange(url + "assets/" + "522de83f-e524-4f76-80f0-463d3815b7a4", HttpMethod.GET,
				getParkingHeaders(), AssetDetails.class);
	}

	@RequestMapping(value = "/getSingleLocationDetails", method = RequestMethod.POST)
	public ResponseEntity<location> getallSingleLocationDetails(@RequestParam String locationuid) {

		location l = restTemplate
				.exchange(url + "locations/" + locationuid, HttpMethod.GET, getParkingHeaders(), location.class)
				.getBody();

		l.setX_coordinate(getXcoordinate(l.getCoordinates()));
		l.setY_coordinate(getYcoordinate(l.getCoordinates()));
		return new ResponseEntity<location>(l, getResponseHeaders(), HttpStatus.CREATED);

	}

	public HttpEntity<String> getParkingHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public HttpEntity<String> getTrafficHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public float getXcoordinate(String s) {
		String[] tokens = s.split(",|[:]+");

		float x_coordinate = 0;
		for (int i = 0; i <= tokens.length - 1; i++) {
			if (i % 2 == 0) {
				x_coordinate += Float.parseFloat(tokens[i]);
			}
		}
		x_coordinate = x_coordinate / (tokens.length / 2);
		return x_coordinate;

	}

	public float getYcoordinate(String s) {
		String[] tokens = s.split(",|[:]+");
		float y_coordinate = 0;
		for (int i = 0; i <= tokens.length - 1; i++) {
			if (i % 2 != 0) {
				y_coordinate += Float.parseFloat(tokens[i]);
			}
		}
		y_coordinate = y_coordinate / (tokens.length / 2);
		return y_coordinate;
	}

	public HttpHeaders getResponseHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		return responseHeaders;

	}

}

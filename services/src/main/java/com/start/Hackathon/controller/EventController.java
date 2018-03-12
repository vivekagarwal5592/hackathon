package com.start.Hackathon.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.assets.locationDetail;
import com.start.Hackathon.model.events.eventproperties;
import com.start.Hackathon.model.events.parkingevent;

@RestController
public class EventController {

	RestTemplate restTemplate;
	private final String url;
	private final String authorization;
	private static int imageid;

	@Autowired
	public EventController(@Value("${predix.asset.event.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
		restTemplate = new RestTemplate();

	}

	@RequestMapping(value = "/getparkingbybbox", method = RequestMethod.POST)
	public ResponseEntity<parkingevent> getparkingbybbox(@RequestParam String x_position,@RequestParam String y_position, @RequestParam String startts,
			@RequestParam String endts) {

	
		
		System.out.println(url+"locations/events?bbox="+ getcoordinates(x_position,y_position)
		+ "&locationType=PARKING_ZONE&eventType=PKIN&startTime="+getdateinTimeStamp(startts)
		+ "&endTime="+getdateinTimeStamp(endts));

		parkingevent pk = restTemplate
				.exchange(url+"locations/events?bbox="+ getcoordinates(x_position,y_position)
						+ "&locationType=PARKING_ZONE&eventType=PKIN&startTime="+getdateinTimeStamp(startts)
						+ "&endTime="+getdateinTimeStamp(endts), HttpMethod.GET, getHeaders(), parkingevent.class)
				.getBody();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin","*");
		return new ResponseEntity<parkingevent>(pk, responseHeaders, HttpStatus.CREATED);

	}

	public HttpEntity<String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		// headers.set("Access-Control-Allow-Origin", "*");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public String getdateinTimeStamp(String date) {

		SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");

		try {
			date = String.valueOf(d.parse(date).getTime());
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;

	}
	
	public String getcoordinates(String x_position,String y_position) {
		
		float x1 = Float.parseFloat(x_position)-1;
		float y1 = Float.parseFloat(y_position)-1;
		float x2 = Float.parseFloat(x_position)+1;
		float y2 = Float.parseFloat(y_position)+1;
		
		String bbox =String.valueOf(x1) + ":" +String.valueOf(y1) + ","+
		String.valueOf(x2) + ":" + String.valueOf(y2);
		
		return bbox;
		
	}
	
	

}

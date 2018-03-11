package com.start.Hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	public ResponseEntity<parkingevent> getparkingbybbox(@RequestParam String bbox, @RequestParam String startts,
			@RequestParam String endts) {

		return restTemplate.exchange(url + "locations/events?bbox=" + bbox
				+ "&locationType=PARKING_ZONE&eventType=PKIN&startTime=" + startts + "&endTime=" + endts,
				HttpMethod.GET, getHeaders(),  parkingevent.class);

		// return restoperations.getForObject(url,companydetails.class);
	}
	
	public HttpEntity<String> getHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;	
	}

}

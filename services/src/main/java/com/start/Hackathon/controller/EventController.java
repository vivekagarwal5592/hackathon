package com.start.Hackathon.controller;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.assets.locationDetail;
import com.start.Hackathon.model.customModels.parkingDetails;
import com.start.Hackathon.model.customModels.parkingsummary;
import com.start.Hackathon.model.customModels.trafficsummary;
import com.start.Hackathon.model.events.PointCoordinate;
import com.start.Hackathon.model.events.locationid;
import com.start.Hackathon.model.events.parkingevent;
import com.start.Hackathon.model.events.trafficevent;

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
	public ResponseEntity<parkingevent> getparkingbybbox(@RequestParam String x_position,
			@RequestParam String y_position, @RequestParam String startts, @RequestParam String endts) {

		parkingevent pk = restTemplate.exchange(
				url + "locations/events?bbox=" + getcoordinates(x_position, y_position)
						+ "&locationType=PARKING_ZONE&eventType=PKIN&startTime=" + getdateinTimeStamp(startts)
						+ "&endTime=" + getdateinTimeStamp(endts),
				HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

		return new ResponseEntity<parkingevent>(pk, getResponseHeaders(), HttpStatus.CREATED);

	}

	@RequestMapping(value = "/getnearestparking", method = RequestMethod.POST)
	public ResponseEntity<List<PointCoordinate>> getnearestparking(@RequestParam String locationdetails) {

		// locationDetail locationdetails = new locationDetail ();
		System.out.println("I am inside getnearestparking");
		// System.out.println("Length is" +locationdetails.length);

		List<PointCoordinate> location_coordinates = new ArrayList<PointCoordinate>();

		parkingevent pkout = restTemplate.exchange(
				url + "locations/" + locationdetails + "/events?eventType=PKOUT&" + "startTime=" + getTime_minusFive()
						+ "&endTime=" + Calendar.getInstance().getTimeInMillis(),
				HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

		// location_coordinates.add();

		for (int i = 0; i <= pkout.getContent().length - 1; i++) {
			location_coordinates.add(new PointCoordinate(
					String.valueOf(getXcoordinate(pkout.getContent()[i].getProperties().getGeoCoordinates())),
					String.valueOf(getYcoordinate(
							String.valueOf(pkout.getContent()[i].getProperties().getGeoCoordinates())))));
		}

		/*
		 * System.out.println("X position "+nearest_place.getX());
		 * System.out.println("X position "+nearest_place.getY());
		 */

		return new ResponseEntity<List<PointCoordinate>>(location_coordinates, getResponseHeaders(),
				HttpStatus.CREATED);

	}

	@RequestMapping(value = "/getPKINForLastTendays", method = RequestMethod.POST)
	public ResponseEntity<parkingsummary> getPKINForLastTwoHours(@RequestParam String parking_loc,
			@RequestParam String startts, @RequestParam String endts) {

		parkingevent pkin = restTemplate.exchange(
				url + "locations/" + parking_loc + "/events?eventType=PKIN&" + "startTime="
						+ getdateinTimeStamp(startts) + "&endTime=" + getdateinTimeStamp(endts),
				HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

		List<parkingDetails> objectuids = getobjectuids(pkin);

		parkingevent pkout = restTemplate.exchange(
				url + "locations/" + parking_loc + "/events?eventType=PKOUT&" + "startTime="
						+ getdateinTimeStamp(startts) + "&endTime=" + getdateinTimeStamp(endts),
				HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

		objectuids = getparkingoutdetail(pkout, objectuids);

		parkingsummary ps = getparkingSummary(objectuids);

		return new ResponseEntity<parkingsummary>(ps, getResponseHeaders(), HttpStatus.CREATED);

	}

	@RequestMapping(value = "/getTrafficForLastTendays", method = RequestMethod.POST)
	public ResponseEntity<trafficsummary> getTrafficForLastTenDays(@RequestParam String traffic_loc,
			@RequestParam String startts, @RequestParam String endts) {

		trafficevent traffic = restTemplate.exchange(
				url + "locations/" + traffic_loc + "/events?eventType=TFEVT&" + "startTime="
						+ getdateinTimeStamp(startts) + "&endTime=" + getdateinTimeStamp(endts),
				HttpMethod.GET, getTrafficHeaders(), trafficevent.class).getBody();

		trafficsummary t = getTrafficSummary(traffic);
		return new ResponseEntity<trafficsummary>(t, getResponseHeaders(), HttpStatus.CREATED);

	}

	public HttpHeaders getResponseHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		return responseHeaders;

	}

	public HttpEntity<String> getParkingHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		// headers.set("Access-Control-Allow-Origin", "*");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public HttpEntity<String> getTrafficHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");
		// headers.set("Access-Control-Allow-Origin", "*");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public String getdateinTimeStamp(String date) {

		SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");

		try {
			date = String.valueOf(d.parse(date).getTime());
			// System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public String getcoordinates(String x_position, String y_position) {
		float x1 = Float.parseFloat(x_position) - 5;
		float y1 = Float.parseFloat(y_position) - 5;
		float x2 = Float.parseFloat(x_position) + 5;
		float y2 = Float.parseFloat(y_position) + 5;

		String bbox = String.valueOf(x1) + ":" + String.valueOf(y1) + "," + String.valueOf(x2) + ":"
				+ String.valueOf(y2);
		return bbox;
	}

	public String getTime_minusFive() {
		Calendar now = Calendar.getInstance();
		String time = String.valueOf((now.getTimeInMillis() - 300000));
		return time;
	}

	public String getTime_minusetwohours() {
		Calendar now = Calendar.getInstance();
		String time = String.valueOf((now.getTimeInMillis() - 7200000));
		return time;
	}

	public String getTime_minusetendays() {
		Calendar now = Calendar.getInstance();
		String time = String.valueOf((now.getTimeInMillis() - 864000000));
		return time;
	}

	public PointCoordinate getnearestcoordinate(float x_position, float y_position, parkingevent pk) {

		pk.getContent();
		List<String> carlocations = new ArrayList<String>();

		if (pk.getContent().length > 0) {

			for (int i = 0; i <= pk.getContent().length - 1; i++) {
				carlocations.add(pk.getContent()[i].getProperties().getGeoCoordinates());
			}

			// Point p= new Point();
			float min_distance = Float.MAX_VALUE;
			float x_min = Float.MAX_VALUE;
			float y_min = Float.MAX_VALUE;
			for (String s : carlocations) {
				String[] tokens = s.split(",|[:-]+");
				float x_coordinate = (Float.parseFloat(tokens[0]) + Float.parseFloat(tokens[2])
						+ Float.parseFloat(tokens[4]) + Float.parseFloat(tokens[8])) / 4;
				float y_coordinate = (Float.parseFloat(tokens[1]) + Float.parseFloat(tokens[3])
						+ Float.parseFloat(tokens[5]) + Float.parseFloat(tokens[7])) / 4;

				float temp = (float) (Math.pow(x_coordinate - x_position, 2) + Math.pow(y_coordinate - y_position, 2));
				if (Math.sqrt(temp) < min_distance) {
					x_min = x_coordinate;
					y_min = y_coordinate;
					min_distance = (float) Math.sqrt(temp);
				}

			}

			PointCoordinate p = new PointCoordinate(String.valueOf(x_min), String.valueOf(y_min));
			return p;
		}
		return null;
	}

	public List<parkingDetails> getobjectuids(parkingevent pe) {
		List<parkingDetails> objectDetails = new ArrayList<parkingDetails>();
		for (int i = 0; i <= pe.getContent().length - 1; i++) {
			objectDetails.add(new parkingDetails(pe.getContent()[i].getLocationUid(), pe.getContent()[i].getAssetUid(),
					pe.getContent()[i].getTimestamp(), pe.getContent()[i].getProperties().getObjectUid()));
		}
		return objectDetails;
	}

	public List<parkingDetails> getparkingoutdetail(parkingevent pe, List<parkingDetails> pk) {

		for (int i = 0; i <= pk.size() - 1; i++) {
			for (int j = 0; j <= pe.getContent().length - 1; j++) {
				if (pe.getContent()[j].getProperties().getObjectUid().equals(pk.get(i).getObjectUid())) {
					pk.get(i).setParkingouttimestamp(pe.getContent()[j].getTimestamp());

					String timeinmilliseconds = String.valueOf(Long.parseLong((pk.get(i).getParkingouttimestamp()))
							- Long.parseLong(pk.get(i).getParkingintimestamp()));

					pk.get(i).setTotaltime(String.valueOf(timeinmilliseconds));
					pe.getContent()[j].getProperties().setObjectUid("");
					break;
				}
			}

			/*
			 * objectDetails.add(new parkingDetails(pe.getContent()[i].getLocationUid(),
			 * pe.getContent()[i].getAssetUid(), pe.getContent()[i].getTimestamp(),
			 * pe.getContent()[i].getProperties().getObjectUid()));
			 */
		}
		return pk;

	}

	public parkingsummary getparkingSummary(List<parkingDetails> objectuids) {

		parkingsummary parkingsummary = new parkingsummary();
		parkingsummary.setTotalNumberOfCars(objectuids.size());

		for (parkingDetails p : objectuids) {
			if (p.getTotaltime() != null) {
				if (Long.parseLong(p.getTotaltime()) > 7200000) {
					parkingsummary.setTimeOverTwoHrs(parkingsummary.getTimeOverTwoHrs() + 1);
				}

				if (p.getParkingintimestamp() != null) {
					Date currentDate = new Date(Long.parseLong(p.getParkingintimestamp()));
					String d = new SimpleDateFormat("yyyy-MM-dd").format(currentDate).toString();
					if (parkingsummary.getNumberOfCarsParked().containsKey(d)) {
						parkingsummary.getNumberOfCarsParked().put(d,
								parkingsummary.getNumberOfCarsParked().get(d) + 1);
					} else {
						parkingsummary.getNumberOfCarsParked().put(d, 1);

					}
				}

			}

		}

		return parkingsummary;
	}

	public trafficsummary getTrafficSummary(trafficevent traffic) {

		trafficsummary t = new trafficsummary();
		t.setLocationUid(traffic.getContent()[0].getLocationUid());

		System.out.println(traffic.getContent().length);

		for (int i = 0; i <= traffic.getContent().length - 1; i++) {
			t.setNoOfVehivles(t.getNoOfVehivles() + traffic.getContent()[i].getMeasures().getVehicleCount());
		}

		return t;

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

}

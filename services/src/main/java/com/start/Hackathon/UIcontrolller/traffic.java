package com.start.Hackathon.UIcontrolller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class traffic {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/getparkinganalysis")
	public String getparkinganalysis() {
		// System.out.println("In pa");
		return "getparkinganalysis";
	}

	@RequestMapping("/gettrafficanalysis")
	public String gettrafficanalysis() {
		// System.out.println("In pa");
		return "gettrafficanalysis";
	}

	@RequestMapping("/currentlocation")
	public String currentlocation() {
		// System.out.println("In pa");
		return "currentlocation";
	}

	@RequestMapping("/locationanalysis")
	public String getlocationanakysis() {
		// System.out.println("In pa");
		return "locationanalysis";
	}
	
	@RequestMapping("/getfutureparkingspace")
	public String getfutureparkingspace() {
		 System.out.println("In pa");
		return "machinelearning";
	}

}

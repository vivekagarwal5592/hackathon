$( function() {
  $( "#fromdate" ).datepicker();
  $( "#todate" ).datepicker();
  $( "#predictiondate").datepicker();


});


let  getfutureprediction =(locationUid)=>{
	   
	console.log(locationUid)
	date = $("#predictiondate").val();
	datettime = date + " " +  $('#predictiontime').val();
	   console.log(datettime)
	   $.ajax({
		  
		   type: "GET",
		   dataType: "json",
		   url : "https://volcanotest.run.aws-usw02-pr.ice.predix.io/api/1.0/parkingprediction",
		   data : {
			  datetime: datettime,
			  locationUid:locationUid
		   },
		   success : function(data) {
		document.getElementById("prediction").innerHTML = "Parking Prediction: "+ data.result;
			 console.log(data)
		   },
		 error: function(jqXHR, textStatus, errorThrown){
		 alert(textStatus);
		 console.log(jqXHR.responseText)
		 }
		 }); 
}


let getAllLocations = () =>{
	 getlocationcoordinates().then(result=>{
		 return  getlocationUids(result.latitude,result.longitude)
	 }).then(result=>{
		 changeMapLocationLoop(result.pDetails,result.latitude,result.longitude)
	 })
	
}



function changeMapLocationLoop(locationdetails,centerx,centery,list) {

	 var uluru = {lat:centerx, lng: centery};
	    var map = new google.maps.Map(document.getElementById('map'), {
	      zoom: 13,
	      center: uluru,
	// mapTypeId: 'satellite'
	    });
	
	
	  var cityCircle = new google.maps.Circle({
         strokeColor: '#FF0000',
         strokeOpacity: 0.8,
         strokeWeight: 2,
         fillColor: '#FF0000',
         fillOpacity: 0.35,
         map: map,
         center: uluru,
         radius:   3000
       });

     cityCircle.setMap(map);
     
   
     locationdetails.forEach(items=>{
   	  var marker = new google.maps.Marker({
             position:  {lat:items.latitude, lng: items.longitude},
             map: map,
             locationUid:items.locationUid,
             lanecoordinates:items.list,
             x:items.latitude,
             y: items.longitude
           });
   	  
   	  marker.addListener('click', function() {
   		getfutureprediction(marker.locationUid)
   		//changeMapLocation(marker.x,marker.y,items.list) 
   	      	getaddressForGeoCode(marker.x,marker.y).then(result=>{
   	      		document.getElementById("e3").innerHTML = "Address: "+result;
       	      	
   	      	})
   	         });
         
         marker.setMap(map) 
     })
    
 }

let getaddressForGeoCode = (latitude,longitude,callback) => {
	   
	   return new Promise((resolve,reject)=>{
		   let geocoder = new google.maps.Geocoder;
		     var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
		         geocoder.geocode( {'location': latlng}, function(results, status) {

		       if (status == google.maps.GeocoderStatus.OK) {
		    	  resolve(results[0].formatted_address)
		      }
	   })
 
  });
}




let  getlocationUids = (latitude,longitude)=> {
	  
	  
	 // console.log(parseFloat(latitude+0.02) + ","
		// +parseFloat(longitude-0.02) )
	  // console.log(parseFloat(latitude-0.02) + ","
		// +parseFloat(longitude+0.02) )
	  
	  return new Promise((resolve,reject)=>{
		  
		 // $(".locations").empty()
		  $('#loading-image').show();
		  $('#map').hide();
	      $.ajax({
	      type: "POST",
	      dataType: "json",
	      url : "getAllLocationswithinbbox",
	      data : {
	        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude-0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude+0.02)

	      },
	      success : function(data) {
	    	 let pDetails = []
	    	        data.content.forEach(item=>{
	    	        	pDetails.push(locationdetails(item))
	    	         })  
	    	        
	    	         resolve({pDetails,latitude,longitude})
	    	        
	      },
	       error: function(jqXHR, textStatus, errorThrown){
	    	   alert("Details about Location currently not available")
	    },
	    complete: function(){
	        $('#loading-image').hide();
	        $('#map').show();
	      }
	    });
		  
		    
	  })
}

let  locationdetails = (ldetails)=> {

	let list = getcoordinates(ldetails.coordinates)
	let xy = getcentroid(list)

	return {'list':list,'latitude':xy.x,'longitude':xy.y,'locationUid':ldetails.locationUid}
	}




let myFunction = () =>{
	
	getlocationcoordinates().then(result =>{
		return getParkingSummary(result.latitude,result.longitude)	
	}).then(data =>{
		 renderparkingChart(data);
	})
	
}



let myFunction2 = () =>{
	
	getlocationcoordinates().then(result =>{
		return getTrafficSummary(result.latitude,result.longitude)	
	}).then(data =>{
		 renderparkingChart(data);
	})
	
}



let myFunction3 = () =>{
	
	getlocationcoordinates().then(result =>{
		return getPedestrainSummary(result.latitude,result.longitude)	
	}).then(data =>{
		console.log(data)
		// renderparkingChart(data);
	})
	
}




let  getlocationcoordinates = ()=> {

	return new Promise((resolve, reject) =>{
		let geocoder = new google.maps.Geocoder;
	    geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego' }, function(results, status) {
	  if (status == google.maps.GeocoderStatus.OK) {
	   let  latitude = results[0].geometry.location.lat();
	     longitude = results[0].geometry.location.lng();
		
	   resolve({latitude, longitude})  
	}
 
  // callback(latitude,longitude)
 });
});
};


let getParkingSummary = (latitude,longitude) =>{
  
		return new Promise((resolve,reject)=>{
			
			
			let hourlyparking = false;
			if($('#hourlyparking').is(':checked') ){
				hourlyparking = true
			}
			
			if($('#fromdate').val() == '' || $('#todate').val() ==''){
				alert("Please select date")
			}
			
			
			  $.ajax({
			      type: "POST",
			      dataType: "json",
			      url : "getparkingsummarycsv",
			      data : {
			        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02),
			        "startts":$('#fromdate').val(),
			        "endts": $('#todate').val(),
			        "hourly": hourlyparking
			      
			      },
			      success : function(data) {
			
			    	  console.log(data)
			    	 // gettrafficheatmap(latitude,longitude,data.trafficsummary);
			    	 // getparkingheatmap(latitude,longitude,data.parkingsummary);
			    	
			    	  
			    	 // renderbarChart(data.trafficsummary);
			    	  resolve(data)
			    	 
			    	  
			    	/*
					 * renderbarChart(data.traffic_chart)
					 * renderparkingChart(data.parking_chart)
					 */
			      },
			       error: function(jqXHR, textStatus, errorThrown){
			    alert(textStatus);
			    console.log(jqXHR.responseText)
			    }
			    });
			
		})
}




let getTrafficSummary = (latitude,longitude) =>{
	  
	return new Promise((resolve,reject)=>{
		
		console.log($('#hourly').is(':checked') )
		
		let hourly = false;
		if($('#hourly').is(':checked') ){
			hourly = true
		}
		
		
		if($('#fromdate').val() == '' || $('#todate').val() ==''){
			alert("Please select date")
		}
		
		
		
		else{
		   $.ajax({
			      type: "POST",
			      dataType: "json",
			      url : "gettrafficsummarycsv",
			      data : {
			        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02),
			        "startts":$('#fromdate').val(),
			        "endts": $('#todate').val(),
			        "hourly":hourly
			      
			      },
			      success : function(data) {
			
			    	  console.log(data)
			    	  
			    	  resolve(data)
			    	 // gettrafficheatmap(latitude,longitude,data.trafficsummary);
			    	 // getparkingheatmap(latitude,longitude,data.parkingsummary);
			    	
			    	  
			    	 // renderbarChart(data.trafficsummary);
			    	 // renderparkingChart(data);
			    	  
			    	/*
					 * renderbarChart(data.traffic_chart)
					 * renderparkingChart(data.parking_chart)
					 */
			      },
			       error: function(jqXHR, textStatus, errorThrown){
			    alert(textStatus);
			    console.log(jqXHR.responseText)
			    }
			    });
	}
		
		
	})
	

	
}



let getPedestrainSummary = (latitude,longitude) =>{
	  
	
	return new Promise((resolve,reject) =>{
		
		console.log("I am in getPedestrain summary")
		
	
	console.log($('#hourlypedestrain').is(':checked') )
	
	let hourly = false;
	if($('#hourlypedestrain').is(':checked') ){
		hourly = true
	}
	
	
	if($('#fromdate').val() == '' || $('#todate').val() ==''){
		alert("Please select date")
	}
	
	

	else{
	   $.ajax({
		      type: "POST",
		      dataType: "json",
		      url : "getpedestrainsummarycsv",
		      data : {
		        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02),
		        "startts":$('#fromdate').val(),
		        "endts": $('#todate').val(),
		        "hourly":hourly
		      
		      },
		      success : function(data) {
		
		    	  console.log(data)
		    	  
		    	  resolve(data)
		    	 // gettrafficheatmap(latitude,longitude,data.trafficsummary);
		    	 // getparkingheatmap(latitude,longitude,data.parkingsummary);
		    	
		    	  
		    	 // renderbarChart(data.trafficsummary);
		    	 // renderparkingChart(data);
		    	  
		    	/*
				 * renderbarChart(data.traffic_chart)
				 * renderparkingChart(data.parking_chart)
				 */
		      },
		       error: function(jqXHR, textStatus, errorThrown){
		    alert(textStatus);
		    console.log(jqXHR.responseText)
		    }
		    });
}
	
	})
}








let getcoordinates = (coordinates) =>{

let xy = coordinates.split(",")

let list = []
 xy.forEach(items=>{
   val = items.split(":")
   list.push({lat:parseFloat(val[0]),lng:parseFloat(val[1])})
 })

return list;
}

let setpolyline = (list)=>{
	 var line = new google.maps.Polyline({
		 path: list,
     strokeColor: "#FF0000",
     strokeOpacity: 1.0,
     strokeWeight: 10,
     // geodesic: true,
     // map: map
    });
    //
    line.setMap(map);
}




function changeMapLocation(latitude,longitude,list) {

     let uluru = {lat: latitude, lng: longitude};
     let map = new google.maps.Map(document.getElementById('map'), {
       zoom: 19,
       center: uluru,
       mapTypeId: 'satellite'
     });

    // var line = new google.maps.Polyline({
     // path: [new google.maps.LatLng(latitude+1,longitude+1), new
		// google.maps.LatLng(latitude-10,longitude-10)],
     // strokeColor: "#FF0000",
     // strokeOpacity: 1.0,
     // strokeWeight: 10,
     // // geodesic: true,
     // // map: map
     // });
     //
     // line.setMap(map);

     var marker = new google.maps.Marker({
       position: uluru,
     });

marker.setMap(map)
       var flightPath = new google.maps.Polyline({
         path: list,
         geodesic: true,
         strokeColor: '#FF0000',
         strokeOpacity: 1.0,
         strokeWeight: 2
       });

       flightPath.setMap(map);
   }

function initMap() {
     var uluru = {lat:33.7157, lng: -117.1611};
     var map = new google.maps.Map(document.getElementById('map'), {
       zoom: 12,
       center: uluru
     });
     var marker = new google.maps.Marker({
       position: uluru,
       map: map
     });
   }

  



function renderbarChart(d) {

	console.log("I am getting traffic analysis")
	   console.log(d)
	   let datapoints = []
	   let i=1
	   
	   let list = []
	   d.forEach(items=>{
		   let coordinates = getcoordinates(items.locationcoordinates);
		   let marker = getcentroid(coordinates)
		  
		   let total_carsSpotted = 0;
		   for(key in  items.numberOfCarsSpotted){
			   
			   total_carsSpotted +=  items.numberOfCarsSpotted[key]
			}
		   
			datapoints.push({label:i,y:total_carsSpotted,locationUid:items.locationUid})
		   i +=1
	   })
	   
	   
	     let data = []
	   let enddata = {type : "column",yValueFormatString : "#,##0.0#",dataPoints:datapoints,  click: function(e){  
		   
		   if($('#predictiondate').val() == ''){
				alert("Please select date")
			}
		   datetime = $('#predictiondate').val()+ " "+ $('#fromtime').val() ;
		   
		   getLocationDetails(datetime,e.dataPoint.locationUid)
		   
		  // alert( "clicked" + );
	   }
	   }
	   data.push(enddata)



var options = {
			animationEnabled : true,
			title : {
				text : "Vehicle statistics"
			},
			axisY : {
				title : "Number of Vehicles",
				includeZero : false
			},
			axisX : {
				title : "Location"
			},
			data :data
		};	

$("#chartContainer").CanvasJSChart(options);

}
   
   
   let renderparkingChart = (d)=> {

	 // console.log(d)
	   let datapoints = []
	   let i=1
	   
	   let list = []
	   d.forEach(items=>{
		   let coordinates = getcoordinates(items.locationcoordinates);
		   let marker = getcentroid(coordinates)
		  
		   let total_carsSpotted = 0;
		   for(key in  items.numberOfCarsParked){
			   
			   total_carsSpotted +=  items.numberOfCarsParked[key]
			}
		   
			datapoints.push({label:i,y:total_carsSpotted,locationUid:items.locationUid})
			  i +=1
	   })
	   
	   
	   
	  /*
		 * for(key in d){ datapoints.push({label:i,y:d[key],locationUid:key}) i
		 * +=1 }
		 */



	   let data = []
	   let enddata = {type : "column",yValueFormatString : "#,##0.0#",dataPoints:datapoints,  click: function(e){ 
		   
		   if($('#predictiondate').val() == '' ||  $('#fromtime').val()=='' ){
				alert("Please select date")
			}
		   else{
			   datetime = $('#predictiondate').val()+ " "+ $('#fromtime').val() ;
			   console.log(datetime)
			   getLocationDetails(datetime,e.dataPoint.locationUid)  
		   }
		
		   
		  // alert( "clicked" + );
	   }
	   }
	   data.push(enddata)

	   console.log(data)

	   var options = {
	   			animationEnabled : true,
	   			title : {
	   				text : "Parking statistics"
	   			},
	   			axisY : {
	   				title : "Number of Vehicles Parked",
	   				includeZero : false
	   			},
	   			axisX : {
	   				title : "Location"
	   			},
	   			data :data
	   		};	

	   $("#chartContainer2").CanvasJSChart(options);

	   }   
   
   
   let getcentroid = (list)=>{
	 
		let x = 0
		let y =0
		list.forEach(item=>{
			x += item.lat
			y += item.lng
		})
		
		x = x/list.length
		y = y/list.length
		
		return {x,y}

	}
   
   let  getLocationDetails = (datetime,locationUid) =>
   {
	  
	   getfutureprediction(datetime,locationUid)
   }
   
  
   
   let  gettrafficheatmap = (latitude,longitude,data) =>{
	
	  
	   let list = []
	   data.forEach(items=>{
		   let coordinates = getcoordinates(items.locationcoordinates);
		   let marker = getcentroid(coordinates)
		  
		   let total_carsSpotted = 0;
		   for(key in  items.numberOfCarsSpotted){ 
			   total_carsSpotted +=  items.numberOfCarsSpotted[key]
			}
		   
		   
		 
		   for(i =0;i<total_carsSpotted/10;i++){
		   list.push(new google.maps.LatLng(marker.x, marker.y))
		   }
		   
		   trafficheatMap(latitude,longitude,list);

		   
	   })
	   
   }
   
   let  getparkingheatmap = (latitude,longitude,data) =>{
		
	   console.log(data)
	   let list = []
	   data.forEach(items=>{
		// console.log("getting parking data")
		 // console.log(items.locationcoordinates)
		   let coordinates = getcoordinates(items.locationcoordinates);
		   let marker = getcentroid(coordinates)
		  
		   let total_carsSpotted = 0;
		   for(key in  items.numberOfCarsParked){
			   
			   total_carsSpotted +=  items.numberOfCarsParked[key]
			}
		   
		   
		 
		   for(i =0;i<total_carsSpotted/10;i++){
		   list.push(new google.maps.LatLng(marker.x, marker.y))
		   }
		   
		   parkingheatMap(latitude,longitude,list);

		   
	   })
	   
   }
   
   
   let trafficheatMap =(latitude,longitude,list)=> {
       let map = new google.maps.Map(document.getElementById('trafficheatmap'), {
         zoom: 14,
         center: {lat: latitude, lng: longitude},
         mapTypeId: 'satellite'
       });

       let heatmap = new google.maps.visualization.HeatmapLayer({
         data: list,
         map: map
       });
     }
   
   let parkingheatMap =(latitude,longitude,list)=> {
       let map = new google.maps.Map(document.getElementById('parkingheatmap'), {
         zoom: 13,
         center: {lat: latitude, lng: longitude},
         mapTypeId: 'satellite'
       });

       let heatmap = new google.maps.visualization.HeatmapLayer({
         data: list,
         map: map
       });
     }
   
   
   
   
   
   

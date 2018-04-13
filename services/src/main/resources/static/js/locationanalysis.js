$( function() {
  $( "#fromdate" ).datepicker();
  $( "#todate" ).datepicker();

 // $('#datetimepicker1').datetimepicker();
// getlocationcoordinates(getlocationUids);
});



let  getlocationcoordinates = (callback)=> {

 let geocoder = new google.maps.Geocoder;
    geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego' /* $('#current_position').val() */}, function(results, status) {
  if (status == google.maps.GeocoderStatus.OK) {
   let  latitude = results[0].geometry.location.lat();
     longitude = results[0].geometry.location.lng();
   callback(latitude,longitude)
 }
});
};


let getLocationSummary = (latitude,longitude) =>{
  
	if($('#fromdate').val() == '' || $('#todate').val() ==''){
		alert("Please select date")
	}
	else{
		
		getParkingSummary(latitude,longitude);
		getTrafficSummary(latitude,longitude)
		  
}
	
}




let getParkingSummary = (latitude,longitude) =>{
		 
		  $('#loading-image2').show();
    	  $('#chartContainer2').hide();
    	
	   $.ajax({
		      type: "POST",
		      dataType: "json",
		      url : "getparkingsummary",
		      data : {
		        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02),
		        "startts":$('#fromdate').val(),
		        "endts": $('#todate').val()
		      
		      },
		      success : function(data) {
		
		    	//  renderbarChart(data.trafficsummary);
		    	  renderparkingChart(data.parkingsummary);
		    	  
		    	//  gettrafficheatmap(latitude,longitude,data.trafficsummary);
		    	  getparkingheatmap(latitude,longitude,data.parkingsummary);
		    	
		    	  
		    	 
		    	/*
				 * renderbarChart(data.traffic_chart)
				 * renderparkingChart(data.parking_chart)
				 */
		      },
		       error: function(jqXHR, textStatus, errorThrown){
		    alert(textStatus);
		    console.log(jqXHR.responseText)
		    },
		    complete: function(){
		    	 
				  $('#loading-image2').hide();
		    	  $('#chartContainer2').show();
		       
		      }
		    });

	
}












let getTrafficSummary = (latitude,longitude) =>{
	  
	
		
		 
		  $('#loading-image3').show();
    	  $('#chartContainer').hide();
    	
    	
	   $.ajax({
		      type: "POST",
		      dataType: "json",
		      url : "gettrafficsummary",
		      data : {
		        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02),
		        "startts":$('#fromdate').val(),
		        "endts": $('#todate').val()
		      
		      },
		      success : function(data) {
		
		    	  renderbarChart(data.trafficsummary);
		    	//  renderparkingChart(data.parkingsummary);
		    	  
		    	  gettrafficheatmap(latitude,longitude,data.trafficsummary);
		    //	  getparkingheatmap(latitude,longitude,data.parkingsummary);
		    	
		    	  
		    	 
		    	/*
				 * renderbarChart(data.traffic_chart)
				 * renderparkingChart(data.parking_chart)
				 */
		      },
		       error: function(jqXHR, textStatus, errorThrown){
		    alert(textStatus);
		    console.log(jqXHR.responseText)
		    },
		    complete: function(){
		    	  $('#loading-image3').hide();
		    	  $('#chartContainer').show();
		       
		      }
		    });

	
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

   let getaddressForGeoCode = (latitude,longitude,callback) => {
     let geocoder = new google.maps.Geocoder;
     var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
         geocoder.geocode( {'location': latlng}, function(results, status) {

       if (status == google.maps.GeocoderStatus.OK) {
        // console.log()
  callback(results[0].formatted_address)
      }
     });
   }




function renderbarChart(d) {

	// console.log("I am getting traffic analysis")
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
		   getLocationDetails(e.dataPoint.locationUid)
		   
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
   
   
   function renderparkingChart(d) {

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
		   getLocationDetails(e.dataPoint.locationUid)
		   
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
   
   let  getLocationDetails = (locationUid) =>
   {
	   $.ajax({
		   type: "POST",
		   dataType: "json",
		   url : "getSingleLocationDetails",
		   data : {
			   locationuid:locationUid
		   },
		   success : function(data) {

			   coordinates = getcoordinates(data.coordinates);
			   marker = getcentroid(coordinates)
			   changeMapLocation(marker.x,marker.y,coordinates) 
			   
				getaddressForGeoCode(marker.x,marker.y,function(result){
	    	      	document.getElementById("e3").innerHTML = result;
	    	      	
	    	      		})
		   },
		 error: function(jqXHR, textStatus, errorThrown){
		 alert(textStatus);
		 }
		 });
	   
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
   
   
   
   
   
   

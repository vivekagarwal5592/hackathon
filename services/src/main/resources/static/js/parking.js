$( function() {
  $( "#fromdate" ).datepicker();
  $( "#todate" ).datepicker();

 // $('#datetimepicker1').datetimepicker();
// getlocationcoordinates(getlocationUids);
});



let  getlocationcoordinates = (callback)=> {

  // let startts = $('#fromdate').val();
  // let endts = $('#todate').val();
  //
  // console.log(startts)
  // console.log(endts)

 let geocoder = new google.maps.Geocoder;
    geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego' /* $('#current_position').val() */}, function(results, status) {
  if (status == google.maps.GeocoderStatus.OK) {
   let  latitude = results[0].geometry.location.lat();
     longitude = results[0].geometry.location.lng();
   callback(latitude,longitude)
  // markarea(latitude,longitude)
 }
});
};
  let  getlocationUids = (latitude,longitude)=> {
	  
	  
	  console.log(parseFloat(latitude+0.02)  + "," +parseFloat(longitude-0.02) )
	   console.log(parseFloat(latitude-0.02)  + "," +parseFloat(longitude+0.02) )
	  
	  $(".locations").empty()
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
    	        
    	         changeMapLocationLoop(pDetails,latitude,longitude)
      },
       error: function(jqXHR, textStatus, errorThrown){
    	   alert("Details about Location currently not available")
    }
    });
}

let  parkingdetails = (locationUid)=> {

   let startts = $('#fromdate').val();
    let endts = $('#todate').val();
    
    if(startts == '' || endts == ''){
    	alert("Please select range fot the data")
    }
    else{
  $.ajax({
  type: "POST",
  dataType: "json",
  url : "getPKINForLastTendays",
  data : {
  parking_loc: locationUid,
startts:startts,
endts:endts
  },
  success : function(data) {

renderChart(data.numberOfCarsParked)
document.getElementById("e1").innerHTML = data.timeOverTwoHrs;
document.getElementById("e2").innerHTML = data.totalNumberOfCars;
  },
   error: function(jqXHR, textStatus, errorThrown){
alert(textStatus);
}
});

// locationdetails(locationUid)
}

}

let  locationdetails = (ldetails)=> {

let list = getcoordinates(ldetails.coordinates)
let xy = getcentroid(list)


 return {'list':list,'latitude':xy.x,'longitude':xy.y,'locationUid':ldetails.locationUid}

// getaddressForGeoCode(data.x_coordinate,data.y_coordinate,function(result){
	// document.getElementById("e3").innerHTML = result;


// }
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


let getcoordinates = (coordinates) =>{
 let xy = coordinates.split(",")
let list = []
 xy.forEach(items=>{
   val = items.split(":")
   list.push({lat:parseFloat(val[0]),lng:parseFloat(val[1])})
 })

return list;
}



function changeMapLocation(latitude,longitude,list) {

	console.log("latitude is "+latitude)
	console.log("longitude is "+longitude)
	console.log("list  is "+JSON.stringify(list))
    
	 var uluru = {lat: latitude, lng: longitude};
	 var map = new google.maps.Map(document.getElementById('mapx'), { 
		  zoom: 19,
		  center: uluru,
		  mapTypeId: 'satellite'
		  });
	 
	
	
	  var flightPath = new google.maps.Polyline({ 
	  path: list, 
	  geodesic: true,
	  strokeColor: '#FF0000', 
	  strokeOpacity: 1.0, 
	  strokeWeight: 2,
	 
	  });
	  
	  
	  flightPath.setMap(map);
	
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
    	      	parkingdetails(marker.locationUid)
    	      	getaddressForGeoCode(marker.x,marker.y,function(result){
    	      	document.getElementById("e3").innerHTML = result;
    	      	changeMapLocation(marker.x,marker.y,items.list) 
    	      		})
    	         });
          
          marker.setMap(map) 
      })
     
     
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
    	   callback(results[0].formatted_address)
      }
     });
   }




   function renderChart(data) {

console.log(data)
let list = []
for(key in data){
  list.push({label:key,y:data[key]})
}

var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2",
	title:{
		text: "Parking Statistics"
	},
	axisY:{
		includeZero: false
	},
	data: [{
		type: "line",
		dataPoints:list
	}]
});
chart.render();

}
   
  

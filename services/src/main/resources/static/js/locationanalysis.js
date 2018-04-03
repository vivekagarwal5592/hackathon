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
	   $.ajax({
		      type: "POST",
		      dataType: "json",
		      url : "getlocationsummary",
		      data : {
		        "bbox":parseFloat(latitude+0.1) + ':' + parseFloat(longitude+0.1) + ','+ parseFloat(latitude-0.1) + ':' + parseFloat(longitude-0.1),
		        "startts":$('#fromdate').val(),
		        "endts": $('#todate').val()
		      
		      },
		      success : function(data) {
		    	  document.getElementById("total-vehicles").innerHTML = data.total_vehicles;
		    	  document.getElementById("total-parked").innerHTML = data.total_cars_parked;
		    	  
		    	  
		    	 // console.log(data)
		    	  list = getcoordinates(data.most_traffic_prone_location)
		    	  coordinates =  getcentroid(list)
		    	 changeMapLocation(coordinates.x,coordinates.y,list)
		    	 getaddressForGeoCode(coordinates.x,coordinates.y,function settrafficlocation(address){
		    		 document.getElementById("most-traffic").innerHTML = address
		    	 })
		      },
		       error: function(jqXHR, textStatus, errorThrown){
		    alert(textStatus);
		    console.log(jqXHR.responseText)
		    }
		    });
}
	
}

let getcentroid = (list) =>{
	
	let x =0;
	let y =0;
	list.forEach(item=>{
		x += item.lat;
		y += item.lng; 
	})
	x /= list.length;
	y /=list.length
	
	return {x,y}
}




let getcoordinates = (coordinates) =>{

let xy = coordinates.split(",")

let list = []
 xy.forEach(items=>{
   val = items.split(":")
   list.push({lat:parseFloat(val[0]),lng:parseFloat(val[1])})
 })

// console.log(list)
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
console.log("I am in change Map location")
console.log(latitude)
console.log(longitude)
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

     var flightPlanCoordinates = [
         {lat: 37.772, lng: -122.214},
         {lat: 21.291, lng: -157.821},
         {lat: -18.142, lng: 178.431},
         {lat: -27.467, lng: 153.027}
       ];
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




   function renderChart(data) {

console.log(data)
let list = []
for(key in data){
  list.push({label:key,y:data[key]})
}

console.log(list)



var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2",
	title:{
		text: "Simple Line Chart"
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

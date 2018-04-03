window.onload = function() {
// var startPos;
 

 

 
};

var geoOptions = {
	     timeout: 10 * 1000
	  }

let getcurrentPosition = () =>{
	 navigator.geolocation.getCurrentPosition(geoSuccess, geoError, geoOptions);
}




let  getlocationcoordinates = (callback)=> {
	console.log("I am here")
	 let geocoder = new google.maps.Geocoder;
	    geocoder.geocode( { 'address': $( "#locationName" ).val()}, function(results, status) {
	  if (status == google.maps.GeocoderStatus.OK) {
	   let  latitude = results[0].geometry.location.lat();
	     longitude = results[0].geometry.location.lng();

	  // markarea(latitude,longitude)
	   callback(latitude,longitude)
	 }
	});
	};

var geoSuccess = function(position) {
    var startPos = position;
  /*
	 * console.log(position) console.log(startPos.coords.latitude)
	 * console.log(startPos.coords.longitude)
	 */
      getlocationUids(startPos.coords.latitude,startPos.coords.longitude)
  };
  
  var geoError = function(error) {
    console.log('Error occurred. Error code: ' + error.code);
    // error.code can be:
    // 0: unknown error
    // 1: permission denied
    // 2: position unavailable (error response from location provider)
    // 3: timed out
  };



let  getlocationUids = (latitude,longitude)=> {
	console.log(latitude)
	console.log(longitude)
	console.log(parseFloat(latitude+0.008))
	console.log( parseFloat(longitude+0.008))
  // latitude= 32.719736
  // longitude = -117.162087
    $.ajax({
    type: "POST",
    dataType: "json",
    url : "getAllTrafficLocationswithinbbox",
    data : {
      "bbox":parseFloat(latitude+0.008) + ':' + parseFloat(longitude+0.008) + ','+ parseFloat(latitude-0.008) + ':' + parseFloat(longitude-0.008)
    },
    success : function(data) {
  // console.log(data);
 getNearestParking(latitude,longitude,data);

    },
     error: function(jqXHR, textStatus, errorThrown){
  alert(textStatus);
  }
  });
}


let  getNearestParking = (latitude,longitude,data)=> {

  let locations = []
  let coordinates = []
      data.content.forEach(items=>{
     locations.push(items.locationUid)
});

  locationdetails = JSON.stringify(locations)
  
    $.ajax({
    type: "POST",
   dataType: "json",
    cache: false,
    url : "getnearestparking",
    data :locationdetails,
    	
    contentType:"application/json; charset=utf-8",
    success : function(d) {
console.log(d)

d.forEach(item=>{
	coordinates.push({lat:parseFloat(item.x),lng:parseFloat(item.y)})
})

 changeMapLocation(latitude,longitude,coordinates)

    },
     error: function(jqXHR, textStatus, errorThrown){
    	 console.log(errorThrown)
  alert(textStatus);
// alert(errorThrown)
console.log(jqXHR.responseText)
  }
  });



// console.log(coordinates)

// latitude= 32.715736
// longitude = -117.161087


}





function changeMapLocation(latitude,longitude,l) {
console.log("I am in change Map location")
     var uluru = {lat: latitude, lng: longitude};
     var map = new google.maps.Map(document.getElementById('map'), {
       zoom: 23,
       center: uluru,
       mapTypeId: 'satellite'
     });

     var marker = new google.maps.Marker({
       position: uluru,
     // icon: 'marker.png',
       map:map
       
     });

//console.log("Size of list is "+l.length)

l.forEach(item=>{
	console.log(" I am in foreach")
  var marker = new google.maps.Marker({
    position: item,
    map:map
  });
// marker.setMap(map)
})




   }

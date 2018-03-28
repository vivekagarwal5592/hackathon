window.onload = function() {
  var startPos;
  var geoOptions = {
     timeout: 10 * 1000
  }

  var geoSuccess = function(position) {
    startPos = position;
    console.log(position)
    console.log(startPos.coords.latitude)
      console.log(startPos.coords.longitude)
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

  navigator.geolocation.getCurrentPosition(geoSuccess, geoError, geoOptions);
};



let  getlocationUids = (latitude,longitude)=> {
  latitude= 32.715736
  longitude = -117.161087
    $.ajax({
    type: "POST",
    dataType: "json",
    url : "getAllLocationswithinbbox",
    data : {
      "bbox":parseFloat(latitude+0.05) + ':' + parseFloat(longitude+0.05) + ','+ parseFloat(latitude-0.05) + ':' + parseFloat(longitude-0.05)
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

    $.ajax({
    type: "POST",
    dataType: "json",
    url : "getnearestparking",
    data :{
          locationdetails:locations
        },
        contentType:"application/json; charset=utf-8",
    success : function(d) {
console.log(d)

// changeMapLocation(latitude,longitude,coordinates)

    },
     error: function(jqXHR, textStatus, errorThrown){
    	 console.log(errorThrown)
  alert(textStatus);
  alert(errorThrown)
console.log(errorThrown)
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
       map:map
     });



l.forEach(item=>{
  var marker = new google.maps.Marker({
    position: item,
    map:map
  });
// marker.setMap(map)
})




   }

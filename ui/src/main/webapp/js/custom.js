$( function() {
  $( "#fromdate" ).datepicker();
  $( "#todate" ).datepicker();
//getlocationcoordinates(getlocationUids);
});

let  getlocationcoordinates = (callback)=> {

  // let startts = $('#fromdate').val();
  //  let endts = $('#todate').val();
  //
  //  console.log(startts)
  //   console.log(endts)

let geocoder = new google.maps.Geocoder;
    geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego' /*$('#current_position').val() */}, function(results, status) {
  if (status == google.maps.GeocoderStatus.OK) {
   let  latitude = results[0].geometry.location.lat();
     longitude = results[0].geometry.location.lng();
   callback(latitude,longitude)
 }
});
};
  let  getlocationUids = (latitude,longitude)=> {
      $.ajax({
      type: "POST",
      dataType: "json",
      url : "http://localhost:9001/getAllLocationswithinbbox",
      data : {
        "bbox":parseFloat(latitude+0.1) + ':' + parseFloat(longitude+0.1) + ','+ parseFloat(latitude-0.1) + ':' + parseFloat(longitude-0.1)

      },
      success : function(data) {
        console.log(data)
        i=0
    //       $(".locations").remove()
        data.content.forEach(item=>{
           $(".locations").append(`<a onclick='parkingdetails("${item.locationUid}")'>Location ${i} </a><br />`);
        //    $(".locations").append(`<a onclick='locationdetails("${item.locationUid}")'>Location ${i} ${i} </a><br />`);
           i +=1;
          //  console.log(item.locationUid)
        })
      },
       error: function(jqXHR, textStatus, errorThrown){
    alert(textStatus);
    }
    });
}

let  parkingdetails = (locationUid)=> {

   let startts = $('#fromdate').val();
    let endts = $('#todate').val();

  $.ajax({
  type: "POST",
  dataType: "json",
  url : "http://localhost:9001/getPKINForLastTendays",
  data : {
  parking_loc: locationUid,
startts:startts,
endts:endts
  },
  success : function(data) {
$(".parkingdetails").append(`<span class='element1'>Number of violations:${data.timeOverTwoHrs} </span><br />`);
$(".parkingdetails").append(`<span class='element2'>Number of Cars spotted:${data.totalNumberOfCars} </span><br />`);
  },
   error: function(jqXHR, textStatus, errorThrown){
alert(textStatus);
}
});

locationdetails(locationUid)


}

let  locationdetails = (locationUid)=> {
$.ajax({
type: "POST",
dataType: "json",
url : "http://localhost:9001/getSingleLocationDetails",
data : {
locationuid: locationUid
},
success : function(data) {

console.log(data)
list = getcoordinates(data.coordinates)
changeMapLocation(data.x_coordinate,data.y_coordinate,list)
},
 error: function(jqXHR, textStatus, errorThrown){
alert(textStatus);
}
});
}

let centroid = (coordinates) =>{}

let getcoordinates = (coordinates) =>{

 let xy = coordinates.split(",")

let list = []
 xy.forEach(items=>{
   val = items.split(":")
   list.push({lat:parseFloat(val[0]),lng:parseFloat(val[1])})
 })

//console.log(list)
return list;
}

$("select" )
.change(function () {
var str = "";
$( "select option:selected" ).each(function() {
  str += $( this ).text() + " ";
  console.log(str)
});

})
.change();


function changeMapLocation(latitude,longitude,list) {

console.log(latitude)
console.log(longitude)
     var uluru = {lat: latitude, lng: longitude};
     var map = new google.maps.Map(document.getElementById('map'), {
       zoom: 20,
       center: uluru
     });

     // var line = new google.maps.Polyline({
     //     path: [new google.maps.LatLng(latitude+1,longitude+1), new google.maps.LatLng(latitude-10,longitude-10)],
     //     strokeColor: "#FF0000",
     //     strokeOpacity: 1.0,
     //     strokeWeight: 10,
     //  //   geodesic: true,
     //  //   map: map
     // });
     //
     //   line.setMap(map);

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
$( function() {
  $( "#fromdate" ).datepicker();
  $( "#todate" ).datepicker();
 
});



let  getlocationcoordinates = (callback)=> {


 let geocoder = new google.maps.Geocoder;
    geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego'}, function(results, status) {
  if (status == google.maps.GeocoderStatus.OK) {
   let  latitude = results[0].geometry.location.lat();
     longitude = results[0].geometry.location.lng();
  // markarea(latitude,longitude)
   callback(latitude,longitude)
 }
});
};
  let  getlocationUids = (latitude,longitude)=> {
	  $(".locations").empty()
      $.ajax({
      type: "POST",
      dataType: "json",
      url : "getAllTrafficLocationswithinbbox",
      data : {
        "bbox":parseFloat(latitude+0.02) + ':' + parseFloat(longitude+0.02) + ','+ parseFloat(latitude-0.02) + ':' + parseFloat(longitude-0.02)

      },
      success : function(data) {
  // i=0
        
        data.content.forEach(item=>{
        	
        	 let pDetails = []
 	        data.content.forEach(item=>{
 	        	pDetails.push(locationdetails(item))
 	         })  
 	        
 	         changeMapLocationLoop(pDetails,latitude,longitude)
        	
       // $(".locations").append(`<a
		// onclick='trafficdetails("${item.locationUid}")'>Traffic Lane ${i}
		// </a><br />`);
         // i +=1;
        })
      },
       error: function(jqXHR, textStatus, errorThrown){
    alert(textStatus);
    }
    });
}

let  trafficdetails = (locationUid)=> {

   let startts = $('#fromdate').val() + " "+ $('#fromtime').val() ;
    let endts = $('#todate').val() + " " +  $('#totime').val() ;

    if($('#fromdate').val()  == '' || $('#todate').val() ==''){
    	alert("Please enter the date and the time")
    }
    else{
    	
  $.ajax({
  type: "POST",
  dataType: "json",
  url : "getTrafficForLastTendays",
  data : {
  traffic_loc: locationUid,
startts:startts,
endts:endts
  },
  success : function(data) {

if(data !=null){
	renderChart(data.numberOfCarsSpotted)
	document.getElementById("e1").innerHTML = data.noOfVehivles;
	
}

  },
   error: function(jqXHR, textStatus, errorThrown){
alert("No data for your search");
}
});

// locationdetails(locationUid)

}
}

let  locationdetails = (ldetails)=> {

let list = getcoordinates(ldetails.coordinates)
let xy = getcentroid(list)

 return {'list':list,'latitude':xy.x,'longitude':xy.y,'locationUid':ldetails.locationUid}

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

/*
 * $("select" ) .change(function () { var str = ""; $( "select option:selected"
 * ).each(function() { str += $( this ).text() + " "; console.log(str) }); })
 * .change();
 */


function changeMapLocation(latitude,longitude,list) {

     var uluru = {lat: latitude, lng: longitude};
     var map = new google.maps.Map(document.getElementById('map2'), {
       zoom: 23,
       center: uluru,
       mapTypeId: 'satellite'
     });

     var marker = new google.maps.Marker({
       position: uluru,
     });

// marker.setMap(map)
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
	   console.log("I am trying to egt address")
     let geocoder = new google.maps.Geocoder;
     var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
         geocoder.geocode( {'location': latlng}, function(results, status) {

       if (status == google.maps.GeocoderStatus.OK) { 
          callback(results[0].formatted_address)
      }
     });
   }

   function renderChart(data) {

// console.log(data)
let list = []
for(key in data){
  list.push({label:key,y:data[key]})
}

console.log(list)



var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2",
	title:{
		text: "Traffic Statistics"
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
	    		  trafficdetails(marker.locationUid)
	    	      	getaddressForGeoCode(marker.x,marker.y,function(result){
	    	      	document.getElementById("e3").innerHTML = result;
	    	      	changeMapLocation(marker.x,marker.y,items.list) 
	    	      		})
	    	         });
	          
	          marker.setMap(map) 
	      })
	     
	     
	  }
  

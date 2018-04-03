
let cards = new Vue({
    el: '#locationSummary',
    data: {
        appName: 'Location Analysis',
        total_vehicles: '',
        total_parked: '',
        startts : '',
        endts: '',
         googlemap : document.getElementById('map'),
         options :{
           zoom: 19,
           center:{lat: 32.7157, lng: -117.1611},
           mapTypeId: 'satellite'
         },
         selectedlocation : '',
         locations:[
           'Balboa Park',
           'Bankers Hill',
           'Barrio Logan',
           'Bay Park',
           'Birdland',
           'Sherman Heights',
           'Wooded Area',
           'Sorrento Valley',
           'Palm City'
         ]

    },
    methods: {
      getlocationcoordinates :function (callback){
    	  console.log("I am in in get location coordinates")
         let vm = this
      let geocoder = new google.maps.Geocoder;
         geocoder.geocode( { 'address': $( ".city" ).val() + 'San Diego' /* $('#current_position').val() */}, function(results, status) {
       if (status == google.maps.GeocoderStatus.OK) {
        let  latitude = results[0].geometry.location.lat();
          longitude = results[0].geometry.location.lng();
          
          console.log(latitude )
       callback(latitude,longitude)
      }
     });
   },

     getLocationSummary :function (latitude,longitude){
       let vm = this
    	 
    	 var config = {
    			 headers: {
    			    'Content-Type': 'application/x-www-form-urlencoded'
    			 }
    			 };
    	 
    	 
       axios.post('getlocationsummary',
    	   {
    	   startts:vm.startts,
           endts:vm.endts,
           bbox: ''
    	   }  ,
    	   config
             
    		  
       )
       .then(data => {
    	   console.log(data.total_vehicles)
        vm.total_vehicles = data.total_vehicles;
        vm.total_parked = data.total_cars_parked;

         list = getcoordinates(data.most_traffic_prone_location)
         coordinates =  getcentroid(list)
        changeMapLocation(coordinates.x,coordinates.y,list)

       }).catch(function(error) {
    	   console.log(error);
       });
     },


      getaddressForGeoCode : function(latitude,longitude,callback) {
       let geocoder = new google.maps.Geocoder;
       var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
           geocoder.geocode( {'location': latlng}, function(results, status) {

         if (status == google.maps.GeocoderStatus.OK) {
    callback(results[0].formatted_address)
        }
       });
     },


    getcoordinates :function (coordinates){
     let xy = coordinates.split(",")
     let list = []
      xy.forEach(items=>{
        val = items.split(":")
        list.push({lat:parseFloat(val[0]),lng:parseFloat(val[1])})
      })

     // console.log(list)
     return list;
   },



    changeMapLocation: function(latitude,longitude,list) {
       let vm = this
       const map = new google.maps.Map(vm.googlemap, vm.options);

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
      },

      initialize : function (){
    	  console.log("In initialize")
        $( "#fromdate" ).datepicker();
        $( "#todate" ).datepicker();
      }

    },
     created: function(){
        this.initialize()
    }
})

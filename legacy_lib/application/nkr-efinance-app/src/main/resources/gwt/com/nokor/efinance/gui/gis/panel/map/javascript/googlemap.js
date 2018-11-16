var gmap = gmap || {};
var me;
var _map;
var _lat;
var _lng;
var _zoomLevel;
var _markerObjs = [];
var _markers = [];
var _firstLoad;

gmap.GoogleMap = function(element, width, height) {
    this.element = element;
    this.element.innerHTML = "<div id='map-canvas' style='width:"+ width +"; height:"+ height +"'></div>";
    _firstLoad = true;
   
    me = this;
    
    me.setLatLng = function (lat, lng) {
        _lat = lat;
        _lng = lng;
    };
    
    me.setZoomLevel = function (zoomLevel) {
    	_zoomLevel = zoomLevel;
    };
    
    me.setMarkers = function (markers) {
    	clearMarkers();
    	_markers = markers;
    	
    	if (!_firstLoad && _markers.length > 0) {
    		createMarkers();
        }
    };

    $(function() {       
        window.initialize = function() {
        	var myLatlng = new google.maps.LatLng(_lat,_lng);
            var mapOptions = {
                zoom : _zoomLevel,
                center : myLatlng,
                panControl: false,
                zoomControl: false,
                scaleControl: false,
                mapTypeControl: false,
                mapTypeId : google.maps.MapTypeId.ROADMAP
            };

            _map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
            
            if (_markers.length > 0) {
            	createMarkers();
            }

            google.maps.event.addListener(_map, 'click', function() {
                me.click();
            });
        };

        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&'
                + 'callback=initialize';
        document.body.appendChild(script);
        
        _firstLoad = false;
    });
}

function clearMarkers() {
	for (var i = 0; i < _markerObjs.length; i++) {	
		_markerObjs[i].setMap(null);
	}
	_markerObjs = [];
}

function createMarkers() {
	var infowindow = new google.maps.InfoWindow();

	var shadow = {
	    url: 'images/beachflag_shadow.png',
	    size: new google.maps.Size(37, 32),
	    origin: new google.maps.Point(0,0),
	    anchor: new google.maps.Point(0, 32)
	};

	var shape = {
	      coord: [1, 1, 1, 20, 18, 20, 18 , 1],
	      type: 'poly'
	};

	for (var i = 0; i < _markers.length; i++) {
		var marker = eval('(' + _markers[i] + ')'); 	
		var contentString = '<div><ul style=\'margin:0; list-style-position:outside;\'>';
		contentString += '<li>' + marker.nbActivate.desc + ': ' + marker.nbActivate.numQuotation +'</li>';
		contentString += '<li>' + marker.nbReject.desc + ': ' + marker.nbReject.numQuotation +'</li>';
		contentString += '<li>' + marker.nbDecline.desc + ': ' + marker.nbDecline.numQuotation +'</li>';
		contentString += '<li>' + marker.nbProposal.desc + ': ' + marker.nbProposal.numQuotation +'</li>';
		contentString += '</ul></div>';

	    var myLatLng = new google.maps.LatLng(Number(marker.latitude), Number(marker.longitude));
	    var markerObj = new google.maps.Marker({
	        position: myLatLng,
	        map: _map,
	        //shadow: shadow,
	        //icon: icon,
	        shape: shape,
	        title: marker.title,
	        content: contentString,
	        zIndex: (i+1)
	    });
	    
	    if (marker.iconUrl != '') {
	    	var icon = {
				    url: marker.iconUrl,
				    // This marker is 20 pixels wide by 32 pixels tall.
				    size: new google.maps.Size(32, 32),
				    // The origin for this image is 0,0.
				    origin: new google.maps.Point(0,0),
				    // The anchor for this image is the base of the flagpole at 0,32.
				    anchor: new google.maps.Point(0, 32)
				};
	    	markerObj.setIcon(icon);
	    }
	    
	    _markerObjs.push(markerObj);
	    
	    google.maps.event.addListener(markerObj, 'click', function() {
	    	infowindow.setContent(this.content);
	    	infowindow.open(_map, this);
	    	me.markerClick(this.getZIndex() - 1);
	    });
	    
	    google.maps.event.addListener(markerObj, 'dblclick', function() {
	    	me.markerDoubleClick(this.getZIndex() - 1);
	    });
	}	
}

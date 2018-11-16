window.com_nokor_efinance_front_ui_panel_map_GoogleMap =
function() {
    // Create the component
    var gMap = new gmap.GoogleMap(this.getElement(), "1130px", "700px");
   
    // Handle changes from the server-side
    this.onStateChange = function() {
        gMap.setLatLng(this.getState().latitude, this.getState().longitude);
        gMap.setMarkers(this.getState().markers);
        gMap.setZoomLevel(this.getState().zoomLevel);
    };
   
    // Pass user interaction to the server-side
    var connector = this;
    gMap.markerClick = function(index) {
        connector.onMarkerClick(index);
    };
    
    gMap.markerDoubleClick = function(index) {
        connector.onMarkerDoubleClick(index);
    };
    
    gMap.click = function() {
        connector.onClick();
    };
};
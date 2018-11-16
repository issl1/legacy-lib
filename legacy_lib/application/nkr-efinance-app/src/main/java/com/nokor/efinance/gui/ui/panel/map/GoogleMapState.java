package com.nokor.efinance.gui.ui.panel.map;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * @author ly.youhort
 */
public class GoogleMapState extends JavaScriptComponentState {

	private static final long serialVersionUID = -4651459348078053486L;

	private double latitude;
	private double longitude;
	private int zoomLevel;
	private String[] markers;

	public GoogleMapState() {
		zoomLevel = 14;
		markers = new String[] {};
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public String[] getMarkers() {
		return markers;
	}

	public void setMarkers(String[] markers) {
		this.markers = markers;
	}

}

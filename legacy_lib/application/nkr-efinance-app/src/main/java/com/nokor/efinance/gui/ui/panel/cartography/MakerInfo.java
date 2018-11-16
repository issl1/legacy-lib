package com.nokor.efinance.gui.ui.panel.cartography;

import java.util.List;

import com.vaadin.tapio.googlemaps.client.LatLon;

public class MakerInfo {
	private LatLon latLon;
	private String desc;
	/**
	 * @return the latLon
	 */
	public LatLon getLatLon() {
		return latLon;
	}
	/**
	 * @param latLon the latLon to set
	 */
	public void setLatLon(LatLon latLon) {
		this.latLon = latLon;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static String getDesc(List<MakerInfo> makerInfos, LatLon latLon) {
		if (makerInfos != null && !makerInfos.isEmpty()) {
			for (MakerInfo makerInfo : makerInfos) {
				if (makerInfo.getLatLon() == latLon) {
					return makerInfo.getDesc();
				}
			}
		}
		return "";
	}
}

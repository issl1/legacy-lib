package com.nokor.efinance.gui.ui.panel.map.model;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Marker implements Serializable {

	private static final long serialVersionUID = -4805152229560752856L;
	
	private Long id;
	private String title;
	private double latitude;
	private double longitude;
	private String iconUrl;
	private int zIndex;
	
	private MarkerInfo nbActivate;
	private MarkerInfo nbReject;
	private MarkerInfo nbDecline;
	private MarkerInfo nbProposal;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	/**
	 * @return the zIndex
	 */
	public int getzIndex() {
		return zIndex;
	}
	/**
	 * @param zIndex the zIndex to set
	 */
	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	/**
	 * @return the nbActivate
	 */
	public MarkerInfo getNbActivate() {
		return nbActivate;
	}
	/**
	 * @param nbActivate the nbActivate to set
	 */
	public void setNbActivate(MarkerInfo nbActivate) {
		this.nbActivate = nbActivate;
	}
	/**
	 * @return the nbReject
	 */
	public MarkerInfo getNbReject() {
		return nbReject;
	}
	/**
	 * @param nbReject the nbReject to set
	 */
	public void setNbReject(MarkerInfo nbReject) {
		this.nbReject = nbReject;
	}
	/**
	 * @return the nbDecline
	 */
	public MarkerInfo getNbDecline() {
		return nbDecline;
	}
	/**
	 * @param nbDecline the nbDecline to set
	 */
	public void setNbDecline(MarkerInfo nbDecline) {
		this.nbDecline = nbDecline;
	}
	/**
	 * @return the nbProposal
	 */
	public MarkerInfo getNbProposal() {
		return nbProposal;
	}
	/**
	 * @param nbProposal the nbProposal to set
	 */
	public void setNbProposal(MarkerInfo nbProposal) {
		this.nbProposal = nbProposal;
	}
	
}

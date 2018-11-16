package com.nokor.common.messaging.share;


/**
 * 
 * @author prasnar
 *
 */
public class UriDTO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = -3019235173233793289L;
	
	private String uri;
	
	/**
	 * Default constructor
	 * Require by WS
	 */
	public UriDTO() {
		
	}
	
	/**
	 * @param id
	 * @param uri
	 */
	public UriDTO(Long id, String uri) {
		this.id = id;
		this.uri = uri;
	}
	
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
}

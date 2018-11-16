package com.nokor.common.messaging.share;


/**
 * 
 * @author prasnar
 *
 */
public class ParamUriDTO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = -3019235173233793289L;
	
	private String uri;
	
	/**
	 * Default constructor
	 * Require by WS
	 */
	public ParamUriDTO() {
		
	}
	
	/**
	 * @param id
	 * @param uri
	 */
	public ParamUriDTO(Long id, String code, String desc, String uri) {
		this.id = id;
		this.code = code;
		this.desc = desc;
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

package com.nokor.efinance.share.document;

import java.io.Serializable;

/**
 * @author youhort.ly
 *
 */
public class DocumentDTO implements Serializable {

	/** 
	 */
	private static final long serialVersionUID = -6454750835552553290L;
	
	private Long id;
	private String code;

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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof DocumentDTO)) {
			 return false;
		 }
		 DocumentDTO documentDTO = (DocumentDTO) arg0;
		 return getId() != null && getId().equals(documentDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}

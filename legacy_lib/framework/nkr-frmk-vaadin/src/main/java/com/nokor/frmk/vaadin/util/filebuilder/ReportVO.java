package com.nokor.frmk.vaadin.util.filebuilder;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created on 03/09/2015
 * @author phirun.kong
 *
 */
public class ReportVO implements Serializable {

	private BigInteger id;
	
	public ReportVO() {
		
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

}

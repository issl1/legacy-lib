package com.nokor.frmk.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author prasnar
 *
 */
@XmlRootElement(name = "refTable")
@XmlAccessorType (XmlAccessType.FIELD)
public class RefTableVO implements Serializable {
	/** */
	private static final long serialVersionUID = 6831591073296948815L;
	
	protected Long id;
	protected String code;
	@XmlElement(name = "refData")
	protected List<RefDataVO> refDatas;
	
	/**
	 * 
	 */
	public RefTableVO() {
		
	}

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
	 * @return the refDatas
	 */
	public List<RefDataVO> getRefDatas() {
		return refDatas;
	}

	/**
	 * @param refDatas the refDatas to set
	 */
	public void setRefDatas(List<RefDataVO> refDatas) {
		this.refDatas = refDatas;
	}

	
	
}

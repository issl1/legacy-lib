package com.nokor.efinance.share.template;

import java.io.Serializable;

/**
 * 
 * @author buntha.chea
 *
 */
public class ELetterTemplateDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -5194251679102948579L;
	
	private Long id;
	private String code;
	private String descEn;
	private String desc;
	private String content;
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
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}
	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
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
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ELetterTemplateDTO)) {
			 return false;
		 }
		 ELetterTemplateDTO letterTemplateDTO = (ELetterTemplateDTO) arg0;
		 return getId() != null && getId().equals(letterTemplateDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}

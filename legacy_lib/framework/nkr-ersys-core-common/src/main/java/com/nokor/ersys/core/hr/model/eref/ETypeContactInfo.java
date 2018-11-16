package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author prasnar
 *
 */
public class ETypeContactInfo extends BaseERefData implements AttributeConverter<ETypeContactInfo, Long> {
	
	/** */
	private static final long serialVersionUID = 6259213633442268089L;
	
	public final static ETypeContactInfo LANDLINE = new ETypeContactInfo("LANDLINE", 1, true);
	public final static ETypeContactInfo FAX = new ETypeContactInfo("FAX", 2, false);
	public final static ETypeContactInfo EMAIL = new ETypeContactInfo("EMAIL", 3, false);
	public final static ETypeContactInfo SKYPE = new ETypeContactInfo("SKYPE", 4, false);
	public final static ETypeContactInfo GMAIL = new ETypeContactInfo("GMAIL", 5, false);
	public final static ETypeContactInfo YAHOO = new ETypeContactInfo("YAHOO", 6, false);
	public final static ETypeContactInfo LINKEDIN = new ETypeContactInfo("LINKEDIN", 7, false);
	public final static ETypeContactInfo TWITTER = new ETypeContactInfo("TWITTER", 8, false);
	public final static ETypeContactInfo FACEBOOK = new ETypeContactInfo("FACEBOOK", 9, false);
	public final static ETypeContactInfo MOBILE = new ETypeContactInfo("MOBILE", 10, false);
	
	private boolean linkTypeAddress;
	
	/**
	 * 
	 */
	public ETypeContactInfo() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeContactInfo(String code, long id, boolean linkTypeAddress) {
		super(code, id);
		this.linkTypeAddress = linkTypeAddress;
	}

	@Override
	public ETypeContactInfo convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETypeContactInfo arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ETypeContactInfo> values() {
		return getValues(ETypeContactInfo.class);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ETypeContactInfo getById(long id) {
		return getById(ETypeContactInfo.class, id);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ETypeContactInfo getByCode(String code) {
		return getByCode(ETypeContactInfo.class, code);
	}
		
	/**
	 * @return the linkTypeAddress
	 */
	public boolean isLinkTypeAddress() {
		return linkTypeAddress;
	}

	/**
	 * @param linkTypeAddress the linkTypeAddress to set
	 */
	public void setLinkTypeAddress(boolean linkTypeAddress) {
		this.linkTypeAddress = linkTypeAddress;
	}	
}
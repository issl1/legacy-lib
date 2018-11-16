package org.seuksa.frmk.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

/**
 * Is used to group RefData ou EntityRef
 * 
 * @author prasnar
 *
 */
public class EGroup extends SimpleERefData implements AttributeConverter<EGroup, Long> {
	/** */
	private static final long serialVersionUID = -2708745992451417068L;
	
	// For ECountry
	public final static EGroup CTRY_NATIO = new EGroup("CTRY_NATIO", 1); 
	public final static EGroup CTRY_ADDR = new EGroup("CTRY_ADDR", 2); 
	public final static EGroup CTRY_BIRT = new EGroup("CTRY_BIRT", 3); 
	public final static EGroup CTRY_CURR = new EGroup("CTRY_CURR", 4);
	public final static EGroup CTRY_LANG_INT = new EGroup("CTRY_LANG_INT", 5);
	public final static EGroup CTRY_LANG_CNT = new EGroup("CTRY_LANG_CNT", 6);
	
	// ECusDomainTarget
	public final static EGroup CDOM_USER = new EGroup("CDOM_USER", 10); 
	public final static EGroup CDOM_EMPLOYEE = new EGroup("CDOM_EMPLOYEE", 11); 
	public final static EGroup CDOM_CONTRACT = new EGroup("CDOM_CONTRACT", 12); 
	public final static EGroup CDOM_MEMBER = new EGroup("CDOM_MEMBER", 13); 
	public final static EGroup CDOM_CONTENT = new EGroup("CDOM_CONTENT", 14); 
	public final static EGroup CDOM_CONTENT_TAG = new EGroup("CDOM_CONTENT_TAG", 15); 
	public final static EGroup CDOM_DOCCONTENT = new EGroup("CDOM_DOCCONTENT", 16); 
	public final static EGroup CDOM_CMSCONTENT = new EGroup("CDOM_CMSCONTENT", 17); 

	public final static EGroup PEOPLE = new EGroup("PEOPLE", 20); 
	public final static EGroup ORGANIZATION = new EGroup("ORGANIZATION", 21); 
	public final static EGroup PROJECT = new EGroup("PROJECT", 22); 
	public final static EGroup DOCUMENT = new EGroup("DOCUMENT", 23); 
	public final static EGroup TEXT = new EGroup("TEXT", 24); 
	public final static EGroup CMS = new EGroup("CMS", 25); 
	public final static EGroup COMMENT = new EGroup("COMMENT", 26); 
	public final static EGroup EVENT = new EGroup("EVENT", 27); 
	public final static EGroup FORUM = new EGroup("FORUM", 28); 

	/**
	 * 
	 */
	public EGroup() {
		loadFromXml = true;
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EGroup(String code, long id) {
		super(code, id);
	}
	
	@Override
	public EGroup convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EGroup arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EGroup getById(long id) {
		return SimpleERefData.getById(EGroup.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EGroup getByCode(String code) {
		return SimpleERefData.getByCode(EGroup.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EGroup valueOf(String code) {
		return SimpleERefData.valueOf(EGroup.class, code);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EGroup> values() {
		return SimpleERefData.getValues(EGroup.class);
	}
	
	
	/**
	 * 
	 */
	public String toString() {
		return super.toString();
	}

}
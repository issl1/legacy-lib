package com.nokor.efinance.core.document.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

import com.nokor.frmk.security.model.SecProfile;

/**
 * Confirm Evidence
 * 
 * @author ly.youhort
 */
public class EConfirmEvidence extends BaseERefData implements AttributeConverter<EConfirmEvidence, Long> {
	/** */
	private static final long serialVersionUID = 2905077918632306528L;

	private SecProfile profile;
	
	/**
	 * 
	 */
	public EConfirmEvidence() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EConfirmEvidence(String code, long id) {
		super(code, id);
	}

	/**
	 * @return the profile
	 */
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EConfirmEvidence convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EConfirmEvidence arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EConfirmEvidence> values() {
		return getValues(EConfirmEvidence.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EConfirmEvidence getByCode(String code) {
		return getByCode(EConfirmEvidence.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EConfirmEvidence getById(long id) {
		return getById(EConfirmEvidence.class, id);
	}
}

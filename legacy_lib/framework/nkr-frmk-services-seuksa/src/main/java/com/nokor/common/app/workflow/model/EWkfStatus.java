package com.nokor.common.app.workflow.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.eref.BaseERefEntity;
import org.seuksa.frmk.model.eref.SimpleERefData;


/**
 * List of Default WkfStatus 
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="ts_wkf_status")
public class EWkfStatus extends BaseERefEntity implements AttributeConverter<EWkfStatus, Long> {
	/** */
	private static final long serialVersionUID = -8367575054913304391L;

	public final static EWkfStatus NEW = new EWkfStatus("NEW", 1); 
	public final static EWkfStatus ASS_V = new EWkfStatus("ASS_V", 2); 
	public final static EWkfStatus REOPE = new EWkfStatus("REOPE", 3); 
	public final static EWkfStatus VERIF = new EWkfStatus("VERIF", 4); 
	public final static EWkfStatus ASS_A = new EWkfStatus("ASS_A", 5); 
	public final static EWkfStatus APPRO = new EWkfStatus("APPRO", 6); 
	public final static EWkfStatus REFUS = new EWkfStatus("REFUS", 7); 
	public final static EWkfStatus CANCE = new EWkfStatus("CANCE", 8);
	public final static EWkfStatus SUBMI = new EWkfStatus("SUBMI", 9);
	public final static EWkfStatus ACCEP = new EWkfStatus("ACCEP", 10);
	public final static EWkfStatus VALID = new EWkfStatus("VALID", 11);
	public final static EWkfStatus ACTIV = new EWkfStatus("ACTIV", 12);

    public static final List<EWkfStatus> WKF_NOT_FINAL_STATUS_LIST = Arrays.asList(EWkfStatus.NEW, EWkfStatus.ASS_V, EWkfStatus.REOPE, EWkfStatus.VERIF, EWkfStatus.ASS_A);
    public static final List<EWkfStatus> WKF_FINAL_STATUS_LIST = Arrays.asList(EWkfStatus.APPRO, EWkfStatus.REFUS, EWkfStatus.CANCE);

    
	/**
	 * 
	 */
	public EWkfStatus() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EWkfStatus(String code, long id) {
		super(code, id);
	}

	@Override
	public EWkfStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EWkfStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_sta_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EWkfStatus> values() {
		return getValues(EWkfStatus.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EWkfStatus getById(long id) {
		return getById(EWkfStatus.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EWkfStatus getByCode(String code) {
		return getByCode(EWkfStatus.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EWkfStatus valueOf(String code) {
		return valueOf(EWkfStatus.class, code);
	}
	
	/**
	 * 
	 * @param listStr
	 * @return
	 */
	public static List<EWkfStatus> fetchValues(String listStr) {
		return SimpleERefData.fetchValuesFromStringList(EWkfStatus.class, listStr);
	}
	
	
}
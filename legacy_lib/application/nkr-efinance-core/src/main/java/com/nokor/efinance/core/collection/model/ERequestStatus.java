package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class ERequestStatus extends BaseERefData implements AttributeConverter<ERequestStatus, Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 802132552520391454L;
	
	public final static ERequestStatus PENDING = new ERequestStatus("PENDING", 1);
	public final static ERequestStatus APPROVE = new ERequestStatus("APPROVE", 2);
	public final static ERequestStatus REJECT = new ERequestStatus("REJECT", 3);
	public final static ERequestStatus ASSIGN = new ERequestStatus("ASSIGN", 4);

	/**
	 */
	public ERequestStatus() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ERequestStatus(String code, long id) {
		super(code, id);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_sta_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ERequestStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ERequestStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ERequestStatus> values() {
		return getValues(ERequestStatus.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERequestStatus getByCode(String code) {
		return getByCode(ERequestStatus.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ERequestStatus getById(long id) {
		return getById(ERequestStatus.class, id);
	}
	
}

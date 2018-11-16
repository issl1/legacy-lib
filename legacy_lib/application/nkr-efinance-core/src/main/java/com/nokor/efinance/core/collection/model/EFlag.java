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
 * @author prasnar
 *
 */
public class EFlag extends BaseERefData implements AttributeConverter<EFlag, Long> {
	
	/** */
	private static final long serialVersionUID = 1926023334544670059L;
	
	public final static EFlag LOST = new EFlag("F001", 1);
	public final static EFlag DAMAGE = new EFlag("F002", 2);
	public final static EFlag SEIZED = new EFlag("F003", 3);
	public final static EFlag DEAD = new EFlag("F004", 4);
	public final static EFlag RETURN = new EFlag("F005", 5);
	public final static EFlag REPO = new EFlag("F006", 6);
	
	/**
	 */
	public EFlag() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EFlag(String code, long id) {
		super(code, id);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EFlag convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFlag arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EFlag> values() {
		return getValues(EFlag.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EFlag getByCode(String code) {
		return getByCode(EFlag.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EFlag getById(long id) {
		return getById(EFlag.class, id);
	}

}
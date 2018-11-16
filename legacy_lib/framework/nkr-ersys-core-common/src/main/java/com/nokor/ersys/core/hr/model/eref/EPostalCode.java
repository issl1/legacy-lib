package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.eref.BaseERefEntity;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name="tu_postal_code")
public class EPostalCode extends BaseERefEntity implements AttributeConverter<EPostalCode, Long> {
	/** */
	private static final long serialVersionUID = -5834296675072520211L;

	/**
	 */
	public EPostalCode() {
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pos_cod_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPostalCode(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPostalCode convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPostalCode arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPostalCode> values() {
		return getValues(EPostalCode.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPostalCode getByCode(String code) {
		return getByCode(EPostalCode.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPostalCode getById(long id) {
		return getById(EPostalCode.class, id);
	}

}

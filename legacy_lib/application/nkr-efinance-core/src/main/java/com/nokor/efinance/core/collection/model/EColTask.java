package com.nokor.efinance.core.collection.model;

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
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_col_task")
public class EColTask extends BaseERefEntity implements AttributeConverter<EColTask, Long> {
	/** */
	private static final long serialVersionUID = 1926023334544670059L;

	/**
	 * 
	 */
	public EColTask() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EColTask(String code, long id) {
		super(code, id);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EColTask convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EColTask arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EColTask> values() {
		return getValues(EColTask.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EColTask getByCode(String code) {
		return getByCode(EColTask.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EColTask getById(long id) {
		return getById(EColTask.class, id);
	}

}
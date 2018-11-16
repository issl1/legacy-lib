package com.nokor.frmk.security.model;

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
@Table(name = "ts_sec_privilege")
public class ESecPrivilege extends BaseERefEntity implements AttributeConverter<ESecPrivilege, Long> {
	/** */
	private static final long serialVersionUID = 4009721751713377021L;

	public static final ESecPrivilege EXECUTE = new ESecPrivilege(1L);
	public static final ESecPrivilege READ = new ESecPrivilege(2L);
	public static final ESecPrivilege WRITE = new ESecPrivilege(3L);

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pri_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * 
	 */
	public ESecPrivilege() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecPrivilege(long id) {
		super(null, id);
	}

	@Override
	public ESecPrivilege convertToEntityAttribute(Long id) {
		return ((ESecPrivilege) super.convertToEntityAttribute(id));
	}
	
	@Override
	public Long convertToDatabaseColumn(ESecPrivilege arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESecPrivilege> values() {
		return getValues(ESecPrivilege.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecPrivilege getById(long id) {
		return getById(ESecPrivilege.class, id);
	}
	
}

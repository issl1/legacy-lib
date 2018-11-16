package com.nokor.efinance.core.history;

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
 * @author uhout.cheng
 */
@Entity
@Table(name="ts_fin_history_type")
public class FinHistoryType extends BaseERefEntity implements AttributeConverter<FinHistoryType, Long> {

	/** */
	private static final long serialVersionUID = 8327623555728639647L;
	
	public final static FinHistoryType FIN_HIS_CNT = new FinHistoryType("FIN_HIS_CNT", 1l);
	public final static FinHistoryType FIN_HIS_CMT = new FinHistoryType("FIN_HIS_CMT", 2l);
	public final static FinHistoryType FIN_HIS_REM = new FinHistoryType("FIN_HIS_REM", 3l);
	public final static FinHistoryType FIN_HIS_SMS = new FinHistoryType("FIN_HIS_SMS", 4l);
	public final static FinHistoryType FIN_HIS_PAY = new FinHistoryType("FIN_HIS_PAY", 5l);
	public final static FinHistoryType FIN_HIS_LCK = new FinHistoryType("FIN_HIS_LCK", 6l);
	public final static FinHistoryType FIN_HIS_REQ = new FinHistoryType("FIN_HIS_REQ", 7l);
	public final static FinHistoryType FIN_HIS_UPD = new FinHistoryType("FIN_HIS_UPD", 8l);
	public final static FinHistoryType FIN_HIS_SYS = new FinHistoryType("FIN_HIS_SYS", 9l);

	/**
	 */
	public FinHistoryType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public FinHistoryType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinHistoryType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(FinHistoryType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fin_his_typ_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
		
	/**
	 * @return
	 */
	public static List<FinHistoryType> values() {
		return getValues(FinHistoryType.class);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static FinHistoryType getById(long id) {
		return getById(FinHistoryType.class, id);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static FinHistoryType getByCode(String code) {
		return getByCode(FinHistoryType.class, code);
	}

	
}
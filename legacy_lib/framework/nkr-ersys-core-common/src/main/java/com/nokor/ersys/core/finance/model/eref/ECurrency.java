package com.nokor.ersys.core.finance.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.eref.BaseERefEntity;

/**
 * Currency
 * @author ly.youhort
 *
 */
@Entity
@Table(name="tu_currency")
public class ECurrency extends BaseERefEntity implements AttributeConverter<ECurrency, Long> {
	/** */
	private static final long serialVersionUID = -2322438602807747655L;

	public final static ECurrency USD = new ECurrency("USD", 1);
	public final static ECurrency KHR = new ECurrency("KHR", 2);
	public final static ECurrency BAT = new ECurrency("BAT", 3);

	private String symbol;
	

	/**
	 * 
	 */
	public ECurrency() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECurrency(String code, long id) {
		super(code, id);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cur_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECurrency convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECurrency arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECurrency> values() {
		return getValues(ECurrency.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECurrency getByCode(String code) {
		return getByCode(ECurrency.class, code);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ECurrency getDefault() {
		return ECurrency.BAT;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECurrency getById(long id) {
		return getById(ECurrency.class, id);
	}
	
	/**
	 * @return the symbol
	 */
    @Column(name = "cur_symbol", nullable = false)
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the defaultCurrency
	 */
	@Transient
	public boolean isDefaultCurrency() {
		return getDefault().equals(this);
	}

	
}

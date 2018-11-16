package com.nokor.efinance.core.stock.model;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.eref.BaseERefEntity;

/**
 * Stock Movement Reason
 *  
 * @author ly.youhort
 *
 */
@Entity
@Table(name="tu_stock_reason")
public class EStockReason extends BaseERefEntity implements AttributeConverter<EStockReason, Long> {
	/** */
	private static final long serialVersionUID = -5973658713915739268L;

	public final static EStockReason INSIDE_DEALER = new EStockReason("INSIDE_DEALER", EStockMovement.OUT, 1); //inside.dealer
	public final static EStockReason IN_THE_STREET = new EStockReason("IN_THE_STREET", EStockMovement.OUT, 2); // in.the.street
	public final static EStockReason IN_FIELD_CHECK = new EStockReason("IN_FIELD_CHECK", EStockMovement.OUT, 3); // in.special.event
	public final static EStockReason IN_SPECIAL_EVENT = new EStockReason("IN_SPECIAL_EVENT", EStockMovement.OUT, 4); // distribution
	public final static EStockReason DISTRIBUTION = new EStockReason("DISTRIBUTION", EStockMovement.OUT, 5); // out.stock
	public final static EStockReason OUT_STOCK = new EStockReason("OUT_STOCK", EStockMovement.OUT, 6); // in.the.street
	public final static EStockReason DELIVERY = new EStockReason("DELIVERY", EStockMovement.IN, 7); // delivery
	public final static EStockReason IN_STOCK = new EStockReason("IN_STOCK", EStockMovement.IN, 8); // in.stock

    private EStockMovement stockMovement;

	/**
	 * 
	 */
	public EStockReason() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EStockReason(String code, EStockMovement stockMovement, long id) {
		super(code, id);
		this.stockMovement = stockMovement;
	}
	

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sto_rea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
		
	/**
	 * @return the stockMovement
	 */
    @Column(name = "sto_mvt_id", nullable = false)
    @Convert(converter = EStockMovement.class)
	public EStockMovement getStockMovement() {
		return stockMovement;
	}

	/**
	 * @param stockMovement the stockMovement to set
	 */
	public void setStockMovement(EStockMovement stockMovement) {
		this.stockMovement = stockMovement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EStockReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EStockReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EStockReason> values() {
		return getValues(EStockReason.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EStockReason getByCode(String code) {
		return getByCode(EStockReason.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EStockReason getById(long id) {
		return getById(EStockReason.class, id);
	}
}

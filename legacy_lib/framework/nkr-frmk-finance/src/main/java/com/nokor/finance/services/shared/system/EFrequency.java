package com.nokor.finance.services.shared.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Frequency
 * 
 * @author ly.youhort
 *
 */
public class EFrequency extends BaseERefData implements AttributeConverter<EFrequency, Long> {
	/** */
	private static final long serialVersionUID = -4927172995861636566L;
	
	public final static EFrequency D = new EFrequency("D", 1, 0); // daily
	public final static EFrequency W = new EFrequency("W", 2, 0); // weekly
	public final static EFrequency M = new EFrequency("M", 3, 1);  // monthly
	public final static EFrequency Q = new EFrequency("Q", 4, 3); // quarterly
	public final static EFrequency H = new EFrequency("H", 5, 6); // half.year
	public final static EFrequency A = new EFrequency("A", 6, 12); // annually

	private int nbMonths;	
	
	/**
	 * 
	 */
	public EFrequency() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EFrequency(String code, long id, int nbMonths) {
		super(code, id);
		this.nbMonths = nbMonths;
	}

	/**
	 * @return the nbMonths
	 */
	public int getNbMonths() {
		return nbMonths;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EFrequency convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFrequency arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EFrequency> values() {
		return getValues(EFrequency.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EFrequency getByCode(String code) {
		return getByCode(EFrequency.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EFrequency getById(long id) {
		return getById(EFrequency.class, id);
	}

	 /**
     * List of frequencies (M, Q, H, A)
     * @return
     */
    public static List<EFrequency> listInMonth() {
    	List<EFrequency> frequencies  = new ArrayList<>();
		frequencies.add(EFrequency.M);
		frequencies.add(EFrequency.Q);
		frequencies.add(EFrequency.H);
		frequencies.add(EFrequency.A);
    	return frequencies;
    }
    
   
    /**
     * @param frequency
     * @return
     */
    public int getNbSchedulePerYear() {
    	return 12 / getNbMonths();
    }

}

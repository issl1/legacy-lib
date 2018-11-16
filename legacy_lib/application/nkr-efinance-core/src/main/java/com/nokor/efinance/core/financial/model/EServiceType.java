package com.nokor.efinance.core.financial.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Service Type
 * @author ly.youhort
 *
 */
public class EServiceType extends BaseERefData implements AttributeConverter<EServiceType, Long> {
	/** */
	private static final long serialVersionUID = -7895203506008759570L;

	public final static EServiceType FEE = new EServiceType("FEE", 1); // fee
	public final static EServiceType COMM = new EServiceType("COMM", 2); // commission
	public final static EServiceType INEX = new EServiceType("INEX", 3); // insurance.expenses
	public final static EServiceType DMIS = new EServiceType("DMIS", 4); // miscellaneous
	public final static EServiceType INSFEE = new EServiceType("INSFEE", 5); // insurance fee
	public final static EServiceType SRVFEE = new EServiceType("SRVFEE", 6); // servicing fee
	public final static EServiceType INSLOS = new EServiceType("INSLOS", 7); // Insurance Lost
	public final static EServiceType INSAOM = new EServiceType("INSAOM", 8); // Insurance AOM
	public final static EServiceType COLFEE = new EServiceType("COLFEE", 9); // Collection Fee
	public final static EServiceType REPOSFEE = new EServiceType("REPOSFEE", 10); // Reposession Fee
	public final static EServiceType OPERFEE = new EServiceType("OPERFEE", 11); // Operation Fee
	public final static EServiceType TRANSFEE = new EServiceType("TRANSFEE", 12); // Transfer Fee
	public final static EServiceType PRESSFEE = new EServiceType("PRESSFEE", 13); // Pressing Fee
	public final static EServiceType FOLLWFEE = new EServiceType("FOLLWFEE", 14); // Following Fee
	public final static EServiceType REDEMFEE = new EServiceType("REDEMFEE", 15); // Redemption Fee
	

	/**
	 * 
	 */
	public EServiceType() {
	}
	
	/** 
	 * @param code
	 * @param id
	 */
	public EServiceType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EServiceType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EServiceType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EServiceType> values() {
		return getValues(EServiceType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EServiceType getByCode(String code) {
		return getByCode(EServiceType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EServiceType getById(long id) {
		return getById(EServiceType.class, id);
	}

	/**
	 * 
	 * @return
	 */
	public static EServiceType getDefault() {
		return getDefault(EServiceType.class);
	}
	
    /**
     * List of service type
     * @return
     */
    public static List<EServiceType> list() {
    	List<EServiceType> serviceTypes  = new ArrayList<>();
    	serviceTypes.add(FEE);
    	serviceTypes.add(INSFEE);
    	serviceTypes.add(SRVFEE);
    	serviceTypes.add(INSLOS);
    	serviceTypes.add(INSAOM);
    	serviceTypes.add(COLFEE);
    	serviceTypes.add(REPOSFEE);
    	return serviceTypes;
    }
    
    /**
     * List of service type
     * @return
     */
    public static List<EServiceType> listDirectCosts() {
    	List<EServiceType> serviceTypes  = new ArrayList<>();
    	serviceTypes.add(COMM);
    	serviceTypes.add(INEX);
    	serviceTypes.add(DMIS);
    	return serviceTypes;
    }
    
    /**
     * @param serviceType
     * @return
     */
    public static boolean isService(EServiceType serviceType) {
    	return COMM.equals(serviceType);
    }
    
    /**
     * @param serviceType
     * @return
     */
    public static int getSign(EServiceType serviceType) {
    	if (COMM.equals(serviceType)) {
    		return -1;
    	} else {
    		return 1;
    	}
    }
}
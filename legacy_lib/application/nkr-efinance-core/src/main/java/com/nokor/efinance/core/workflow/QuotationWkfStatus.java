package com.nokor.efinance.core.workflow;

import java.util.List;

import org.seuksa.frmk.tools.reflection.MyClassUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Quotation status
 * 
 * @author ly.youhort
 *
 */
public class QuotationWkfStatus {

	// GLF
	public final static EWkfStatus QUO = EWkfStatus.getById(1);  // Quotation												
	public final static EWkfStatus PRO = EWkfStatus.getById(2);  // Proposal,												
	public final static EWkfStatus DEC = EWkfStatus.getById(3);  // Declined												
	public final static EWkfStatus PRA = EWkfStatus.getById(4);  // proposal.approved										
	public final static EWkfStatus SUB = EWkfStatus.getById(5);  // Submitted												
	public final static EWkfStatus RAD = EWkfStatus.getById(6);  // Request additional info									
	public final static EWkfStatus RFC = EWkfStatus.getById(7);  // Request field check										
	public final static EWkfStatus REU = EWkfStatus.getById(8);  // Rejected underwriter									
	public final static EWkfStatus REJ = EWkfStatus.getById(9);  // Rejected												
	public final static EWkfStatus APU = EWkfStatus.getById(10); // Approved underwriter							
	public final static EWkfStatus APS = EWkfStatus.getById(11); // Approved underwriter supervisor				
	public final static EWkfStatus AWU = EWkfStatus.getById(12); // Approved with condition underwriter			
	public final static EWkfStatus AWS = EWkfStatus.getById(13); // Approved with condition underwriter supervisor
	public final static EWkfStatus AWT = EWkfStatus.getById(14); // Approved with condition
	public final static EWkfStatus APV = EWkfStatus.getById(15); // Approved
	public final static EWkfStatus ACT = EWkfStatus.getById(16); // Activated			
	public final static EWkfStatus CAN = EWkfStatus.getById(17); // Cancelled			
	public final static EWkfStatus RCG = EWkfStatus.getById(18); // Request change guarantor
	public final static EWkfStatus LCG = EWkfStatus.getById(19); // Allowed change guarantor
	public final static EWkfStatus ACG = EWkfStatus.getById(20); // Approved change guarantor
	public final static EWkfStatus RVG = EWkfStatus.getById(21); // Request validate change guarantor
	public final static EWkfStatus PPO = EWkfStatus.getById(22); // Pending purchase order
	
	// GL Thai
	public final static EWkfStatus NEW = EWkfStatus.getById(101);												
	public final static EWkfStatus INPRO = EWkfStatus.getById(102);												
//	public final static EWkfStatus RE_PRO = EWkfStatus.getById(103);
	public final static EWkfStatus ASS_LEV1 = EWkfStatus.getById(104);												
//	public final static EWkfStatus ASS_LEV2 = EWkfStatus.getById(105);
	public final static EWkfStatus HOLD = EWkfStatus.getById(106);
	public final static EWkfStatus REJECT = EWkfStatus.getById(119);												
	public final static EWkfStatus ACTIVE = EWkfStatus.getById(120);												

	
	/**
	 * 
	 * @return
	 */
    public static List<EWkfStatus> values() {
    	List<EWkfStatus> statuses = (List<EWkfStatus>) MyClassUtils.getStaticValues(QuotationWkfStatus.class, EWkfStatus.class);
    	return statuses;
    }
}
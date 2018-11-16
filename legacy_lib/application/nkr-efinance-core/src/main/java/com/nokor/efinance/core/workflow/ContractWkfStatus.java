
package com.nokor.efinance.core.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Contract status
 * 
 * @author prasnar
 *
 */
public class ContractWkfStatus {
	
	public final static EWkfStatus PEN = EWkfStatus.getById(200);														
	public final static EWkfStatus RECEIVED = EWkfStatus.getById(201);
	public final static EWkfStatus FIN = EWkfStatus.getById(202); // financed
	public final static EWkfStatus CAN = EWkfStatus.getById(203); // cancel
	public final static EWkfStatus EAR = EWkfStatus.getById(204); // early.settlement
	// public final static EWkfStatus PEN = EWkfStatus.getById(205); // pending
	public final static EWkfStatus LOS = EWkfStatus.getById(206); // loss
	public final static EWkfStatus CLO = EWkfStatus.getById(207); // closed
	public final static EWkfStatus REP = EWkfStatus.getById(208); // repossessed
	public final static EWkfStatus THE = EWkfStatus.getById(209); // theft
	public final static EWkfStatus ACC = EWkfStatus.getById(210); // accident
	public final static EWkfStatus FRA = EWkfStatus.getById(211); // fraud
	public final static EWkfStatus WRI = EWkfStatus.getById(212); // write.off
	public final static EWkfStatus BLOCKED = EWkfStatus.getById(213);
	// public final static EWkfStatus BOOKED = EWkfStatus.getById(214);
	// public final static EWkfStatus VALIDATED = EWkfStatus.getById(215);
	public final static EWkfStatus HOLD_PAY = EWkfStatus.getById(216);
	// public final static EWkfStatus COM = EWkfStatus.getById(214);
	public final static EWkfStatus WITHDRAWN = EWkfStatus.getById(217);

	public final static EWkfStatus PEN_TRAN = EWkfStatus.getById(230);
	public final static EWkfStatus BLOCKED_TRAN = EWkfStatus.getById(231);
	
	public final static EWkfStatus UNDER_LITIGATION = EWkfStatus.getById(232);
	public final static EWkfStatus UNDER_PROCESS_OF_PAYMENT = EWkfStatus.getById(233);
	public final static EWkfStatus ACCOUNT_CLOSE_WITH_ON_GOING_PAYMENT = EWkfStatus.getById(234);
	public final static EWkfStatus INVESTIGATION_REQUESTED_BY_AC_OWNER = EWkfStatus.getById(235);
	
	public final static EWkfStatus NORMAL = EWkfStatus.getById(236);
	public final static EWkfStatus CLOSED_ACCOUNT = EWkfStatus.getById(237);
	public final static EWkfStatus DEBT_MORATORIUM_AS_PER_GOVERNMENT_POLICY = EWkfStatus.getById(238);
	public final static EWkfStatus PAST_DUE_OVER_90_DAYS = EWkfStatus.getById(239);
	//public final static EWkfStatus UNDER_LITIGATION = EWkfStatus.getById(240);Under the processs of payment as agreed upon in the courts of law.
	public final static EWkfStatus UNDER_PROCESS_PAYMENT_AGREED_UPON_IN_COURTS_LAW = EWkfStatus.getById(240);
	public final static EWkfStatus CASE_DISMISSED_DUE_TO_LASPSE_PERIOD_PRESCRIPTION = EWkfStatus.getById(241);
	public final static EWkfStatus WRITE_OFF_ACCOUNT = EWkfStatus.getById(242);
	//public final static EWkfStatus ACCOUNT_CLOSED_WITH_ON_GO_PAYMENT = EWkfStatus.getById(243);
	//public final static EWkfStatus INVESTINGATION_REQUESTED_BY_AC_OWNER = EWkfStatus.getById(244);
	public final static EWkfStatus DEBT_TRANSFERRED_OR_SOLD = EWkfStatus.getById(243);
	public final static EWkfStatus CLOSING_OF_DISPOSALS = EWkfStatus.getById(244);
	
	/**
	 * 
	 * @return
	 */
    public static List<EWkfStatus> listAfterSales() {
    	List<EWkfStatus> contractStatus = new ArrayList<>();
    	contractStatus.add(EAR);
    	contractStatus.add(LOS);
    	contractStatus.add(REP);
    	contractStatus.add(THE);
    	contractStatus.add(ACC);
    	contractStatus.add(FRA);
    	contractStatus.add(WRI);
    	return contractStatus;
    }
    
    /**
	 * 
	 * @return
	 */
    public static List<EWkfStatus> listContractStatus() {
    	List<EWkfStatus> contractStatus = new ArrayList<>();
    	contractStatus.add(PEN);
    	contractStatus.add(BLOCKED);
    	contractStatus.add(FIN);
    	contractStatus.add(CAN);
    	contractStatus.add(HOLD_PAY);
    	contractStatus.add(EAR);
    	contractStatus.add(LOS);
    	contractStatus.add(CLO);
    	contractStatus.add(REP);
    	contractStatus.add(THE);
    	contractStatus.add(WRI);
    	return contractStatus;
    }
    
    /**
  	 * 
  	 * @return
  	 */
      public static List<EWkfStatus> listLegalStatus() {
      	List<EWkfStatus> contractStatus = new ArrayList<>();
      	contractStatus.add(UNDER_LITIGATION);
      	contractStatus.add(UNDER_PROCESS_OF_PAYMENT);
      	contractStatus.add(ACCOUNT_CLOSE_WITH_ON_GOING_PAYMENT);
      	contractStatus.add(INVESTIGATION_REQUESTED_BY_AC_OWNER);
      	return contractStatus;
      }
      
      /**
       * 
       * @return
       */
      public static List<EWkfStatus> listSummaryStatus() {
    	  List<EWkfStatus> contractStatus = new ArrayList<>();
    	  contractStatus.add(RECEIVED);
    	  contractStatus.add(FIN);
    	  contractStatus.add(WRI);
    	  contractStatus.add(PEN);
    	  return contractStatus;
      }
      
      /**
       * 
       * @return
       */
      public static List<EWkfStatus> listNBCStatus() {
  		List<EWkfStatus> NBCStatus = new ArrayList<>();
  		NBCStatus.add(NORMAL);
  		NBCStatus.add(CLOSED_ACCOUNT);
  		NBCStatus.add(DEBT_MORATORIUM_AS_PER_GOVERNMENT_POLICY);
  		NBCStatus.add(PAST_DUE_OVER_90_DAYS);
  		NBCStatus.add(UNDER_PROCESS_PAYMENT_AGREED_UPON_IN_COURTS_LAW);
  		NBCStatus.add(CASE_DISMISSED_DUE_TO_LASPSE_PERIOD_PRESCRIPTION);
  		NBCStatus.add(WRITE_OFF_ACCOUNT);
  		NBCStatus.add(ACCOUNT_CLOSE_WITH_ON_GOING_PAYMENT);
  		NBCStatus.add(INVESTIGATION_REQUESTED_BY_AC_OWNER);
  		NBCStatus.add(DEBT_TRANSFERRED_OR_SOLD);
  		NBCStatus.add(CLOSING_OF_DISPOSALS);
  		return NBCStatus;
  	}
}
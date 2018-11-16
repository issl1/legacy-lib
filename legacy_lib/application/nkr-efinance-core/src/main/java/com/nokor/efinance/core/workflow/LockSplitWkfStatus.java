
package com.nokor.efinance.core.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Lock split work flow status
 * @author uhout.cheng
 */
public class LockSplitWkfStatus {
	
	public final static EWkfStatus LNEW = EWkfStatus.getById(700); // New
	public final static EWkfStatus LPEN = EWkfStatus.getById(701); // Pending
	public final static EWkfStatus LPAR = EWkfStatus.getById(702); // Partial Paid
	public final static EWkfStatus LPAI = EWkfStatus.getById(703); // Paid
	public final static EWkfStatus LEXP = EWkfStatus.getById(704); // Expired
	public final static EWkfStatus LCAN = EWkfStatus.getById(705); // Canceled
	
	/**
	 * 
	 * @return
	 */
    public static List<EWkfStatus> listLockSplitStatus() {
    	List<EWkfStatus> contractStatus = new ArrayList<EWkfStatus>();
    	contractStatus.add(LNEW);
    	contractStatus.add(LEXP);
    	contractStatus.add(LPAR);
    	contractStatus.add(LPEN);
    	contractStatus.add(LPAI);
    	contractStatus.add(LCAN);
    	return contractStatus;
    }
	
}
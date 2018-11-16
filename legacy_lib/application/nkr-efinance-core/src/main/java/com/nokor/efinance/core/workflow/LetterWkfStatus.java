package com.nokor.efinance.core.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class LetterWkfStatus {

	public final static EWkfStatus LENEW = EWkfStatus.getById(1300l);
	public final static EWkfStatus LESEND = EWkfStatus.getById(1301l);
	public final static EWkfStatus LEPENDING = EWkfStatus.getById(1302l);
	public final static EWkfStatus LESUCCESS = EWkfStatus.getById(1303l);
	
	/**
	 * 
	 * @return
	 */
	public static List<EWkfStatus> listLetterStatus() {
		List<EWkfStatus> letterStatus = new ArrayList<EWkfStatus>();
		letterStatus.add(LENEW);
		letterStatus.add(LESEND);
		letterStatus.add(LEPENDING);
		letterStatus.add(LESUCCESS);
		return letterStatus;
	}
}

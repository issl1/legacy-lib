package org.seuksa.frmk.model.entity;

import java.util.List;



/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface StatusRecordListAware {
	List<EStatusRecord> getStatusRecordList();
	void setStatusRecordList(List<EStatusRecord> statusRecordList);
}

package com.nokor.frmk.model.eref;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;




/**
 * 
 * @author prasnar
 *
 */
public interface EntityRef extends RefDataId {
    
    Integer getSortIndex();
    
    EStatusRecord getStatusRecord();
    
    void setStatusRecord(EStatusRecord statusRecord);

	void fillSysBlock(String username);
  
}

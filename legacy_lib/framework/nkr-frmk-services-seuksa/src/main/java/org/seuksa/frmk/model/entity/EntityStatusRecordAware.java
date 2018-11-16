package org.seuksa.frmk.model.entity;





/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface EntityStatusRecordAware extends Entity, StatusRecordAware {
	
	public EStatusRecord getStatusRecord();
	
	/**
	 * 
	 * @return
	 */
    public default boolean isRecycledBin() {
        return EStatusRecord.RECYC.equals(getStatusRecord());
    }
    
    /**
     * 
     * @return
     */
    public default boolean isActive() {
        return getStatusRecord() == null || EStatusRecord.ACTIV.equals(getStatusRecord());
    }
    

    /**
     * 
     * @param isActive
     */
    public default void setActive(boolean isActive) {
		setStatusRecord(isActive ? EStatusRecord.ACTIV : EStatusRecord.INACT);
    }
    
    /**
     * 
     */
    public default void switchStatus() {
    	setActive(isActive());
    }
}
